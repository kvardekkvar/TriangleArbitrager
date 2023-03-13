package org.example.API;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.example.*;
import org.example.models.book_request.BookData;
import org.example.models.responses.BookResponse;
import org.example.models.responses.SubscriptionResponse;
import org.example.models.responses.SymbolsResponse;
import org.example.models.symbols_request.Symbol;
import org.example.util.Util;

import javax.websocket.MessageHandler;
import java.util.List;
import java.util.Random;

public class MsgHandler implements MessageHandler {

    private MarketData marketData = MarketData.INSTANCE;

    private Random random = new Random();

    private PoloniexApi api;

    public void sendBuyMessage(OrientedPair pair, double amount) {
        Crypto crypto = new Crypto();

        long timestamp = System.currentTimeMillis();

        int randomId = random.nextInt();
        String symbol = pair.getSymbol().toString();

        Asset bought = pair.isReversed() ? pair.getSource() : pair.getDestination();
        String side = pair.isReversed() ? "BUY" : "SELL";
        String amountOrQuantity = pair.isReversed() ? "amount" : "quantity";

        String amountString = Util.formattedAmount(amount, bought.getMaxDigitsAfterZero());

        String body = String.format("{\n" +
                "    \"symbol\": \"%s\",  \n" +
                "    \"type\": \"MARKET\",\n" +
                "    \"%s\": \"%s\",\n" +
                "    \"side\": \"%s\",\n" +
                "    \"timeInForce\": \"GTC\",\n" +
                "    \"clientOrderId\": \"%d\"\n" +
                "}", symbol, amountOrQuantity, amountString, side, randomId);

        String request = String.format(

                "POST\n" +
                        "/orders\n" +
                        "requestBody=%s" +
                        "&signTimestamp=%d", body, timestamp);

        //System.out.println(request);

        String signature = crypto.getSignature(request);

        /*
        String requestAuth = String.format("{\n" +
                "\"event\": \"subscribe\",\n" +
                "\"channel\": [\"auth\"],\n" +
                "\"params\": {\n" +
                "\"key\": \"%s\",\n" +
                "\"signTimestamp\": %d,\n" +
                "\"signature\": \"%s\"}\n}", Crypto.API_KEY, timestamp, signature);

        System.out.println(requestAuth);
        */

        System.out.printf("Order message sent: %s\n%n", pair.logPrices());
        api.makeOrder(body, signature, timestamp);
    }

    public void handleMessage(String message) {

        Gson gson = new Gson();
        //System.out.println("received message\n");
        //System.out.println(message);


        SymbolsResponse symbolsResponse;
        SubscriptionResponse subscriptionResponse;
        BookResponse bookResponse;

        try {
            bookResponse = gson.fromJson(message, BookResponse.class);
            if (bookResponse.getData().get(0).getAsks().size() > 0) {

                BookData data = bookResponse.getData().get(0);
                TradingPair pair = marketData.findTradingPairBySymbol(data.getSymbol());

                List<List<String>> asks = data.getAsks();
                List<List<String>> bids = data.getBids();
                double askPrice;
                double askAmount;
                double bidPrice;
                double bidAmount;

                //4 lines below throw IndexOutOfBounds when trading is stopped by Poloniex and asks and bids lists come empty
                //price to buy destination
                askPrice = Double.parseDouble(asks.get(0).get(0));
                askAmount = Double.parseDouble(asks.get(0).get(1));

                //price to buy source
                bidPrice = Double.parseDouble(bids.get(0).get(0));
                bidAmount = Double.parseDouble(bids.get(0).get(1));

                //System.out.printf("%s %s %s %s\n",askPrice, askAmount, bidPrice, bidAmount);

                BookEntry order = new BookEntry(pair, bidPrice, bidAmount, askPrice, askAmount);
                marketData.setBookEntryAtTradingPair(pair, order);

                List<List<Triangle>> triangleInfo = marketData.getProfitableTrianglesThatIncludeTradingPair(pair);
                List<Triangle> profitableTriangles = triangleInfo.get(0);
                List<Triangle> profitableReversedTriangles = triangleInfo.get(1);

                for (Triangle triangle : profitableTriangles) {
                    sendBuyMessage(triangle.getFirst(), triangle.getAmountToTrade1());
                    sendBuyMessage(triangle.getSecond(), triangle.getAmountToTrade2());
                    sendBuyMessage(triangle.getThird(), triangle.getAmountToTrade3());
                }
                for (Triangle reversedTriangle : profitableReversedTriangles) {
                    //needs implementation
                }

            }
        } catch (IndexOutOfBoundsException | NullPointerException | JsonSyntaxException ignored) {

        }

        try {
            subscriptionResponse = gson.fromJson(message, SubscriptionResponse.class);
            if (subscriptionResponse.getEvent() != null) {
                //System.out.println("subscribed to ticker with response: " + subscriptionResponse.getChannel() + subscriptionResponse.getSymbols());
            }
        } catch (NullPointerException | JsonSyntaxException ignored) {

        }

        try {
            symbolsResponse = gson.fromJson(message, SymbolsResponse.class);
            if (symbolsResponse.getData() != null) {
                List<List<Symbol>> symbols2DArray = symbolsResponse.getData();
                for (List<Symbol> symbolRow : symbols2DArray) {
                    for (Symbol symbol : symbolRow) {

                        //System.out.println(symbol.getSymbol());

                        Asset source = Asset.fromSymbol(symbol, true);
                        Asset destination = Asset.fromSymbol(symbol, false);
                        TradingPair pair = new TradingPair(source, destination);

                        marketData.addPair(pair);
                    }
                    marketData.initializeTriangles();

                }
            }
        } catch (NullPointerException | JsonSyntaxException ignored) {
        }


    }

    public void setApi(PoloniexApi api) {
        this.api = api;
    }

}

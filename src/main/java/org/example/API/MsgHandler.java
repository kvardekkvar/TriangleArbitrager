package org.example.API;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.example.*;
import org.example.models.book_request.BookData;
import org.example.models.requests.MarketOrderRequest;
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


    public String prepareBuyMessageBody(String symbol, String amountString, boolean isAmount, String side) {
        MarketOrderRequest orderRequest = new MarketOrderRequest(symbol, amountString, isAmount, side);
        Gson gson = new Gson();
        return gson.toJson(orderRequest);
    }

    public String prepareBuyMessageSignature(String body, long timestamp) {
        Crypto crypto = new Crypto();

        String request = String.format(
                "POST\n" +
                        "/orders\n" +
                        "requestBody=%s" +
                        "&signTimestamp=%d", body, timestamp);

        String signature = crypto.getSignature(request);
        return signature;
    }

    public void sendBuyMessage(OrientedPair pair, double amount) {
        long timestamp = System.currentTimeMillis();
        String symbol = pair.getSymbol().toString();
        boolean isAmount = pair.isReversed();
        String side = isAmount ? "BUY" : "SELL";
        String amountOrQuantity = isAmount ? "amount" : "quantity";
        int scale = isAmount ? pair.getAmountScale() : pair.getQuantityScale();
        String amountString = Util.formattedAmount(amount, scale);
        boolean isSuccess = false;

        while (!isSuccess)
            try {
                String body = prepareBuyMessageBody(symbol, amountString, isAmount, side);
                String signature = prepareBuyMessageSignature(body, timestamp);

                System.out.printf("Order message sent: %s\n%n", pair.logPrices());
                api.makeOrder(body, signature, timestamp);
                isSuccess = true;
            } catch (LowBalanceException e) {
                double currentAmount = Double.parseDouble(amountString);
                double lesserAmount = currentAmount * 0.993;
                amountString = Util.formattedAmount(lesserAmount, scale);
            }
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
                        TradingPair pair = TradingPair.fromSymbol(symbol);
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

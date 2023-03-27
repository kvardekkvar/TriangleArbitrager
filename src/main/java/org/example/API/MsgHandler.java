package org.example.API;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.*;
import org.example.models.book_request.BookData;
import org.example.models.requests.MarketOrderRequest;
import org.example.models.responses.BookResponse;
import org.example.models.responses.SymbolsResponse;
import org.example.models.symbols_request.Symbol;
import org.example.util.JSONHandler;
import org.example.util.JacksonHandler;
import org.example.util.Util;

import javax.websocket.MessageHandler;
import java.util.List;

public class MsgHandler implements MessageHandler {

    private final MarketData marketData = MarketData.INSTANCE;
    private final JSONHandler jsonHandler = new JacksonHandler();

    private PoloniexApi api;



    public String prepareBuyMessageBody(String symbol, String amountString, boolean isAmount, String side) {
        MarketOrderRequest orderRequest = new MarketOrderRequest(symbol, amountString, isAmount, side);
        return jsonHandler.toJSON(orderRequest);


    }

    public String prepareBuyMessageSignature(String body, long timestamp) {
        Crypto crypto = Crypto.INSTANCE;

        String request = String.format(
                "POST\n" +
                        "/orders\n" +
                        "requestBody=%s" +
                        "&signTimestamp=%d", body, timestamp);

        return crypto.getSignature(request);
    }

    public void sendBuyMessage(OrientedPair orientedPair, double amount) {
        long timestamp = System.currentTimeMillis();
        String symbol = orientedPair.getSymbol();

        boolean isAmount = orientedPair.isReversed();
        TradingPair pair = orientedPair.getPair();
        String side = isAmount ? "BUY" : "SELL";
        String amountOrQuantity = isAmount ? "amount" : "quantity";
        int scale = isAmount ? pair.getAmountScale() : pair.getQuantityScale();
        String amountString = Util.formattedAmount(amount, scale);

        boolean isSuccess = false;
        int cnt = 0;
        while (!isSuccess && cnt < 10)
            try {
                cnt++;
                String body = prepareBuyMessageBody(symbol, amountString, isAmount, side);
                String signature = prepareBuyMessageSignature(body, timestamp);

                System.out.printf("Order message sending: %s\n%n", pair.logPrices());
                api.makeOrder(body, signature, timestamp);
                isSuccess = true;
            } catch (LowBalanceException e) {
                double currentAmount = Double.parseDouble(amountString);
                double lesserAmount = currentAmount * 0.93;
                amountString = Util.formattedAmount(lesserAmount, scale);
            }
    }

    public void handleMessage(String message) {

        SymbolsResponse symbolsResponse;
        BookResponse bookResponse;


        JsonNode node = jsonHandler.readEntireJSON(message);

        if (    node.has("channel") &&
                node.get("channel").asText().equals("book") &&
                node.has("data") &&
                node.get("data").has(0)) {

            bookResponse = jsonHandler.fromJSON(message, BookResponse.class);

            if (bookResponse != null &&
                    bookResponse.getChannel().equals("book") &&
                    bookResponse.getData() != null &&
                    bookResponse.getData().get(0).getAsks().size() > 0) {

                BookData data = bookResponse.getData().get(0);
                TradingPair pair = marketData.findTradingPairBySymbol(data.getSymbol());

                List<String> asks = data.getAsks().get(0);
                List<String> bids = data.getBids().get(0);

                //4 lines below throw IndexOutOfBounds when trading is stopped by Poloniex and asks and bids lists come empty
                double askPrice = Double.parseDouble(asks.get(0));
                double askAmount = Double.parseDouble(asks.get(1));

                double bidPrice = Double.parseDouble(bids.get(0));
                double bidAmount = Double.parseDouble(bids.get(1));

                marketData.setBookEntryAtTradingPair(pair, bidPrice, bidAmount, askPrice, askAmount);

                List<List<Triangle>> triangleInfo = marketData.getProfitableTriangleHavingEdge(pair);
                List<Triangle> profitableTriangles = triangleInfo.get(0);
                List<Triangle> profitableReversedTriangles = triangleInfo.get(1);

                for (Triangle triangle : profitableTriangles) {
                    sendBuyMessage(triangle.getFirst(), triangle.getAmountToTrade1());
                    sendBuyMessage(triangle.getSecond(), triangle.getAmountToTrade2());
                    sendBuyMessage(triangle.getThird(), triangle.getAmountToTrade3());
                    break;
                }
                for (Triangle reversedTriangle : profitableReversedTriangles) {
                    //needs implementation
                    break;
                }
                return;
            }

        }

        if (    node.has("channel") &&
                node.get("channel").asText().equals("symbols") &&
                node.has("data") &&
                node.get("data").has(0)) {

            symbolsResponse = jsonHandler.fromJSON(message, SymbolsResponse.class);


            if (symbolsResponse.getChannel().equals("symbols") && symbolsResponse.getData() != null) {
                List<List<Symbol>> symbols2DArray = symbolsResponse.getData();
                for (List<Symbol> symbolRow : symbols2DArray) {
                    for (Symbol symbol : symbolRow) {
                        TradingPair pair = TradingPair.fromSymbol(symbol);
                        marketData.addPair(pair);
                    }

                }
                marketData.initialize();
            }
        }

    }

    public void setApi(PoloniexApi api) {
        this.api = api;
    }

}

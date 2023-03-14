package org.example;

import org.example.API.PoloniexApi;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.LockSupport;

public class Main {

    public static boolean RESTART_NEEDED = false;

    public static void main(String[] args) {

        while (true) {
            RESTART_NEEDED = false;
            PoloniexApi poloniexApi = new PoloniexApi();

            String symbolsRequest = "{\n" +
                    "  \"event\": \"subscribe\",\n" +
                    "  \"channel\": [\"symbols\"],\n" +
                    "  \"symbols\": [\"all\"]\n" +
                    "}\n";
            poloniexApi.sendPublic(symbolsRequest);

            List<TradingPair> subscriptions = new ArrayList<>();
            for (Triangle triangle : MarketData.INSTANCE.getTriangles()) {
                TradingPair first = triangle.getFirst().getSymbol();
                TradingPair second = triangle.getSecond().getSymbol();
                TradingPair third = triangle.getThird().getSymbol();
                if (!subscriptions.contains(first)) {
                    subscriptions.add(first);
                }
                if (!subscriptions.contains(second)) {
                    subscriptions.add(second);
                }
                if (!subscriptions.contains(third)) {
                    subscriptions.add(third);
                }
            }


            for (TradingPair pair : subscriptions) {
                String bookRequest = String.format(
                        "{\n\"event\": \"subscribe\",\n \"channel\": [\"book\"],\n \"symbols\": [\"%s\"]\n}\n", pair.toString()
                );
                poloniexApi.sendPublic(bookRequest);
            }

            while (!RESTART_NEEDED) {
                try {
                    LockSupport.parkNanos(16384) ;
                } catch (Exception e) {
                    break;
                }
            }
        }
    }
}
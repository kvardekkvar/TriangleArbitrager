package org.example;

import org.example.API.Crypto;
import org.example.API.Pinger;
import org.example.API.PoloniexApi;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.LockSupport;

import static org.example.util.Constants.NORMAL_STATE;

public class Main {

    public static boolean RESTART_NEEDED = false;

    public static void main(String[] args) throws InterruptedException {
        long restarts = 0;
        while (true) {
            RESTART_NEEDED = false;
            PoloniexApi poloniexApi = PoloniexApi.INSTANCE;
            if (restarts > 0) {
                poloniexApi.reconnect();
            }
            restarts++;
            Pinger pinger = new Pinger();

            String symbolsRequest = "{\n" +
                    "  \"event\": \"subscribe\",\n" +
                    "  \"channel\": [\"symbols\"],\n" +
                    "  \"symbols\": [\"all\"]\n" +
                    "}\n";
            poloniexApi.sendPublic(symbolsRequest);
            Thread.sleep(5000);

            List<String> subscriptions = new LinkedList<>();
            for (Triangle triangle : MarketData.INSTANCE.getTriangles()) {
                TradingPair pair1 = triangle.getFirst().getPair();
                TradingPair pair2 = triangle.getSecond().getPair();
                TradingPair pair3 = triangle.getThird().getPair();
                if (!pair1.getState().equals(NORMAL_STATE) || !pair2.getState().equals(NORMAL_STATE) || !pair3.getState().equals(NORMAL_STATE)) {
                    continue;
                }

                String firstSymbol = pair1.getSymbol();
                String secondSymbol = pair2.getSymbol();
                String thirdSymbol = pair3.getSymbol();

                if (!subscriptions.contains(firstSymbol)) {
                    subscriptions.add(firstSymbol);
                }
                if (!subscriptions.contains(secondSymbol)) {
                    subscriptions.add(secondSymbol);
                }
                if (!subscriptions.contains(thirdSymbol)) {
                    subscriptions.add(thirdSymbol);
                }

            }

            for (String symbol : subscriptions) {
                LockSupport.parkNanos(1_000_000L);
                String bookRequest = String.format(
                        "{\n\"event\": \"subscribe\",\n \"channel\": [\"book\"],\n \"symbols\": [\"%s\"]\n}\n", symbol
                );
                poloniexApi.sendPublic(bookRequest);
            }
            System.out.println("Subscribed to all channels");

            while (!RESTART_NEEDED) {
                if (System.currentTimeMillis()%1000 < 5){
                    pinger.sendPing();
                }

                try {
                    LockSupport.parkNanos(16384);
                } catch (Exception e) {
                    break;
                }
            }
        }
    }
}
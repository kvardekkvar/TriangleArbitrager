package org.example;

import org.example.API.PoloniexApi;
import org.example.util.Constants;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.LockSupport;

import static org.example.util.Constants.NORMAL_STATE;

public class Main {

    public static boolean RESTART_NEEDED = false;

    public static void main(String[] args) throws InterruptedException {

        while (true) {
            RESTART_NEEDED = false;
            PoloniexApi poloniexApi = new PoloniexApi();

            String symbolsRequest = "{\n" +
                    "  \"event\": \"subscribe\",\n" +
                    "  \"channel\": [\"symbols\"],\n" +
                    "  \"symbols\": [\"all\"]\n" +
                    "}\n";
            poloniexApi.sendPublic(symbolsRequest);
            //Thread.sleep(5000);

            List<String> subscriptions = new LinkedList<>();
            for (Triangle triangle : MarketData.INSTANCE.getTriangles()) {
                OrientedPair pair1 = triangle.getFirst();
                OrientedPair pair2 = triangle.getSecond();
                OrientedPair pair3 = triangle.getThird();
                if (!pair1.getState().equals(NORMAL_STATE) || !pair2.getState().equals(NORMAL_STATE) || !pair3.getState().equals(NORMAL_STATE)){
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
                //System.out.printf("%s \n %s \n%s \n", firstSymbol, secondSymbol, thirdSymbol);

            }


            for (String symbol : subscriptions) {
                String bookRequest = String.format(
                        "{\n\"event\": \"subscribe\",\n \"channel\": [\"book\"],\n \"symbols\": [\"%s\"]\n}\n", symbol
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
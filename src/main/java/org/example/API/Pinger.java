package org.example.API;

public class Pinger extends Thread {
    private PoloniexApi poloniexApi = PoloniexApi.INSTANCE;

    public static  String PING_MESSAGE = "{\n" +
            "\"event\": \"ping\"\n" +
            "}";

    public void sendPing(){
        poloniexApi.sendPublic(PING_MESSAGE);
    }
}

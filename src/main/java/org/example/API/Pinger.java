package org.example.API;

import org.example.Main;

import java.util.concurrent.locks.LockSupport;

public class Pinger implements Runnable {
    private PoloniexApi poloniexApi = PoloniexApi.INSTANCE;

    public static  String PING_MESSAGE = "{\n" +
            "\"event\": \"ping\"\n" +
            "}";

    public void sendPing(){
        poloniexApi.sendPublic(PING_MESSAGE);
    }

    public void run(){
        while (!Main.RESTART_NEEDED){
            sendPing();
            LockSupport.parkNanos(2_000_000_000L);
        }
    }
}

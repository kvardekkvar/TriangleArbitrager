package org.example.API;

import javax.websocket.SendResult;
import java.net.URI;

public class PoloniexApi {

    private final WebsocketClientEndpoint publicWebsocket;

    private final WebsocketClientEndpoint privateWebsocket;

    private final RestApiClient privateRest;
    public PoloniexApi() {
        URI publicEndpoint = URI.create("wss://ws.poloniex.com/ws/public");
        URI privateEndpoint = URI.create("wss://ws.poloniex.com/ws/private");

        this.publicWebsocket = new WebsocketClientEndpoint(publicEndpoint);
        this.privateWebsocket = new WebsocketClientEndpoint(privateEndpoint);

        MsgHandler messageHandler = new MsgHandler();
        messageHandler.setApi(this);
        publicWebsocket.addMessageHandler(messageHandler);
        MsgHandler messageHandler2 = new MsgHandler();
        messageHandler2.setApi(this);
        privateWebsocket.addMessageHandler(messageHandler2);

        this.privateRest = new RestApiClient();
    }

    public void send(String data, boolean isPublic) {
        if (isPublic) {
            publicWebsocket.sendMessage(data);
        } else {
            privateWebsocket.sendMessage(data);
        }
    }

    public void sendPrivate(String data) {
        send(data, false);
    }

    public void sendPublic(String data) {
        send(data, true);
    }

    public void makeOrder(String json, String signature, long timestamp) throws LowBalanceException {
        privateRest.sendMakeOrderRequest(json, signature, timestamp);
    }

}

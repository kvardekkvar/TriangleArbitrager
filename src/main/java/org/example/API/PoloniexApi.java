package org.example.API;

import java.net.URI;

public class PoloniexApi {

    public static PoloniexApi INSTANCE = new PoloniexApi();
    private WebsocketClientEndpoint publicWebsocket;

    private RestApiClient privateRest;
    private PoloniexApi() {
        connect();
    }

    private void connect(){
        URI publicEndpoint = URI.create("wss://ws.poloniex.com/ws/public");
        URI privateEndpoint = URI.create("wss://ws.poloniex.com/ws/private");

        this.publicWebsocket = new WebsocketClientEndpoint(publicEndpoint);

        MsgHandler messageHandler = new MsgHandler();
        messageHandler.setApi(this);
        publicWebsocket.addMessageHandler(messageHandler);

        this.privateRest = new RestApiClient();

    }
    public void reconnect(){
        connect();
    }

    public void send(String data, boolean isPublic) {
        if (isPublic) {
            publicWebsocket.sendMessage(data);
        }
    }

    public void sendPublic(String data) {
        send(data, true);
    }

    public void makeOrder(String json, String signature, long timestamp) throws LowBalanceException {
        privateRest.sendMakeOrderRequest(json, signature, timestamp);
    }

}

package org.example.API;

import org.example.Main;

import java.io.IOException;
import java.net.URI;

public class PoloniexApi {

    public static PoloniexApi INSTANCE = new PoloniexApi();
    private WebsocketClientEndpoint publicWebsocket;

    private RestApiClient privateRest;

    private PoloniexApi() {
        connect();
    }

    private void connect() {
        URI publicEndpoint = URI.create("wss://ws.poloniex.com/ws/public");

        if (this.publicWebsocket != null && this.publicWebsocket.userSession != null) {
            try {
                this.publicWebsocket.userSession.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            this.publicWebsocket.userSession = null;
            this.publicWebsocket.container = null;
            this.publicWebsocket = null;
            System.gc();
        }
        Main.RESTART_NEEDED = false;
        this.publicWebsocket = new WebsocketClientEndpoint(publicEndpoint);

        MsgHandler messageHandler = new MsgHandler();
        messageHandler.setApi(this);
        publicWebsocket.addMessageHandler(messageHandler);

        this.privateRest = new RestApiClient();

    }

    public void reconnect() {
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

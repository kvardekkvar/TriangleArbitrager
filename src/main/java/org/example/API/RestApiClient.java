package org.example.API;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.io.IOException;

public class RestApiClient {

    public static final String BASE_URL =  "https://api.poloniex.com";
    private OkHttpClient client = new OkHttpClient();
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    public final static String HEADER_TIMESTAMP = "signTimestamp";
    public final static String HEADER_KEY = "key";
    public final static String HEADER_SIGN_METHOD = "signMethod";
    public final static String HEADER_SIGN_VERSION = "signVersion";
    public final static String HEADER_SIGNATURE = "signature";

    public void sendMakeOrderRequest(String json, String signature, long timestamp){
        String url = String.format("%s/orders", BASE_URL);

        RequestBody body = RequestBody.create(json, JSON);
        String timestampString = String.format("%d", timestamp);

        Request request = new Request.Builder()
                .url(url)
                .header(HEADER_KEY, Crypto.API_KEY)
                .header(HEADER_SIGNATURE, signature)
                .header(HEADER_TIMESTAMP, timestampString)
                .post(body)
                .build();

        try {
            client.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

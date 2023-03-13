package org.example.API;

import okhttp3.*;

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
            System.out.println("Api sends market order...\n");
            Response response = client.newCall(request).execute();
            assert response.body() != null;
            System.out.println(response.body().string());
        } catch (NullPointerException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}

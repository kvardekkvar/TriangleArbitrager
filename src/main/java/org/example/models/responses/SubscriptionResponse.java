package org.example.models.responses;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SubscriptionResponse {


    @SerializedName("event")
    private String event;
    @SerializedName("channel")
    private String channel;
    @SerializedName("symbols")
    private List<String> symbols;

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public List<String> getSymbols() {
        return symbols;
    }

    public void setSymbols(List<String> symbols) {
        this.symbols = symbols;
    }

}

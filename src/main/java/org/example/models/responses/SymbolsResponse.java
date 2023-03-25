package org.example.models.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.example.models.symbols_request.Symbol;

import java.util.List;

public class SymbolsResponse {
    @Expose
    @SerializedName("channel")
    String channel;

    @Expose
    @SerializedName("data")
    List<List<Symbol>> data;

    @SerializedName("action")
    String action;

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public List<List<Symbol>> getData() {
        return data;
    }

    public void setData(List<List<Symbol>> data) {
        this.data = data;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}

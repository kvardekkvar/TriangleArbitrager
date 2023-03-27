package org.example.models.book_request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.Expose;

import java.util.List;

public class BookData {
    @Expose
    private String symbol;
    @JsonIgnore
    private long createTime;
    @Expose
    private List<List<String>> asks;
    @Expose
    private List<List<String>> bids;
    @JsonIgnore
    private long id;
    @JsonIgnore
    private long ts;

    public String getSymbol() { return symbol; }
    public void setSymbol(String value) { this.symbol = value; }

    public long getCreateTime() { return createTime; }
    public void setCreateTime(long value) { this.createTime = value; }

    public List<List<String>> getAsks() { return asks; }
    public void setAsks(List<List<String>> value) { this.asks = value; }

    public List<List<String>> getBids() { return bids; }
    public void setBids(List<List<String>> value) { this.bids = value; }

    public long getID() { return id; }
    public void setID(long value) { this.id = value; }

    public long getTs() { return ts; }
    public void setTs(long value) { this.ts = value; }

}

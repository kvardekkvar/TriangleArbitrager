package org.example.models.book_request;

import java.util.List;

public class BookData {
    private String symbol;
    private long createTime;
    private List<List<String>> asks;
    private List<List<String>> bids;
    private long id;
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

package org.example;

public class BookEntry {


    private TradingPair tradingPair;


    private double bidPrice;
    private double bidAmount;

    private double askPrice;

    private double askAmount;


    private long timestampWhenUpdated;


    public BookEntry(TradingPair tradingPair, double bidPrice, double bidAmount, double askPrice, double askAmount) {
        this.tradingPair = tradingPair;
        this.bidPrice = bidPrice;
        this.bidAmount = bidAmount;
        this.askPrice = askPrice;
        this.askAmount = askAmount;
    }

    public TradingPair getTradingPair() {
        return tradingPair;
    }

    public void setTradingPair(TradingPair tradingPair) {
        this.tradingPair = tradingPair;
    }

    public double getBidPrice() {
        return bidPrice;
    }

    public void setBidPrice(double bidPrice) {
        this.bidPrice = bidPrice;
    }

    public double getBidAmount() {
        return bidAmount;
    }

    public void setBidAmount(double bidAmount) {
        this.bidAmount = bidAmount;
    }

    public double getAskPrice() {
        return askPrice;
    }

    public void setAskPrice(double askPrice) {
        this.askPrice = askPrice;
    }

    public double getAskAmount() {
        return askAmount;
    }

    public void setAskAmount(double askAmount) {
        this.askAmount = askAmount;
    }

    public long getTimestampWhenUpdated() {
        return timestampWhenUpdated;
    }

    public void setTimestampWhenUpdated(long timestampWhenUpdated) {
        this.timestampWhenUpdated = timestampWhenUpdated;
    }

}

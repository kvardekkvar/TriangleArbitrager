package org.example;

public class BookEntry {


    private TradingPair tradingPair;


    private double sourcePrice;
    private double sourceAmount;

    private double destinationPrice;

    private double destinationAmount;


    private long timestampWhenUpdated;


    public BookEntry(TradingPair tradingPair, double sourcePrice, double sourceAmount, double destinationPrice, double destinationAmount) {
        this.tradingPair = tradingPair;
        this.sourcePrice = sourcePrice;
        this.sourceAmount = sourceAmount;
        this.destinationPrice = destinationPrice;
        this.destinationAmount = destinationAmount;
    }

    public TradingPair getTradingPair() {
        return tradingPair;
    }

    public void setTradingPair(TradingPair tradingPair) {
        this.tradingPair = tradingPair;
    }

    public double getSourcePrice() {
        return sourcePrice;
    }

    public void setSourcePrice(double sourcePrice) {
        this.sourcePrice = sourcePrice;
    }

    public double getSourceAmount() {
        return sourceAmount;
    }

    public void setSourceAmount(double sourceAmount) {
        this.sourceAmount = sourceAmount;
    }

    public double getDestinationPrice() {
        return destinationPrice;
    }

    public void setDestinationPrice(double destinationPrice) {
        this.destinationPrice = destinationPrice;
    }

    public double getDestinationAmount() {
        return destinationAmount;
    }

    public void setDestinationAmount(double destinationAmount) {
        this.destinationAmount = destinationAmount;
    }

    public long getTimestampWhenUpdated() {
        return timestampWhenUpdated;
    }

    public void setTimestampWhenUpdated(long timestampWhenUpdated) {
        this.timestampWhenUpdated = timestampWhenUpdated;
    }

}

package org.example.models.symbols_request;

public class SymbolTradeLimit {

    public String symbol;
    public int priceScale;
    public int quantityScale;
    public int amountScale;
    public String minQuantity;
    public String minAmount;
    public String highestBid;
    public String lowestAsk;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public int getPriceScale() {
        return priceScale;
    }

    public void setPriceScale(int priceScale) {
        this.priceScale = priceScale;
    }

    public int getQuantityScale() {
        return quantityScale;
    }

    public void setQuantityScale(int quantityScale) {
        this.quantityScale = quantityScale;
    }

    public int getAmountScale() {
        return amountScale;
    }

    public void setAmountScale(int amountScale) {
        this.amountScale = amountScale;
    }

    public String getMinQuantity() {
        return minQuantity;
    }

    public void setMinQuantity(String minQuantity) {
        this.minQuantity = minQuantity;
    }

    public String getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(String minAmount) {
        this.minAmount = minAmount;
    }

    public String getHighestBid() {
        return highestBid;
    }

    public void setHighestBid(String highestBid) {
        this.highestBid = highestBid;
    }

    public String getLowestAsk() {
        return lowestAsk;
    }

    public void setLowestAsk(String lowestAsk) {
        this.lowestAsk = lowestAsk;
    }

}

package org.example.models.requests;

public class MarketOrderRequest {
    private String symbol;
    private String side;

    private String quantity;
    private String amount;


    public MarketOrderRequest(String symbol, String amountOrQuantity, boolean isAmount, String side) {
        this.symbol = symbol;
        this.side = side;
        if (isAmount) {
            amount = amountOrQuantity;
        } else {
            quantity = amountOrQuantity;
        }
    }

    public void setAmountOrQuantity(String newValue, boolean isAmount) {
        if (isAmount) {
            amount = newValue;
        } else {
            quantity = newValue;
        }

    }

}

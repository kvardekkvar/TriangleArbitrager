package org.example.models.requests;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.gson.annotations.Expose;

@JsonInclude(JsonInclude.Include.NON_NULL)

public class MarketOrderRequest {


    @Expose
    private String symbol;
    @Expose
    private String side;
    @Expose
    private String quantity;
    @Expose
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

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}



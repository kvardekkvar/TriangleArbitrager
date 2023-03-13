package org.example;

import org.example.models.symbols_request.Symbol;
import org.example.models.symbols_request.SymbolTradeLimit;

public class Asset {



    private String name;
    private double minAmount;

    private boolean isDelisted;
    public Asset(String name, double minAmount){
        this.name = name;
        this.minAmount = minAmount;
    }

    public static Asset fromSymbol(Symbol symbol, boolean isFirst){
        String name = isFirst? symbol.getBaseCurrencyName() : symbol.getQuoteCurrencyName();
        SymbolTradeLimit limit = symbol.getSymbolTradeLimit();
        String limitAmount = isFirst? limit.getMinQuantity() : limit.getMinAmount();
        double minAmount = Double.parseDouble(limitAmount);
        return new Asset(name, minAmount);
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(double minAmount) {
        this.minAmount = minAmount;
    }

    public boolean isDelisted() {
        return isDelisted;
    }

    public void setDelisted(boolean delisted) {
        isDelisted = delisted;
    }

    public boolean equals(Asset other){
        return this.getName().equals(other.getName());
    }

    public String toString(){
        return name;
    }
}

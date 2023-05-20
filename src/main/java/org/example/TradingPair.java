package org.example;

import org.example.models.symbols_request.Symbol;
import org.example.models.symbols_request.SymbolTradeLimit;

public class TradingPair {

    protected Asset base;


    protected Asset quote;

    int quantityScale;


    int amountScale;



    String state;


    String symbol;


    double minQuantity;
    double minAmount;

    public TradingPair(Asset base, Asset quote, int quantityScale, int amountScale, String state, String symbol, double minQuantity, double minAmount) {
        this.base = base;
        this.quote = quote;
        this.quantityScale = quantityScale;
        this.amountScale = amountScale;
        this.state = state;
        this.symbol = symbol;
        this.minQuantity = minQuantity;
        this.minAmount = minAmount;
    }

    public static TradingPair fromSymbol(Symbol symbol) {
        SymbolTradeLimit limit = symbol.getSymbolTradeLimit();
        Asset source = Asset.fromSymbol(symbol, true);
        Asset destination = Asset.fromSymbol(symbol, false);
        return new TradingPair(
                source, destination,
                limit.getQuantityScale(), limit.getAmountScale(),
                symbol.getState(), symbol.getSymbol(),
                Double.parseDouble(limit.getMinQuantity()), Double.parseDouble(limit.getMinAmount())
        );
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
    public int getQuantityScale() {
        return quantityScale;
    }

    public int getAmountScale() {
        return amountScale;
    }

    public Asset getBase() {
        return base;
    }

    public void setBase(Asset base) {
        this.base = base;
    }

    public Asset getQuote() {
        return quote;
    }

    public void setQuote(Asset quote) {
        this.quote = quote;
    }

    public String toString() {
        return base.getName() + "_" + quote.getName();
    }

    public boolean equals(TradingPair other) {
        return this.symbol.equals(other.symbol);
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
    public double getMinQuantity() {
        return minQuantity;
    }

    public void setMinQuantity(double minQuantity) {
        this.minQuantity = minQuantity;
    }

    public double getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(double minAmount) {
        this.minAmount = minAmount;
    }


    public String logPrices() {
        String result = "";
        MarketData data = MarketData.INSTANCE;
        TradingPair pair = data.findTradingPairBetween(base, quote);
        BookEntry entry = data.getDataTable().get(pair);
        if (entry != null) {
            result = String.format("pair %s, price1: %s, price2: %s", this, entry.getBidPrice(), entry.getAskPrice());
        } else {
            System.out.printf("MarketData has no pair  %s %s %s\n", base, quote, this);
        }
        return result;
    }
}

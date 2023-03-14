package org.example;

import org.example.models.symbols_request.Symbol;
import org.example.models.symbols_request.SymbolTradeLimit;

public class TradingPair {

    protected Asset source;


    protected Asset destination;

    int quantityScale;

    public int getQuantityScale() {
        return quantityScale;
    }

    public int getAmountScale() {
        return amountScale;
    }

    int amountScale;

    public TradingPair(Asset source, Asset destination, int quantityScale, int amountScale) {
        this.source = source;
        this.destination = destination;
        this.quantityScale = quantityScale;
        this.amountScale = amountScale;
    }

    public static TradingPair fromSymbol(Symbol symbol) {
        SymbolTradeLimit limit = symbol.getSymbolTradeLimit();
        Asset source = Asset.fromSymbol(symbol, true);
        Asset destination = Asset.fromSymbol(symbol, false);
        return new TradingPair(source, destination,
                limit.getQuantityScale(), limit.getAmountScale());
    }

    public Asset getSource() {
        return source;
    }

    public void setSource(Asset source) {
        this.source = source;
    }

    public Asset getDestination() {
        return destination;
    }

    public void setDestination(Asset destination) {
        this.destination = destination;
    }

    public String toString() {
        return source.getName() + "_" + destination.getName();
    }

    public boolean equals(TradingPair other) {
        return this.toString().equals(other.toString());
    }

    public void buy(Asset asset) {
        if (asset.getName().equals(source.getName())) {
            System.out.println("buying source");
        } else if (asset.getName().equals(destination.getName())) {
            System.out.println("buying destination");
        }

    }

    public String logPrices() {
        String result = "";
        MarketData data = MarketData.INSTANCE;
        TradingPair pair = data.findTradingPairBetween(source, destination);
        BookEntry entry = data.getDataTable().get(pair);
        if (entry != null) {
            result = String.format("pair %s, price1: %s, price2: %s", this, entry.sourcePrice, entry.destinationPrice);
        } else {
            System.out.printf("ololo %s %s %s\n", source, destination, this);
        }
        return result;
    }
}

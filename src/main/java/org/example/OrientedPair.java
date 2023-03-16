package org.example;

public class OrientedPair extends TradingPair {


    Asset baseCurrency;
    boolean isReversed;

    public OrientedPair(Asset source, Asset destination, int quantityScale, int amountScale, String state, String symbol) {
        super(source, destination, quantityScale, amountScale, state, symbol);
        baseCurrency = destination;
        isReversed = false;
    }


    public void reverse() {
        Asset temp = destination;
        destination = source;
        source = temp;

        isReversed = !isReversed;
    }

    public boolean isReversed() {
        return isReversed;
    }

    public String getSymbol() {
            return isReversed? String.format("%s_%s", destination, source):String.format("%s_%s", source, destination);
    }

    @Override
    public String toString() {

        return String.format("%s_%s%s", source, destination, isReversed ? "r" : "");
    }
}

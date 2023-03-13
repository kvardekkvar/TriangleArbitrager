package org.example;

public class OrientedPair extends TradingPair {


    Asset baseCurrency;
    boolean isReversed;

    public OrientedPair(Asset source, Asset destination) {
        super(source, destination);
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

    public TradingPair getSymbol() {
        if (isReversed) {
            return new TradingPair(destination, source);
        } else {
            return this;
        }
    }

    @Override
    public String toString() {

        return String.format("%s_%s%s", source, destination, isReversed ? "r" : "");
    }
}

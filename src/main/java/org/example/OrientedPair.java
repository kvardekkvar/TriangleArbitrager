package org.example;

public class OrientedPair extends TradingPair {




    boolean isReversed;

    public OrientedPair(Asset source, Asset destination) {
        super(source, destination);
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
    @Override
    public String toString() {

        return String.format("%s_%s%s", source, destination, isReversed?"r":"");
    }
}

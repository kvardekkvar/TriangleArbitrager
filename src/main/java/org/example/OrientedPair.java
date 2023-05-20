package org.example;

public class OrientedPair  {


    private TradingPair pair;
    private boolean isReversed;

    public OrientedPair(TradingPair pair) {
        this.pair = pair;
        isReversed = false;
    }
    public void reverse() {
        isReversed = !isReversed;
    }

    public TradingPair getPair() {
        return pair;
    }

    public boolean isReversed() {
        return isReversed;
    }

    public String getSymbol() {
            return pair.getSymbol();
    }

    public Asset getSource(){
        return isReversed ? pair.getQuote() : pair.getBase();
    }

    public Asset getDestination(){
        return isReversed ? pair.getBase() : pair.getQuote();
    }

    public Asset getBase(){
        return pair.getBase();
    }

    public Asset getQuote(){
        return pair.getQuote();
    }
    public int getScale(){
        return isReversed ? pair.getAmountScale() : pair.getQuantityScale();
    }

    public String toString() {

        return String.format("%s_%s%s", pair.getBase(), pair.getQuote(), isReversed ? "r" : "");
    }
}

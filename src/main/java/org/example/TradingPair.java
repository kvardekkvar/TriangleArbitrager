package org.example;

public class TradingPair {

    protected Asset source;


    protected Asset destination;

    public TradingPair(Asset source, Asset destination) {
        this.source = source;
        this.destination = destination;
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
    public boolean equals(TradingPair other){
        return this.toString().equals(other.toString());
    }

    public void buy(Asset asset){
        if (asset.getName().equals(source.getName())){
            System.out.println("buying source");
        }
        else if (asset.getName().equals(destination.getName())){
            System.out.println("buying destination");
        }

    }

    public String logPrices(){
        String result = "";
        BookEntry entry = MarketData.INSTANCE.getDataTable().get(this);
        result = String.format("pair %s, price1: %s, price2: %s", this.toString(), entry.sourcePrice, entry.destinationPrice);
        return result;
    }
}

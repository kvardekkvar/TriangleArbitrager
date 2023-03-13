package org.example;

import java.util.HashMap;

import static java.lang.Math.min;

public class Triangle {


    private final OrientedPair first;

    private final OrientedPair second;

    private final OrientedPair third;

    private double amountToTrade;

    public Triangle(TradingPair first, TradingPair second, TradingPair third) {
        OrientedPair oriented1 = new OrientedPair(first.getSource(), first.getDestination());
        OrientedPair oriented2 = new OrientedPair(second.getSource(), second.getDestination());
        OrientedPair oriented3 = new OrientedPair(third.getSource(), third.getDestination());

        boolean isReverseNeeded = first.getSource().equals(second.getSource()) || first.getSource().equals(third.getSource()) || second.getSource().equals(third.getSource());
        if (isReverseNeeded) {
            if (isReverseOfPairNotNeeded(second, third)) {
                oriented1.reverse();
            }
            if (isReverseOfPairNotNeeded(first, third)) {
                oriented2.reverse();
            }
            if (isReverseOfPairNotNeeded(first, second)) {
                oriented3.reverse();
            }
        }

        this.first = oriented1;
        this.second = oriented2;
        this.third = oriented3;
    }

    public boolean isReverseOfPairNotNeeded(TradingPair one, TradingPair other) {
        return one.getSource().equals(other.getDestination()) || one.getDestination().equals(other.getSource());
    }

    public OrientedPair getFirst() {
        return first;
    }

    public OrientedPair getSecond() {
        return second;
    }

    public OrientedPair getThird() {
        return third;
    }

    public String toString() {
        return first.toString() + " " + second.toString() + " " + third.toString();
    }

    public boolean isProfitable() {
        MarketData marketData = MarketData.INSTANCE;
        Asset asset1 = first.getSource();
        Asset asset2 = second.getSource();
        Asset asset3 = third.getSource();

        double price1 = marketData.getPriceFromPair(asset1, asset2);
        double price2 = marketData.getPriceFromPair(asset2, asset3);
        double price3 = marketData.getPriceFromPair(asset3, asset1);
        price1 = first.isReversed()? 1/price1 : price1;
        price2 = second.isReversed()? 1/price2 : price2;
        price3 = third.isReversed()? 1/price3 : price3;

        if (price1 * price2 * price3 / Math.pow(1 - FeeSchedule.getMultiplicatorFee(), 3) > 1) {
            //System.out.printf("unprofitable by price: %s: %s, %s, %s\n", this, price1, price2, price3);
            return false;
        }

        double amount1 = marketData.getAmountFromPair(asset1, asset2);
        double amount2 = marketData.getAmountFromPair(asset2, asset3);
        double amount3 = marketData.getAmountFromPair(asset3, asset1);
        double amountToTrade = min(min(amount1, amount2*price1), amount3*price1*price2);

        boolean amountLimitation = amountToTrade < asset1.getMinAmount() ||
                amountToTrade*price1 < asset2.getMinAmount() ||
                amountToTrade*(price1*price2) < asset3.getMinAmount();



        if (amountLimitation) {
            /*System.out.printf("unprofitable by amount: (%s, %s, %s) < (%s, %s, %s) %s\n", amount1, amount2, amount3,
                    asset1.getMinAmount(), asset2.getMinAmount(), asset3.getMinAmount(), this);
             */
            return false;
        }

        this.amountToTrade = amountToTrade;

        System.out.printf("triangle %s is profitable\nPrices %s, %s, %s\n", this, first.logPrices(), second.logPrices(), third.logPrices());
        return true;

    }

    public boolean isProfitableWhenReversed() {

        HashMap<TradingPair, BookEntry> data = MarketData.INSTANCE.getDataTable();
        BookEntry order1 = data.get(first);
        BookEntry order2 = data.get(second);
        BookEntry order3 = data.get(third);

        double price1 = order1.getSourcePrice();
        double price2 = order2.getSourcePrice();
        double price3 = order3.getSourcePrice();

        if (price1 * price2 * price3 / Math.pow(1 - FeeSchedule.getMultiplicatorFee(), 3) > 1) {
            return false;
        }

        double amount1 = order1.getSourceAmount();
        double amount2 = order2.getSourceAmount();
        double amount3 = order3.getSourceAmount();
        double amountToTrade = min(min(amount1, amount2), amount3);

        boolean amountLimitation = amountToTrade < first.getSource().getMinAmount() ||
                amountToTrade < second.getSource().getMinAmount() ||
                amountToTrade < third.getSource().getMinAmount();

        if (amountLimitation) {
            return false;
        }
        this.amountToTrade = amountToTrade;
        return true;
    }

    public void refresh() {
        System.out.println("refreshing " + this.toString());
        return;
    }

    public double getAmountToTrade() {
        return amountToTrade;
    }
}

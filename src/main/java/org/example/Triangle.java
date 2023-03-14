package org.example;

import org.example.util.Constants;

import java.util.HashMap;

import static java.lang.Math.min;

public class Triangle {


    private final OrientedPair first;

    private final OrientedPair second;

    private final OrientedPair third;


    private double amountToTrade1; // Amount of BTC to sell
    private double amountToTrade2; // Amount of X to sell
    private double amountToTrade3; // Amount of Y to sell

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

        if (!oriented1.source.equals(Constants.BTC)) {
            oriented1.reverse();
            oriented2.reverse();
            oriented3.reverse();
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

        //get assets of triangle
        Asset asset1 = first.getSource(); // BTC
        assert asset1.equals(Constants.BTC);
        Asset asset2 = second.getSource();
        Asset asset3 = third.getSource();


        //get prices from marketData
        //price_i is money received when selling 1 unit of asset_i (denominated in base of asset_i, asset_{i+1} pair)
        double price1 = marketData.getPriceFromPair(asset1, asset2);
        double price2 = marketData.getPriceFromPair(asset2, asset3);
        double price3 = marketData.getPriceFromPair(asset3, asset1);

        // needed to deal with the case when two bases coincide
        price1 = first.isReversed() ? 1 / price1 : price1;
        price2 = second.isReversed() ? 1 / price2 : price2;
        price3 = third.isReversed() ? 1 / price3 : price3;

        if (price1 * price2 * price3 * Math.pow(1 - FeeSchedule.getMultiplicatorFee(), 3) < 1) {
            //System.out.printf("unprofitable by price: %s: %s, %s, %s\n", this, price1, price2, price3);
            return false;
        }

        // NOT SURE what it is
        /*double amount1 = marketData.getAmountFromPair(asset1, asset2);
        double amount2 = marketData.getAmountFromPair(asset2, asset3);
        double amount3 = marketData.getAmountFromPair(asset3, asset1);


         amountOfBTCToUse = min(min(amount1, amount2 * price1), amount3 * price1 * price2);

         */
        double amountOfBTCToUse = 0.005;

        boolean amountLimitation = amountOfBTCToUse < asset1.getMinAmount() ||
                amountOfBTCToUse * price1 < asset2.getMinAmount() ||
                amountOfBTCToUse * (price1 * price2) < asset3.getMinAmount();


        if (amountLimitation) {
            return false;
        }

        double fee = (1 - FeeSchedule.getMultiplicatorFee());
        /*
        this.amountToTrade1 = first.isReversed() ? amountOfBTCToUse * price1 * fee : amountOfBTCToUse * fee;
        this.amountToTrade2 = second.isReversed() ? amountOfBTCToUse * price1 * price2 * fee * fee : amountOfBTCToUse * price1 * fee * fee;
        this.amountToTrade3 = third.isReversed() ? amountOfBTCToUse * price1 * price2 * price3 * fee * fee * fee : amountOfBTCToUse * price1 * price2 * fee * fee * fee;
        */
        this.amountToTrade1 = amountOfBTCToUse * price1 * fee;
        this.amountToTrade2 = amountToTrade1 * price2 * fee;
        this.amountToTrade3 = amountToTrade2 * price3 * fee;


        System.out.printf("<TRIANGLE profitable>\n %s \n Prices %s, %s, %s \n Amounts %s, %s, %s \n </TRIANGLE>\n", this,
                first.logPrices(), second.logPrices(), third.logPrices(),
                amountToTrade1, amountToTrade2, amountToTrade3);
        return true;

    }

    public boolean isProfitableWhenReversed() {
        // needs implementation
        return false;
    }

    public void refresh() {
        System.out.println("refreshing " + this.toString());
        return;
    }

    public double getAmountToTrade1() {
        return amountToTrade1;
    }

    public double getAmountToTrade2() {
        return amountToTrade2;
    }

    public double getAmountToTrade3() {
        return amountToTrade3;
    }
}

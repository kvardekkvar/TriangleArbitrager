package org.example;

import org.example.util.Constants;
import org.example.util.Util;

import java.util.Collections;
import java.util.List;

import static org.example.util.Constants.ACCEPTABLE_TRIANGLE_AGE_IN_MILLIS;
import static org.example.util.Constants.AMOUNT_OF_BTC_TO_TRADE;

public class Triangle {


    private final OrientedPair first;

    private final OrientedPair second;

    private final OrientedPair third;


    private final Asset asset1;
    private final Asset asset2;
    private final Asset asset3;
    private double amountToTrade1; // Amount of BTC to sell
    private double amountToTrade2; // Amount of X to sell
    private double amountToTrade3; // Amount of Y to sell

    private final BookEntry entry1;
    private final BookEntry entry2;
    private final BookEntry entry3;

    private double price1;
    private double price2;
    private double price3;
    private final MarketData marketData = MarketData.INSTANCE;
    private double amountOfBTCToUse;

    public Triangle(TradingPair first, TradingPair second, TradingPair third) {
        OrientedPair oriented1 = new OrientedPair(first.getSource(), first.getDestination(), first.quantityScale, first.amountScale, first.getState(), first.getSymbol());
        OrientedPair oriented2 = new OrientedPair(second.getSource(), second.getDestination(), second.quantityScale, second.amountScale, second.getState(), second.getSymbol());
        OrientedPair oriented3 = new OrientedPair(third.getSource(), third.getDestination(), third.quantityScale, third.amountScale, third.getState(), third.getSymbol());

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

        asset1 = this.first.getSource(); // BTC
        asset2 = this.second.getSource();
        asset3 = this.third.getSource();

        entry1 = marketData.getBookEntryByPair(asset1, asset2);
        entry2 = marketData.getBookEntryByPair(asset2, asset3);
        entry3 = marketData.getBookEntryByPair(asset1, asset3);
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

    public boolean triangleIsNew() {
        long timestamp1 = entry1.getTimestampWhenUpdated();
        long timestamp2 = entry2.getTimestampWhenUpdated();
        long timestamp3 = entry3.getTimestampWhenUpdated();
        long timestamp = System.currentTimeMillis();

        return (timestamp - timestamp1 <= ACCEPTABLE_TRIANGLE_AGE_IN_MILLIS)
                && (timestamp - timestamp2 <= ACCEPTABLE_TRIANGLE_AGE_IN_MILLIS)
                && (timestamp - timestamp3 <= ACCEPTABLE_TRIANGLE_AGE_IN_MILLIS);
    }


    public boolean trianglePricesAreProfitable() {
        price1 = first.isReversed() ? 1 / marketData.getAskPrice(asset1, asset2) : marketData.getBidPrice(asset1, asset2);
        price2 = second.isReversed() ? 1 / marketData.getAskPrice(asset2, asset3) : marketData.getBidPrice(asset2, asset3);
        price3 = third.isReversed() ? 1 / marketData.getAskPrice(asset3, asset1) : marketData.getBidPrice(asset3, asset1);

        //System.out.printf("unprofitable by price: %s: %s, %s, %s\n", this, price1, price2, price3);

        return !(price1 * price2 * price3 * Math.pow(1 - FeeSchedule.getMultiplicatorFee(), 3) < 1);
    }

    public double setAmount(OrientedPair pair, Asset firstAsset, Asset secondAsset){
        boolean reversed = pair.isReversed();
        double amount = reversed ? marketData.getAskAmount(firstAsset, secondAsset) : marketData.getBidAmount(firstAsset, secondAsset);
        amount = roundAmount(pair, amount);
        return amount;
    }
    public double roundAmount(OrientedPair pair, double amount){
        boolean reversed = pair.isReversed();
        int scale = reversed ? pair.getAmountScale() : pair.getQuantityScale();
        amount = Util.amountRoundedDown(amount, scale);
        return amount;
    }
    public boolean triangleAmountsAreGreaterThanMinimum() {
        double amount1 = setAmount(first, asset1, asset2);
        double amount2 = setAmount(second, asset2, asset3);
        double amount3 = setAmount(third, asset3, asset1);

        List<Double> amounts = List.of(amount1, amount2 / price1, amount3 / (price1 * price2), AMOUNT_OF_BTC_TO_TRADE);

        amountOfBTCToUse = Collections.min(amounts);

        boolean amountLimitation = amountOfBTCToUse < asset1.getMinAmount() ||
                amountOfBTCToUse * price1 < asset2.getMinAmount() ||
                amountOfBTCToUse * (price1 * price2) < asset3.getMinAmount();

        return !amountLimitation;
    }



    public boolean isProfitable() {


        if (!triangleIsNew()) {
            return false;
        }
        if (!trianglePricesAreProfitable()) {
            return false;
        }

        if (!triangleAmountsAreGreaterThanMinimum()) {
            return false;
        }


        double fee = (1 - FeeSchedule.getMultiplicatorFee());
        amountToTrade1 = roundAmount(first, amountOfBTCToUse * fee);
        amountToTrade2 = roundAmount(second, amountToTrade1 * price1 * fee);
        amountToTrade3 = roundAmount(third, amountToTrade2 * price2 * fee);




        System.out.printf("<TRIANGLE profitable>\n %s \n Prices %s, %s, %s \n Amounts %s, %s, %s \n </TRIANGLE>\n", this,
                first.logPrices(), second.logPrices(), third.logPrices(),
                amountToTrade1, amountToTrade2, amountToTrade3);
        return true;

    }

    public boolean isProfitableWhenReversed() {
        // needs implementation
        return false;
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

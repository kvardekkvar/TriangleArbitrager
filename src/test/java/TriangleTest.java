import org.example.*;
import org.example.util.Constants;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.SQLOutput;

import static org.example.util.Constants.NORMAL_STATE;

@RunWith(MockitoJUnitRunner.class)

public class TriangleTest {

    @Mock
    MarketData marketData;

    @Test
    public void amountsTest() {

        //Arrange
        Asset BTC = Constants.BTC;
        Asset USDT = new Asset("USDT", 10);
        Asset ETH = new Asset("ETH", 10);

        TradingPair pair1 = new TradingPair(BTC, USDT, 3, 0, NORMAL_STATE, "BTC_USDT", 0.00001, 0.00001);
        TradingPair pair2 = new TradingPair(ETH, USDT, 1, 0, NORMAL_STATE, "ETH_USDT", 0.00001, 0.00001);
        TradingPair pair3 = new TradingPair(ETH, BTC, 1, 3, NORMAL_STATE, "ETH_BTC", 0.00001, 0.00001);

        Triangle triangle = new Triangle(pair1, pair2, pair3);
        MarketData data = MarketData.INSTANCE;

        data.addPair(pair1);
        data.addPair(pair2);
        data.addPair(pair3);
        data.initialize();
        BookEntry entry1 = new BookEntry(pair1, 20000, 864.29448, 20010, 1);
        BookEntry entry2 = new BookEntry(pair2, 10000, 2, 100, 0.2357);
        BookEntry entry3 = new BookEntry(pair3, 0.5, 35.425882, 0.52, 1);

        // Продаем BTC (bid)  -- scale 3
        // Покупаем ETH (ask) -- scale 0
        // Продаем ETH (bid)  -- scale 1

        double expected1 = 864.294;
        double expected2 = 24;
        double expected3 = 35.4;


        data.setBookEntryAtTradingPair(pair1, entry1);
        data.setBookEntryAtTradingPair(pair2, entry2);
        data.setBookEntryAtTradingPair(pair3, entry3);

        OrientedPair oriented1 = triangle.getFirst();
        OrientedPair oriented2 = triangle.getSecond();
        OrientedPair oriented3 = triangle.getThird();
        //Act
        double amount1 = triangle.setAmount(oriented1, oriented1.getSource(), oriented1.getDestination());
        double amount2 = triangle.setAmount(oriented2, oriented2.getSource(), oriented2.getDestination());
        double amount3 = triangle.setAmount(oriented3, oriented3.getSource(), oriented3.getDestination());


        //Assert
        double precisionDelta = 0.00001;
        System.out.printf("%s %s %s", amount1, amount2, amount3);
        Assert.assertEquals(expected1, amount1, precisionDelta);
        Assert.assertTrue(oriented2.isReversed());
        Assert.assertEquals(expected2, amount2, precisionDelta);
        Assert.assertEquals(expected3, amount3, precisionDelta);

    }


    @Test
    public void BTC_ETH_USDT() {
        //DO NOT CHANGE!
        // has 10 BTC
        // ETH_BTC_r: BUY <!-- 100 --> ETH for 10 BTC, ask
        // ETH_USDT: SELL 100 ETH, bid
        // BTC_USDT_r: BUY <!--10.5--> BTC for 21000 USDT, ask

        double modifier = 1E-4;

        double BTC_ETH_price = 0.1f;
        double ETH_USDT_price = 2100f;
        double BTC_USDT_price = 20000f;


        Asset BTC = new Asset("BTC", 0);
        Asset USDT = new Asset("USDT", 0);
        Asset ETH = new Asset("ETH", 0);

        TradingPair ETH_BTC = new TradingPair(ETH, BTC, 8, 8, NORMAL_STATE, "ETH_BTC", 0.00001, 0.00001);
        TradingPair ETH_USDT = new TradingPair(ETH, USDT, 8, 8, NORMAL_STATE, "ETH_USDT", 0.00001, 0.00001);
        TradingPair BTC_USDT = new TradingPair(BTC, USDT, 8, 8, NORMAL_STATE, "BTC_USDT", 0.00001, 0.00001);


        Mockito.when(marketData.getAskPrice(ETH_BTC)).thenReturn(BTC_ETH_price);
        Mockito.when(marketData.getBidPrice(ETH_USDT)).thenReturn(ETH_USDT_price);
        Mockito.when(marketData.getAskPrice(BTC_USDT)).thenReturn(BTC_USDT_price);

        Mockito.when(marketData.getAskAmount(ETH_BTC)).thenReturn(20 * modifier);
        Mockito.when(marketData.getBidAmount(ETH_USDT)).thenReturn(20 * modifier);
        Mockito.when(marketData.getAskAmount(BTC_USDT)).thenReturn(2.1 * modifier);

        BookEntry entry1 = new BookEntry(ETH_BTC, 2, 2, BTC_ETH_price, 20 * modifier);
        BookEntry entry2 = new BookEntry(ETH_USDT, ETH_USDT_price, 20 * modifier, 2, 2);
        BookEntry entry3 = new BookEntry(BTC_USDT, 2, 2, BTC_USDT_price, 2.1 * modifier);
        long timestamp = System.currentTimeMillis();
        entry1.setTimestampWhenUpdated(timestamp);
        entry2.setTimestampWhenUpdated(timestamp);
        entry3.setTimestampWhenUpdated(timestamp);

        Mockito.when(marketData.getBookEntryByPair(ETH_BTC)).thenReturn(entry1);
        Mockito.when(marketData.getBookEntryByPair(ETH_USDT)).thenReturn(entry2);
        Mockito.when(marketData.getBookEntryByPair(BTC_USDT)).thenReturn(entry3);

        Triangle triangle = new Triangle(marketData, ETH_BTC, ETH_USDT, BTC_USDT);

        Assert.assertTrue(triangle.triangleIsNew());
        Assert.assertTrue(triangle.trianglePricesAreProfitable());
        Assert.assertTrue(triangle.triangleAmountsAreGreaterThanMinimum());
        Assert.assertTrue(triangle.isProfitable());

        System.out.printf("%s BTC, %s ETH, %s USDT\n", triangle.getAmountToTrade1()/modifier, triangle.getAmountToTrade2()/modifier, triangle.getAmountToTrade3()/modifier);

        double acceptable_error = 0.001 * modifier;
        double fee = 1 - FeeSchedule.getMultiplicatorFee();
        Assert.assertEquals(2 * modifier * fee, triangle.getAmountToTrade1(), acceptable_error);
        Assert.assertEquals(20 * modifier * fee * fee, triangle.getAmountToTrade2(), acceptable_error);
        Assert.assertEquals(42000 * modifier * fee * fee * fee, triangle.getAmountToTrade3(), acceptable_error);

    }
    @Test
    public void BTC_FUND_TRX() {
        // has 10 BTC
        // FUND_BTCr: BUY x FUND for 10 BTC, ask
        // FUND_TRX: SELL 1792750 FUND, bid
        // TRX_BTC: SELL 4123325 TRX, bid

        double modifier = 1E-5;

        double BTC_FUND_price = 179275f;
        double FUND_TRX_price = 2.3f;
        double TRX_BTC_price = 1/300000f;

        Asset BTC = new Asset("BTC", 0);
        Asset FUND = new Asset("FUND", 0);
        Asset TRX = new Asset("TRX", 0);

        TradingPair FUND_BTC = new TradingPair(FUND, BTC, 8, 8, NORMAL_STATE, "FUND_BTC", 0.0000001, 0.0000001);
        TradingPair FUND_TRX = new TradingPair(FUND, TRX, 8, 8, NORMAL_STATE, "FUND_TRX", 0.0000001, 0.0000001);
        TradingPair TRX_BTC = new TradingPair(TRX, BTC, 8, 8, NORMAL_STATE, "TRX_BTC", 0.0000001, 0.0000001);


        Mockito.when(marketData.getAskPrice(FUND_BTC)).thenReturn(1/BTC_FUND_price);
        Mockito.when(marketData.getBidPrice(FUND_TRX)).thenReturn(FUND_TRX_price);
        Mockito.when(marketData.getBidPrice(TRX_BTC)).thenReturn(TRX_BTC_price);

        Mockito.when(marketData.getAskAmount(FUND_BTC)).thenReturn(1792750 * modifier);
        Mockito.when(marketData.getBidAmount(FUND_TRX)).thenReturn(4123325 * modifier);
        Mockito.when(marketData.getBidAmount(TRX_BTC)).thenReturn(111111111 * modifier);

        BookEntry entry1 = new BookEntry(FUND_BTC, 2, 2, BTC_FUND_price, 1792750 * modifier);
        BookEntry entry2 = new BookEntry(FUND_TRX, FUND_TRX_price, 4123325 * modifier, 2, 2);
        BookEntry entry3 = new BookEntry(TRX_BTC, TRX_BTC_price, 11.1 * modifier, 2, 0.2);
        long timestamp = System.currentTimeMillis();
        entry1.setTimestampWhenUpdated(timestamp);
        entry2.setTimestampWhenUpdated(timestamp);
        entry3.setTimestampWhenUpdated(timestamp);

        Mockito.when(marketData.getBookEntryByPair(FUND_BTC)).thenReturn(entry1);
        Mockito.when(marketData.getBookEntryByPair(FUND_TRX)).thenReturn(entry2);
        Mockito.when(marketData.getBookEntryByPair(TRX_BTC)).thenReturn(entry3);

        Triangle triangle = new Triangle(marketData, FUND_BTC, FUND_TRX, TRX_BTC);

        Assert.assertTrue(triangle.triangleIsNew());
        Assert.assertTrue(triangle.trianglePricesAreProfitable());
        Assert.assertTrue(triangle.triangleAmountsAreGreaterThanMinimum());
        Assert.assertTrue(triangle.isProfitable());

        System.out.printf("HEY %s BTC, %s FUND, %s TRX\n", triangle.getAmountToTrade1()/modifier, triangle.getAmountToTrade2()/modifier, triangle.getAmountToTrade3()/modifier);


        double acceptable_error = 1 * modifier;
        double fee = 1 - FeeSchedule.getMultiplicatorFee();
        Assert.assertEquals(10 * modifier * fee, triangle.getAmountToTrade1(), acceptable_error);
        Assert.assertEquals(1792750 * modifier * fee * fee, triangle.getAmountToTrade2(), acceptable_error);
        Assert.assertEquals(4123325 * modifier * fee * fee * fee, triangle.getAmountToTrade3(), acceptable_error);

    }

    @Test
    public void pairWithGoodPricesPassesValidation() {
        double modifier = 10e-5;

        double BTC_USDT_price = 20000f;
        double BTC_ETH_price = 0.1f;
        double ETH_USDT_price = 2100f;

        Asset BTC = new Asset("BTC", 0);
        Asset USDT = new Asset("USDT", 0);
        Asset ETH = new Asset("ETH", 0);

        TradingPair ETH_BTC = new TradingPair(ETH, BTC, 8, 8, NORMAL_STATE, "ETH_BTC", 0.00001, 0.00001);
        TradingPair ETH_USDT = new TradingPair(ETH, USDT, 8, 8, NORMAL_STATE, "ETH_USDT", 0.00001, 0.00001);
        TradingPair BTC_USDT = new TradingPair(BTC, USDT, 8, 8, NORMAL_STATE, "BTC_USDT", 0.00001, 0.00001);


        BookEntry entry1 = new BookEntry(ETH_BTC, 2, 2, BTC_ETH_price, 2 * modifier);
        BookEntry entry2 = new BookEntry(ETH_USDT, BTC_ETH_price, 2 * modifier, 2, 2);
        BookEntry entry3 = new BookEntry(BTC_USDT, 2, 2, BTC_ETH_price, 0.21 * modifier);
        long timestamp = System.currentTimeMillis();
        entry1.setTimestampWhenUpdated(timestamp);
        entry2.setTimestampWhenUpdated(timestamp);
        entry3.setTimestampWhenUpdated(timestamp);

        Mockito.when(marketData.getBookEntryByPair(ETH_BTC)).thenReturn(entry1);
        Mockito.when(marketData.getBookEntryByPair(ETH_USDT)).thenReturn(entry2);
        Mockito.when(marketData.getBookEntryByPair(BTC_USDT)).thenReturn(entry3);

        Triangle triangle = new Triangle(marketData, ETH_BTC, ETH_USDT, BTC_USDT);

        boolean actual = triangle.amountsInPairAreGood(triangle.getFirst(), 0.000015, 0.000015);

        Assert.assertTrue(actual);
    }

    }

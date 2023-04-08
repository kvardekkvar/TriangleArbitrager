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

        TradingPair pair1 = new TradingPair(BTC, USDT, 3, 0, NORMAL_STATE, "BTC_USDT");
        TradingPair pair2 = new TradingPair(ETH, USDT, 1, 0, NORMAL_STATE, "ETH_USDT");
        TradingPair pair3 = new TradingPair(ETH, BTC, 1, 3, NORMAL_STATE, "ETH_BTC");

        Triangle triangle = new Triangle(pair1, pair2, pair3);
        MarketData data = MarketData.INSTANCE;

        data.addPair(pair1);
        data.addPair(pair2);
        data.addPair(pair3);
        data.initialize();
        BookEntry entry1 = new BookEntry(pair1, 20000, 864.29448, 20010, 1);
        BookEntry entry2 = new BookEntry(pair2, 10000, 2, 10010, 23.57293);
        BookEntry entry3 = new BookEntry(pair3, 0.5, 35.425882, 0.52, 1);

        // Продаем BTC (bid)  -- scale 3
        // Покупаем ETH (ask) -- scale 0
        // Продаем ETH (bid)  -- scale 1

        double expected1 = 864.294;
        double expected2 = 23;
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
        // has 10 BTC
        // BTC_ETH_r: BUY 100 ETH, ask
        // ETH_BTC: SELL 100 ETH, bid
        // USDT_BTC_r: BUY 10.5 BTC, ask

        double modifier = 0.01;

        double BTC_USDT_price = 20000f;
        double BTC_ETH_price = 0.1f;
        double ETH_USDT_price = 2100f;

        Asset BTC = new Asset("BTC", 0.0001*modifier);
        Asset USDT = new Asset("USDT", 0.0001*modifier);
        Asset ETH = new Asset("ETH", 0.0001*modifier);

        TradingPair ETH_BTC = new TradingPair(ETH, BTC, 8, 8, NORMAL_STATE, "ETH_BTC");
        TradingPair ETH_USDT = new TradingPair(ETH, USDT, 8, 8, NORMAL_STATE, "ETH_USDT");
        TradingPair BTC_USDT = new TradingPair(BTC, USDT, 8, 8, NORMAL_STATE, "BTC_USDT");


        Mockito.when(marketData.getAskPrice(ETH_BTC)).thenReturn(BTC_ETH_price);
        Mockito.when(marketData.getBidPrice(ETH_USDT)).thenReturn(ETH_USDT_price);
        Mockito.when(marketData.getAskPrice(BTC_USDT)).thenReturn(BTC_USDT_price);

        Mockito.when(marketData.getAskAmount(ETH_BTC)).thenReturn(100.0*modifier);
        Mockito.when(marketData.getBidAmount(ETH_USDT)).thenReturn(100.0*modifier);
        Mockito.when(marketData.getAskAmount(BTC_USDT)).thenReturn(10.5*modifier);
        
        BookEntry entry1 = new BookEntry(ETH_BTC, 2, 2, BTC_ETH_price, 100d*modifier);
        BookEntry entry2 = new BookEntry(ETH_USDT, BTC_ETH_price, 100*modifier, 2, 2);
        BookEntry entry3 = new BookEntry(BTC_USDT, 2, 2, BTC_ETH_price, 10.5*modifier);
        long timestamp = System.currentTimeMillis();
        entry1.setTimestampWhenUpdated(timestamp);
        entry2.setTimestampWhenUpdated(timestamp);
        entry3.setTimestampWhenUpdated(timestamp);

        Mockito.when(marketData.getBookEntryByPair(ETH_BTC)).thenReturn(entry1);
        Mockito.when(marketData.getBookEntryByPair(ETH_USDT)).thenReturn(entry2);
        Mockito.when(marketData.getBookEntryByPair(BTC_USDT)).thenReturn(entry3);

        Triangle triangle = new Triangle(marketData, ETH_BTC, ETH_USDT, BTC_USDT);
        Assert.assertTrue(triangle.isProfitable());

        System.out.printf("%s, %s, %s\n", triangle.getAmountToTrade1(), triangle.getAmountToTrade2(), triangle.getAmountToTrade3());
        double acceptable_error = 0.000000001*modifier;
        Assert.assertEquals(100.0*modifier, triangle.getAmountToTrade1(), acceptable_error);
        Assert.assertEquals(100.0*modifier, triangle.getAmountToTrade2(), acceptable_error);
        Assert.assertEquals(10.5*modifier, triangle.getAmountToTrade3(), acceptable_error);

    }
}

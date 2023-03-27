import org.example.*;
import org.example.util.Constants;
import org.junit.Assert;
import org.junit.Test;

import static org.example.util.Constants.NORMAL_STATE;

public class TriangleTest {


    @Test
    public void amountsTest(){

        //Arrange
        Asset BTC = Constants.BTC;
        Asset USDT = new Asset("USDT", 10);
        Asset ETH = new Asset("ETH", 10);

        TradingPair pair1 = new TradingPair(BTC, USDT, 3, 0, NORMAL_STATE, "BTC_USDT");
        TradingPair pair2 = new TradingPair(ETH, USDT, 1, 0, NORMAL_STATE, "ETH_USDT" );
        TradingPair pair3 = new TradingPair(ETH, BTC, 1, 3, NORMAL_STATE, "ETH_BTC");
        
        Triangle triangle  = new Triangle(pair1, pair2, pair3);
        MarketData data = MarketData.INSTANCE;

        data.addPair(pair1);
        data.addPair(pair2);
        data.addPair(pair3);
        data.initialize();
        BookEntry entry1 = new BookEntry(pair1, 20000, 864.29448, 20010, 1);
        BookEntry entry2 = new BookEntry(pair1, 10000, 2, 10010, 23.57293);
        BookEntry entry3 = new BookEntry(pair1, 0.5, 35.425882, 0.52, 1);

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
        Assert.assertEquals(true, oriented2.isReversed());
        Assert.assertEquals(expected2, amount2, precisionDelta);
        Assert.assertEquals(expected3, amount3, precisionDelta);

    }
}

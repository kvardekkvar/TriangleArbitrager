import org.example.Asset;
import org.example.TradingPair;
import org.example.Triangle;
import org.junit.Assert;
import org.junit.Test;

public class TriangleTest {


    @Test
    public void changingField(){

        Asset asset1 = new Asset("ololo1", 1);
        Asset asset2 = new Asset("ololo2", 2);
        Asset asset3 = new Asset("ololo3", 3);
        TradingPair pair1 = new TradingPair(asset1, asset2);
        TradingPair pair2 = new TradingPair(asset2, asset3);
        TradingPair pair3 = new TradingPair(asset3, asset1);

        Triangle triangle = new Triangle(pair1, pair2, pair3);
        int hash1 = triangle.hashCode();

        triangle.setAmountToTrade(200000);

        int hash2 = triangle.hashCode();

        Assert.assertEquals(hash1, hash2);
    }
}

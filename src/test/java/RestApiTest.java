import org.example.API.MsgHandler;
import org.example.API.PoloniexApi;
import org.example.Asset;
import org.example.OrientedPair;
import org.junit.Test;

public class RestApiTest {

    @Test
    public void testPostRequestOrders() {

        PoloniexApi api = new PoloniexApi();
        Asset asset1 = new Asset("BTC", 0.0001, 6);
        Asset asset2 = new Asset("USDT", 1, 2);
        MsgHandler handler = new MsgHandler();
        handler.setApi(api);
        OrientedPair orientedPair = new OrientedPair(asset1, asset2);
        handler.sendBuyMessage(orientedPair, 10000000);
    }

}

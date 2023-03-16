import org.example.API.MsgHandler;
import org.junit.Test;

public class MessageHandlerTest {

    @Test
    public void handleMessage(){
        String message ="{\n" +
                "  \"channel\": \"book\",\n" +
                "  \"data\": [{\n" +
                "    \"symbol\": \"BTC_USDT\",\n" +
                "    \"createTime\": 1648052239156,\n" +
                "    \"asks\": [],\n" +
                "    \"bids\": [\n" +
                "      [\"40001.5\", \"2.87\"],\n" +
                "      [\"39999.4\", \"1\"]\n" +
                "    ],\n" +
                "    \"id\": 123456,\n" +
                "    \"ts\": 1648052239192\n" +
                "  }, \n" +
                "  {\n" +
                "    \"symbol\": \"BTC_USDT\",\n" +
                "    \"createTime\": 1648052239156,\n" +
                "    \"asks\": [],\n" +
                "    \"bids\": [\n" +
                "      [\"40001\", \"2.87\"],\n" +
                "      [\"39999\", \"1\"]\n" +
                "    ],\n" +
                "    \"id\": 345678,\n" +
                "    \"ts\": 1648052239192\n" +
                "  }]\n" +
                "}";

        MsgHandler handler = new MsgHandler();

        handler.handleMessage(message);
    }
}

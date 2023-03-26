import org.example.API.JSONHandler;
import org.example.models.symbols_request.Symbol;
import org.junit.Assert;
import org.junit.Test;

public class JSONTest {

    @Test
    public void serializeTest(){
        Symbol symb = new Symbol();
        symb.setDisplayName("ololo");
        JSONHandler jsoner = new JSONHandler();

        String expected = "{\"displayName\": \"ololo\"}";
        String actual = jsoner.toJSON(symb, Symbol.class);

        Assert.assertEquals(expected, actual);
    }
}

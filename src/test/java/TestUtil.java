import org.example.util.Util;
import org.junit.Assert;
import org.junit.Test;

public class TestUtil {

    @Test
    public void formattedAmontTest(){
        double amount = 0.1 + 0.2;

        String expected = "0.30000000";
        String actual = Util.formattedAmount(amount);

        Assert.assertEquals(expected, actual);
    }
}

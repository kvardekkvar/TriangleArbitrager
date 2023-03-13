import org.example.util.Util;
import org.junit.Assert;
import org.junit.Test;

public class TestUtil {

    @Test
    public void formattedAmountTest(){
        double amount = 0.1234567;

        String expected = "0.123";
        String actual = Util.formattedAmount(amount, 3);

        Assert.assertEquals(expected, actual);
    }
}

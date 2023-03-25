import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.example.API.Crypto;
import org.junit.Assert;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

import org.apache.commons.codec.binary.Base64;

public class AuthTest {

    @Test
    public void testEncryption() {
        Crypto crypto = Crypto.INSTANCE;
        long timestamp = 1234567890;
        String request = String.format("GET\n" +
                "/ws\n" +
                "signTimestamp=%d", timestamp);

        String authToken = crypto.getSignature(request);
        String expected = "238Fx6Ogj7bpu0qYy4FvY9yndtDVF4AKRkjEwIdx8Mw=";

        Assert.assertEquals(String.format("got %s", authToken), expected, authToken);
    }


    @Test
    public void testBase64() throws DecoderException {
        String input = "e60e11c790366cbc8c585811dc0aa9f81e30fa22642fb9f9383dd2b98b4224af";
        byte[] decodedHex = Hex.decodeHex(input);

        byte[] output = Base64.encodeBase64(decodedHex);

        String expected = "5g4Rx5A2bLyMWFgR3Aqp+B4w+iJkL7n5OD3SuYtCJK8=";
        String actual = new String(output, StandardCharsets.UTF_8);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testHmac() {
        Crypto crypto = Crypto.INSTANCE;
        String key = "key";
        String input = "The quick brown fox jumps over the lazy dog";

        String hmac = crypto.getHMAC(key, input);
        String expected = "f7bc83f430538424b13298e6aa6fb143ef4d59a14946175997479dbc2d1a3cd8";

        Assert.assertEquals(String.format("got %s", hmac), expected, hmac);
    }

    @Test
    public void getBytesTest() {
        String input = "Ded Moroz\n {} \" \" ";

        byte[] array = input.getBytes(StandardCharsets.UTF_8);

        String actual = new String(array, StandardCharsets.UTF_8);

        Assert.assertEquals(input, actual);

    }
}

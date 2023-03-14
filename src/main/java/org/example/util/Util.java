package org.example.util;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Arrays;

public class Util {

    public static String formattedAmount(double amount, int precision) {
        StringBuilder pattern = new StringBuilder("0.");
        int i = 0;
        while (i < precision) {
            pattern.append("0");
            i++;
        }
        DecimalFormat df = new DecimalFormat(new String(pattern));
        df.setRoundingMode(RoundingMode.HALF_UP);
        return df.format(amount).replace(',', '.');
    }
}

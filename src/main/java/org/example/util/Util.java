package org.example.util;

public class Util {

    public static String formattedAmount(double amount){

        return String.format("%.8g", amount).replace(',', '.');
    }
}

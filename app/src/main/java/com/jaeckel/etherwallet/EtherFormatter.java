package com.jaeckel.etherwallet;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Locale;

public class EtherFormatter {

    public static String formatWeiAsEther(long wei) {
        double ether = (double) wei / 1000000000000000000L;
        return String.format(Locale.getDefault(), "%.2f" + "ETH", ether);
    }

    public static String formatWeiAsEther(String wei) {
        return formatWeiAsEther(Long.parseLong(wei));
    }

    public static String formatWeiAsEther(BigInteger wei) {
        BigDecimal weiDecimal = new BigDecimal(wei);
        BigDecimal ether = weiDecimal.divide(new BigDecimal("1000000000000000000"));
        return String.format(Locale.getDefault(), "%.2f" + "ETH", ether);
    }
}

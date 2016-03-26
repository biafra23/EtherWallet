package com.jaeckel.etherwallet;

import java.util.Locale;

public class EtherFormatter {

    public static String formatWeiAsEther(long wei) {
        double ether = (double) wei / 1000000000000000000L;

        return String.format(Locale.getDefault(), "%.2f" + "ETH", ether);
    }
}

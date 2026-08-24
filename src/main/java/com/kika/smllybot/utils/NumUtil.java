package com.kika.smllybot.utils;

import java.util.Locale;

public class NumUtil {

    public static String german(long num) {
        return String.format(Locale.GERMAN, "%,d", num);
    }

    public static String german(double num) {
        return String.format(Locale.GERMAN, "%.2f", num);
    }

    public static String us(double num) {
        return String.format(Locale.US, "%.2f", num);
    }

}

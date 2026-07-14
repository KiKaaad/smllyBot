package com.kika.smllybot.utils;

import java.util.Locale;

public class Formatter {

    public static String germanNum(long num) {
        return String.format(Locale.GERMAN, "%,d", num);
    }

    public static String usNum(double num) {
        return String.format(Locale.US, "%.2f", num);
    }

}

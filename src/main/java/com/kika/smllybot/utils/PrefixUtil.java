package com.kika.smllybot.utils;

public class PrefixUtil {
    public static String getCommandBody(String content, String[] prefixes) {
        for (String prefix : prefixes) {
            if (content.length() >= prefix.length() && content.substring(0, prefix.length()).equalsIgnoreCase(prefix)) {
                return content.substring(prefix.length());
            }
        }
        return null;
    }

}


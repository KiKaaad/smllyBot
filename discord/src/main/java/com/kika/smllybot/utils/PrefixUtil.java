package com.kika.smllybot.utils;

public class PrefixUtil {

    public static String getCommandBody(String content, String[] prefixes) {
        if (content == null || content.isEmpty()) return null;

        String match = null;

        for (String prefix : prefixes) {
            if (content.regionMatches(true, 0, prefix, 0, prefix.length())) {
                if (match == null || prefix.length() > match.length()) {
                    match = prefix;
                }
            }
        }

        if (match != null) {
            return content.substring(match.length()).trim();
        }

        return null;
    }

}



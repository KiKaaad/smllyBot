package com.kika.smllybot.utils;

public class StackTrace {

    public static String getCallerLocation() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

        if (stackTrace.length > 3) {
            StackTraceElement caller = stackTrace[3];
            return "%s.%s()".formatted(caller.getClassName(), caller.getMethodName());
        }

        return "unknown";
    }
}

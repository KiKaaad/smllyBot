package com.kika.smllybot.modules.fun;

public record FunContext(
        String emoji, String action,
        long author,
        long reply,
        String afterText,
        String replica) {}

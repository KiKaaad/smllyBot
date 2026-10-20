package com.kika.smllybot.modules.economy;

public record FarmContext(
        long baseReward,
        long finalReward,
        double starMultiplier,
        String multiplierText,
        String cooldown
) {}

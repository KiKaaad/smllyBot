package com.kika.smllybot.modules.economy;

public record FarmContext(
        long baseReward,
        long finalReward,
        String multiplierText,
        String cooldown
) {}

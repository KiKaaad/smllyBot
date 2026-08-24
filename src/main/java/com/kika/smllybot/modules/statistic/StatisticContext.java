package com.kika.smllybot.modules.statistic;

import net.dv8tion.jda.api.entities.User;

public record StatisticContext(
        String jdaVersion,
        int shardTotal,
        long serverCount,
        long userCount,
        String botAvatarUrl,
        User user
) {}

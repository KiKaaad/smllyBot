package com.kika.smllybot.modules.statistic;

import net.dv8tion.jda.api.entities.User;

public record StatisticContext(
        String jdaVersion,
        int shardTotal,
        long serversCount,
        long userCount,
        String botAvatarUrl,
        User user
) {}

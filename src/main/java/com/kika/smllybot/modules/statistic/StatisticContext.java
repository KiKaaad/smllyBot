package com.kika.smllybot.modules.statistic;

public record StatisticContext(
        String jdaVersion,
        int shardTotal,
        long serversCount,
        long userCount,
        String botAvatarUrl,
        String botBannerUrl
) {}

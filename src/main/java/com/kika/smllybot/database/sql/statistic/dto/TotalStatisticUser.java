package com.kika.smllybot.database.sql.statistic.dto;

public record TotalStatisticUser(
        long day,
        long week,
        long month,
        long total
) {}

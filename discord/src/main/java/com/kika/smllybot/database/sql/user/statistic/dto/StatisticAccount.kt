package com.kika.smllybot.database.sql.user.statistic.dto

data class StatisticAccount(
    val day: Long,
    val week: Long,
    val month: Long,
    val total: Long
)

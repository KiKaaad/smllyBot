package com.kika.smllybot.database.sql.ban.dto

import java.time.OffsetDateTime

data class BanCreateData(
    val guildId: Long,
    val discordId: Long,
    val byDiscordId: Long,
    val reason: String?,
    val until: OffsetDateTime?
)

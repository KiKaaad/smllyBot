package com.kika.smllybot.database.sql.guild.warn.dto

import java.time.OffsetDateTime

data class WarnCreateData(
    val guildId: Long,
    val discordId: Long,
    val byDiscordId: Long,
    val reason: String?,
    val until: OffsetDateTime?
)

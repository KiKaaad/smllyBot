package com.kika.smllybot.database.sql.guild.warn.dto

import java.time.OffsetDateTime

data class WarnData(
    val id: Long,
    val guildId: Long,
    val discordId: Long,
    val byDiscordId: Long,
    val reason: String?,
    val createdAt: OffsetDateTime,
    val until: OffsetDateTime?,
    val removedAt: OffsetDateTime?,
    val removedBy: Long?,
    val active: Boolean
)

package com.kika.smllybot.database.sql.mute.dto

import java.time.OffsetDateTime

data class MuteData(
    val id: Long,
    val muteType: String,
    val guildId: Long,
    val discordId: Long,
    val byDiscordId: Long,
    val reason: String,
    val createdAt: OffsetDateTime,
    val until: OffsetDateTime,
    val removedAt: OffsetDateTime,
    val removedBy: Long,
    val active: Boolean
)
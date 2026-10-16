package com.kika.smllybot.database.sql.mute.dto

import java.time.OffsetDateTime

data class MuteCreateData(
    val muteType: String,
    val guildId: Long,
    val discordId: Long,
    val byDiscordId: Long,
    val reason: String? = null,
    val until: OffsetDateTime? = null,
)
package com.kika.smllybot.database.sql.profile.dto

import java.time.OffsetDateTime

data class ProfileAccount(
    val id: Long,
    val guildId: Long,
    val name: String,
    val aboutMe: String?,
    val createdAt: OffsetDateTime
)
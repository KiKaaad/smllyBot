package com.kika.smllybot.database.sql.user.users.dto

import java.time.OffsetDateTime

data class UserAccount(
    val id: Long,
    val role: String?,
    val name: String,
    val discordId: Long,
    val createdAt: OffsetDateTime,
    val motto: String?,
    val reaction: Long,
    val citizenship: Long?,
    val citizenshipData: OffsetDateTime?,
    val badges: List<String>?
)

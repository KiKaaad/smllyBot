package com.kika.smllybot.database.sql.users.dto

import java.time.OffsetDateTime

data class UserAccount(
    val id: Long,
    val discordId: Long,
    val role: String?,
    val name: String,
    val motto: String?,
    val createdAt: OffsetDateTime,
    val reaction: Long,
    val citizenship: Long?,
    val citizenshipData: OffsetDateTime?
)

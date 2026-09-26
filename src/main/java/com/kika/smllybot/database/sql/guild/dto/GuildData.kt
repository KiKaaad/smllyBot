package com.kika.smllybot.database.sql.guild.dto

data class GuildData(
    val id: Long,
    val name: String,
    val staging: Boolean,
    val muteType: String,
    val muteRole: Long?
)

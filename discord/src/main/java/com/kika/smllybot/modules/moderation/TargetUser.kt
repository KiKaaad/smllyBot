package com.kika.smllybot.modules.moderation

import net.dv8tion.jda.api.entities.UserSnowflake

data class TargetUser(
    val user: UserSnowflake,
    val duration: Long?,
    val unit: String?,
    val reason: String?
)

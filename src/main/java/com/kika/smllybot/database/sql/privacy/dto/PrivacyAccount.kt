package com.kika.smllybot.database.sql.privacy.dto

data class PrivacyAccount(
    val id: Long,
    val bag: Boolean,
    val activity: Boolean,
    val lastActivity: Boolean
)

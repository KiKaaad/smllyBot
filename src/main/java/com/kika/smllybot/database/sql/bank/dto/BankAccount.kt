package com.kika.smllybot.database.sql.bank.dto

import java.sql.Timestamp

data class BankAccount(
    val id: Long,
    val name: String,
    val star: Long,
    val iris: Long,
    val irisCoin: Long,
    val lastFarm: Timestamp
)

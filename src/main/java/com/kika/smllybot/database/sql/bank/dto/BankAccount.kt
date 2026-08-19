package com.kika.smllybot.database.sql.bank.dto;

import java.sql.Timestamp;

public record BankAccount(
        long id, String name,
        long star, long iris,
        long irisCoin, Timestamp lastFarm) {}

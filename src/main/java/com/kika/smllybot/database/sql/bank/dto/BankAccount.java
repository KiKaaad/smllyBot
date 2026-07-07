package com.kika.smllybot.database.sql.bank.dto;

import java.sql.Timestamp;

public record BankAccount(
        int id, String name,
        int star, long iris,
        long irisCoin, Timestamp lastFarm) {}

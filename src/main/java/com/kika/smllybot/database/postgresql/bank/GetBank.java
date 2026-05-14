package com.kika.smllybot.database.postgresql.bank;

import java.sql.Timestamp;

public record GetBank(int id, String name, long iris, long irisCoin, Timestamp lastFarm) {}

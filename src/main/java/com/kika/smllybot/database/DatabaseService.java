package com.kika.smllybot.database;

import com.kika.smllybot.database.postgresql.bank.BankTable;
import com.kika.smllybot.database.postgresql.bank.GetBank;
import com.kika.smllybot.database.postgresql.user.GetUsers;
import com.kika.smllybot.database.postgresql.user.UserTable;

public class DatabaseService {
    public static UsersData getFullData(long discordId, String username) {

        GetUsers user = UserTable.getOrCreateUser(discordId);
        GetBank bank = BankTable.getOrCreateBank(user.internalId(), username);

        return new UsersData(user, bank);
    }
}

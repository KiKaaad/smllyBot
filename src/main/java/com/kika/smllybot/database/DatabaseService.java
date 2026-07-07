package com.kika.smllybot.database;


import com.kika.smllybot.database.sql.bank.BankTable;
import com.kika.smllybot.database.sql.bank.dto.BankAccount;
import com.kika.smllybot.database.sql.user.dto.UserAccount;
import com.kika.smllybot.database.sql.user.UserTable;

@Deprecated
public class DatabaseService {
    public static UsersData getFullData(long discordId, String username) {

        UserAccount user = UserTable.getOrCreateUser(discordId, username);
        BankAccount bank = BankTable.getOrCreateBank(user.internalId(), username);

        return new UsersData(user, bank);
    }
}
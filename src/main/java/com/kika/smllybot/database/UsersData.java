package com.kika.smllybot.database;

import com.kika.smllybot.database.sql.bank.dto.BankAccount;
import com.kika.smllybot.database.sql.user.dto.UserAccount;

@Deprecated
public record UsersData(UserAccount dbUser, BankAccount dbBank) {}

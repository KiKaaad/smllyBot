package com.kika.smllybot.database;

import com.kika.smllybot.database.postgresql.bank.GetBank;
import com.kika.smllybot.database.postgresql.user.GetUsers;

public record UsersData(GetUsers dbUser, GetBank dbBank) {}

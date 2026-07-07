package com.kika.smllybot.modules.economy;

import com.kika.smllybot.database.sql.bank.dto.BankAccount;
import net.dv8tion.jda.api.entities.User;

public record BagContext(
        User target,
        BankAccount bank
) {}

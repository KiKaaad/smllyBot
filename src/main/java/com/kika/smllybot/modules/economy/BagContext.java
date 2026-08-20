package com.kika.smllybot.modules.economy;

import com.kika.smllybot.database.sql.bank.dto.BankAccount;
import com.kika.smllybot.database.sql.privacy.dto.PrivacyAccount;
import net.dv8tion.jda.api.entities.User;

public record BagContext(
        long author,
        User target,
        BankAccount bank,
        PrivacyAccount privacy
) {}

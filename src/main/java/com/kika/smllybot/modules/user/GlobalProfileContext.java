package com.kika.smllybot.modules.user;

import com.kika.smllybot.database.sql.bank.dto.BankAccount;
import com.kika.smllybot.database.sql.privacy.dto.PrivacyAccount;
import com.kika.smllybot.database.sql.user.dto.UserAccount;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;

public record GlobalProfileContext(
        User target,        // Чей профиль смотрим
        User viewer,        // Кто профиль смотрит
        Member member,      // Отображение активности (онлайн / оффлайн)
        UserAccount user,
        BankAccount bank,
        PrivacyAccount privacy
) {}

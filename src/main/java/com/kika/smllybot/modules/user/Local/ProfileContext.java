package com.kika.smllybot.modules.user.Local;

import com.kika.smllybot.database.sql.bank.dto.BankAccount;
import com.kika.smllybot.database.sql.profile.dto.ProfileAccount;
import com.kika.smllybot.database.sql.statistic.dto.StatisticAccount;
import com.kika.smllybot.database.sql.users.dto.UserAccount;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;

public record ProfileContext(
        User target,        // Чей профиль смотрим
        User viewer,        // Кто профиль смотрит
        Member member,
        ProfileAccount profile,
        StatisticAccount statistic,
        UserAccount user,
        BankAccount bank,
        long guildId
) {}

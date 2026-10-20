package com.kika.smllybot.modules.user.Local;

import com.kika.smllybot.database.sql.user.bank.dto.BankAccount;
import com.kika.smllybot.database.sql.user.profile.dto.ProfileAccount;
import com.kika.smllybot.database.sql.user.statistic.dto.StatisticAccount;
import com.kika.smllybot.database.sql.user.users.dto.UserAccount;
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

package com.kika.smllybot.modules.user;

import com.kika.smllybot.database.UsersData;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;

public record GlobalProfileContext(
        User target,   // Чей профиль смотрим
        User viewer,   // Кто профиль смотрит
        Member member, // Отображение активности (онлайн / оффлайн)
        UsersData data // Данные с базы данных
) {}

package com.kika.smllybot.modules.user.Local;

import com.kika.smllybot.database.sql.users.dto.UserAccount;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

public record CitizenshipContext(
        String text,
        String button,
        Member member,
        Guild guild,
        UserAccount user
) {}

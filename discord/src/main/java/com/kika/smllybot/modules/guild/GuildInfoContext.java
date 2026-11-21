package com.kika.smllybot.modules.guild;

import com.kika.smllybot.database.sql.guild.guild.dto.GuildData;
import net.dv8tion.jda.api.entities.Guild;

public record GuildInfoContext(
        GuildData guildData,
        Guild guild
) {}

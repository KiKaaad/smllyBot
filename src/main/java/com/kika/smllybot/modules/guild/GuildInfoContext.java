package com.kika.smllybot.modules.guild;

import com.kika.smllybot.database.sql.guild.dto.GuildData;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public record GuildInfoContext(
        GuildData guild,
        MessageReceivedEvent event
) {}

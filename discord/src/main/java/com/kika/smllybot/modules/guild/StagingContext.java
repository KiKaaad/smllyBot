package com.kika.smllybot.modules.guild;

import com.kika.smllybot.database.sql.guild.guild.dto.GuildData;

public record StagingContext(
        GuildData guild,
        long authorId
) {}

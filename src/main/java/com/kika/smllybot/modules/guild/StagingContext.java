package com.kika.smllybot.modules.guild;

import com.kika.smllybot.database.sql.guild.dto.GuildFull;

public record StagingContext(
        GuildFull guild,
        long authorId
) {}

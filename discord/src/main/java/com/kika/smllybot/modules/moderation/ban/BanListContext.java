package com.kika.smllybot.modules.moderation.ban;

import com.kika.smllybot.database.sql.guild.ban.dto.BanData;

import java.util.List;

public record BanListContext(
        List<BanData> data,
        long owner
) {}

package com.kika.smllybot.modules.moderation.mute;

import com.kika.smllybot.database.sql.mute.dto.MuteData;

import java.util.List;

public record MuteListContext(
        List<MuteData> data,
        long owner
) {}

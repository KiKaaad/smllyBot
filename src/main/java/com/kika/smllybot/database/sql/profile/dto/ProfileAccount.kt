package com.kika.smllybot.database.sql.profile.dto;

import java.time.OffsetDateTime;

public record ProfileAccount(
        long id,
        long guildId,
        String name,
        String aboutMe,
        OffsetDateTime createdAt
) {}

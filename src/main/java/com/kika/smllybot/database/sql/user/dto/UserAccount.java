package com.kika.smllybot.database.sql.user.dto;

import java.time.OffsetDateTime;

public record UserAccount(long internalId, long discordId,
                          String role,
                          String name,
                          String motto,
                          OffsetDateTime createdAt) {}

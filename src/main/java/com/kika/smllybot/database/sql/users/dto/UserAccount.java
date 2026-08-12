package com.kika.smllybot.database.sql.users.dto;

import java.time.OffsetDateTime;

public record UserAccount(long id, long discordId,
                          String role,
                          String name,
                          String motto,
                          OffsetDateTime createdAt,
                          int reaction,
                          Long citizenship,
                          OffsetDateTime citizenshipData
) {}

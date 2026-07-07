package com.kika.smllybot.database.sql.user.dto;

public record UserAccount(int internalId, long discordId,
                          String role,
                          String name,
                          String motto,
                          String createdAt) {}

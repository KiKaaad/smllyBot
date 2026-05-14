package com.kika.smllybot.database.postgresql.user;

public record GetUsers(int internalId, long discordId, String motto, String createdAt) {}

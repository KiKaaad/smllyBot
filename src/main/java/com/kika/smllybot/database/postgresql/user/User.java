package com.kika.smllybot.database.postgresql.user;

public record User(int internalId, long discordId, String aboutMe) {}

package com.kika.smllybot.database.sql.privacy.dto;

public record PrivacyAccount(
        int id,
        boolean bag, boolean activity, boolean lastActivity) {}

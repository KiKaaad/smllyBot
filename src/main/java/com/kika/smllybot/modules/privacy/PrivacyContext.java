package com.kika.smllybot.modules.privacy;

import com.kika.smllybot.database.sql.privacy.dto.PrivacyAccount;

public record PrivacyContext(long discordId, PrivacyAccount privacy) {
}

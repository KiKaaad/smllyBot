package com.kika.smllybot.modules.tops.Global;

import com.kika.smllybot.database.sql.bank.dto.BankTopAmount;

import java.util.List;

public record GlobalTopContext(
        List<BankTopAmount> bank,
        String value,
        long owner
) {}

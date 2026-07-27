package com.kika.smllybot.modules.tops.Global;

import com.kika.smllybot.database.sql.bank.dto.BankTopAmount;

public interface IGlobalTop {
    long extract(BankTopAmount amount);
}

package com.kika.smllybot.modules.tops;

import com.kika.smllybot.database.sql.bank.dto.BankTopAmount;

public interface IFarmTop {
    long extract(BankTopAmount amount);
}

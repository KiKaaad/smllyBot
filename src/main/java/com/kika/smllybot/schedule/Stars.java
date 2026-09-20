package com.kika.smllybot.schedule;

import com.kika.smllybot.database.sql.bank.BankTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Stars {

    private static final Logger log = LoggerFactory.getLogger(Stars.class);

    public static void minusStarsScheduler() {
        ScheduledExecutorService schedule = Executors.newScheduledThreadPool(1);

        Runnable reductionTask = () -> {
            try {
                BankTable.scheduleMinusStars();
                log.info("⬇️ Уменьшена звездность пользователей на 1%");
            } catch (Exception e) {
                log.error("❌ Не смогли уменьшить звездность пользователей", e);
            }
        };

        schedule.scheduleAtFixedRate(reductionTask, 5, 5, TimeUnit.DAYS);
    }
}

package com.kika.smllybot.schedule;

import com.kika.smllybot.database.sql.ban.BanTable;
import com.kika.smllybot.database.sql.ban.dto.BanData;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.UserSnowflake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class BanScheduler {

    private static final Logger log = LoggerFactory.getLogger(BanScheduler.class);
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final JDA jda;
    private final BanTable banTable;


    BanScheduler(JDA jda, BanTable banTable) {
        this.jda = jda;
        this.banTable = banTable;
    }

    public void start() {
        scheduler.scheduleWithFixedDelay(this::checkExpiredBans, 10, 30, TimeUnit.SECONDS);
    }

    private void checkExpiredBans() {
        try {
            List<BanData> expiredBans = banTable.getExpiredBans();

            for (BanData ban : expiredBans) {
                Guild guild = jda.getGuildById(ban.getGuildId());

                if (guild != null) {
                    jda.retrieveUserById(ban.getDiscordId()).queue(
                            user -> guild.unban(user).queue(
                                    success -> {
                                        banTable.markAsUnbanned(ban.getId(), null, ban.getGuildId());
                                        log.info("✅ Пользователь {} разбанен", ban.getDiscordId());
                                    },
                                    throwable ->
                                            log.error("❌ Не удалось разблокировать пользователя: {}", throwable.getMessage())
                            ),
                            throwable -> {
                                log.error("❌ Пользователь не найден: {}", throwable.getMessage());
                            }
                    );
                }
                if (guild == null) banTable.markAsUnbanned(ban.getId(), null, ban.getGuildId());
            }
        } catch (Exception e) {
            log.error("❌ Ошибка при проверке на истекшие баны: ", e);
        }
    }

}
package com.kika.smllybot.schedule;

import com.kika.smllybot.database.sql.mute.MuteTable;
import com.kika.smllybot.database.sql.mute.dto.MuteData;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import org.jetbrains.annotations.UnknownNullability;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

// TODO: Доделать шедулер этот ваш
public class MuteScheduler {

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private final MuteTable muteTable;
    private final JDA jda;

    public MuteScheduler(MuteTable muteTable, JDA jda) {
        this.muteTable = muteTable;
        this.jda = jda;
    }

    public void start() {
        scheduler.scheduleAtFixedRate(this::checkExpiredMutes, 10, 30, TimeUnit.SECONDS);
    }

    private void checkExpiredMutes() {
        try {
            List<MuteData> expiredMutes = muteTable.getExpiredMutes();

            for (MuteData mute : expiredMutes) {
                Guild guild = jda.getGuildById(mute.getGuildId());
                if (guild == null) {
                    muteTable.markAsUnmuted(mute.getId(), null);
                    continue;
                }

                guild.retrieveMemberById(mute.getDiscordId()).queue(
                        member -> processUnmute(guild, member, mute),
                        throwable -> {
                            muteTable.markAsUnmuted(mute.getId(), null);
                        }
                );
            }
        } catch (Exception e) {
            System.err.println("❌ Ошибка при проверке истекших мутов: " + e.getMessage());
        }
    }

    private void processUnmute(Guild guild, Member member, @UnknownNullability MuteData mute) {
        if ("TIMEOUT".equalsIgnoreCase(mute.getMuteType())) {
            guild.removeTimeout(member)
                    .reason("Истекло время мута (Автоматически)")
                    .queue(
                            success -> muteTable.markAsUnmuted(mute.getId(), null),
                            error -> muteTable.markAsUnmuted(mute.getId(), null)
                    );
        }
    }
}

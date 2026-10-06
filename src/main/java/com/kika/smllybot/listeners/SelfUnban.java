package com.kika.smllybot.listeners;

import com.kika.smllybot.database.sql.ban.BanTable;
import net.dv8tion.jda.api.events.guild.GuildUnbanEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SelfUnban extends ListenerAdapter {

    private final BanTable banTable;
    public SelfUnban(BanTable banTable) {
        this.banTable = banTable;
    }

    private static final Logger log = LoggerFactory.getLogger(SelfUnban.class);

    @Override
    public void onGuildUnban(@NotNull GuildUnbanEvent unbanEvent) {
        long discordId = unbanEvent.getUser().getIdLong();
        long guildId = unbanEvent.getGuild().getIdLong();

        banTable.markAsUnbanned(discordId, null, guildId);
        log.info("✅ Пользователь {} разбанен", discordId);
    }
}

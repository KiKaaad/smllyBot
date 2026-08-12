package com.kika.smllybot.listeners;

import com.kika.smllybot.database.sql.statistic.StatisticTable;
import com.kika.smllybot.database.sql.users.UsersTable;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageCounter extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getMessage().getAuthor().isBot()) return;

        long discordId;
        long guildId;

        if (event.getMember() != null) {
            discordId = event.getMember().getIdLong();
            guildId = event.getGuild().getIdLong();
            UsersTable.getOrCreateUser(discordId, event.getAuthor().getName());
        } else return;

        long userId = UsersTable.getUserId(discordId);

        StatisticTable.createStatistic(userId, guildId);
    }
}

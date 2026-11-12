package com.kika.smllybot.listeners;

import com.kika.smllybot.listeners.ui.UserBannedUI;
import net.dv8tion.jda.api.events.guild.GuildBanEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class UserBanned extends ListenerAdapter {

    public void onGuildBan(GuildBanEvent event) {
        event.getUser().openPrivateChannel().queue(
                success -> success.sendMessageComponents(UserBannedUI.defaultHelp(event))
                        .useComponentsV2(true).queue(),
                error -> System.out.println("Личные сообщения пользователя закрыты(")
        );
    }
}

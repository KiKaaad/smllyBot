package com.kika.smllybot.listeners;

import com.kika.smllybot.listeners.ui.FirstJoinUI;
import net.dv8tion.jda.api.components.container.Container;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class FirstJoin extends ListenerAdapter {

    @Override
    public void onGuildJoin(GuildJoinEvent event) {
        String avatar = event.getJDA().getSelfUser().getEffectiveAvatarUrl();

        Container response = FirstJoinUI.defaultHelp(avatar);

        event.getGuild().getDefaultChannel().asStandardGuildMessageChannel()
                .sendMessageComponents(response)
                .useComponentsV2(true)
                .queue();
    }
}

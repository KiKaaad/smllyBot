package com.kika.smllybot.modules.ping;

import com.kika.smllybot.styles.component.ping.PingComponent;
import net.dv8tion.jda.api.components.container.Container;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;


public class SlashPing extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("ping")) {

            PingService.fetchPing(event.getJDA(), data -> {
                Container text = PingComponent.render(data);
                event.replyComponents(text)
                        .useComponentsV2(true)
                        .queue();
            });
        }

    }

}

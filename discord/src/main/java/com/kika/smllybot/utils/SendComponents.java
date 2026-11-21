package com.kika.smllybot.utils;

import net.dv8tion.jda.api.components.container.Container;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.time.Duration;
import java.util.List;

public class SendComponents {

    public static void sendTo15(MessageReceivedEvent event, List<Container> container) {
        event.getChannel().sendMessageComponents(container).useComponentsV2(true)
                .delay(Duration.ofSeconds(15))
                .flatMap(Message::delete)
                .queue();
    }

    public static void sendTo15(SlashCommandInteractionEvent event, List<Container> container) {
        event.deferReply().queue();
        event.getHook().sendMessageComponents(container).useComponentsV2(true)
                .delay(Duration.ofSeconds(15))
                .flatMap(Message::delete)
                .queue();
    }
}

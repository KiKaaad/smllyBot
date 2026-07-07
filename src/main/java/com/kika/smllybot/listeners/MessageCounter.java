package com.kika.smllybot.listeners;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageCounter extends ListenerAdapter {

    public static long messageCount;

    // TODO: Сделать счетчик сообщений
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getMessage().getAuthor().isBot()) return;

        messageCount++;
    }
}

package com.kika.smllybot.modules.ping;

import com.kika.smllybot.Main;
import com.kika.smllybot.styles.component.ping.PingComponent;
import com.kika.smllybot.utils.PrefixUtil;
import net.dv8tion.jda.api.components.container.Container;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Set;

public class PrefixPing extends ListenerAdapter {

    private static final Set<String> COMMANDS = Set.of("ping", "pong", "пинг", "понг");

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;

        String command = PrefixUtil.getCommandBody(event.getMessage().getContentRaw(), Main.PREFIXES);
        if (command == null) return;

        if (COMMANDS.contains(command)) {
            PingService.fetchPing(event.getJDA(), data -> {
                Container text = PingComponent.render(data);
                event.getChannel().sendMessageComponents(text)
                        .useComponentsV2(true)
                        .queue();
            });
        }

//        if (commands.contains(command)) {
//            PingService.fetchPing(event.getJDA(), data -> {
//                String text = PingText.render(data);
//                event.getChannel().sendMessage(text).queue();
//            });
//        }
    }
}



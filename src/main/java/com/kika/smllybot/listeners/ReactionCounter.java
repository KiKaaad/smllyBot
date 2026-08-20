package com.kika.smllybot.listeners;

import com.kika.smllybot.database.sql.users.UsersTable;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReactionCounter extends ListenerAdapter {

    private static final Logger log = LoggerFactory.getLogger(ReactionCounter.class);

    @Override
    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {
        if (event.getUser().isBot()) return;

        long reactorId = event.getUserIdLong();
        long authorId = event.getMessageAuthorIdLong();

        if (authorId == 0) {
            log.warn("🟡 Не удалось получить автора сообщения, возможно сообщение слишком старое");
            return;
        }

        if (reactorId != authorId) {
            log.info("ℹ️ Пользователю {} поставили реакцию", authorId);
            UsersTable.plusReputation(authorId);
        }
    }

    @Override
    public void onMessageReactionRemove(MessageReactionRemoveEvent event) {
        long reactorId = event.getUserIdLong();
        if (event.getUser().isBot()) return;

        event.getChannel().retrieveMessageById(event.getMessageIdLong()).queue(
                message -> {
                    long authorId = message.getAuthor().getIdLong();

                    if (authorId != reactorId && authorId != 0) {
                        log.info("ℹ️ Пользователю {} убрали реакцию", authorId);
                        UsersTable.minusReputation(authorId);
                    }
                }
        );
    }
}

package com.kika.smllybot.modules.user;

import com.kika.smllybot.Main;
import com.kika.smllybot.database.DatabaseService;
import com.kika.smllybot.database.UsersData;
import com.kika.smllybot.modules.user.ui.GlobalProfileUI;
import net.dv8tion.jda.api.components.container.Container;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.time.Duration;
import java.util.Set;

public class GlobalProfile extends ListenerAdapter {

    private static final Set<String> COMMANDS = Set.of("анкета", "anketa");

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;

        String rawContent = event.getMessage().getContentRaw().trim();
        String prefix = Main.prefixes[0];

        if (!rawContent.toLowerCase().startsWith(prefix.toLowerCase())) return;

        String withoutPrefix = rawContent.substring(prefix.length()).trim();
        String[] parts = withoutPrefix.split("\\s+", 2);
        String command = parts[0].toLowerCase();

        if (!COMMANDS.contains(command)) return;

        net.dv8tion.jda.api.entities.User targetUser;

        // Если это ответ - берем автора из сообщения на которое
        // был произведен ответ
        if (event.getMessage().getReferencedMessage() != null) {
            targetUser = event.getMessage().getReferencedMessage().getAuthor();
        }

        else if (parts.length > 1) {
            String arg = parts[1];

            if (!event.getMessage().getMentions().getUsers().isEmpty()) {
                targetUser = event.getMessage().getMentions().getUsers().getFirst();
            }

            // Поиск по айди
            else if (arg.matches("\\d+")) {
                try {
                    targetUser = event.getJDA().retrieveUserById(arg).complete();
                } catch (Exception e) {
                    event.getChannel().sendMessage("❌ Упс... Пользователь с таким ID не найден")
                            .delay(Duration.ofSeconds(5))
                            .flatMap(Message::delete)
                            .queue();
                    return;
                }
            }

            // ———— ———— ———— ———— ———— ———— ———— ———— ————
            // Поиск по юзернейму*
            // * - Работает только внутри самого сервера,
            //     то есть поиск среди людей внутри гильдии
            //
            // P.S Думаю, позже просто можно записывать чела в бд и позже его показывать оттуда
            //     помечая как "архивные данные"
            // ———— ———— ———— ———— ———— ———— ———— ———— ————

            else {
                var members = event.getGuild().getMembersByName(arg, true);

                if (members.isEmpty()) {
                    members = event.getGuild().getMembersByNickname(arg, true);
                }

                if (!members.isEmpty()) {
                    targetUser = members.getFirst().getUser();
                } else {
                    event.getChannel().sendMessage("❌ Упс... Пользователь с таким юзернеймом не найден")
                            .delay(Duration.ofSeconds(5))
                            .flatMap(Message::delete)
                            .queue();
                    return;
                }
            }
        } else {
            targetUser = event.getAuthor();
        }

        UsersData data = DatabaseService.getFullData(targetUser.getIdLong(), targetUser.getName());
        Member targetMember = event.getGuild().getMember(targetUser);

        GlobalProfileContext ctx = new GlobalProfileContext(
                targetUser,
                event.getAuthor(),
                targetMember,
                data
        );

        Container response = GlobalProfileUI.buildProfile(ctx);

        event.getChannel().sendMessageComponents(response)
                .useComponentsV2(true)
                .queue();
    }

}
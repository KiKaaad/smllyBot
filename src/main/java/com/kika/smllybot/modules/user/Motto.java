package com.kika.smllybot.modules.user;

import com.kika.smllybot.Main;
import com.kika.smllybot.database.DatabaseService;
import com.kika.smllybot.database.UsersData;
import com.kika.smllybot.database.postgresql.user.GetUsers;
import com.kika.smllybot.database.postgresql.user.UserTable;
import com.kika.smllybot.modules.user.ui.GlobalProfileUI;
import com.kika.smllybot.modules.user.ui.MottoUI;
import com.kika.smllybot.utils.Interaction;
import com.kika.smllybot.utils.PrefixUtil;
import net.dv8tion.jda.api.components.container.Container;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.Set;

public class Motto extends ListenerAdapter {

    private static final Set<String> COMMANDS = Set.of("девиз", "motto");

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;

        String body = PrefixUtil.getCommandBody(event.getMessage().getContentRaw(), Main.prefixes);
        if (body == null || body.isEmpty()) return;

        String[] parts = body.split("\\n");

        String fullCommand = parts[0].trim().toLowerCase();

        char action = fullCommand.charAt(0);
        String cleanCommand = (action == '+' || action == '-') ? fullCommand.substring(1) : fullCommand;

        if (!COMMANDS.contains(cleanCommand)) return;

        long discordId = event.getAuthor().getIdLong();

        if (action == '-') {
            UserTable.updateMotto(discordId, null);
            GetUsers dbUser = UserTable.getOrCreateUser(discordId);
            Container response = MottoUI.buildMotto(event.getAuthor(), dbUser, "❌ Описание удалено", false);

            event.getChannel().sendMessageComponents(response).useComponentsV2(true).queue();
            return;
        }

        if (action != '-' && (parts.length < 2 || parts[1].trim().isEmpty())) {
            GetUsers dbUser = UserTable.getOrCreateUser(discordId);
            Container response = MottoUI.buildMotto(event.getAuthor(), dbUser, "Ваш текущий девиз", false);

            event.getChannel().sendMessageComponents(response).useComponentsV2(true).queue();
            return;
        }

        if (parts[1].trim().isEmpty()) {
            event.getChannel().sendMessage("""
            ❌ **Упс.. Вероятно вы использовали команду не так:**
            ```
            jda!девиз
            Какой же я красавчик... 😎
            ```
            """)
                    .delay(Duration.ofSeconds(5)).flatMap(Message::delete).queue();
            return;
        }

        String aboutMeText = parts[1].trim();
        UserTable.updateMotto(discordId, aboutMeText);
        response(event, "✅ Описание обновлено");
    }

    private void response(MessageReceivedEvent event, String title) {
        long userId = event.getAuthor().getIdLong();
        GetUsers dbUser = UserTable.getOrCreateUser(userId);

        Container response = MottoUI.buildMotto(event.getAuthor(), dbUser, title, true);

        event.getChannel().sendMessageComponents(response)
                .useComponentsV2(true)
                .queue();
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        if (!Interaction.checkOwner(event)) return;

        if (event.getComponentId().startsWith("motto::back")) {
            User user = event.getUser();

            UsersData data = DatabaseService.getFullData(user.getIdLong(), user.getName());

            GlobalProfileContext ctx = new GlobalProfileContext(user, user, event.getMember(), data);

            Container profile = GlobalProfileUI.buildProfile(ctx);

            event.editComponents(profile)
                    .useComponentsV2(true)
                    .queue();
        }
    }
}


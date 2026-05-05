package com.kika.smllybot.modules.user;

import com.kika.smllybot.Main;
import com.kika.smllybot.database.postgresql.user.User;
import com.kika.smllybot.database.postgresql.user.UserTable;
import com.kika.smllybot.modules.user.ui.GlobalProfileUI;
import com.kika.smllybot.utils.Interaction;
import net.dv8tion.jda.api.components.buttons.Button;
import net.dv8tion.jda.api.components.container.Container;
import net.dv8tion.jda.api.components.section.Section;
import net.dv8tion.jda.api.components.separator.Separator;
import net.dv8tion.jda.api.components.textdisplay.TextDisplay;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.time.Duration;
import java.util.Set;

public class AboutMe extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

        if (event.getAuthor().isBot()) return;

        String rawContent = event.getMessage().getContentRaw().trim();
        String prefix = Main.prefixes[0];

        if (!rawContent.toLowerCase().startsWith(prefix.toLowerCase())) return;

        String withoutPrefix = rawContent.substring(prefix.length()).trim();
        String[] parts = withoutPrefix.split("\\n", 2);
        String command = parts[0].toLowerCase();

        Set<String> commands = Set.of("о себе", "about me");

        if (!commands.contains(command)) {
            return;
        }

        if (parts.length < 2 || parts[1].trim().isEmpty()) {
            event.getChannel().sendMessage("""
            **❌ Упс.. Вы не ввели описание:**
            ```
            jda!about me
            Тут ваше невероятно крутое описание...
            ```
            """).delay(Duration.ofSeconds(5))
                    .flatMap(Message::delete)
                    .queue();
            return;
        }

        long discordId = event.getAuthor().getIdLong();
        String aboutMeText = parts[1].trim();

        UserTable.updateAboutMe(discordId, aboutMeText);

        User dbUser = UserTable.getOrCreateUser(discordId);

        Container response = Container.of(
                Section.of(
                        Button.primary("aboutMe::back::" + event.getAuthor().getId(), "◀️ Назад"),
                        TextDisplay.of("## ✅ Описание обновлено!")
                ),
                Separator.createDivider(Separator.Spacing.SMALL),
                TextDisplay.of("**Новое описание:**"),
                TextDisplay.of("%s".formatted(dbUser.aboutMe()))
        );


        event.getChannel().sendMessageComponents(response)
                .useComponentsV2(true)
                .queue();

    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        if (!Interaction.checkOwner(event)) return;
        net.dv8tion.jda.api.entities.User jdaUser = event.getUser();

        long discordId = jdaUser.getIdLong();

        if (event.getComponentId().startsWith("aboutMe::back")) {
            User updatedUser = UserTable.getOrCreateUser(discordId);

            Container profile = GlobalProfileUI.buildProfile(jdaUser, updatedUser, event.getUser());

            event.editComponents(profile)
                    .useComponentsV2(true)
                    .queue();
        }
    }
}

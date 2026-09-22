package com.kika.smllybot.handler;

import com.kika.smllybot.utils.StackTrace;
import com.kika.smllybot.utils.TimeUtil;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.components.buttons.Button;
import net.dv8tion.jda.api.components.container.Container;
import net.dv8tion.jda.api.components.section.Section;
import net.dv8tion.jda.api.components.separator.Separator;
import net.dv8tion.jda.api.components.textdisplay.TextDisplay;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.time.Duration;
import java.time.OffsetDateTime;

public class ErrorThrow {

    private ErrorThrow() {}

    public static void noAccessPermission(MessageReceivedEvent event, Permission permission) {
        createErrorMessage(
                event,
                "## \\❌ Нет требуемых прав",
                "Нет следующего права: %s".formatted(permission.getName())
        );
    }

    public static void userNotFound(MessageReceivedEvent event, String user) {
        createErrorMessage(
                event,
                "## \\❌ Пользователь не найден",
                "Пытались найти: " + user
        );
    }

    public static void timeOverhead(MessageReceivedEvent event) {
        createErrorMessage(
                event,
                "## \\❌ Время слишком большое",
                "Время мута __не может__ быть более чем **на 28 дней**"
        );
    }

    private static void createErrorMessage(MessageReceivedEvent event,
            String header, String text) {

        OffsetDateTime time = OffsetDateTime.now().plusSeconds(15);
        String timestampRelative = TimeUtil.getTimestampRelative(time);

        TextDisplay head = TextDisplay.of("%s | %s".formatted(header, timestampRelative));
        Separator separator = Separator.create(true, Separator.Spacing.SMALL);
        TextDisplay main = TextDisplay.of(text);

        Section section = Section.of(
                Button.secondary("undefined", "скоро...").asDisabled(),
                TextDisplay.of(StackTrace.getCallerLocation()));

        event.getChannel()
                .sendMessageComponents(
                        Container.of(head, separator, main).withAccentColor(0xff6b6b),
                        Container.of(section).withAccentColor(0xff6b6b)
                )
                .useComponentsV2(true)
                .delay(Duration.ofSeconds(15))
                .flatMap(Message::delete)
                .queue();
    }

}
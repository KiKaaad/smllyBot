package com.kika.smllybot.handler;

import com.kika.smllybot.utils.StackTrace;
import com.kika.smllybot.utils.TimeUtil;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.components.buttons.Button;
import net.dv8tion.jda.api.components.container.Container;
import net.dv8tion.jda.api.components.section.Section;
import net.dv8tion.jda.api.components.separator.Separator;
import net.dv8tion.jda.api.components.textdisplay.TextDisplay;

import java.awt.*;
import java.time.OffsetDateTime;
import java.util.List;

public class ErrorThrow {

    private ErrorThrow() {}

    public static List<Container> noAccessPermission(Permission permission) {
        return createErrorMessage(
                "## \\❌ Нет требуемых прав",
                "Нет следующего права: %s".formatted(permission.getName())
        );
    }

    public static List<Container> noAccessPermissionBot(Permission permission) {
        return createErrorMessage(
                "## \\❌ У бота нет требуемых прав",
                "Нет следующего права: %s".formatted(permission.getName())
        );
    }

    public static List<Container> notEnoughArgument(String text) {
        return createErrorMessage(
                "## \\❌ Неверно указаны аргументы",
                "%s".formatted(text)
        );
    }

    public static List<Container> smallPermission() {
        return createErrorMessage(
                "## \\❌ Ваша роль или роль бота ниже",
                "Вы или бот не могут замутить пользователя выше своей роли"
        );
    }

    public static List<Container> notNumber(String input) {
        return createErrorMessage(
                "## \\❌ Вы ввели не число",
                """
                        Пожалуйста, проверьте, как вы заполнили аргументы команды
                        Было введено -> `%s` вместо требуемого аргумента
                        """.formatted(input)
        );
    }

    public static List<Container> userNotFound(String user) {
        return createErrorMessage(
                "## \\❌ Пользователь не найден",
                "Пытались найти: " + user
        );
    }

    public static List<Container> timeoutOverhead() {
        return createErrorMessage(
                "## \\❌ Время слишком большое",
                "Время мута __не может__ быть более, чем **на 28 дней**"
        );
    }

    public static List<Container> onlyOnStaging() {
        return createErrorMessage(
                "## \\❌ Режим тестирования выключен",
                """
                        \\⚠️ Режим тестирования крайне нестабилен, используйте на свой страх и риск OwO
                        Включить режим тестирования можно командой `рд` (нужны права администратора)
                        """
        );
    }

    public static List<Container> undefined(String error) {
        return createErrorMessage(
                "## \\❌ Неизвестная ошибка",
                """
                        ## Ошибка:
                        ```Java
                        %s
                        ```
                        """.formatted(error)
        );
    }

    private static List<Container> createErrorMessage(String header, String text) {

        OffsetDateTime time = OffsetDateTime.now().plusSeconds(15);
        String timestampRelative = TimeUtil.getTimestampRelative(time);

        TextDisplay head = TextDisplay.of("%s | %s".formatted(header, timestampRelative));
        Separator separator = Separator.create(true, Separator.Spacing.SMALL);
        TextDisplay main = TextDisplay.of(text);

        Section section = Section.of(
                Button.secondary("undefined", "скоро...").asDisabled(),
                TextDisplay.of(StackTrace.getCallerLocation()));

        return List.of(
                Container.of(head, separator, main).withAccentColor(Color.RED),
                Container.of(section).withAccentColor(Color.RED)
        );
    }

}
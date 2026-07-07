package com.kika.smllybot.modules.helper.ui;

import net.dv8tion.jda.api.components.container.Container;
import net.dv8tion.jda.api.components.container.ContainerChildComponent;
import net.dv8tion.jda.api.components.textdisplay.TextDisplay;

import java.util.ArrayList;
import java.util.List;

public class GlobalHelpUI {

    public static Container gtop() {
        List<ContainerChildComponent> components = new ArrayList<>(10);

        ContainerChildComponent header = TextDisplay.of("# \\🏆 Гтоп - глобальные топы");
        ContainerChildComponent main = TextDisplay.of("""
                        - `гтоп` <ириски / коины> - выводит глобальный топ в зависимости от аргумента
                           - `ириски` - выводит топ по ирискам (\\🍬)
                           - `коины` - выводит топ по коинам (\\☢️)
                        """);

        components.add(header);
        components.add(main);

        return Container.of(components);
    }

    public static Container statBot() {
        List<ContainerChildComponent> components = new ArrayList<>(10);

        ContainerChildComponent header = TextDisplay.of("# \\📊 Статбот - статистика бота");
        ContainerChildComponent main = TextDisplay.of("""
                - `статбот` - выводит статистику бота
                """);

        components.add(header);
        components.add(main);

        return Container.of(components);
    }

    public static Container globalProfile() {
        List<ContainerChildComponent> components = new ArrayList<>(10);

        ContainerChildComponent header = TextDisplay.of("# \\👤 Анкета");
        ContainerChildComponent main = TextDisplay.of("""
                - `анкета` - выводит глобальный профиль пользователя
                   - `анкета <юзернейм / айди>` - выводит профиль указанного пользователя
                   - `анкета` в ответ на чье-то сообщение - выводит профиль пользователя, на чье сообщение был произведен ответ
                """);

        components.add(header);
        components.add(main);

        return Container.of(components);
    }

    public static Container defaultHelp() {

        List<ContainerChildComponent> components = new ArrayList<>(10);

        ContainerChildComponent header = TextDisplay.of("# \\🛟 Помощь по боту");

        components.add(header);

        return Container.of(components);
    }

}

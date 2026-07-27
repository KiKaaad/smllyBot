package com.kika.smllybot.modules.helper.ui;

import net.dv8tion.jda.api.components.container.Container;
import net.dv8tion.jda.api.components.container.ContainerChildComponent;
import net.dv8tion.jda.api.components.textdisplay.TextDisplay;

import java.util.ArrayList;
import java.util.List;

public class GlobalHelpUI {

    // TODO: Сделать кнопку с выбором с категории команд
    public static Container defaultHelp() {

        List<ContainerChildComponent> components = new ArrayList<>(10);

        ContainerChildComponent header = TextDisplay.of("# \\🛟 Помощь по боту");
        ContainerChildComponent main = TextDisplay.of("""
                Здесь есть помощь по всем командам бота
                Вводите - `хелп <название\\_модуля>`
                ## \\🛰️ Модули
                ### Пользователь
                - анкета
                   - девиз
                - профиль
                ### Топы
                - гтоп
                ### Экономика
                - Ферма
                - Мешок
                ### Приватность
                - Приватность
                ### Статистика
                - статбот
                - гильдия
                - пинг
                """);

        components.add(header);
        components.add(main);

        return Container.of(components);
    }

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

        ContainerChildComponent header = TextDisplay.of("# \\📊 Статистика");
        ContainerChildComponent main = TextDisplay.of("""
                - `статбот` - выводит статистику бота, отображает его нагрузку на ЦП, занятость ОЗУ и количество пользователей
                -# P. S: При первом использовании нагрузка на ЦП может отобразить как 0%
                - `гильдия` - выводит статистику гильдии, количество участников, онлайн сервера и его владельца
                - `пинг` - выводит пинг от сервера бота до дискорда
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
                - `девиз` - отображает ваш текущий девиз
                - `-девиз` - удалит ваш текущий девиз
                - `девиз` ваш_девиз (на следующей строке) - установит новый девиз в анкете
                - `профиль` - выводит локальный профиль со статистикой с гильдии
                """);

        components.add(header);
        components.add(main);

        return Container.of(components);
    }

}

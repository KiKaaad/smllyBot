package com.kika.smllybot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.IntegrationType;
import net.dv8tion.jda.api.interactions.InteractionContextType;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SlashCommandRegister extends ListenerAdapter {

    public static void registerCommands(JDA jda) {

        jda.updateCommands().addCommands(
                Commands.context(Command.Type.USER, "Анкета")
                        .setContexts(InteractionContextType.ALL)
                        .setIntegrationTypes(IntegrationType.ALL),
                Commands.slash("анкета", "Посмотреть чей-то глобальный профиль")
                        .setContexts(InteractionContextType.ALL)
                        .addOption(OptionType.USER, "юзернейм", "Введите конкретного пользователя")
                        .setIntegrationTypes(IntegrationType.ALL),

                Commands.slash("ферма", "Зафармить вкусненьких ирис-коинов (☢️)")
                        .setContexts(InteractionContextType.ALL)
                        .setIntegrationTypes(IntegrationType.ALL),

                Commands.slash("инфа", "Узнайте вероятность какого-либо события в процентах")
                        .setContexts(InteractionContextType.ALL)
                        .addOption(OptionType.STRING, "вопрос", "Вероятность чего вы хотели бы узнать")
                        .setIntegrationTypes(IntegrationType.ALL),
                Commands.slash("данет", "Узнайте, ДА или НЕТ")
                        .setContexts(InteractionContextType.ALL)
                        .addOption(OptionType.STRING, "вопрос", "Правда или случится ли ЭТО? ")
                        .setIntegrationTypes(IntegrationType.ALL),

                Commands.slash("хелп", "Справочник всех команд бота")
                        .setContexts(InteractionContextType.ALL)
                        .addOption(OptionType.STRING, "модуль", "Конкретный модуль бота", false, true)
                        .setIntegrationTypes(IntegrationType.ALL)
        ).queue();
    }

    @Override
    public void onCommandAutoCompleteInteraction(CommandAutoCompleteInteractionEvent event) {
        String[] helpAutoComplete = {
                "анкета и профиль", "модерация", "топы",
                "экономика", "статистика", "интерактивные команды",
                "другое"
        };

        if (event.getName().equals("хелп") && event.getFocusedOption().getName().equals("модуль")) {
            List<Command.Choice> options = Stream.of(helpAutoComplete)
                    .filter(word -> word.startsWith(event.getFocusedOption().getValue()))
                    .map(word -> new Command.Choice(word, word))
                    .collect(Collectors.toList());
            event.replyChoices(options).queue();
        }
    }

}

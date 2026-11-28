package com.kika.smllybot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.IntegrationType;
import net.dv8tion.jda.api.interactions.InteractionContextType;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SlashCommandRegister extends ListenerAdapter {

    public static void registerCommands(JDA jda) {

        jda.updateCommands().addCommands(
                Commands.slash("ранний", "🧪 Включает эксперементальные функции бота")
                        .addSubcommands(new SubcommandData("доступ", "🧪 Включает эксперементальные функции бота"))
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR))
                        .setIntegrationTypes(IntegrationType.GUILD_INSTALL),

                Commands.slash("мут", "🔇 Выдать тайм-аут пользователю (замутить)")
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.MODERATE_MEMBERS))
                        .setIntegrationTypes(IntegrationType.GUILD_INSTALL)
                        .addOption(OptionType.USER,
                                "юзер",
                                "Укажите пользователя, которого нужно замутить",
                                true)
                        .addOption(OptionType.INTEGER,
                                "время",
                                "Укажите время, на которое пользователь будет замучен",
                                true, true)
                        .addOption(OptionType.STRING,
                                "интервал",
                                "Укажите интервал мута (минуты, часы, дни и др.)",
                                true,
                                true)
                        .addOption(OptionType.STRING,
                                "причина",
                                "Причина по которой вы мутите пользователя",
                                false, false
                        ),


                Commands.slash("гильдия", "🌇 Информация о гильдии")
                        .setIntegrationTypes(IntegrationType.GUILD_INSTALL),

                Commands.context(Command.Type.USER, "Анкета")
                        .setContexts(InteractionContextType.ALL)
                        .setIntegrationTypes(IntegrationType.ALL),
                Commands.slash("анкета", "👤 Посмотреть чей-то глобальный профиль")
                        .setContexts(InteractionContextType.ALL)
                        .addOption(OptionType.USER, "юзернейм", "Введите конкретного пользователя")
                        .setIntegrationTypes(IntegrationType.ALL),

                Commands.slash("ферма", "🪏 Зафармить вкусненьких ирис-коинов (☢️)")
                        .setContexts(InteractionContextType.ALL)
                        .setIntegrationTypes(IntegrationType.ALL),

                Commands.slash("инфа", "🪙 Узнайте вероятность какого-либо события в процентах")
                        .setContexts(InteractionContextType.ALL)
                        .addOption(OptionType.STRING, "вопрос", "Вероятность чего вы хотели бы узнать")
                        .setIntegrationTypes(IntegrationType.ALL),
                Commands.slash("данет", "❔ Узнайте, ДА или НЕТ")
                        .setContexts(InteractionContextType.ALL)
                        .addOption(OptionType.STRING, "вопрос", "Правда или случится ли ЭТО? ")
                        .setIntegrationTypes(IntegrationType.ALL),

                Commands.slash("хелп", "🛟 Справочник всех команд бота")
                        .setContexts(InteractionContextType.ALL)
                        .addOption(OptionType.STRING, "модуль", "Конкретный модуль бота", false, true)
                        .setIntegrationTypes(IntegrationType.ALL)
        ).queue();
    }

    @Override
    public void onCommandAutoCompleteInteraction(CommandAutoCompleteInteractionEvent event) {
        String[] help = {
                "анкета и профиль", "модерация", "топы",
                "экономика", "статистика", "интерактивные команды", "гильдия",
                "другое"
        };
        int[] durations = {1, 2, 6, 12, 24};
        String[] unit = {"секунды", "минуты", "часы", "дни", "недели", "месяца", "год"};

        if (event.getName().equals("хелп") && event.getFocusedOption().getName().equals("модуль")) {
            List<Command.Choice> options = Stream.of(help)
                    .filter(word -> word.startsWith(event.getFocusedOption().getValue()))
                    .map(word -> new Command.Choice(word, word))
                    .collect(Collectors.toList());
            event.replyChoices(options).queue();
        }

        if (event.getName().equals("мут") && event.getFocusedOption().getName().equals("время")) {
            List<Command.Choice> options = Arrays.stream(durations)
                    .filter(word -> String.valueOf(word).startsWith(event.getFocusedOption().getValue()))
                    .mapToObj(word -> new Command.Choice(String.valueOf(word), word))
                    .collect(Collectors.toList());
            event.replyChoices(options).queue();
        }

        if (event.getName().equals("мут") && event.getFocusedOption().getName().equals("интервал")) {
            List<Command.Choice> options = Stream.of(unit)
                    .filter(word -> word.startsWith(event.getFocusedOption().getValue()))
                    .map(word -> new Command.Choice(word, word))
                    .collect(Collectors.toList());
            event.replyChoices(options).queue();
        }
    }

}

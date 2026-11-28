package com.kika.smllybot.modules.helper;

import com.kika.smllybot.annotations.ButtonPrefix;
import com.kika.smllybot.modules.helper.ui.GlobalHelpUI;
import com.kika.smllybot.other.BaseCmd;
import com.kika.smllybot.utils.Interaction;
import net.dv8tion.jda.api.components.container.Container;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Set;

public class GlobalHelp extends BaseCmd {

    public GlobalHelp() {
        super(Set.of("хелпа", "хелп", "хелпер", "помощь", "help"));
    }

    @Override
    public Container execute(MessageReceivedEvent event, String raw, String args) {

        if (args.isEmpty()) {
            Container response = GlobalHelpUI.defaultHelp(event.getAuthor().getIdLong(), 1);

            event.getChannel().sendMessageComponents(response)
                    .useComponentsV2(true)
                    .queue();

            return response;
        }

        String[] parts = args.trim().split("\\s+");
        String subCommand = parts[0].toLowerCase();

        // TODO: Добавить хелп <категория / команда>

        return null;
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        long discordId = event.getUser().getIdLong();

        if (event.getName().equals("хелп") && event.getOption("модуль") != null) {
            switch (event.getOption("модуль").getAsString()) {
                case "анкета и профиль" -> {
                    Container response = GlobalHelpUI.profileAndAnketa(discordId);

                    event.deferReply().queue();
                    event.getHook().sendMessageComponents(response).useComponentsV2(true).queue();
                }
                case "модерация" -> {
                    Container response = GlobalHelpUI.moderation(discordId);

                    event.deferReply().queue();
                    event.getHook().sendMessageComponents(response).useComponentsV2(true).queue();
                }
                case "топы" -> {
                    Container response = GlobalHelpUI.tops(discordId);

                    event.deferReply().queue();
                    event.getHook().sendMessageComponents(response).useComponentsV2(true).queue();
                }
                case "экономика" -> {
                    Container response = GlobalHelpUI.economy(discordId);

                    event.deferReply().queue();
                    event.getHook().sendMessageComponents(response).useComponentsV2(true).queue();
                }
                case "статистика" -> {
                    Container response = GlobalHelpUI.statistic(discordId);

                    event.deferReply().queue();
                    event.getHook().sendMessageComponents(response).useComponentsV2(true).queue();
                }
                case "интерактивные команды" -> {
                    Container response = GlobalHelpUI.interactive(discordId);

                    event.deferReply().queue();
                    event.getHook().sendMessageComponents(response).useComponentsV2(true).queue();
                }
                case "гильдия" -> {
                    Container response = GlobalHelpUI.guild(discordId);

                    event.deferReply().queue();
                    event.getHook().sendMessageComponents(response).useComponentsV2(true).queue();
                }
                case "другое" -> {
                    Container response = GlobalHelpUI.other(discordId);

                    event.deferReply().queue();
                    event.getHook().sendMessageComponents(response).useComponentsV2(true).queue();
                }
            }
        } else {
            Container response = GlobalHelpUI.defaultHelp(event.getUser().getIdLong(), 1);

            event.deferReply().queue();
            event.getHook().sendMessageComponents(response).useComponentsV2(true).queue();
        }
    }

    @ButtonPrefix(prefix = "help")
    public void onButton(ButtonInteractionEvent event, String[] args) {
        if (!Interaction.checkOwner(event, args)) return;
        String[] componentId = event.getComponentId().split(":");
        var discordId = event.getUser().getIdLong();
        int page = 1;
        if (Long.parseLong(componentId[2]) <= 10) page = Integer.parseInt(componentId[2]);

        switch (componentId[1]) {
            case "anketaAndProfile" -> {
                var response = GlobalHelpUI.profileAndAnketa(discordId);

                event.editComponents(response)
                        .useComponentsV2(true)
                        .queue();
            }
            case "tops" -> {
                var response = GlobalHelpUI.tops(discordId);

                event.editComponents(response)
                        .useComponentsV2(true)
                        .queue();
            }
            case "economy" -> {
                var response = GlobalHelpUI.economy(discordId);

                event.editComponents(response)
                        .useComponentsV2(true)
                        .queue();
            }
            case "statistic" -> {
                var response = GlobalHelpUI.statistic(discordId);

                event.editComponents(response)
                        .useComponentsV2(true)
                        .queue();
            }
            case "interactive" -> {
                var response = GlobalHelpUI.interactive(discordId);

                event.editComponents(response)
                        .useComponentsV2(true)
                        .queue();
            }
            case "moderation" -> {
                var response = GlobalHelpUI.moderation(discordId);

                event.editComponents(response)
                        .useComponentsV2(true)
                        .queue();
            }
            case "guild" -> {
                var response = GlobalHelpUI.guild(discordId);

                event.editComponents(response)
                        .useComponentsV2(true)
                        .queue();
            }
            case "other" -> {
                var response = GlobalHelpUI.other(discordId);

                event.editComponents(response)
                        .useComponentsV2(true)
                        .queue();
            }
            case "back" -> {
                var response = GlobalHelpUI.defaultHelp(discordId, page);

                event.editComponents(response)
                        .useComponentsV2(true)
                        .queue();
            }
            case "redo" -> {
                var response = GlobalHelpUI.defaultHelp(discordId, 2);

                event.editComponents(response)
                        .useComponentsV2(true)
                        .queue();
            }
            case "undo" -> {
                var response = GlobalHelpUI.defaultHelp(discordId, 1);

                event.editComponents(response)
                        .useComponentsV2(true)
                        .queue();
            }
        }
    }
}
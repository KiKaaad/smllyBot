package com.kika.smllybot.modules.helper;

import com.kika.smllybot.annotations.ButtonPrefix;
import com.kika.smllybot.modules.helper.ui.GlobalHelpUI;
import com.kika.smllybot.other.BaseCmd;
import com.kika.smllybot.utils.Interaction;
import net.dv8tion.jda.api.components.container.Container;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Set;

public class GlobalHelp extends BaseCmd {

    public GlobalHelp() {
        super(Set.of("хелпа", "хелп", "хелпер"));
    }

    @Override
    public Container execute(MessageReceivedEvent event, String raw, String args) {

        if (args.isEmpty()) {
            Container response = GlobalHelpUI.defaultHelp(event.getAuthor().getIdLong());

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

    @ButtonPrefix(prefix = "help")
    public void onButton(ButtonInteractionEvent event, String[] args) {
        if (!Interaction.checkOwner(event, args)) return;
        String[] componentId = event.getComponentId().split(":");
        var discordId = event.getUser().getIdLong();

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
            case "other" -> {
                var response = GlobalHelpUI.other(discordId);

                event.editComponents(response)
                        .useComponentsV2(true)
                        .queue();
            }
            case "back" -> {
                var response = GlobalHelpUI.defaultHelp(discordId);

                event.editComponents(response)
                        .useComponentsV2(true)
                        .queue();
            }
        }
    }
}
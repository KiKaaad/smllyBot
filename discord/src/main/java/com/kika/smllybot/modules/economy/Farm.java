package com.kika.smllybot.modules.economy;

import com.kika.smllybot.modules.economy.ui.FarmUI;
import com.kika.smllybot.other.BaseCmd;
import net.dv8tion.jda.api.components.container.Container;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Set;

public class Farm extends BaseCmd {

    public Farm() {
        super(Set.of("фарма", "ферма", "фарм", "farm", "ferma"));
    }

    @Override
    public Container execute(MessageReceivedEvent event, String raw, String args) {
        long discordId = event.getAuthor().getIdLong();
        String name = event.getAuthor().getEffectiveName();

        FarmContext context = FarmService.reward(discordId, name);

        Container response = FarmUI.buildFarm(context);

        event.getChannel().sendMessageComponents(response).useComponentsV2(true).queue();

        return response;
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        long discordId = event.getUser().getIdLong();
        String name = event.getUser().getEffectiveName();

        FarmContext context = FarmService.reward(discordId, name);
        Container response = FarmUI.buildFarm(context);

        if (event.getName().equals("ферма")) {
            event.deferReply().queue();

            event.getHook().sendMessageComponents(response).useComponentsV2(true).queue();
        }
    }
}
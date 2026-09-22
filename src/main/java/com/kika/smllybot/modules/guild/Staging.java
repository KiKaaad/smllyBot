package com.kika.smllybot.modules.guild;

import com.kika.smllybot.annotations.ButtonPrefix;
import com.kika.smllybot.database.sql.Repository;
import com.kika.smllybot.database.sql.guild.GuildTable;
import com.kika.smllybot.handler.ErrorThrow;
import com.kika.smllybot.modules.guild.ui.StagingUI;
import com.kika.smllybot.other.BaseCmd;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.components.container.Container;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Set;

public class Staging extends BaseCmd {

    public Staging() {
        super(Set.of("staging", "рд"));
    }

    @Override
    public Container execute(MessageReceivedEvent event, String raw, String args) {
        if (!event.isFromGuild()) return null;
        if (event.getMember() == null || !event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            ErrorThrow.noAccessPermission(event,  Permission.ADMINISTRATOR);

            return null;
        }

        Repository repo = new Repository();
        long guildId = event.getGuild().getIdLong();
        String guildName = event.getGuild().getName();

        StagingContext context = new StagingContext(
                repo.getGuild(guildId, guildName),
                event.getMember().getIdLong()
        );

        var response = StagingUI.build(context);

        event.getChannel().sendMessageComponents(response)
                .useComponentsV2(true)
                .queue();

        return null;
    }

    @ButtonPrefix(prefix = "staging")
    public void onButton(ButtonInteractionEvent event, String[] args) {
        String[] parts = event.getComponentId().split(":");
        long guildId = event.getGuild().getIdLong();

        if (parts[1].equals("on")) {
            GuildTable.editStaging(guildId, false);
        } else if (parts[1].equals("off")) {
            GuildTable.editStaging(guildId, true);
        }

        Repository repo = new Repository();
        String guildName = event.getGuild().getName();

        StagingContext context = new StagingContext(
                repo.getGuild(guildId, guildName),
                event.getUser().getIdLong()
        );

        var response = StagingUI.build(context);

        event.editComponents(response).useComponentsV2(true).queue();
    }
}

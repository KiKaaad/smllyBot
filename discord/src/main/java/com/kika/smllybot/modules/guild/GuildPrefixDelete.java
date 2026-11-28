package com.kika.smllybot.modules.guild;

import com.kika.smllybot.database.sql.guild.guild.GuildTable;
import com.kika.smllybot.handler.ErrorThrow;
import com.kika.smllybot.modules.guild.ui.GuildPrefixUI;
import com.kika.smllybot.other.BaseCmd;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.components.container.Container;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Set;

public class GuildPrefixDelete extends BaseCmd {

    public GuildPrefixDelete() {
        super(Set.of("сбросить префиксы", "сбросить префикс", "-префиксы", "-префикс"));
    }

    @Override
    public Container execute(MessageReceivedEvent event, String raw, String args) {
        if (!event.isFromGuild()) return null;
        if (event.getMember() == null || !event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            ErrorThrow.noAccessPermission(Permission.ADMINISTRATOR);

            return null;
        }
        long guildId = event.getGuild().getIdLong();

        GuildTable.deletePrefix(guildId);

        var response = GuildPrefixUI.deletePrefix();

        event.getChannel().sendMessageComponents(response).useComponentsV2(true).queue();

        return null;
    }
}

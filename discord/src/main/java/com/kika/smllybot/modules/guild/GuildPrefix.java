package com.kika.smllybot.modules.guild;

import com.kika.smllybot.database.sql.guild.guild.GuildTable;
import com.kika.smllybot.handler.ErrorThrow;
import com.kika.smllybot.modules.guild.ui.GuildPrefixUI;
import com.kika.smllybot.other.BaseCmd;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.components.container.Container;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class GuildPrefix extends BaseCmd {

    public GuildPrefix() {
        super(Set.of("установить префиксы", "установить префикс"));
    }

    @Override
    public Container execute(MessageReceivedEvent event, String raw, String args) {
        if (!event.isFromGuild()) return null;
        if (event.getMember() == null || !event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            ErrorThrow.noAccessPermission(event, Permission.ADMINISTRATOR);

            return null;
        }
        long guildId = event.getGuild().getIdLong();

        List<String> prefix = Arrays.stream(args.split(","))
                .map(String::strip)
                .distinct()
                .collect(Collectors.toList());

        GuildTable.editPrefix(guildId, prefix);

        var response = GuildPrefixUI.build(prefix);

        event.getChannel().sendMessageComponents(response).useComponentsV2(true).queue();

        return null;
    }

}

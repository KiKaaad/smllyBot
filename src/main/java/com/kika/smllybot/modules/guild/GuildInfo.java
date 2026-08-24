package com.kika.smllybot.modules.guild;

import com.kika.smllybot.modules.guild.ui.GuildInfoUI;
import com.kika.smllybot.other.BaseCmd;
import net.dv8tion.jda.api.components.container.Container;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.EnumSet;
import java.util.Set;

public class GuildInfo extends BaseCmd {

    public GuildInfo() {
        super(Set.of("гильдия"));
    }

    @Override
    public Container execute(MessageReceivedEvent event, String raw, String args) {
        if (!event.isFromGuild()) return null;

        GuildInfoContext ctx = new GuildInfoContext(event);

        Container response = GuildInfoUI.build(ctx);

        event.getChannel().sendMessageComponents(response)
                .setAllowedMentions(EnumSet.noneOf(Message.MentionType.class))
                .useComponentsV2(true)
                .queue();

        return response;
    }

}
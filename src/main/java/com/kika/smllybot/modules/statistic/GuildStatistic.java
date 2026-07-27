package com.kika.smllybot.modules.statistic;

import com.kika.smllybot.modules.statistic.ui.GuildStatisticUI;
import com.kika.smllybot.other.BaseCmd;
import net.dv8tion.jda.api.components.container.Container;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.EnumSet;
import java.util.Set;

public class GuildStatistic extends BaseCmd {

    public GuildStatistic() {
        super(Set.of("гильдия"));
    }

    @Override
    public Container execute(MessageReceivedEvent event, String raw, String args) {

        GuildStatisticContext ctx = new GuildStatisticContext(event);

        Container response = GuildStatisticUI.buildGuildStatistic(ctx);

        event.getChannel().sendMessageComponents(response)
                .setAllowedMentions(EnumSet.noneOf(Message.MentionType.class))
                .useComponentsV2(true)
                .queue();

        return response;
    }

}

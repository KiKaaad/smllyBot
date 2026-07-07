package com.kika.smllybot.modules.statistic;

import com.kika.smllybot.other.BaseCmd;
import com.kika.smllybot.modules.statistic.ui.StatisticUI;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDAInfo;
import net.dv8tion.jda.api.components.container.Container;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.EnumSet;
import java.util.Set;

public class Statistic extends BaseCmd {

        public Statistic() {
                super(Set.of("статбот", "бостат", "statbot", "botstat"));
        }

        @Override
        public void execute(MessageReceivedEvent event, String arg) {
                int shardTotal = JDA.ShardInfo.SINGLE.getShardTotal();
                var jdaVersion = JDAInfo.VERSION;
                var serversCount = (long) event.getJDA().getGuilds().size();
                var userCount = event.getJDA().getUserCache().stream().count();
                var botAvatarUrl = event.getJDA().getSelfUser().getAvatarUrl();

                var botId = event.getJDA().getSelfUser().getId();
                User user = event.getJDA().retrieveUserById(botId).complete();
                User.Profile user1 = user.retrieveProfile().complete();
                String botBannerUrl = user1.getBanner().getUrl(1024);

                StatisticContext ctx = new StatisticContext(
                        jdaVersion,
                        shardTotal,
                        serversCount,
                        userCount,
                        botAvatarUrl,
                        botBannerUrl
                );

                Container response = StatisticUI.buildStatistic(ctx);


                event.getChannel().sendMessageComponents(response)
                        .setAllowedMentions(EnumSet.noneOf(Message.MentionType.class))
                        .useComponentsV2(true)
                        .queue();
        }
}
package com.kika.smllybot.modules.statistic;

import com.kika.smllybot.modules.statistic.ui.StatisticBotUI;
import com.kika.smllybot.other.BaseCmd;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDAInfo;
import net.dv8tion.jda.api.components.container.Container;
import net.dv8tion.jda.api.components.container.ContainerChildComponent;
import net.dv8tion.jda.api.components.mediagallery.MediaGallery;
import net.dv8tion.jda.api.components.mediagallery.MediaGalleryItem;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class StatisticBot extends BaseCmd {

    public StatisticBot() {
        super(Set.of("статбот", "ботстат", "statbot", "botstat"));
    }

    @Override
    public Container execute(MessageReceivedEvent event, String raw, String args) {
        int shardTotal = JDA.ShardInfo.SINGLE.getShardTotal();
        var jdaVersion = JDAInfo.VERSION;
        var serversCount = (long) event.getJDA().getGuilds().size();
        var userCount = event.getJDA().getUserCache().stream().count();
        var botAvatarUrl = event.getJDA().getSelfUser().getEffectiveAvatarUrl();
        var botId = event.getJDA().getSelfUser().getId();
        User bot = event.getJDA().getUserById(botId);

        StatisticContext ctx = new StatisticContext(
                jdaVersion,
                shardTotal,
                serversCount,
                userCount,
                botAvatarUrl,
                bot
        );

        event.getJDA().getSelfUser().retrieveProfile().queue(
            profile -> {
                Container response = StatisticBotUI.buildStatistic(ctx);
                List<ContainerChildComponent> components = new ArrayList<>(response.getComponents());

                MediaGallery banner;
                if (profile.getBanner() != null) {
                    String bannerUrl = profile.getBanner().getUrl(1024);
                    banner = MediaGallery.of(MediaGalleryItem.fromUrl(bannerUrl));
                    components.addFirst(banner);
                }

                response = Container.of(components);

                event.getChannel().sendMessageComponents(response)
                        .setAllowedMentions(EnumSet.noneOf(Message.MentionType.class))
                        .useComponentsV2(true)
                        .queue();
            }
        );

        return null;
    }
}
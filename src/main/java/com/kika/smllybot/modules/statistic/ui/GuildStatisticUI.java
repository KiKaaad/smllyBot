package com.kika.smllybot.modules.statistic.ui;

import com.kika.smllybot.modules.statistic.GuildStatisticContext;
import net.dv8tion.jda.api.components.container.Container;
import net.dv8tion.jda.api.components.container.ContainerChildComponent;
import net.dv8tion.jda.api.components.section.Section;
import net.dv8tion.jda.api.components.separator.Separator;
import net.dv8tion.jda.api.components.textdisplay.TextDisplay;
import net.dv8tion.jda.api.components.thumbnail.Thumbnail;

import java.util.ArrayList;
import java.util.List;

// TODO: Доделать статистику гильдий
public class GuildStatisticUI {

    public static Container buildGuildStatistic(GuildStatisticContext ctx) {
        List<ContainerChildComponent> components = new ArrayList<>(12);

        long guildId = ctx.event().getGuild().getIdLong();
        String guildTitle = ctx.event().getGuild().getName();
        String description = ctx.event().getGuild().getDescription();
        int rolesCount = ctx.event().getGuild().getRoles().size();
        String ownerName = ctx.event().getGuild().getOwner().getUser().getName();
        long ownerId = ctx.event().getGuild().getOwner().getIdLong();
        int voiceChannels = ctx.event().getGuild().getVoiceChannels().size();
        int channels = ctx.event().getGuild().getChannels().size();
        int boostCount = ctx.event().getGuild().getBoostCount();
        int userCount = ctx.event().getGuild().getMemberCount();

        String icon = "";
        if (ctx.event().getGuild().getIcon() != null) icon = ctx.event().getGuild().getIcon().getUrl(1024);

        ContainerChildComponent header = Section.of(
                Thumbnail.fromUrl(icon),
                TextDisplay.of("""
                        # %s
                        """.formatted(guildTitle)),
                TextDisplay.of("""
                        ### Описание:
                        %s""".formatted(description))
                );
        ContainerChildComponent separator = Separator.createDivider(Separator.Spacing.SMALL);
        ContainerChildComponent main = TextDisplay.of("## Подробности:");
        ContainerChildComponent owner = TextDisplay.of("Владелец: %s | ID: `%d`".formatted(ownerName, ownerId));
        ContainerChildComponent users = TextDisplay.of("Людей (бустов): %d (%d) | Ботов: %d"
                .formatted(userCount, boostCount, -1));

        components.add(header);
        components.add(separator);
        components.add(main);
        components.add(owner);
        components.add(users);

        return Container.of(components);
    }

}

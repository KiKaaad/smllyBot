package com.kika.smllybot.modules.user.Local.ui;

import com.kika.smllybot.modules.user.Local.ProfileContext;
import com.kika.smllybot.utils.TimeUtil;
import net.dv8tion.jda.api.components.container.Container;
import net.dv8tion.jda.api.components.container.ContainerChildComponent;
import net.dv8tion.jda.api.components.section.Section;
import net.dv8tion.jda.api.components.separator.Separator;
import net.dv8tion.jda.api.components.textdisplay.TextDisplay;
import net.dv8tion.jda.api.components.thumbnail.Thumbnail;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProfileUI {

    public static Container buildProfile(ProfileContext ctx) {
        List<ContainerChildComponent> components = new ArrayList<>();

        String avatarUrl = ctx.target().getEffectiveAvatarUrl();
        String name = ctx.target().getEffectiveName();
        String timestamp = TimeUtil.getTimestamp(ctx.profile().getCreatedAt());
        String timestampRelative = TimeUtil.getTimestampRelative(ctx.profile().getCreatedAt());

        ContainerChildComponent header = Section.of(
                Thumbnail.fromUrl(avatarUrl),
                TextDisplay.of("# \\👤 Это %s".formatted(name)),
                TextDisplay.of("### О себе:"),
                TextDisplay.of("%s".formatted(ctx.profile().getAboutMe()))
        );
        ContainerChildComponent separator = Separator.createDivider(Separator.Spacing.SMALL);
        ContainerChildComponent main = TextDisplay.of("## Основная информация:");
        ContainerChildComponent owner = TextDisplay.of("\\👑 Создатель дискорд-сервера");
        ContainerChildComponent citizen = TextDisplay.of("\\🪪 Гражданин этого сервера");
        ContainerChildComponent reputation = TextDisplay.of("Репутация: \\✨ %d | \\➕ %d"
                .formatted(ctx.bank().getStar(), ctx.user().getReaction()));
        ContainerChildComponent firstEntry = TextDisplay.of("Впервые появился здесь %s (%s)"
                .formatted(timestamp, timestampRelative));
        ContainerChildComponent activity = TextDisplay.of("**Актив** (д | н | м | весь): %d | %d | %d | %d"
                .formatted(ctx.statistic().getDay(), ctx.statistic().getWeek(), ctx.statistic().getMonth(), ctx.statistic().getTotal()));
        ContainerChildComponent rewardsHeader = TextDisplay.of("## \\🏆 Награды");
        ContainerChildComponent rewards = TextDisplay.of("...");
        ContainerChildComponent footer = TextDisplay
                .of("-# Это - **локальный** профиль. Часть информации выводится исключительно из этой гильдии");

        components.add(header);
        components.add(separator);
        components.add(main);
        if (ctx.member().isOwner()) components.add(owner);
        if (Objects.equals(ctx.user().getCitizenship(), ctx.guildId())) components.add(citizen);
        components.add(reputation);
        components.add(firstEntry);
        components.add(activity);
        components.add(separator);
        components.add(rewardsHeader);
        components.add(rewards);
        components.add(separator);
        components.add(footer);

        return Container.of(components);
    }
}
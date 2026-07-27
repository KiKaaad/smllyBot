package com.kika.smllybot.modules.user.Local.ui;

import com.kika.smllybot.modules.user.Local.ProfileContext;
import net.dv8tion.jda.api.components.container.Container;
import net.dv8tion.jda.api.components.container.ContainerChildComponent;
import net.dv8tion.jda.api.components.section.Section;
import net.dv8tion.jda.api.components.separator.Separator;
import net.dv8tion.jda.api.components.textdisplay.TextDisplay;
import net.dv8tion.jda.api.components.thumbnail.Thumbnail;

import java.util.ArrayList;
import java.util.List;

public class ProfileUI {

    public static Container buildProfile(ProfileContext ctx) {
        List<ContainerChildComponent> components = new ArrayList<>();

        String avatarUrl = ctx.event().getMember().getEffectiveAvatarUrl();
        String name = ctx.event().getMember().getEffectiveName();

        ContainerChildComponent header = Section.of(
                Thumbnail.fromUrl(avatarUrl),
                TextDisplay.of("# \\👤 Это пользователь %s".formatted(name)),
                TextDisplay.of("### О себе:"),
                TextDisplay.of("...")
        );
        ContainerChildComponent separator = Separator.createDivider(Separator.Spacing.SMALL);
        ContainerChildComponent main = TextDisplay.of("## Основная информация:");
        ContainerChildComponent owner = TextDisplay.of("\\👑 Создатель дискорд-сервера");
        ContainerChildComponent citizen = TextDisplay.of("\\🪪 Гражданин этого сервера %s");
        ContainerChildComponent reputation = TextDisplay.of("Репутация: \\✨ %d | \\➕ %d");
        ContainerChildComponent firstEntry = TextDisplay.of("Впервые появился здесь %s");
        ContainerChildComponent activity = TextDisplay.of("Актив (д | н | м | весь): %d | %d | %d | %d");
        ContainerChildComponent rewardsHeader = TextDisplay.of("## \\🏆 Награды");
        ContainerChildComponent rewards = TextDisplay.of("...");
        ContainerChildComponent footer = TextDisplay
                .of("-# Это - **локальный** профиль. Статистика берется исключительно с этой гильдии");

        components.add(header);
        components.add(separator);
        components.add(main);
        components.add(owner);
        components.add(citizen);
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

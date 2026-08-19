package com.kika.smllybot.modules.economy.ui;

import com.kika.smllybot.modules.economy.BagContext;
import com.kika.smllybot.utils.NumUtil;
import net.dv8tion.jda.api.components.container.Container;
import net.dv8tion.jda.api.components.container.ContainerChildComponent;
import net.dv8tion.jda.api.components.separator.Separator;
import net.dv8tion.jda.api.components.textdisplay.TextDisplay;

import java.util.ArrayList;
import java.util.List;

public abstract class BagUI {

    public static Container buildBug(BagContext ctx) {
        List<ContainerChildComponent> components = new ArrayList<>(5);

        String irisCoin = NumUtil.german(ctx.bank().getIrisCoin());
        String iris = NumUtil.german(ctx.bank().getIris());
        String star = NumUtil.german(ctx.bank().getStar());

        ContainerChildComponent header = TextDisplay.of("# \\💰 Мешок %s".formatted(ctx.bank().getName()));
        ContainerChildComponent separator = Separator.createDivider(Separator.Spacing.SMALL);
        ContainerChildComponent economy1 = TextDisplay.of("\\🍬 **%s** ирисок | \\⭐ **%s** звездочек".formatted(iris, star));
        ContainerChildComponent economy2 = TextDisplay.of("\\☢️ **%s** i¢".formatted(irisCoin));
        ContainerChildComponent footer = TextDisplay.of("-# Каждый день от звёздности отнимается **0.1%**");
        ContainerChildComponent privacyAuthor = TextDisplay.of("-# Скрыто (видно только вам)");

        if (!ctx.privacy().getBag() || ctx.author() == ctx.target().getIdLong()) {
            // Мешок ...
            components.add(header);
            if (ctx.privacy().getBag()) components.add(privacyAuthor);
            components.add(separator);
            // ... ирисок | ... звездочек
            components.add(economy1);
            // ... i¢
            components.add(economy2);
            // Каждый день от звёздности отнимается 0.1%
            components.add(footer);
        } else {
            ContainerChildComponent privacy = TextDisplay.of("## \\❌ Увы и ах мешок этого пользователя скрыт");
            ContainerChildComponent footerPrivacy = TextDisplay
                    .of("-# Попросите пользователя открыть мешок в настройках приватности");
            components.add(privacy);
            components.add(footerPrivacy);
        }


        return Container.of(components);
    }
}

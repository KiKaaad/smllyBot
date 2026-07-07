package com.kika.smllybot.modules.economy.ui;

import com.kika.smllybot.modules.economy.BagContext;
import com.kika.smllybot.utils.Formatter;
import net.dv8tion.jda.api.components.container.Container;
import net.dv8tion.jda.api.components.container.ContainerChildComponent;
import net.dv8tion.jda.api.components.separator.Separator;
import net.dv8tion.jda.api.components.textdisplay.TextDisplay;

import java.util.ArrayList;
import java.util.List;

public abstract class BagUI {

    public static Container buildBug(BagContext ctx) {
        List<ContainerChildComponent> components = new ArrayList<>(5);

        String irisCoin = Formatter.germanNum(ctx.bank().irisCoin());
        String iris = Formatter.germanNum(ctx.bank().iris());
        String star = Formatter.germanNum(ctx.bank().star());

        ContainerChildComponent header = TextDisplay.of("# \\💰 Мешок %s".formatted(ctx.bank().name()));
        ContainerChildComponent separator = Separator.createDivider(Separator.Spacing.SMALL);
        ContainerChildComponent economy1 = TextDisplay.of("\\🍬 **%s** ирисок | \\⭐ **%s** звездочек".formatted(iris, star));
        ContainerChildComponent economy2 = TextDisplay.of("\\☢️ **%s** i¢".formatted(irisCoin));
        ContainerChildComponent footer = TextDisplay.of("-# Каждый день от звёздности отнимается **0.1%**");

        components.add(header); components.add(separator);
        components.add(economy1); components.add(economy2);
        components.add(footer);


        return Container.of(components);
    }
}

package com.kika.smllybot.modules.fun.ui;

import com.kika.smllybot.modules.fun.FunContext;
import net.dv8tion.jda.api.components.container.Container;
import net.dv8tion.jda.api.components.container.ContainerChildComponent;
import net.dv8tion.jda.api.components.textdisplay.TextDisplay;

import java.util.ArrayList;
import java.util.List;

public class SoftUI {

    public static Container buildSoftUI(FunContext ctx) {
        List<ContainerChildComponent> components = new ArrayList<>(3);

        ContainerChildComponent main = TextDisplay.of("### \\%s | <@%d> %s <@%d> %s"
                .formatted(ctx.emoji(), ctx.author(), ctx.action(), ctx.reply(), ctx.afterText()));
        ContainerChildComponent replica = TextDisplay.of("**С репликой:** " + ctx.replica());

        components.add(main);
        if (ctx.replica() != null) components.add(replica);

        return Container.of(components);
    }

}

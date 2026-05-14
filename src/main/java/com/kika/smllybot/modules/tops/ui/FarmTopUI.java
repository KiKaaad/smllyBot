package com.kika.smllybot.modules.tops.ui;

import com.kika.smllybot.modules.tops.FarmTopContext;
import net.dv8tion.jda.api.components.container.Container;
import net.dv8tion.jda.api.components.container.ContainerChildComponent;
import net.dv8tion.jda.api.components.separator.Separator;
import net.dv8tion.jda.api.components.textdisplay.TextDisplay;

import java.util.ArrayList;
import java.util.List;

public class FarmTopUI {

    public static Container buildFarmTop(FarmTopContext ctx) {
        List<ContainerChildComponent> components = new ArrayList<>(30);

        String[] lines = ctx.topFarm().split("\n");

        // Верхний компонент
        ContainerChildComponent main = TextDisplay.of("# ☢️ Глобальный топ по ирис-коинам");
        components.add(main);

        // Пользователи
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            if (line.trim().isEmpty()) continue;

            ContainerChildComponent user = TextDisplay.of(line);
            components.add(user);

            if (i < lines.length - 1) {
                ContainerChildComponent separator = Separator.createDivider(Separator.Spacing.SMALL);
                components.add(separator);
            }
        }

        return Container.of(components);
    }

}

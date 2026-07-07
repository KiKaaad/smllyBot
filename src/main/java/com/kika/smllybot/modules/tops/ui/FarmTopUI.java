package com.kika.smllybot.modules.tops.ui;

import com.kika.smllybot.modules.tops.FarmTopContext;
import net.dv8tion.jda.api.components.actionrow.ActionRow;
import net.dv8tion.jda.api.components.buttons.Button;
import net.dv8tion.jda.api.components.container.Container;
import net.dv8tion.jda.api.components.container.ContainerChildComponent;
import net.dv8tion.jda.api.components.separator.Separator;
import net.dv8tion.jda.api.components.textdisplay.TextDisplay;

import java.util.ArrayList;
import java.util.List;

public class FarmTopUI {

    public static final int ITEMS_PER_PAGE = 15;

    public static Container buildFarmTop(String emoji,
                                         String title,
                                         String subCommand,
                                         int limit,
                                         int page,
                                         String ownerId,
                                         FarmTopContext ctx) {
        List<ContainerChildComponent> components = new ArrayList<>(40);

        ContainerChildComponent main = TextDisplay.of("# \\%s Глобальный топ по %s".formatted(emoji, title));
        components.add(main);

        List<String> lines = ctx.topFarmLines();
        int totalItems = lines.size();
        int totalPages = (int) Math.ceil((double) totalItems / ITEMS_PER_PAGE);
        if (totalPages == 0) totalPages = 1;

        int start = page * ITEMS_PER_PAGE;
        int end = Math.min(start + ITEMS_PER_PAGE, totalItems);

        for (int i = start; i < end; i++) {
            String line = lines.get(i);
            if (line.trim().isEmpty()) continue;

            components.add(TextDisplay.of(line));

            if (i < end - 1) {
                components.add(Separator.createDivider(Separator.Spacing.SMALL));
            }
        }

        if (totalItems > ITEMS_PER_PAGE) {
            components.add(TextDisplay.of("## Управление топом"));

            String baseId = "gtop:%s:%d".formatted(subCommand, limit);

            Button prevButton = Button.primary("%s:%d:%s".formatted(baseId, page - 1, ownerId), "◀️ Назад");
            Button pageIndicator = Button.secondary("gtop:noop:" + ownerId, "Стр. %d/%d".formatted(page + 1, totalPages))
                    .asDisabled();
            Button nextButton = Button.primary("%s:%d:%s".formatted(baseId, page + 1, ownerId), "Вперед ▶️");

            if (page == 0) prevButton = prevButton.asDisabled();
            if (page >= totalPages - 1) nextButton = nextButton.asDisabled();

            components.add(ActionRow.of(prevButton, pageIndicator, nextButton));
        }

        return Container.of(components);
    }
}

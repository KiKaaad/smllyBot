package com.kika.smllybot.modules.tops.ui;

import com.kika.smllybot.database.sql.bank.BankTable;
import com.kika.smllybot.database.sql.bank.dto.BankTopAmount;
import com.kika.smllybot.handlers.ButtonHandler;
import com.kika.smllybot.modules.tops.FarmTopContext;
import com.kika.smllybot.modules.tops.IFarmTop;
import com.kika.smllybot.other.BaseCmd;
import com.kika.smllybot.utils.Interaction;
import net.dv8tion.jda.api.components.container.Container;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class GlobalTopInteraction extends BaseCmd implements ButtonHandler {

    public GlobalTopInteraction() { super(Set.of("gtop")); }

    @Override
    public void onButton(ButtonInteractionEvent event, String[] args) {
        if (!Interaction.checkOwner(event, args)) return;

        String subCommand = args[1];
        int limit = Integer.parseInt(args[2]);
        int targetPage = Integer.parseInt(args[3]);
        String ownerId = args[args.length - 1];

        String title;
        String icon;
        String suffix;
        List<BankTopAmount> topEntries;
        IFarmTop value;

        switch (subCommand) {
            case "коины" -> {
                title = "ирис-коинам";
                icon = "☢️";
                suffix = "i¢";
                topEntries = BankTable.getTopIrisCoins(limit);
                value = BankTopAmount::amount;
            }
            case "ириски" -> {
                title = "ирискам";
                icon = "🍬";
                suffix = "шт.";
                topEntries = BankTable.getTopIris(limit);
                value = BankTopAmount::amount;
            }
            default -> { return; }
        }

        List<String> topFarmLines = new ArrayList<>();
        for (int i = 0; i < topEntries.size(); i++) {
            BankTopAmount entry = topEntries.get(i);
            String formattedValue = String.format(Locale.GERMAN, "%,d", value.extract(entry));
            String placeEmoji = switch (i) {
                case 0 -> "1. \\🥇";
                case 1 -> "2. \\🥈";
                case 2 -> "3. \\🥉";
                default -> (i + 1) + ".";
            };
            // 🥇 KiKa — 123.456 i¢
            topFarmLines.add("%s **%s** — %s %s".formatted(placeEmoji, entry.name(), formattedValue, suffix));
        }

        FarmTopContext ctx = new FarmTopContext(topFarmLines);

        Container updatedResponse = FarmTopUI.buildFarmTop(
                icon, title,
                subCommand,
                limit, targetPage,
                ownerId, ctx);

        event.editComponents(updatedResponse)
                .useComponentsV2(true)
                .queue();
    }

}

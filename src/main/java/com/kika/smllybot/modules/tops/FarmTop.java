package com.kika.smllybot.modules.tops;

import com.kika.smllybot.database.sql.bank.dto.BankTopAmount;
import com.kika.smllybot.other.BaseCmd;
import com.kika.smllybot.database.sql.bank.BankTable;
import com.kika.smllybot.modules.tops.ui.FarmTopUI;
import net.dv8tion.jda.api.components.container.Container;
import net.dv8tion.jda.api.components.container.ContainerChildComponent;
import net.dv8tion.jda.api.components.textdisplay.TextDisplay;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class FarmTop extends BaseCmd {

    public FarmTop() {
        super(Set.of("гтоп", "gtop"));
    }

    @Override
    public void execute(MessageReceivedEvent event, String arg) {

        String[] parts = arg.trim().split("\\s+", 2);
        String subCommand = parts[0].toLowerCase();

        String title;
        String icon;
        String suffix;
        String ownerId = event.getAuthor().getId();
        List<BankTopAmount> topEntries;
        IFarmTop value;

        int limit = 1000;

        if (parts.length > 2) {

            try {
                int parsedLimit = Integer.parseInt(parts[2].trim());

                if (parsedLimit >= 1 && parsedLimit <= 100) {
                    limit = parsedLimit;
                } else {
                    event.getChannel().sendMessage("🟡 Упс! Число вне диапазона **от 1 до 100**")
                            .delay(Duration.ofSeconds(5))
                            .flatMap(Message::delete)
                            .queue();
                    return;
                }
            } catch (NumberFormatException e) {
                return;
            }

        }

        switch (subCommand) {
            case "коины" -> {
                title = "ирис-коинам";
                icon = "☢️";
                suffix = " i¢";
                topEntries = BankTable.getTopIrisCoins(limit);
                value = BankTopAmount::amount;
            }
            case "ириски" -> {
                title = "ирискам";
                icon = "🍬";
                suffix = " шт.";
                topEntries = BankTable.getTopIris(limit);
                value = BankTopAmount::amount;
            }
            default -> { return; }
        }

        if (topEntries.isEmpty()) {
            ContainerChildComponent main = TextDisplay.of("## \\💀 Как-то тут пусто однако...");
            Container response = Container.of(main);

            event.getChannel().sendMessageComponents(response)
                    .useComponentsV2(true)
                    .queue();
            return;
        }

        List<String> topLines = new ArrayList<>();
        for (int i = 0; i < topEntries.size(); i++) {
            BankTopAmount entry = topEntries.get(i);

            String formattedValue = String.format(Locale.GERMAN, "%,d", value.extract(entry));

            String placeEmoji = switch (i) {
                case 0 -> "1. \\🥇";
                case 1 -> "2. \\🥈";
                case 2 -> "3. \\🥉";
                default -> (i + 1) + ".";
            };

            String line = "%s **%s** — %s%s".formatted(placeEmoji, entry.name(), formattedValue, suffix);
            topLines.add(line);
        }

        FarmTopContext ctx = new FarmTopContext(topLines);

        Container response = FarmTopUI.buildFarmTop(icon, title, subCommand, limit, 0, ownerId, ctx);

        event.getChannel().sendMessageComponents(response)
                .useComponentsV2(true)
                .queue();
    }
}

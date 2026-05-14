package com.kika.smllybot.modules.tops;

import com.kika.smllybot.Main;
import com.kika.smllybot.database.postgresql.bank.BankTable;
import com.kika.smllybot.database.postgresql.bank.GetBank;
import com.kika.smllybot.modules.tops.ui.FarmTopUI;
import net.dv8tion.jda.api.components.container.Container;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.time.Duration;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class FarmTop extends ListenerAdapter {

    private static final Set<String> COMMANDS = Set.of("топ", "top");

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;

        String rawContent = event.getMessage().getContentRaw().trim();
        String prefix = Main.prefixes[0];

        if (!rawContent.toLowerCase().startsWith(prefix.toLowerCase())) return;

        String withoutPrefix = rawContent.substring(prefix.length()).trim();
        String[] parts = withoutPrefix.split("\\s+");
        String command = parts[0].toLowerCase();

        if (!COMMANDS.contains(command)) return;

        // Дефолтный лимит - 30
        int limit = 30;

        if (parts.length > 1) {

            try {
                int parsedLimit = Integer.parseInt(parts[1].trim());

                if (parsedLimit >= 1 && parsedLimit <= 50) {
                    limit = parsedLimit;
                } else {
                    event.getChannel().sendMessage("🟡 Упс! Число вне диапазона **от 1 до 50**")
                            .delay(Duration.ofSeconds(5))
                            .flatMap(Message::delete)
                            .queue();
                    return;
                }
            } catch (NumberFormatException e) {
                // TODO: Дебаг вывод требуется удалить
                System.out.println("В FarmTop пользователь ввел что-то не то: топ <тут_неверный_аргумент>. Вполне возможно он не хотел вызывать команду");
                return;
            }

        }

        List<GetBank> topEntries = BankTable.getTopIrisCoins(limit);

        // Топ пустой
        if (topEntries.isEmpty()) {
            event.getChannel().sendMessage("💀 Как-то тут пусто однако...").queue();
            return;
        }

        // Делаем все красивенько:
        // Значки для первых 3 мест
        // {число}. Жирный ник - число ирис-коинов
        StringBuilder topFarm = new StringBuilder();
        for (int i = 0; i < topEntries.size(); i++) {
            GetBank entry = topEntries.get(i);
            String irisCoin = String.format(Locale.GERMAN, "%,d", entry.irisCoin());

            String placeEmoji = switch (i) {
                case 0 -> "1. 🥇";
                case 1 -> "2. 🥈";
                case 2 -> "3. 🥉";
                default -> (i + 1) + ".";
            };

            topFarm.append(placeEmoji)
                    .append(" **")
                    .append(entry.name())
                    .append("** — ")
                    .append(irisCoin)
                    .append(" i¢\n");
        }

        FarmTopContext ctx = new FarmTopContext(
                topFarm.toString()
        );

        Container response = FarmTopUI.buildFarmTop(ctx);

        event.getChannel().sendMessageComponents(response)
                .useComponentsV2(true)
                .queue();
    }

}

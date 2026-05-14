package com.kika.smllybot.modules.economy;

import com.kika.smllybot.Main;
import com.kika.smllybot.database.postgresql.bank.BankTable;
import com.kika.smllybot.database.postgresql.bank.GetBank;
import com.kika.smllybot.database.postgresql.user.GetUsers;
import com.kika.smllybot.database.postgresql.user.UserTable;
import com.kika.smllybot.modules.economy.ui.FarmUI;
import com.kika.smllybot.utils.PrefixUtil;
import com.kika.smllybot.utils.TimeUtil;
import net.dv8tion.jda.api.components.container.Container;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Locale;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class Farm extends ListenerAdapter {

    private static final Set<String> COMMANDS = Set.of("фарма", "ферма", "фарм", "farm", "ferma");

    private static int baseReward() {
        int roll = ThreadLocalRandom.current().nextInt(0, 1000);

        // Шанс 0.5%
        if (roll < 5) return ThreadLocalRandom.current().nextInt(1000, 2000);
        // Шанс 5%
        if (roll < 55) return ThreadLocalRandom.current().nextInt(200, 500);

        // Шанс 94.5%
        return ThreadLocalRandom.current().nextInt(0, 50);
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;

        String command = PrefixUtil.getCommandBody(event.getMessage().getContentRaw(), Main.prefixes);
        if (command == null || !COMMANDS.contains(command.toLowerCase())) return;

        long discordId = event.getAuthor().getIdLong();
        String name = event.getAuthor().getEffectiveName();

        GetUsers user = UserTable.getOrCreateUser(discordId);
        GetBank bank = BankTable.getOrCreateBank(user.internalId(), name);

        // Расчет фармы с момента, когда последний раз команда использована
        // Разница = Время сейчас - время в бд
        long waitMillis = System.currentTimeMillis() - bank.lastFarm().getTime();
        long minWait = 4 * 60 * 60 * 1000L;     // Часы в минуты в секунды в миллисекунды
        long maxWait = 24 * 60 * 60 * 1000L;    // Absolute cinema

        // Прошло менее 4 часов, перед получением ирисисев
        if (waitMillis < minWait) {
            long timeLeft = minWait - waitMillis;
            String formattedTime = TimeUtil.formatTimeLeft(timeLeft);

            FarmContext ctx = new FarmContext(0, 0, "", formattedTime);

            Container response = FarmUI.buildFarmOther(ctx);

            event.getChannel().sendMessageComponents(response)
                    .useComponentsV2(true)
                    .queue();
            return;
        }

        double multiplier;
        if (waitMillis >= maxWait) {
            multiplier = 3.0;
        } else {
            // Получаем коэффициент
            double progress = (double) (waitMillis - minWait) / (maxWait - minWait);
            multiplier = 1.0 + (progress * 2.0);
        }
        multiplier = Math.round(multiplier * 100.0) / 100.0;

        String multiplierText = String.format(Locale.US, "%.2f", multiplier);

        long baseReward = baseReward();
        long finalReward = (long) (baseReward * multiplier);

        BankTable.addIrisCoin(user.internalId(), finalReward);
        BankTable.updateLastFarm(user.internalId());

        FarmContext ctx = new FarmContext(
                baseReward,
                finalReward,
                multiplierText,
                ""
        );

        Container response = FarmUI.buildFarm(ctx);

        event.getChannel().sendMessageComponents(response)
                .useComponentsV2(true)
                .queue();

    }
}
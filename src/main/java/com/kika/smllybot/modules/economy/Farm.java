package com.kika.smllybot.modules.economy;

import com.kika.smllybot.database.sql.bank.BankTable;
import com.kika.smllybot.database.sql.bank.dto.BankAccount;
import com.kika.smllybot.database.sql.user.UserTable;
import com.kika.smllybot.database.sql.user.dto.UserAccount;
import com.kika.smllybot.modules.economy.ui.FarmUI;
import com.kika.smllybot.other.BaseCmd;
import com.kika.smllybot.utils.TimeUtil;
import net.dv8tion.jda.api.components.container.Container;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Locale;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class Farm extends BaseCmd {

    public Farm() {
        super(Set.of("фарма", "ферма", "фарм", "farm", "ferma"));
    }

    private static int baseReward() {
        int roll = ThreadLocalRandom.current().nextInt(0, 1000);

        // Шанс 0.1% потерять ирис-коины
        if (roll < 1) return ThreadLocalRandom.current().nextInt(-51, 0);
        // Шанс 0.5%
        if (roll < 5) return ThreadLocalRandom.current().nextInt(1000, 2001);
        // Шанс 5%
        if (roll < 55) return ThreadLocalRandom.current().nextInt(200, 501);

        // Шанс 94.5%
        return ThreadLocalRandom.current().nextInt(5, 51);
    }

    @Override
    public void execute(MessageReceivedEvent event, String arg) {

        long discordId = event.getAuthor().getIdLong();
        String name = event.getAuthor().getEffectiveName();

        UserAccount user = UserTable.getOrCreateUser(discordId, name);
        assert user != null;
        BankAccount bank = BankTable.getOrCreateBank(user.internalId(), name);

        // Расчет фармы с момента, когда последний раз команда использована
        // Разница = Время сейчас - время в бд
        assert bank != null;
        long waitMillis = System.currentTimeMillis() - bank.lastFarm().getTime();
        long minWait = 4 * 60 * 60 * 1000L;     // Часы в минуты в секунды в миллисекунды
        long maxWait = 24 * 60 * 60 * 1000L;    // Absolute cinema

        // Прошло менее 4 часов, перед получением ирисисев
        if (waitMillis < minWait) {
            long timeLeft = minWait - waitMillis;
            String formattedTime = TimeUtil.formatTimeLeft(timeLeft);

            FarmContext ctx = new FarmContext(0, 0, 0, "", formattedTime);

            Container response = FarmUI.buildFarmOther(ctx);

            event.getChannel().sendMessageComponents(response)
                    .useComponentsV2(true)
                    .queue();
            return;
        }

        long star = bank.star();

        double multiplier;
        // Чтобы получить максимальный множитель, нужно иметь 10.000 звездочек
        double starMultiplier = (star * 0.001) + 1;
        if (starMultiplier >= 10) starMultiplier = 10;

        if (waitMillis >= maxWait) {
            multiplier = 3.0;
        } else {
            double progress = (double) (waitMillis - minWait) / (maxWait - minWait);
            multiplier = 1.0 + (progress * 2.0);
        }
        multiplier = Math.round(multiplier * 100.0) / 100.0;

        String multiplierText = String.format(Locale.US, "%.2f", multiplier);

        long baseReward = baseReward();
        if (baseReward < 0) starMultiplier = 1;
        long finalReward = (long) (baseReward * multiplier * starMultiplier);

        BankTable.addIrisCoin(user.internalId(), finalReward);
        BankTable.updateLastFarm(user.internalId());

        FarmContext ctx = new FarmContext(
                baseReward,
                finalReward,
                starMultiplier,
                multiplierText,
                ""
        );

        Container response = FarmUI.buildFarm(ctx);

        event.getChannel().sendMessageComponents(response)
                .useComponentsV2(true)
                .queue();

    }
}
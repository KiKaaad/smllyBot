package com.kika.smllybot.modules.economy;

import com.kika.smllybot.Config;
import com.kika.smllybot.database.sql.bank.BankTable;
import com.kika.smllybot.database.sql.bank.dto.BankAccount;
import com.kika.smllybot.database.sql.users.UsersTable;
import com.kika.smllybot.database.sql.users.dto.UserAccount;
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

        int minLoss = Config.getInstance().getInt("economy.min_loss");
        int maxLoss = Config.getInstance().getInt("economy.max_loss");
        int min = Config.getInstance().getInt("economy.min");
        int max = Config.getInstance().getInt("economy.max");
        int minMini = Config.getInstance().getInt("economy.min_mini");
        int maxMini = Config.getInstance().getInt("economy.max_mini");
        int minLarge = Config.getInstance().getInt("economy.min_mini");
        int maxLarge = Config.getInstance().getInt("economy.max_large");
        double chanceLossReward = Config.getInstance().getFloat("economy.chances.loss_reward");
        double chanceMiniReward = Config.getInstance().getFloat("economy.chances.mini_reward");
        double chanceLargeReward = Config.getInstance().getFloat("economy.chances.large_reward");

        double currentSum = 0.0;

        double roll = ThreadLocalRandom.current().nextDouble(0.0, 100.0);

        // +1 Так как верхняя граница не учитывается. То есть если верхняя граница 100, максимум выпадет 99, мяу
        currentSum += chanceLossReward;
        if (roll < currentSum) return ThreadLocalRandom.current().nextInt(maxLoss, minLoss + 1);
        currentSum += chanceMiniReward;
        if (roll < currentSum) return ThreadLocalRandom.current().nextInt(minLarge, maxLarge + 1);
        currentSum += chanceLargeReward;
        if (roll < currentSum) return ThreadLocalRandom.current().nextInt(minMini, maxMini + 1);

        return ThreadLocalRandom.current().nextInt(min, max);
    }

    @Override
    public Container execute(MessageReceivedEvent event, String raw, String args) {

        long discordId = event.getAuthor().getIdLong();
        String name = event.getAuthor().getEffectiveName();

        UserAccount user = UsersTable.getOrCreateUser(discordId, name);
        BankAccount bank = BankTable.getOrCreateBank(user.id(), name);

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
            return response;
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

        BankTable.addIrisCoin(user.id(), finalReward);
        BankTable.updateLastFarm(user.id());

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

        return response;
    }
}
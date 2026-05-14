package com.kika.smllybot.modules.economy.ui;

import com.kika.smllybot.modules.economy.FarmContext;
import net.dv8tion.jda.api.components.container.Container;
import net.dv8tion.jda.api.components.container.ContainerChildComponent;
import net.dv8tion.jda.api.components.separator.Separator;
import net.dv8tion.jda.api.components.textdisplay.TextDisplay;

import java.util.ArrayList;
import java.util.List;

public abstract class FarmUI {

    public static Container buildFarm(FarmContext ctx) {
        List<ContainerChildComponent> components = new ArrayList<>(5);

        // База до 150
        ContainerChildComponent main0Base = TextDisplay.of("## ✅ ЗАЧЁТ! ☢️ +%d i¢ = %d×%s"
                .formatted(ctx.finalReward(), ctx.baseReward(), ctx.multiplierText())
        );
        // Бонус до 500
        ContainerChildComponent main0Bonus = TextDisplay.of("## ✅ 🔥 УДАЧА!!! Вы нашли затерянный сундук с ирисками! ☢️ +%d i¢ = %d×%s"
                .formatted(ctx.finalReward(), ctx.baseReward(), ctx.multiplierText())
        );
        // Бонус после 500
        ContainerChildComponent main0BonusPlus = TextDisplay.of("## ✅ 🔥 💰 МЕГАУДАЧА!!! Вы нашли хранилище с ирисками и забрали часть с него! ☢️ +%d i¢ = %d×%s"
                .formatted(ctx.finalReward(), ctx.baseReward(), ctx.multiplierText())
        );
        ContainerChildComponent separator = Separator.createDivider(Separator.Spacing.SMALL);
        ContainerChildComponent main1 = TextDisplay.of("✨ **Сила звёздности:** %.2f");
        ContainerChildComponent main2 = TextDisplay.of("⏳ **Урожайность:** %s".formatted(ctx.multiplierText()));

        if (ctx.baseReward() <= 150) components.add(main0Base);
        if (ctx.baseReward() > 150 && ctx.baseReward() <= 500) components.add(main0Bonus);
        if (ctx.baseReward() > 501) components.add(main0BonusPlus);
        components.add(separator);
        components.add(main1);
        components.add(main2);

        return Container.of(components);
    }

    // Прошло менее 4 часов, перед получением ирисисев
    public static Container buildFarmOther(FarmContext ctx) {
        List<ContainerChildComponent> components = new ArrayList<>(3);

        ContainerChildComponent main0 = TextDisplay.of("## ❌ НЕЗАЧЁТ! Фармить можно раз в 4 часа.");
        ContainerChildComponent main1 = TextDisplay.of("Следующая добыча через **%s**".formatted(ctx.cooldown()));

        components.add(main0);
        components.add(main1);

        return Container.of(components);
    }

}

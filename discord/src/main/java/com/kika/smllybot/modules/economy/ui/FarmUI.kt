package com.kika.smllybot.modules.economy.ui

import com.kika.smllybot.modules.economy.FarmContext
import net.dv8tion.jda.api.components.container.Container
import net.dv8tion.jda.api.components.container.ContainerChildComponent
import net.dv8tion.jda.api.components.separator.Separator
import net.dv8tion.jda.api.components.textdisplay.TextDisplay
import java.util.Locale

class FarmUI {

    companion object {
        @JvmStatic
        fun buildFarm(ctx: FarmContext): Container {
            val components: MutableList<ContainerChildComponent?> = ArrayList(5)

            val multiplierText: String = String.format(Locale.US, "%.2f", ctx.starMultiplier)

            if (ctx.cooldown != null) {
                val main: ContainerChildComponent = TextDisplay.of(
                    """
                    ## \❌ НЕЗАЧЁТ! Фармить можно раз в 4 часа.
                    Следующая добыча через **${ctx.cooldown}**
                    """.trimIndent()
                )

                components.add(main)

                return Container.of(components)
            }

            val mainMinus = TextDisplay.of(
                "## \\❌ КРИТИЧЕСКИЙ ПРОМАХ! Вы настолько растяпа, что потеряли ${ctx.finalReward} i¢"
            )

            // База до 150
            val mainBase: ContainerChildComponent = TextDisplay.of(
                "## \\✅ ЗАЧЁТ! \\☢️ +${ctx.finalReward} i¢ = ${ctx.baseReward}×${ctx.multiplierText}"
            )

            // Бонус до 500
            val mainBonus: ContainerChildComponent = TextDisplay.of(
                "## \\✅ \\🔥 УДАЧА!!! Вы нашли затерянный сундук с ирисками! \\☢️ +${ctx.finalReward} i¢ = ${ctx.baseReward}×${ctx.multiplierText}"
            )

            // Бонус после 500
            val mainBonusPlus: ContainerChildComponent = TextDisplay.of(
                "## \\✅ \\🔥 \\💰 МЕГАУДАЧА!!! Вы нашли хранилище с ирисками и забрали часть с него! \\☢️ +${ctx.finalReward} i¢ = ${ctx.baseReward}×${ctx.multiplierText}"
            )
            val separator: ContainerChildComponent = Separator.createDivider(Separator.Spacing.SMALL)
            val footer: ContainerChildComponent = TextDisplay.of(
                """
                    \✨ **Сила звёздности:** $multiplierText
                    \⏳ **Урожайность:** ${ctx.multiplierText}
                    """
                    .trimIndent()
            )

            if (ctx.baseReward < 0) components.add(mainMinus)
            if (ctx.baseReward in 1..150) components.add(mainBase)
            if (ctx.baseReward in 151..500) components.add(mainBonus)
            if (ctx.baseReward > 501) components.add(mainBonusPlus)
            components.add(separator)
            components.add(footer)

            return Container.of(components)
        }
    }
}
package com.kika.smllybot.modules.economy.ui

import com.kika.smllybot.modules.economy.BagContext
import com.kika.smllybot.utils.NumUtil
import net.dv8tion.jda.api.components.container.Container
import net.dv8tion.jda.api.components.container.ContainerChildComponent
import net.dv8tion.jda.api.components.separator.Separator
import net.dv8tion.jda.api.components.textdisplay.TextDisplay

class BagUI {

    companion object {
        @JvmStatic
        fun buildBug(ctx: BagContext): Container {
            val components: MutableList<ContainerChildComponent?> = ArrayList(10)

            val irisCoin = NumUtil.german(ctx.bank.irisCoin)
            val iris = NumUtil.german(ctx.bank.iris)
            val star = NumUtil.german(ctx.bank.star)

            val header = TextDisplay.of("# \\💰 Мешок ${ctx.bank.name.replace("@", "\\@")}")
            val separator = Separator.createDivider (Separator.Spacing.SMALL)
            val economy1 = TextDisplay.of("\\🍬 **$iris** ирисок | \\⭐ **$star** звездочек")
            val economy2 = TextDisplay.of("\\☢️ **$irisCoin** i¢")
            val footer = TextDisplay.of("-# Каждый день от звёздности отнимается **0.1%**")
            val privacyAuthor = TextDisplay.of("-# Скрыто (видно только вам)")

            if (!ctx.privacy.bag || ctx.author == ctx.target.idLong) {
                // Мешок ...
                components.add(header)
                if (ctx.privacy.bag) components.add(privacyAuthor)
                components.add(separator)
                // ... ирисок | ... звездочек
                components.add(economy1)
                // ... i¢
                components.add(economy2)
                // Каждый день от звёздности отнимается 0.1%
                components.add(footer)
            } else {
                val privacy = TextDisplay.of("## \\❌ Увы и ах мешок этого пользователя скрыт")
                val footerPrivacy = TextDisplay
                    .of("-# Попросите пользователя открыть мешок в настройках приватности")
                components.add(privacy)
                components.add(footerPrivacy)
            }

            return Container.of(components)
        }
    }
}
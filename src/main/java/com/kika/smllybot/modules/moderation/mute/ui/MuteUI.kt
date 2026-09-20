package com.kika.smllybot.modules.moderation.mute.ui

import com.kika.smllybot.utils.TimeUtil
import net.dv8tion.jda.api.components.container.Container
import net.dv8tion.jda.api.components.container.ContainerChildComponent
import net.dv8tion.jda.api.components.separator.Separator
import net.dv8tion.jda.api.components.textdisplay.TextDisplay
import java.time.OffsetDateTime

class MuteUI {

    companion object {
        @JvmStatic
        fun build(discordId: Long, byDiscordId: Long, time: OffsetDateTime?, reason: String?): Container {
            val components: MutableList<ContainerChildComponent?> = ArrayList(12)

            val timeRelative = time?.let { TimeUtil.getTimestampRelative(it) } ?: "**навсегда**"

            val header = TextDisplay.of("## \\🤫 <@$discordId> лишается права слова на $timeRelative")
            val main = TextDisplay.of("**Модератор:** <@$byDiscordId>")
            val reasonText = TextDisplay.of("**Причина:** $reason")

            components.add(header)
            components.add(main)
            if (reason != null) components.add(reasonText)

            return Container.of(components)
        }

        @JvmStatic
        fun buildError(): Container {
            val components: MutableList<ContainerChildComponent?> = ArrayList(12)

            val header = TextDisplay.of("# \\❌ Недостаточно прав")
            val separator = Separator.createDivider(Separator.Spacing.SMALL)
            val main = TextDisplay.of("Требуется право выдавать таймауты")
            val footer = TextDisplay.of("-# `Permission.MODERATE_MEMBERS`")

            components.add(header)
            components.add(separator)
            components.add(main)
            components.add(footer)

            return Container.of(components)
        }
    }
}
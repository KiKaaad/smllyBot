package com.kika.smllybot.modules.moderation.warn.ui

import com.kika.smllybot.database.sql.guild.warn.dto.WarnCreateData
import com.kika.smllybot.utils.TimeUtil
import net.dv8tion.jda.api.components.container.Container
import net.dv8tion.jda.api.components.container.ContainerChildComponent
import net.dv8tion.jda.api.components.separator.Separator
import net.dv8tion.jda.api.components.textdisplay.TextDisplay
import java.time.OffsetDateTime

class WarnUI {

    companion object {
        @JvmStatic
        fun build(data: WarnCreateData, time: OffsetDateTime?, warnCount: Int): Container {
            val components: MutableList<ContainerChildComponent> = mutableListOf()

            val normalizeTime = TimeUtil.formatTimeLeft(time?.plusSeconds(1)) ?: "навсегда"

            val header = TextDisplay.of("# \\❗ <@${data.discordId}> получает предупреждение ($warnCount/3)")
            val separator = Separator.createDivider(Separator.Spacing.SMALL)
            val time = TextDisplay.of("\\🕐 Выдано на **$normalizeTime**")
            val reason = TextDisplay.of("\\📋 **Причина:** ${data.reason}")

            components.add(header)
            components.add(separator)
            components.add(time)
            if (data.reason != null) components.add(reason)

            return Container.of(components)
        }

        @JvmStatic
        fun buildKick(discordId: Long): Container {
            val header = TextDisplay.of("## \\❗\\❗\\❗ <@$discordId> достиг лимита варнов (3/3). Кикаю...")

            return Container.of(header)
        }

        @JvmStatic
        fun buildUnWarn(discordId: Long): Container {
            val header = TextDisplay.of("## \\✅ С <@$discordId> сняты все варны!")

            return Container.of(header)
        }
    }
}
package com.kika.smllybot.listeners.ui

import net.dv8tion.jda.api.components.actionrow.ActionRow
import net.dv8tion.jda.api.components.buttons.Button
import net.dv8tion.jda.api.components.buttons.ButtonStyle
import net.dv8tion.jda.api.components.container.Container
import net.dv8tion.jda.api.components.container.ContainerChildComponent
import net.dv8tion.jda.api.components.separator.Separator
import net.dv8tion.jda.api.components.textdisplay.TextDisplay
import net.dv8tion.jda.api.events.guild.GuildBanEvent

class UserBannedUI {

    companion object {
        @JvmStatic
        fun defaultHelp(ctx: GuildBanEvent): Container {

            val header = TextDisplay.of("### Вы заблокированы в гильдии ${ctx.guild.name} `${ctx.guild.idLong}`")
            val separator = Separator.createDivider(Separator.Spacing.SMALL)
            val reason = TextDisplay.of("**Причина:** \$reason")

            val page = buildList {
                add(header)
                add(separator)
                add(reason)
            }

            return Container.of(page)
        }
    }
}
package com.kika.smllybot.modules.guild.ui

import dev.minn.jda.ktx.interactions.components.TextDisplay
import net.dv8tion.jda.api.components.container.Container
import net.dv8tion.jda.api.components.container.ContainerChildComponent
import net.dv8tion.jda.api.components.separator.Separator
import net.dv8tion.jda.api.components.textdisplay.TextDisplay

class GuildPrefixUI {

    companion object {
        @JvmStatic
        fun build(prefix: List<String>): Container {
            val components: MutableList<ContainerChildComponent?> = ArrayList(12)

            val header = TextDisplay.of("# \\✅ Префиксы установлены")
            val separator = Separator.createDivider(Separator.Spacing.SMALL)
            val prefixes = TextDisplay.of("Новые префиксы: ${prefix.joinToString(", ")}")

            components.add(header)
            components.add(separator)
            components.add(prefixes)

            return Container.of(components)
        }
    }
}
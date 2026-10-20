package com.kika.smllybot.modules.`fun`.ui

import net.dv8tion.jda.api.components.container.Container
import net.dv8tion.jda.api.components.container.ContainerChildComponent
import net.dv8tion.jda.api.components.textdisplay.TextDisplay
import java.util.concurrent.ThreadLocalRandom

class InfaUI {

    companion object {
        @JvmStatic
        fun build(name: String): Container {
            val components: MutableList<ContainerChildComponent> = ArrayList(3)

            val random = ThreadLocalRandom.current().nextInt(0, 101)

            val main = TextDisplay.of("### $name я думаю, что вероятность $random%")

            components.add(main)

            return Container.of(components)
        }
    }

}
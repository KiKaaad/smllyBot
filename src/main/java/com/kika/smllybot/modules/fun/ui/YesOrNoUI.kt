package com.kika.smllybot.modules.`fun`.ui

import net.dv8tion.jda.api.components.container.Container
import net.dv8tion.jda.api.components.container.ContainerChildComponent
import net.dv8tion.jda.api.components.textdisplay.TextDisplay
import java.util.LinkedList
import java.util.concurrent.ThreadLocalRandom

class YesOrNoUI {

    companion object {
        @JvmStatic
        fun build(name: String): Container {
            val components: MutableList<ContainerChildComponent> = ArrayList(3)

            val result: Array<String> = arrayOf(
                "думаю, что лучше не говорить об этом",
                "да",
                "нет",
                "не сегодня",
                "не в этот день",
                "можешь быть уверен в этом",
                "не знаю",
                "не могу быть уверен",
                "можешь быть уверен в этом",
                "хороший вопрос...",
                "тяжелый вопрос...",
                "частично",
                "))",
                "тут даже нет смысла говорить, все и так понятно",
                "рядом",
                "Z"
            )

            val random = ThreadLocalRandom.current().nextInt(0, result.size)

            val main = TextDisplay.of("### \\🎱 $name ${result[random]}")

            components.add(main)

            return Container.of(components)
        }
    }

}
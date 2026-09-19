package com.kika.smllybot.modules.guild.ui

import com.kika.smllybot.modules.guild.GuildInfoContext
import com.kika.smllybot.modules.guild.StagingContext
import net.dv8tion.jda.api.components.buttons.Button
import net.dv8tion.jda.api.components.container.Container
import net.dv8tion.jda.api.components.container.ContainerChildComponent
import net.dv8tion.jda.api.components.section.Section
import net.dv8tion.jda.api.components.textdisplay.TextDisplay

class StagingUI {

    companion object {
        @JvmStatic
        fun build(ctx: StagingContext): Container {
            val components: MutableList<ContainerChildComponent?> = ArrayList(12)

            val button: Button = if (ctx.guild.staging) Button.success("staging:on:${ctx.authorId}","✅")
            else Button.danger("staging:off:${ctx.authorId}","❌")

            val header = Section.of(
                button,
                TextDisplay.of("# \\🛠️ Режим раннего доступа")
            )
            val text = TextDisplay.of("""
                ### Учтите:
                - Команды могут работать нестабильно
                - Команды могут иметь уязвимости
                - При найденных ошибках сообщить об этом на [GitHub issues](https://github.com/KiKaaad/smllyBot/issues) или [сервер поддержки](https://discord.gg/3JSz5fEeee)
                """.trimIndent())
            val new = TextDisplay.of("""
                ## При включении станут доступны:
                ### \🛡️ Модерация
                - `бан` <юзернейм / айди / никнейм>\❔ <время>\❔ <причина>\❔
                   - `разбан` <юзернейм / айди / никнейм>\❔
                - `мут` <юзернейм / айди / никнейм>\❔ <время>\❔ <причина>\❔
                   - `размут` <юзернейм / айди / никнейм>\❔
                - `варн` <юзернейм / айди / никнейм>\❔ <время>\❔ <причина>\❔
                   - `-варн` <юзернейм / айди / никнейм>\❔
                -# \❔ — знаком вопроса помечаются необязательные аргументы
            """.trimIndent())

            components.add(header)
            components.add(text)
            components.add(new)

            return Container.of(components)
        }

        @JvmStatic
        fun buildError(): Container {
            val components: MutableList<ContainerChildComponent?> = ArrayList(12)

            val main = TextDisplay.of("# \\❌ Возникла ошибка")
            val text = TextDisplay.of("Похоже, вы не имеете прав администратора")

            components.add(main)
            components.add(text)

            return Container.of(components)
        }
    }
}
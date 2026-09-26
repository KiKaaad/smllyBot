package com.kika.smllybot.modules.guild.ui

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
                - `мут` <юзернейм / айди / никнейм>\❔ <время>\❔ <тип>\❔ <причина>\❔\⬇️ - выдает тайм-аут на срок не более 28 дней. Также можно использовать в ответ на чье-либо сообщение
                   - `<тип>`: секунды, минуты, часы, дни, недели, месяца, года (последние 2 типа временно не поддерживаются)
                   - **Пример**:
                   ```
                   мут 1234567801213 14 дней
                   low iq
                   ```
                -# \❔ — знаком вопроса помечаются необязательные аргументы
                -# \⬇️ — знаком вниз помечаются переносы строки
            """.trimIndent())

            components.add(header)
            components.add(text)
            components.add(new)

            return Container.of(components)
        }
    }
}
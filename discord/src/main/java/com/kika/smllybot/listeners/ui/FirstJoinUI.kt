package com.kika.smllybot.listeners.ui

import net.dv8tion.jda.api.components.actionrow.ActionRow
import net.dv8tion.jda.api.components.buttons.Button
import net.dv8tion.jda.api.components.buttons.ButtonStyle
import net.dv8tion.jda.api.components.container.Container
import net.dv8tion.jda.api.components.container.ContainerChildComponent
import net.dv8tion.jda.api.components.section.Section
import net.dv8tion.jda.api.components.separator.Separator
import net.dv8tion.jda.api.components.textdisplay.TextDisplay
import net.dv8tion.jda.api.components.thumbnail.Thumbnail

class FirstJoinUI {

    companion object {
        @JvmStatic
        fun defaultHelp(avatar: String): Container {

            val header = Section.of(
                Thumbnail.fromUrl(avatar),
                TextDisplay.of("# Привет \\👋 Спасибо за установку бота <3"),
                TextDisplay.of(
                    """
                Бот находится в активном процессе разработки. У бота может быть множество ошибок которые я стараюсь исправлять по мере своих возможностей
                """.trimIndent()
                )
            )
            val info = TextDisplay.of("""
                Дефолтные префиксы бота - `!`, `.`, `ирис`. Большинство команд работает без префиксов
                -# Вечный (если решите установить кастомный префикс и забудите его) - <@873596380659191859> [команда]
                """.trimIndent())
            val info1 = TextDisplay.of("Расширенная помощь бота - `хелп`")
            val wiki = Section.of(
                Button.of(ButtonStyle.LINK, "https://wiki.smlly.cc/", "📖 Вики"),
                TextDisplay.of("## \\📖 Вики"),
                TextDisplay.of("""
                    Оно наиболее удобно в отличии от встроенной помощи в команде `хелп` из-за большего разнообразия визуальных подсказок
                    """.trimIndent())
            )
            val bugs = Section.of(
                Button.of(ButtonStyle.LINK, "https://github.com/KiKaaad/smllyBot/issues", "🐞 Нашел ошибку"),
                TextDisplay.of("## \\🐞 Баги"),
                TextDisplay.of("""
                    Если вы нашли баг, вы можете сообщить об этом на гитхаб или в нашем дискорд-сервере поддержки
                    """.trimIndent())
            )
            val github = Section.of(
                Button.of(ButtonStyle.LINK, "https://github.com/KiKaaad/smllyBot", "🐈‍⬛ Гитхаб"),
                TextDisplay.of("## \\⚙️ Исходный код"),
                TextDisplay.of("""
                    Исходный код позволяет создать свою self-host версию, если захотите быть более независимыми. 
                    Читайте [README.md](https://github.com/KiKaaad/smllyBot/blob/main/README.md) или [главную страницу](https://smlly.gitbook.io/i) вики
                    """.trimIndent())
            )
            val separator: ContainerChildComponent = Separator.createDivider(Separator.Spacing.SMALL)
            val button = ActionRow.of(
                Button.of(ButtonStyle.LINK, "https://discord.gg/3JSz5fEeee", "🛟 Сервер поддержки"),
                Button.of(ButtonStyle.LINK, "https://t.me/smaaaly", "✈️ Телеграм канал"),
                Button.of(ButtonStyle.LINK, "https://t.me/kikaaad", "🤩 Разработчик")
            )

            val page = buildList {
                add(header)
                add(info)
                add(info1)
                add(separator)
                add(wiki)
                add(separator)
                add(bugs)
                add(separator)
                add(github)
                add(button)
            }

            return Container.of(page)
        }
    }
}
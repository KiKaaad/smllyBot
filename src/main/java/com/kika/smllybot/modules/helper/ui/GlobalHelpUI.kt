package com.kika.smllybot.modules.helper.ui

import net.dv8tion.jda.api.components.actionrow.ActionRow
import net.dv8tion.jda.api.components.buttons.Button
import net.dv8tion.jda.api.components.buttons.ButtonStyle
import net.dv8tion.jda.api.components.container.Container
import net.dv8tion.jda.api.components.container.ContainerChildComponent
import net.dv8tion.jda.api.components.section.Section
import net.dv8tion.jda.api.components.separator.Separator
import net.dv8tion.jda.api.components.textdisplay.TextDisplay

class GlobalHelpUI {

    companion object {
        @JvmStatic
        fun defaultHelp(discordId: Long): Container {

            val header: ContainerChildComponent = TextDisplay.of("# \\ℹ️ Справочная информация")
            val mainInfo: ContainerChildComponent = TextDisplay.of(
                """
                Здесь содержится вся информация о боте которая может быть полезна вам.
                На сервере поддержки также можно предложить свои идеи для бота.
                """.trimIndent()
            )
            val buttons: ContainerChildComponent = ActionRow.of(
                Button.of(ButtonStyle.LINK, "https://discord.gg/3JSz5fEeee", "🛟 Сервер поддержки"),
                Button.of(ButtonStyle.LINK, "https://github.com/KiKaaad/smllyBot", "🐈‍⬛ Гитхаб"),
                Button.of(ButtonStyle.LINK, "https://github.com/KiKaaad/smllyBot/issues", "🐞 Нашел ошибку")
            )
            val separator: ContainerChildComponent = Separator.createDivider(Separator.Spacing.SMALL)
            val userModule: ContainerChildComponent = Section.of(
                Button.of(ButtonStyle.SECONDARY, "help:anketaAndProfile:$discordId", "➡️"),
                TextDisplay.of("## \\👤 Профили & Анкеты"),
                TextDisplay
                    .of("### Анкеты и профили описывают конкретного юзера, которую пользователь указал у себя в профиле.")
            )
            val tops: ContainerChildComponent = Section.of(
                Button.of(ButtonStyle.SECONDARY, "help:tops:$discordId", "➡️"),
                TextDisplay.of("## \\🏆 Топы"),
                TextDisplay.of("### Топы позволяют посмотреть лидирующих пользователей или гильдии по каким-либо параметрам.")
            )
            val economy: ContainerChildComponent = Section.of(
                Button.of(ButtonStyle.SECONDARY, "help:economy:$discordId", "➡️"),
                TextDisplay.of("## \\🏦 Экономика"),
                TextDisplay.of("### Экономика бота позволяет зарабатывать ирис-коины, обменивать их на ириски и зарабатывать звездность (репутацию)")
            )
            val statistic: ContainerChildComponent = Section.of(
                Button.of(ButtonStyle.SECONDARY, "help:statistic:$discordId", "➡️"),
                TextDisplay.of("## \\📊 Статистика"),
                TextDisplay.of("### Показывает статистику бота, гильдии, пользователя: Чарты участников, сообщений и другого")
            )
            val interactive: ContainerChildComponent = Section.of(
                Button.of(ButtonStyle.SECONDARY, "help:interactive:$discordId", "➡️"),
                TextDisplay.of("## \\🥰 Интерактивные, рп-команды"),
                TextDisplay.of("### Позволяют взаимодействовать с другими пользователями путем ответа на его сообщение определенном командой")
            )
            val others: ContainerChildComponent = Section.of(
                Button.of(ButtonStyle.SECONDARY, "help:other:$discordId", "➡️"),
                TextDisplay.of("## \\❔ Другое"),
                TextDisplay.of("### Все команды, что не поддаются описаниям категорий выше")
            )

            val components = buildList {
                add(header)
                add(mainInfo)
                add(buttons)
                add(separator)
                add(userModule)
                add(separator)
                add(tops)
                add(separator)
                add(economy)
                add(separator)
                add(statistic)
                add(separator)
                add(interactive)
                add(separator)
                add(others)
            }

            return Container.of(components)
        }

        @JvmStatic
        fun profileAndAnketa(discordId: Long): Container {

            val header: ContainerChildComponent = Section.of(
                Button.of(ButtonStyle.SECONDARY, "help:back:$discordId", "🔙"),
                TextDisplay.of("# \\👤 Анкеты & Профили")
            )
            val mainInfo: ContainerChildComponent = TextDisplay.of(
                "### Анкеты и профили описывают конкретного юзера, которую пользователь указал у себя в профиле.")
            val separator: ContainerChildComponent = Separator.createDivider(Separator.Spacing.SMALL)
            val anketaModule: ContainerChildComponent = TextDisplay.of("""
                ## Анкеты
                - `анкета ?<юзернейм / айди / никнейм>` — выводит глобальный профиль пользователя, который одинаковый во всех гильдиях
                - `девиз` — показывает ваш текущий девиз
                   - `-девиз` — удаляет ваш девиз
                   - `+девиз` & `девиз` — устанавливает девиз который указан со следующей строки после команды (shift + enter)
                - `приватность` — показывает ваши текущие настройки приватности
                """.trimIndent())
            val profileModule: ContainerChildComponent = TextDisplay.of("""
                ## Профили
                - `профиль ?<юзернейм / айди / никнейм>` — выводит локальный профиль пользователя, который уникален для каждой гильдии
                - `о себе` — показывает ваш текущее описание о себе
                   - `+о себе` & `о себе` — устанавливает описание которое указано со следующей строки после команды (shift + enter)
                   - `-о себе` — удаляет ваше описание
                - `гражданство` — показывает ваше текущее гражданство
                   - `+гражданство` — устанавливает гражданство в той гильдии, где прописана команда
                   - `-гражданство` — удаляет гражданство
                """.trimIndent())
            val footer: ContainerChildComponent = TextDisplay.of("-# **?** — знаком вопроса помечаются необязательные аргументы")
            val components = buildList {
                add(header)
                add(mainInfo)
                add(separator)
                add(anketaModule)
                add(separator)
                add(profileModule)
                add(footer)
            }

            return Container.of(components)
        }

        @JvmStatic
        fun tops(discordId: Long): Container {

            val header: ContainerChildComponent = Section.of(
                Button.of(ButtonStyle.SECONDARY, "help:back:$discordId", "🔙"),
                TextDisplay.of("# \\🏆 Топы")
            )
            val mainInfo: ContainerChildComponent = TextDisplay.of(
                "### Топы позволяют посмотреть лидирующих пользователей или гильдии по каким-либо параметрам.")
            val separator: ContainerChildComponent = Separator.createDivider(Separator.Spacing.SMALL)
            val globalTopModule: ContainerChildComponent = TextDisplay.of("""
                ## Глобальные топы
                - `гтоп <ириски / коины>` — показывает глобальный топ в зависимости от параметров
                   - `ириски` — показывает топ по ирискам (\🍬)
                   - `коины` — показывает топ по ирис-коинам (\☢️)
                """.trimIndent())
            val localTopModule: ContainerChildComponent = TextDisplay.of("""
                ## Локальные топы
                Когда нибудь, в скором времени, оно появится...
                """.trimIndent())
            val footer: ContainerChildComponent = TextDisplay.of("-# **?** — знаком вопроса помечаются необязательные аргументы")
            val components = buildList {
                add(header)
                add(mainInfo)
                add(separator)
                add(globalTopModule)
                add(separator)
                add(localTopModule)
                add(footer)
            }

            return Container.of(components)
        }

        @JvmStatic
        fun economy(discordId: Long): Container {

            val header: ContainerChildComponent = Section.of(
                Button.of(ButtonStyle.SECONDARY, "help:back:$discordId", "🔙"),
                TextDisplay.of("# \\🏦 Экономика")
            )
            val mainInfo: ContainerChildComponent = TextDisplay.of(
                "### Экономика бота позволяет зарабатывать ирис-коины, обменивать их на ириски и зарабатывать звездность (репутацию).")
            val separator: ContainerChildComponent = Separator.createDivider(Separator.Spacing.SMALL)
            val economyModule: ContainerChildComponent = TextDisplay.of("""
                - `мешок ?<юзернейм / айди / никнейм>` — покажет мешок: ириски (\🍬) , ирис-коины (\☢️) и звездность (\⭐)
                - `ферма` — позволяет зарабатывать ирис-коины (\☢️)
                """.trimIndent())
            val footer: ContainerChildComponent = TextDisplay.of("-# **?** — знаком вопроса помечаются необязательные аргументы")
            val components = buildList {
                add(header)
                add(mainInfo)
                add(separator)
                add(economyModule)
                add(separator)
                add(footer)
            }

            return Container.of(components)
        }

        @JvmStatic
        fun statistic(discordId: Long): Container {

            val header: ContainerChildComponent = Section.of(
                Button.of(ButtonStyle.SECONDARY, "help:back:$discordId", "🔙"),
                TextDisplay.of("# \\📊 Статистика")
            )
            val mainInfo: ContainerChildComponent = TextDisplay.of(
                "### Показывает статистику бота, гильдии, пользователя: Чарты участников, сообщений и другого.")
            val separator: ContainerChildComponent = Separator.createDivider(Separator.Spacing.SMALL)
            val statisticModule: ContainerChildComponent = TextDisplay.of("""
                - `статбот` — покажет статистику бота: Нагрузка на ЦП, использование ОЗУ, количество серверов и пользователей в кэше, а также количество шардов, версию бота и JDA (Java Discord API)
                - `пинг` — покажет Rest и Gateway пинг бота 
                """.trimIndent())
            val footer: ContainerChildComponent = TextDisplay.of("-# **?** — знаком вопроса помечаются необязательные аргументы")
            val components = buildList {
                add(header)
                add(mainInfo)
                add(separator)
                add(statisticModule)
                add(separator)
                add(footer)
            }

            return Container.of(components)
        }

        @JvmStatic
        fun interactive(discordId: Long): Container {

            val header: ContainerChildComponent = Section.of(
                Button.of(ButtonStyle.SECONDARY, "help:back:$discordId", "🔙"),
                TextDisplay.of("# \\🥰 Интерактивные, рп-команды")
            )
            val mainInfo: ContainerChildComponent = TextDisplay.of(
                "### Позволяют взаимодействовать с другими пользователями путем ответа на его сообщение определенном командой.")
            val separator: ContainerChildComponent = Separator.createDivider(Separator.Spacing.SMALL)
            val interactiveModule: ContainerChildComponent = TextDisplay.of("""
                - `рп-команда ?<действие>` — в ответ на сообщение пользователя. Также можно указать реплику со следующей строки
                - `обнять`, `погладить`, `убить`, `сжечь`, `ударить`, `уебать`, `выебать`, `трахнуть`, `осеменить`, `поцеловать`, `шлепнуть`, `отсосать`, `укусить`, `связать`, `офурить`, `дать пять`, `потискать`, `лизнуть`, `выстрелить`, `прижать`, `пнуть`, `взять за руку`, `отшлепать`
                """.trimIndent())
            val footer: ContainerChildComponent = TextDisplay.of("-# **?** — знаком вопроса помечаются необязательные аргументы")
            val components = buildList {
                add(header)
                add(mainInfo)
                add(separator)
                add(interactiveModule)
                add(separator)
                add(footer)
            }

            return Container.of(components)
        }

        @JvmStatic
        fun other(discordId: Long): Container {

            val header: ContainerChildComponent = Section.of(
                Button.of(ButtonStyle.SECONDARY, "help:back:$discordId", "🔙"),
                TextDisplay.of("# \\❔ Другое")
            )
            val mainInfo: ContainerChildComponent = TextDisplay.of(
                "### Все команды, что не поддаются описаниям других категорий.")
            val separator: ContainerChildComponent = Separator.createDivider(Separator.Spacing.SMALL)
            val otherModule: ContainerChildComponent = TextDisplay.of("""
                Пока что тут ничего нет, но тут обязательно когда-нибудь что-то появится!
                """.trimIndent())
            val footer: ContainerChildComponent = TextDisplay.of("-# **?** — знаком вопроса помечаются необязательные аргументы")
            val components = buildList {
                add(header)
                add(mainInfo)
                add(separator)
                add(otherModule)
                add(separator)
                add(footer)
            }

            return Container.of(components)
        }
    }
}
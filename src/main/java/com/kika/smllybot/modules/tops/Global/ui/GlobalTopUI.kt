package com.kika.smllybot.modules.tops.Global.ui

import com.kika.smllybot.modules.tops.Global.GlobalTopContext
import com.kika.smllybot.utils.NumUtil
import net.dv8tion.jda.api.components.actionrow.ActionRow
import net.dv8tion.jda.api.components.buttons.Button
import net.dv8tion.jda.api.components.container.Container
import net.dv8tion.jda.api.components.container.ContainerChildComponent
import net.dv8tion.jda.api.components.separator.Separator
import net.dv8tion.jda.api.components.textdisplay.TextDisplay
import kotlin.math.ceil

class GlobalTopUI {
    companion object {
        private const val ITEMS_PER_PAGE: Int = 10

        @JvmStatic
        fun build(ctx: GlobalTopContext, page: Int = 1): Container {
            val component: MutableList<ContainerChildComponent> = mutableListOf()
            var emoji = "?"
            var type = "?"

            if (ctx.bank.isEmpty()) {
                val emptyMessage: ContainerChildComponent = TextDisplay.of("""
                    # \💀 Как-то тут пусто однако...
                    ### Возможные причины:
                    1. В топе пока что никого нет
                    2. База данных недоступна
                    3. Возникла ошибка при попытке достать данные
                """.trimIndent())

                component.add(emptyMessage)
                return Container.of(component)
            }

            val pages = ceil(ctx.bank.size.toDouble() / ITEMS_PER_PAGE).toInt()

            val currentPage = page.coerceIn(1, pages)

            val skip = (currentPage - 1) * ITEMS_PER_PAGE
            val pageItems = ctx.bank.drop(skip).take(ITEMS_PER_PAGE)

            val buttonPrev: Button = Button.primary("gtop:${currentPage - 1}:${ctx.value}:${ctx.owner}", "⬅️ Назад")
                .withDisabled(currentPage <= 1)
            val buttonIndex: Button = Button.secondary("gtop:select:${ctx.value}:${ctx.owner}", "📖 Стр. $currentPage / $pages")
            val buttonNext: Button = Button.primary("gtop:${currentPage + 1}:${ctx.value}:${ctx.owner}", "Вперед ➡️")
                .withDisabled(currentPage >= pages)

            when (ctx.value) {
                "coin" -> {
                    emoji = "\\☢️"
                    type = "ирис-коинов"
                }
                "iris" -> {
                    emoji = "\\🍬"
                    type = "ирисок"
                }
            }

            val header: ContainerChildComponent = TextDisplay.of("# $emoji Глобальный топ $type")
            val separator: ContainerChildComponent = Separator.createDivider(Separator.Spacing.SMALL)

            val footer: ContainerChildComponent = TextDisplay.of("## Управление топом")
            val actionRows: ContainerChildComponent = ActionRow.of(
                buttonPrev,
                buttonIndex,
                buttonNext
            )

            component.add(header)
            pageItems.forEachIndexed { index, item ->
                val position = skip + index + 1
                val amount = NumUtil.german(item.amount)
                var text = "$position. **${item.name}** — $amount i¢"
                when (position) {
                    1 -> text = "1. \\🥇 **${item.name}** — $amount i¢"
                    2 -> text = "${index + 1}. \\🥈 **${item.name}** — $amount i¢"
                    3 -> text = "${index + 1}. \\🥉 **${item.name}** — $amount i¢"
                }
                val main: ContainerChildComponent = TextDisplay.of(text)

                component.add(main)
                if (index < pageItems.lastIndex) component.add(separator)
            }

            if (ctx.bank.size > ITEMS_PER_PAGE) {
                component.add(separator)
                component.add(footer)
                component.add(actionRows)
            }

            return Container.of(component)
        }
    }
}
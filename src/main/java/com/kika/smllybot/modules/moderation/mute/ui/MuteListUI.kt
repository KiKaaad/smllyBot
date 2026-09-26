package com.kika.smllybot.modules.moderation.mute.ui

import com.kika.smllybot.modules.moderation.mute.MuteListContext
import com.kika.smllybot.modules.tops.Global.GlobalTopContext
import com.kika.smllybot.utils.NumUtil
import com.kika.smllybot.utils.TimeUtil
import net.dv8tion.jda.api.components.actionrow.ActionRow
import net.dv8tion.jda.api.components.buttons.Button
import net.dv8tion.jda.api.components.container.Container
import net.dv8tion.jda.api.components.container.ContainerChildComponent
import net.dv8tion.jda.api.components.section.Section
import net.dv8tion.jda.api.components.separator.Separator
import net.dv8tion.jda.api.components.textdisplay.TextDisplay
import org.jetbrains.letsPlot.Geom
import kotlin.math.ceil

class MuteListUI {

    companion object {
        private const val ITEMS_PER_PAGE: Int = 6

        @JvmStatic
        fun build(ctx: MuteListContext, page: Int = 1): Container {
            val component: MutableList<ContainerChildComponent> = mutableListOf()
            val data = ctx.data

            if (data.isEmpty()) {
                val emptyMessage: ContainerChildComponent = TextDisplay.of("""
                    # \💀 Как-то тут пусто однако...
                    ### Возможные причины:
                    - База данных все - В С Ё
                    - Еще не было ни единого мута
                """.trimIndent())

                component.add(emptyMessage)
                return Container.of(component)
            }

            val pages = ceil(data.size.toDouble() / ITEMS_PER_PAGE).toInt()

            val currentPage = page.coerceIn(1, pages)

            val skip = (currentPage - 1) * ITEMS_PER_PAGE
            val pageItems = data.drop(skip).take(ITEMS_PER_PAGE)

            val buttonPrev: Button = Button.primary("mute_list:${currentPage - 1}:${ctx.owner}", "⬅️ Назад")
                .withDisabled(currentPage <= 1)
            val buttonIndex: Button = Button.secondary("mute_list:select:${ctx.owner}", "📖 Стр. $currentPage / $pages")
            val buttonNext: Button = Button.primary("mute_list:${currentPage + 1}:${ctx.owner}", "Вперед ➡️")
                .withDisabled(currentPage >= pages)

            val header: ContainerChildComponent = TextDisplay.of("# \\📋 История мутов")
            val separator: ContainerChildComponent = Separator.createDivider(Separator.Spacing.SMALL)

            val actionRows: ContainerChildComponent = ActionRow.of(
                buttonPrev,
                buttonIndex,
                buttonNext
            )

            component.add(header)
            pageItems.forEachIndexed { index, item ->
                val position = skip + index + 1
                val reason = item.reason ?: "Без причины"
                val time = TimeUtil.getTimestamp(item.createdAt)
                val timeRelative = if (item.active) TimeUtil.getTimestampRelative(item.until) else TimeUtil.getTimestampRelative(item.removedAt)
                val active = if (item.active) "Окончится" else "Окончился"

                val text = """
                    ### $position. <@${item.discordId}> (IDD: `${item.discordId}`)
                    **Дата:** $time | **$active** $timeRelative
                    **Причина:** $reason
                    **Модератор:** <@${item.byDiscordId}> `${item.byDiscordId}`
                """.trimIndent()
                val main: ContainerChildComponent = Section.of(
                    Button.primary("id:$position", "ℹ️ Подробнее"),
                    TextDisplay.of(text)
                )


                component.add(main)
                if (index < pageItems.lastIndex) component.add(separator)
            }

            if (data.size > ITEMS_PER_PAGE) {
                component.add(separator)
                component.add(actionRows)
            }

            return Container.of(component)
        }
    }

}
package com.kika.smllybot.modules.statistic.ui

import com.kika.smllybot.Main
import com.kika.smllybot.database.sql.users.UsersTable
import com.kika.smllybot.modules.statistic.StatisticContext
import com.kika.smllybot.utils.NumUtil
import com.sun.management.OperatingSystemMXBean
import net.dv8tion.jda.api.components.container.Container
import net.dv8tion.jda.api.components.container.ContainerChildComponent
import net.dv8tion.jda.api.components.section.Section
import net.dv8tion.jda.api.components.separator.Separator
import net.dv8tion.jda.api.components.textdisplay.TextDisplay
import net.dv8tion.jda.api.components.thumbnail.Thumbnail
import java.lang.management.ManagementFactory

class StatisticBotUI {

    companion object {
        @JvmStatic
        fun buildStatistic(ctx: StatisticContext): Container {
            val components: MutableList<ContainerChildComponent?> = ArrayList(12)

            val dbUserCount = UsersTable.getTotalUsers()

            val osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean::class.java)

            // Красивое форматирование: 123456 -> 123.456
            // ОЗУ
            val ram = NumUtil.german(osBean.totalMemorySize / 1048576)
            val freeRam = NumUtil.german((osBean.totalMemorySize - osBean.freeMemorySize) / 1048576)

            // Загрузка ЦП: 12.34%
            val systemLoad = osBean.cpuLoad * 100

            // ЦП
            val systemLoadFriendly = NumUtil.german(systemLoad)

            val diffRam = NumUtil.german(freeRam.toDouble() / ram.toDouble() * 100)

            val systemLoadEmoji: String = when (systemLoad.toInt()) {
                in 0..25 -> "🟢"
                in 25..50 -> "🟡"
                in 51..75 -> "🟠"
                in 95..99 -> "🔴"
                else -> "💀 ВСЁ"
            }

            // Шапка
            val header = Section.of(
                Thumbnail.fromUrl(ctx.botAvatarUrl),
                TextDisplay.of("# \\📊 Статистика бота"),
                TextDisplay.of("\\$systemLoadEmoji Использование ЦП **$systemLoadFriendly%**"),
                TextDisplay.of("Использование ОЗУ **$freeRam / $ram МБ** (занято $diffRam%)"))

            val headerOther = TextDisplay.of("## \\🌃 Прочая статистика")
            val servers = TextDisplay.of("Серверов: **${ctx.serverCount}**")
            val users = TextDisplay.of("Пользователей (в кэше | бд): **${ctx.userCount}** | **$dbUserCount**")
            val shards = TextDisplay.of("Количество шардов: **${ctx.shardTotal}**")
            val version = TextDisplay.of("Версия JDA **${ctx.jdaVersion}** | Версия бота **${Main.VERSION}**")
            val copyright = TextDisplay.of("-# \\©️ 2026 [KiKa](https://t.me/KiKaaad) | " + Main.OWNER)

            components.add(header)
            components.add(Separator.createDivider(Separator.Spacing.SMALL))

            components.add(headerOther)

            components.add(servers)
            components.add(users)
            components.add(shards)
            components.add(version)

            components.add(copyright)

            return Container.of(components)
        }
    }

}
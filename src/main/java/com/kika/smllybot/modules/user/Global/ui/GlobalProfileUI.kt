package com.kika.smllybot.modules.user.Global.ui

import com.kika.smllybot.database.sql.statistic.StatisticTable
import com.kika.smllybot.database.sql.statistic.dto.StatisticAccount
import com.kika.smllybot.modules.user.Global.GlobalProfileContext
import com.kika.smllybot.utils.NumUtil
import com.kika.smllybot.utils.TimeUtil
import net.dv8tion.jda.api.components.actionrow.ActionRow
import net.dv8tion.jda.api.components.buttons.Button
import net.dv8tion.jda.api.components.container.Container
import net.dv8tion.jda.api.components.container.ContainerChildComponent
import net.dv8tion.jda.api.components.section.Section
import net.dv8tion.jda.api.components.separator.Separator
import net.dv8tion.jda.api.components.textdisplay.TextDisplay
import net.dv8tion.jda.api.components.thumbnail.Thumbnail

class GlobalProfileUI {

    companion object {
        @JvmStatic
        fun buildProfile(ctx: GlobalProfileContext): Container {
            val component: MutableList<ContainerChildComponent> = mutableListOf()

            val isOwner: Boolean = ctx.target.idLong == ctx.viewer.idLong

            val statistic: StatisticAccount = StatisticTable.getTotalUserStatistic(ctx.user().id)

            val status: String = UserStatus.getEmoji(ctx.member())

            val discordCreated: String = TimeUtil.getTimestamp(ctx.target().timeCreated)
            val discordCreatedRelative: String = TimeUtil.getTimestampRelative(ctx.target().timeCreated)
            val botCreated: String = TimeUtil.getTimestamp(ctx.user().createdAt)
            val botCreatedRelative: String = TimeUtil.getTimestampRelative(ctx.user().createdAt)
            val citizenshipData: String

            val iris: String = NumUtil.german(ctx.bank.iris)
            val stars: String = NumUtil.german(ctx.bank.star)
            val irisCoin: String = NumUtil.german(ctx.bank.irisCoin)

            var motto: String? = ctx.user.motto
            if (motto.isNullOrBlank()) motto = "Пользователь не указал описание."

            val main: Section = Section.of(
                Thumbnail.fromUrl(ctx.target().getEffectiveAvatarUrl()),
                TextDisplay.of("# Это ${ctx.target().effectiveName} $status"),
                TextDisplay.of("### Девиз:"),
                TextDisplay.of(motto)
            )

            val separator: ContainerChildComponent = Separator.createDivider(Separator.Spacing.SMALL)

            val headerMid: ContainerChildComponent = TextDisplay.of("## \\👀 Краткая информация:")
            val activity: ContainerChildComponent = TextDisplay.of("\\📊 Активность (день | нед | мес | всего): ${statistic.day} | ${statistic.week} | ${statistic.month} | ${statistic.total}")
            val dataDiscord: ContainerChildComponent = TextDisplay.of("\\🕐 Во вселенной дискорд с $discordCreated ($discordCreatedRelative)")
            val dataBot: ContainerChildComponent = TextDisplay.of("\\⌛ Во вселенной бота с $botCreated ($botCreatedRelative)")
            val idb: ContainerChildComponent = TextDisplay.of("IDB `${ctx.user().id}`")
            val idd: ContainerChildComponent = TextDisplay.of("IDD `${ctx.target().idLong}`")
            val headerEconomy: ContainerChildComponent = TextDisplay.of("### \\💰 Мешок")
            val irisAndStars: ContainerChildComponent = TextDisplay.of("\\🍬 **$iris** | \\⭐ **$stars**")
            val irisCoins: ContainerChildComponent = TextDisplay.of("\\☢️ **$irisCoin** i¢")
            val anketaSettings: ContainerChildComponent = TextDisplay.of("### Настройки профиля")
            val action: ContainerChildComponent = ActionRow.of(
                Button.primary("anketa:modal:" + ctx.target.id, "ℹ️ Редактировать профиль"),
                Button.secondary("private:private:" + ctx.target.id, "🕶️ Приватность")
            )
            val footer: ContainerChildComponent = TextDisplay.of("-# **IDB** - айди внутри бота. **IDD** - айди внутри дискорд.")

            val privacy: ContainerChildComponent = TextDisplay.of("-# Скрыто (видно только вам)")

            component.add(main)
            component.add(separator)
            component.add(headerMid)
            if (!ctx.privacy.activity) component.add(activity)
            else if (isOwner) {
                component.add(activity)
                component.add(privacy)
            }
            component.add(dataDiscord)
            component.add(dataBot)
            component.add(idb)
            component.add(idd)
            if (!ctx.privacy.bag) {
                component.add(separator)
                component.add(headerEconomy)
                component.add(irisAndStars)
                component.add(irisCoins)
            } else if (isOwner) {
                component.add(separator)
                component.add(headerEconomy)
                component.add(privacy)
                component.add(irisAndStars)
                component.add(irisCoins)
            } else component.add(privacy)
            if (isOwner) {
                component.add(anketaSettings)
                component.add(separator)
                component.add(action)
            }
            component.add(footer)

            return Container.of(component)
        }
    }
}
package com.kika.smllybot.modules.user.Local.ui

import com.kika.smllybot.modules.user.Local.ProfileContext
import com.kika.smllybot.utils.NumUtil
import com.kika.smllybot.utils.TimeUtil
import net.dv8tion.jda.api.components.actionrow.ActionRow
import net.dv8tion.jda.api.components.buttons.Button
import net.dv8tion.jda.api.components.buttons.ButtonStyle
import net.dv8tion.jda.api.components.container.Container
import net.dv8tion.jda.api.components.container.ContainerChildComponent
import net.dv8tion.jda.api.components.section.Section
import net.dv8tion.jda.api.components.separator.Separator
import net.dv8tion.jda.api.components.textdisplay.TextDisplay
import net.dv8tion.jda.api.components.thumbnail.Thumbnail

class ProfileUI {

    companion object {
        @JvmStatic
        fun buildProfile(ctx: ProfileContext): Container {
            val components: MutableList<ContainerChildComponent?> = ArrayList()

            val avatarUrl = ctx.target.getEffectiveAvatarUrl()
            val name = ctx.target.getEffectiveName()
            val timestamp = TimeUtil.getTimestamp(ctx.profile.createdAt)
            val timestampRelative = TimeUtil.getTimestampRelative(ctx.profile.createdAt)
            val reaction = NumUtil.german(ctx.user.reaction)
            val star = NumUtil.german(ctx.bank.star)
            val day = NumUtil.german(ctx.statistic.day)
            val week = NumUtil.german(ctx.statistic.week)
            val month = NumUtil.german(ctx.statistic.month)
            val total = NumUtil.german(ctx.statistic.total)
            val aboutMe = ctx.profile.aboutMe?.replace("@", "\\@") ?: "Пользователь не указал описание"

            val header: ContainerChildComponent = Section.of(
                Thumbnail.fromUrl(avatarUrl),
                TextDisplay.of("# \\👤 Это ${name.replace("@", "\\@")}"),
                TextDisplay.of("### О себе:"),
                TextDisplay.of(aboutMe)
            )
            val separator: ContainerChildComponent = Separator.createDivider(Separator.Spacing.SMALL)
            val main: ContainerChildComponent = TextDisplay.of("## Основная информация:")
            val owner: ContainerChildComponent = TextDisplay.of("\\👑 Создатель дискорд-сервера")
            val citizen: ContainerChildComponent = TextDisplay.of("\\🪪 Гражданин этого сервера")
            val reputation: ContainerChildComponent = TextDisplay.of(
                "Репутация: \\✨ $star | \\➕ $reaction"
            )
            val firstEntry: ContainerChildComponent = TextDisplay.of(
                "Впервые появился здесь $timestamp ($timestampRelative)"
            )
            val activity: ContainerChildComponent = TextDisplay.of(
                "**Актив** (д | н | м | весь): $day | $week | $month | $total"
            )
            val rewardsHeader: ContainerChildComponent = TextDisplay.of("## \\🏆 Награды")
            val rewards: ContainerChildComponent = TextDisplay.of("...")
            val buttonsHeader = TextDisplay.of("### Настройки профиля")
            val button = ActionRow.of(Button.of(ButtonStyle.PRIMARY, "profile:modal:${ctx.target.idLong}", "ℹ️ Редактировать о себе"))
            val footer: ContainerChildComponent = TextDisplay
                .of("-# Это - **локальный** профиль. Часть информации выводится исключительно из этой гильдии")

            components.add(header)
            components.add(separator)
            components.add(main)
            if (ctx.member.isOwner) components.add(owner)
            if (ctx.user.citizenship == ctx.guildId) components.add(citizen)
            components.add(reputation)
            components.add(firstEntry)
            components.add(activity)
//            components.add(rewardsHeader)
//            components.add(rewards)
            if (ctx.target.idLong == ctx.viewer.idLong) {
                components.add(separator)
                components.add(buttonsHeader)
                components.add(button)
            }
            components.add(separator)
            components.add(footer)

            return Container.of(components)
        }
    }
}
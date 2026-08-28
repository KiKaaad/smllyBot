package com.kika.smllybot.modules.guild.ui

import com.kika.smllybot.modules.guild.GuildInfoContext
import com.kika.smllybot.utils.TimeUtil
import net.dv8tion.jda.api.components.container.Container
import net.dv8tion.jda.api.components.container.ContainerChildComponent
import net.dv8tion.jda.api.components.mediagallery.MediaGallery
import net.dv8tion.jda.api.components.mediagallery.MediaGalleryItem
import net.dv8tion.jda.api.components.section.Section
import net.dv8tion.jda.api.components.separator.Separator
import net.dv8tion.jda.api.components.textdisplay.TextDisplay
import net.dv8tion.jda.api.components.thumbnail.Thumbnail

class GuildInfoUI {

    companion object {
        @JvmStatic
        fun build(ctx: GuildInfoContext): Container {
            val components: MutableList<ContainerChildComponent?> = ArrayList(12)

            val guildId = ctx.event.guild.idLong
            val guildTitle = ctx.event.guild.name
            val description: String = ctx.event.guild.description ?: "Описание гильдии не установлено"
            val rolesCount = ctx.event.guild.roles.size
            val ownerName = ctx.event.guild.owner!!.user.name
            val ownerId = ctx.event.guild.owner!!.idLong
            val voiceChannels = ctx.event.guild.voiceChannels.size
            val channelsSize = ctx.event.guild.channels.size
            val boostCount = ctx.event.guild.boostCount
            val userCount = ctx.event.guild.memberCount
            val botCount = ctx.event.guild.members.count { it.user.isBot }
            val data = TimeUtil.getTimestamp(ctx.event.guild.timeCreated)
            val dataRelative = TimeUtil.getTimestampRelative(ctx.event.guild.timeCreated)

            val levelBoost = ctx.event.guild.boostCount
            val emojiBoost: String = when (levelBoost) {
                0 -> "👾"
                in 2..6 -> "🍬"
                in 7..13 -> "🍭"
                else -> "🎂"
            }

            ctx.event.guild.banner?.let { guildBanner ->
                val bannerUrl = guildBanner.getUrl(2048)
                val banner = MediaGallery.of(MediaGalleryItem.fromUrl(bannerUrl))
                components.addFirst(banner)
            }

            var icon = "https://i.pinimg.com/736x/a9/bd/25/a9bd25bc0cb5a63fa184e4a8bab2b2ea.jpg"
            if (ctx.event.guild.icon != null) icon = ctx.event.guild.getIcon()!!.getUrl(1024)

            val header = Section.of(
                Thumbnail.fromUrl(icon),
                TextDisplay.of("# $guildTitle \\$emojiBoost"),
                TextDisplay.of(
                    """
                            ### Описание:
                            $description
                            """.trimIndent()
                )
            )
            val separator: ContainerChildComponent = Separator.createDivider(Separator.Spacing.SMALL)
            val dates = TextDisplay.of("Создана $data ($dataRelative)")
            val mainUsers = TextDisplay.of("## Участники:")
            val owner = TextDisplay.of("\\🕶️ **Владелец:** $ownerName | **ID:** `$ownerId`")
            val users = TextDisplay.of("\\👤 **Людей (бустов):** $userCount ($boostCount) | \\🤖 **Ботов:** $botCount")
            val guildInfo = TextDisplay.of("## Информация о гильдии:")
            val channels = TextDisplay.of("\\💬 **Каналов:** $channelsSize | \\🎙️ **Войсов:** $voiceChannels")
            val roles = TextDisplay.of("\\👔 **Ролей:** $rolesCount | **IDG:** `$guildId`")
            val footer = TextDisplay.of("-# **IDG** - айди гильдии (ID Guild)")

            components.add(header)
            components.add(separator)
            components.add(dates)
            components.add(mainUsers)
            components.add(owner)
            components.add(users)
            components.add(separator)
            components.add(guildInfo)
            components.add(channels)
            components.add(roles)
            components.add(separator)
            components.add(footer)

            return Container.of(components)
        }
    }
}
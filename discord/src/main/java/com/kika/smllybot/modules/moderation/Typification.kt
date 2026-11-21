package com.kika.smllybot.modules.moderation

import com.kika.smllybot.handler.ErrorThrow
import com.kika.smllybot.utils.SendComponents
import net.dv8tion.jda.api.entities.UserSnowflake
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

class Typification {

    fun find(event: MessageReceivedEvent, message: String): TargetUser {
        var duration: String? = null
        var unit: String? = null
        var reason: String? = null

        /*
        * parts[0] - <@юзер> / 123456789 / юзер
        * parts[1] - время: 123
        * parts[2] - тип: часов
        * lines[1] - причина
        *
        * Может смещаться, если команда использована в ответ на сообщение
        */
        val lines: List<String> = message.trim().split("\\n".toRegex(), 2)
        val parts: List<String> = lines[0].trim().split("\\h+".toRegex(), 4)

        if (parts.size == 2) {
            duration = parts[0]
            unit = parts[1]
        }
        if (lines.size == 2 && !lines[1].isBlank()) reason = lines[1].trim()

        try {
            duration?.toLong()
        } catch (e: NumberFormatException) {
            SendComponents.sendTo15(event, ErrorThrow.notNumber(e.message))
            return TargetUser(UserSnowflake.fromId(0), null, null, null)
        }

        if (event.message.referencedMessage != null) {
            return TargetUser(
                event.message.referencedMessage!!.author,
                duration?.toLong(),
                unit,
                reason
            )
        }

        if (parts.size == 3) {
            duration = parts[1]
            unit = parts[2]
        }

        try {
            duration?.toLong()
        } catch (e: NumberFormatException) {
            SendComponents.sendTo15(event, ErrorThrow.notNumber(e.message))
            return TargetUser(UserSnowflake.fromId(0), null, null, null)
        }

        val userIdAndNickname = parts[0]
        if (userIdAndNickname.matches(Regex("\\d+"))) {
            val user = UserSnowflake.fromId(userIdAndNickname)

            return TargetUser(
                user,
                duration?.toLong(),
                unit,
                reason
            )
        }

        if (!event.guild.getMembersByNickname(userIdAndNickname, true).isEmpty()) {
            val user = event.guild.getMembersByNickname(userIdAndNickname, true).first()

            return TargetUser(
                user,
                duration?.toLong(),
                unit,
                reason
            )
        }

        if (!event.message.mentions.users.isEmpty()) {
            val user = event.message.mentions.users.last()

            return TargetUser(
                user,
                duration?.toLong(),
                unit,
                reason
            )
        }

        return TargetUser(UserSnowflake.fromId(0), null, null, null)
    }
}
package com.kika.smllybot.modules.moderation

import com.kika.smllybot.handler.ErrorThrow
import net.dv8tion.jda.api.entities.UserSnowflake
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

class Typification {

    fun find(event: MessageReceivedEvent, message: String): TargetUser {
        var duration: String? = null
        var unit: String? = null
        var reason: String? = null

        /*
        * parts[0] - мут
        * parts[1] - <@юзер> / 123456789 / юзер
        * parts[2] - время: 123
        * parts[3] - тип: часов
        * lines[1] - причина
        *
        * Может смещаться, если команда использована в ответ на сообщение
        */
        val lines: List<String> = message.trim().split("\\n".toRegex(), 2)
        val parts: List<String> = lines[0].trim().split("\\h+".toRegex(), 4)

        if (parts.size == 3) {
            duration = parts[1]
            unit = parts[2]
        }
        if (lines.size == 2 && !lines[1].isBlank()) reason = lines[1].trim()

        try {
            duration?.toLong()
        } catch (e: NumberFormatException) {
            ErrorThrow.notNumber(event, e.message)
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

        if (parts.size == 4) {
            duration = parts[2]
            unit = parts[3]
        }

        try {
            duration?.toLong()
        } catch (e: NumberFormatException) {
            ErrorThrow.notNumber(event, e.message)
            return TargetUser(UserSnowflake.fromId(0), null, null, null)
        }

        if (!event.message.mentions.users.isEmpty()) {
            val user = event.message.mentions.users.first()

            return TargetUser(
                user,
                duration?.toLong(),
                unit,
                reason
            )
        }

        val userIdAndNickname = parts[1]
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

        return TargetUser(UserSnowflake.fromId(0), null, null, null)
    }
}
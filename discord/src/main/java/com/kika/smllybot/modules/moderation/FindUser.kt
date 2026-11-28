package com.kika.smllybot.modules.moderation

import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.UserSnowflake

class FindUser {

    fun find(message: Message, arg: String): UserSnowflake {
        if (message.referencedMessage != null) {
            return message.referencedMessage!!.author
        }

        if (message.mentions.users.isNotEmpty()) {
            return message.mentions.users.first()
        }

        if (arg.matches("\\d+".toRegex())) {
            return UserSnowflake.fromId(arg.toLong())
        }

        return UserSnowflake.fromId(0)
    }
}
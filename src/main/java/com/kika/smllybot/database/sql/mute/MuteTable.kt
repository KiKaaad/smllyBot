package com.kika.smllybot.database.sql.mute

import com.kika.smllybot.database.sql.mute.dto.MuteCreateData
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.javatime.timestampWithTimeZone
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import java.time.OffsetDateTime

object MuteTable : Table("mute") {
    val id = long("id").autoIncrement()
    val muteType = varchar("mute_type", 16)
    val guildId = long("guild_id")
    val discordId = long("discord_id")
    val byDiscordId = long("by_discord_id")
    val reason = varchar("reason", 512).nullable()
    val createdAt = timestampWithTimeZone("created_at").default(OffsetDateTime.now())
    val until = timestampWithTimeZone("until").nullable()
    val removedAt = timestampWithTimeZone("removed_at").nullable()
    val removedBy = long("removed_by").nullable()
    val active = bool("active").default(true)

    override val primaryKey = PrimaryKey(id)
}

fun createTable() {
    transaction {
        SchemaUtils.create(MuteTable)
    }
}

fun createMute(data: MuteCreateData) {
    transaction {
        MuteTable.insert { row ->
            row[MuteTable.muteType] = data.muteType
            row[MuteTable.guildId] = data.guildId
            row[MuteTable.discordId] = data.discordId
            row[MuteTable.byDiscordId] = data.byDiscordId
            row[MuteTable.reason] = data.reason
            row[MuteTable.until] = data.until
        }
    }
}
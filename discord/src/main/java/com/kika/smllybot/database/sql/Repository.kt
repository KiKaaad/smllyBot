package com.kika.smllybot.database.sql

import com.kika.smllybot.database.sql.user.bank.BankTable
import com.kika.smllybot.database.sql.user.bank.dto.BankAccount
import com.kika.smllybot.database.sql.guild.guild.GuildTable
import com.kika.smllybot.database.sql.guild.guild.dto.GuildData
import com.kika.smllybot.database.sql.user.privacy.PrivacyTable
import com.kika.smllybot.database.sql.user.privacy.dto.PrivacyAccount
import com.kika.smllybot.database.sql.user.profile.ProfileTable
import com.kika.smllybot.database.sql.user.profile.dto.ProfileAccount
import com.kika.smllybot.database.sql.user.statistic.StatisticTable
import com.kika.smllybot.database.sql.user.statistic.dto.StatisticAccount
import com.kika.smllybot.database.sql.user.users.UsersTable
import com.kika.smllybot.database.sql.user.users.dto.UserAccount
import java.time.OffsetDateTime

/*
 * Суперски удобный класс, который сам создает юзера прежде, чем достать данные
 * Всегда используется именно discordId, а не внутренний айди бота, от чего невозможного
 * запутаться между discordId и internalId.
 *
 * Выглядит примерно как:
 * repo.getUser(discordId, name).motto
 * UwU
 */

class Repository {

    fun getUser(discordId: Long, name: String): UserAccount {
        val user: UserAccount = UsersTable.getOrCreateUser(discordId, name)

        return user
    }

    fun getBank(discordId: Long, name: String): BankAccount {
        val user: UserAccount = UsersTable.getOrCreateUser(discordId, name)
        val bank: BankAccount = BankTable.getOrCreateBank(user.id, name)

        return bank
    }

    fun getProfile(discordId: Long, guildId: Long, name: String, dataTime: OffsetDateTime): ProfileAccount {
        val user: UserAccount = UsersTable.getOrCreateUser(discordId, name)
        val profile: ProfileAccount = ProfileTable.getOrCreateProfile(user.id, guildId, name, dataTime)

        return profile
    }

    fun getPrivacy(discordId: Long, name: String): PrivacyAccount {
        val user: UserAccount = UsersTable.getOrCreateUser(discordId, name)
        val privacy: PrivacyAccount = PrivacyTable().getOrCreatePrivacy(user.id)

        return privacy
    }

    fun getStatistic(discordId: Long, name: String): StatisticAccount {
        val user: UserAccount = UsersTable.getOrCreateUser(discordId, name)
        val statistic: StatisticAccount = StatisticTable.getTotalUserStatistic(user.id)

        return statistic
    }

    fun getStatistic(discordId: Long, name: String, guildId: Long): StatisticAccount {
        val user: UserAccount = UsersTable.getOrCreateUser(discordId, name)
        val statistic: StatisticAccount = StatisticTable.getTotalStatisticUserGuild(user.id, guildId)

        return statistic
    }

    fun getGuild(guildId: Long, guildName: String): GuildData {
        val guild: GuildData = GuildTable.getOrCreateGuild(guildId, guildName)

        return guild
    }

}
package com.kika.smllybot.database.sql

import com.kika.smllybot.database.sql.bank.BankTable
import com.kika.smllybot.database.sql.bank.dto.BankAccount
import com.kika.smllybot.database.sql.privacy.PrivacyTable
import com.kika.smllybot.database.sql.privacy.dto.PrivacyAccount
import com.kika.smllybot.database.sql.profile.ProfileTable
import com.kika.smllybot.database.sql.profile.dto.ProfileAccount
import com.kika.smllybot.database.sql.statistic.StatisticTable
import com.kika.smllybot.database.sql.statistic.dto.StatisticAccount
import com.kika.smllybot.database.sql.users.UsersTable
import com.kika.smllybot.database.sql.users.dto.UserAccount
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
        val privacy: PrivacyAccount = PrivacyTable.getOrCreatePrivacy(user.id)

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

}
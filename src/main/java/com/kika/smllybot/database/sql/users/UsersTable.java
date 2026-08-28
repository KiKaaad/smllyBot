package com.kika.smllybot.database.sql.users;

import com.kika.smllybot.database.sql.DatabaseManager;
import com.kika.smllybot.database.sql.users.dto.UserAccount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;

public class UsersTable {

    private static final Logger log = LoggerFactory.getLogger(UsersTable.class);

    private static final RowMapper<UserAccount> USER_MAPPER = (rs, rowNum) -> new UserAccount(
            rs.getLong("id"),
            rs.getLong("discord_id"),
            rs.getString("role"),
            rs.getString("name"),
            rs.getString("motto"),
            rs.getObject("created_at", OffsetDateTime.class),
            rs.getInt("reaction"),
            rs.getObject("citizenship", Long.class),
            rs.getObject("citizenship_data", OffsetDateTime.class)
        );

    public static void createTable() {
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));

        String sql = """
            CREATE TABLE IF NOT EXISTS users (
                id          BIGSERIAL PRIMARY KEY,
                role        VARCHAR,
                name        VARCHAR,
                discord_id  BIGINT UNIQUE NOT NULL,
                created_at  TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
                motto       VARCHAR(255) DEFAULT 'Пользователь не указал описание.',
                reaction    INT DEFAULT 0,
                citizenship BIGINT,
                citizenship_data TIMESTAMPTZ
            );
            """;

        try {
            DatabaseManager.getQuery().execute(sql);
            log.info("✅ Таблица USERS проверена / создана");
        } catch (Exception e) {
            log.error("❌ Ошибка создания таблицы USERS: ", e);
        }
    }

    public static UserAccount getOrCreateUser(long discordId, String name) {
        String selectSql = """
        SELECT id, discord_id, role, name, motto, created_at, reaction, citizenship, citizenship_data
        FROM users WHERE discord_id = ?;
        """;

        String insertSql = """
        INSERT INTO users (discord_id, name) VALUES (?, ?)
        ON CONFLICT (discord_id) DO NOTHING
        RETURNING id, discord_id, role, name, motto, created_at, reaction, citizenship, citizenship_data;
        """;

        try {
            return DatabaseManager.getQuery().queryForObject(selectSql, USER_MAPPER, discordId);
        } catch (EmptyResultDataAccessException e) {
            try {
                return DatabaseManager.getQuery().queryForObject(insertSql, USER_MAPPER, discordId, name.replace("@", "\\@"));
            } catch (Exception ex) {
                log.error("❌ Ошибка при попытке создать юзера ", ex);
                return null;
            }
        } catch (Exception e) {
            log.error("Возникла ошибка при попытке достать юзера: ", e);
        }
        return null;
    }

    public static void setMotto(long discordId, String newText) {
        String sql = "UPDATE users SET motto = ? WHERE discord_id = ?";

        try {
            DatabaseManager.getQuery().update(sql, newText, discordId);
            log.info("✅ Данные пользователя {} обновлены.", discordId);
        } catch (Exception e) {
            log.error("❌ Возникла ошибка при обновлении девиза (discordId {}): ", discordId, e);
        }
    }

    public static void setUsername(long discordId, String currentName) {
        String sql = "UPDATE users SET name = ? WHERE discord_id = ?";

        try {
            DatabaseManager.getQuery().update(sql, currentName.replace("@", "\\@"), discordId);
            log.info("✅ Имя пользователя обновлено");
        } catch (Exception e) {
            log.info("❌ Ошибка записи имени пользователя");
        }

    }

    public static Long getTotalUsers() {
        String sql = "SELECT COUNT(id) FROM users";

        try {
            Long total = DatabaseManager.getQuery().queryForObject(sql, Long.class);
            return total != null ? total : 0;
        } catch (Exception e) {
            log.error("❌ Ошибка при попытке достать количество юзеров из таблицы USERS: ");
        }
        return null;
    }

    public static long getUserId(long discordId) {
        String sql = "SELECT id FROM users WHERE discord_id = ?";

        try {
            Long id = DatabaseManager.getQuery().queryForObject(sql, Long.class, discordId);
            return id != null ? id : 0;
        } catch (Exception e) {
            log.error("❌ Ошибка при попытке достать айди пользователя: ", e);
        }
        return 0;
    }

    public static void plusReputation(long discordId) {
        String sql = """
                UPDATE users
                SET reaction = reaction + 1
                WHERE discord_id = ?;
                """;
        try {
            DatabaseManager.getQuery().update(sql, discordId);
        } catch (Exception e) {
            log.error("❌ Ошибка при попытке обновить реакции пользователя: ", e);
        }
    }

    public static void minusReputation(long discordId) {
        String sql = """
                UPDATE users
                SET reaction = reaction - 1
                WHERE discord_id = ?;
                """;
        try {
            DatabaseManager.getQuery().update(sql, discordId);
        } catch (Exception e) {
            log.error("❌ Ошибка при попытке обновить реакции пользователя: ", e);
        }
    }

    public static void setCitizenship(long discordId, Long guildId) {
        String sql = "UPDATE users SET citizenship = ?, citizenship_data = NOW() WHERE discord_id = ?";

        try {
            DatabaseManager.getQuery().update(sql, guildId, discordId);
        } catch (Exception e) {
            log.error("Ошибка при установке гражданства (DISCORD_ID: {} | GUILD_ID: {}) ", discordId, guildId, e);
        }

    }
}
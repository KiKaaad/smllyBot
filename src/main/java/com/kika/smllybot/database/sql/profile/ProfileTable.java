package com.kika.smllybot.database.sql.profile;

import com.kika.smllybot.database.sql.DatabaseManager;
import com.kika.smllybot.database.sql.profile.dto.ProfileAccount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;

public class ProfileTable {

    private static final Logger log = LoggerFactory.getLogger(ProfileTable.class);

    private static final RowMapper<ProfileAccount> PROFILE_MAPPER = (rs, rowNum) -> new ProfileAccount(
            rs.getLong("id"),
            rs.getLong("guild_id"),
            rs.getString("name"),
            rs.getString("about_me"),
            rs.getObject("created_at", OffsetDateTime.class)
    );

    public static void createTable() {
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));

        String sql = """
            CREATE TABLE IF NOT EXISTS profile (
                id          BIGINT REFERENCES users(id) ON DELETE CASCADE,
                guild_id    BIGINT,
                name        VARCHAR,
                created_at  TIMESTAMPTZ,
                about_me    VARCHAR(255) DEFAULT 'Пользователь ничего не рассказал о себе.',
                PRIMARY KEY (id, guild_id)
            );
            """;

        try {
            DatabaseManager.getQuery().execute(sql);
            log.info("✅ Таблица PROFILES проверена / создана");
        } catch (Exception e) {
            log.error("❌ Ошибка создания таблицы PROFILES: ", e);
        }
    }

    public static ProfileAccount getOrCreateProfile(long id, long guildId, String name, OffsetDateTime dateTime) {
        String selectSql = """
                SELECT id, guild_id, name, created_at, about_me
                FROM profile WHERE id = ? AND guild_id = ?;
                """;

        String insertSql = """
                INSERT INTO profile (id, guild_id, name, created_at) VALUES (?, ?, ?, ?)
                ON CONFLICT (id, guild_id) DO UPDATE SET name = EXCLUDED.name
                RETURNING id, guild_id, name, created_at, about_me;
                """;

        try {
            return DatabaseManager.getQuery().queryForObject(selectSql, PROFILE_MAPPER, id, guildId);
        } catch (EmptyResultDataAccessException e) {
            try {
                return DatabaseManager.getQuery().queryForObject(insertSql, PROFILE_MAPPER, id, guildId, name.replace("@", "\\@"), dateTime);
            } catch (Exception ex) {
                log.error("❌ Ошибка при создании профиля (id {}, guild {}): ", id, guildId, ex);
                return null;
            }
        } catch (Exception e) {
            log.error("❌ Возникла ошибка при попытке взять профиль (id {}, guild {}): ", id, guildId, e);
            return null;
        }
    }

    public static void setAboutMe(long id, long guildId, String aboutMe) {
        String sql = "UPDATE profile SET about_me = ? WHERE id = ? AND guild_id = ?";

        try {
            DatabaseManager.getQuery().update(sql, aboutMe, id, guildId);
        } catch (Exception e) {
            log.error("Возникла ошибка при попытке обновить информацию пользователя о себе: ", e);
        }
    }
}
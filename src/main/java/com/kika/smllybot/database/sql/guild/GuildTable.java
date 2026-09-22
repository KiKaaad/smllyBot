package com.kika.smllybot.database.sql.guild;

import com.kika.smllybot.database.sql.DatabaseManager;
import com.kika.smllybot.database.sql.guild.dto.GuildData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class GuildTable {

    private static final Logger log = LoggerFactory.getLogger(GuildTable.class);

    private static final RowMapper<GuildData> GUILD_MAPPER = (rs, rowNum) -> new GuildData(
            rs.getLong("id"),
            rs.getString("title"),
            rs.getBoolean("staging"),
            rs.getString("mute_type"),
            rs.getLong("mute_role")
    );

    public static void createTable() {
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));

        String sql = """
                CREATE TABLE IF NOT EXISTS guild (
                id BIGINT PRIMARY KEY,
                title TEXT,
                staging BOOLEAN DEFAULT FALSE,
                mute_type VARCHAR(32) DEFAULT 'TIMEOUT',
                mute_role BIGINT
                );
                """;

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            log.info("✅ Таблица GUILD успешно проверена / создана");
        } catch (SQLException e) {
            log.error("❌ Ошибка создания таблицы GUILD: ", e);
        }
    }

    public static GuildData getOrCreateGuild(long id, String guildName) {
        JdbcTemplate query = DatabaseManager.getQuery();
        String upsertSql = """
                INSERT INTO guild (id, title) VALUES (?, ?)
                ON CONFLICT (id) DO UPDATE SET id = EXCLUDED.id
                RETURNING id, title, staging;
                """;

        try {
            return query.queryForObject(upsertSql, GUILD_MAPPER, id, guildName);
        } catch (Exception e) {
            log.error("❌ Возникла ошибка при попытке вернуть / записать данные гильдии %s", e);
            return null;
        }
    }

    public static void editStaging(long id, boolean staging) {
        JdbcTemplate query = DatabaseManager.getQuery();
        String sql = "UPDATE guild SET staging = ? WHERE id = ?";

        try {
            query.update(sql, staging, id);
        } catch (Exception e) {
            log.error("❌ Возникла ошибка при попытке обновить STAGING", e);
        }
    }
}

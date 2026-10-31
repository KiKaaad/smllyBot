package com.kika.smllybot.database.sql.guild.guild;

import com.kika.smllybot.database.sql.DatabaseManager;
import com.kika.smllybot.database.sql.guild.guild.dto.GuildData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.sql.Array;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

public class GuildTable {

    private static final Logger log = LoggerFactory.getLogger(GuildTable.class);

    private static final RowMapper<GuildData> GUILD_MAPPER = (rs, rowNum) -> {
        Array arr = rs.getArray("prefix");
        List<String> prefix = arr != null ? Arrays.asList((String[]) arr.getArray()) : List.of();

        return new GuildData(
                rs.getLong("id"),
                rs.getString("title"),
                rs.getBoolean("staging"),
                rs.getString("mute_type"),
                rs.getLong("mute_role"),
                prefix
        );
    };

    public static void createTable() {
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));

        String sql = """
                CREATE TABLE IF NOT EXISTS guild (
                    id          BIGINT PRIMARY KEY,
                    title       TEXT,
                    staging     BOOLEAN DEFAULT FALSE,
                    mute_type   VARCHAR(32) DEFAULT 'TIMEOUT',
                    mute_role   BIGINT
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
        String selectSql = """
                SELECT id, title, staging, mute_type, mute_role, prefix
                FROM guild
                WHERE id = ?
                """;

        String insertSql = """
                INSERT INTO guild (id, title) VALUES (?, ?)
                ON CONFLICT (id) DO UPDATE SET id = EXCLUDED.id
                RETURNING id, title, staging, mute_type, mute_role, prefix;
                """;

        try {
            return query.queryForObject(selectSql, GUILD_MAPPER, id);
        } catch (EmptyResultDataAccessException e) {
            try {
                return query.queryForObject(insertSql, GUILD_MAPPER, id, guildName);
            } catch (Exception ex) {
                log.error("❌ Ошибка при попытке создать гильдию ", ex);
                return null;
            }
        } catch (Exception e) {
            log.error("❌ Возникла ошибка при попытке достать гильдию: ", e);
        }
        return null;
    }

    public static void editStaging(long guildId, boolean staging) {
        JdbcTemplate query = DatabaseManager.getQuery();
        String sql = "UPDATE guild SET staging = ? WHERE guildId = ?";

        try {
            query.update(sql, staging, guildId);
        } catch (Exception e) {
            log.error("❌ Возникла ошибка при попытке обновить STAGING", e);
        }
    }

    public static void editPrefix(long guildId, List<String> prefix) {
        JdbcTemplate query = DatabaseManager.getQuery();
        String sql = """
                INSERT INTO guild_prefix (guild_id, prefix) VALUES (?, ?)
                ON CONFLICT (guild_id) DO UPDATE SET prefix = EXCLUDED.prefix
                """;

        try {
            query.update(sql, prefix, guildId);
        } catch (Exception e) {
            log.error("Возникла ошибка при попытке обновить префиксы гильдии");
        }
    }
}

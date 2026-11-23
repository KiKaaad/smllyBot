package com.kika.smllybot.database.sql.guild.warn;

import com.kika.smllybot.database.sql.DatabaseManager;
import com.kika.smllybot.database.sql.guild.warn.dto.WarnCreateData;
import com.kika.smllybot.database.sql.guild.warn.dto.WarnData;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;

public class WarnTable {

    private static final Logger log = LoggerFactory.getLogger(WarnTable.class);
    private static final RowMapper<WarnData> WARN_MAPPER = DataClassRowMapper.newInstance(WarnData.class);
    private final JdbcTemplate query;

    public WarnTable(JdbcTemplate query) {
        this.query = query;
    }

    public void createWarn(WarnCreateData data) {
        String sql = """
                INSERT INTO warn (guild_id, discord_id, by_discord_id, reason, until)
                VALUES (?, ?, ?, ?, ?)
                """;

        try {
            query.update(
                    sql,
                    data.getGuildId(),
                    data.getDiscordId(),
                    data.getByDiscordId(),
                    data.getReason(),
                    data.getUntil()
            );
        } catch (Exception e) {
            log.error("❌ Ошибка при попытке выдать варн", e);
        }
    }

    public void markAsUnwarned(long discordId, Long removedByDiscordId, long guildId, long id) {
        String sql = """
            UPDATE warn
            SET active = false,
                removed_at = NOW(),
                removed_by = ?
            WHERE discord_id = ? and active = true and guild_id = ? and id = ?;
            """;

        try {
            query.update(sql, removedByDiscordId, discordId, guildId, id);
        } catch (Exception e) {
            log.error("❌ Ошибка при попытке снять варн пользователя", e);
        }
    }

    public List<WarnData> getExpiredWarns() {
        String sql = """
            SELECT * FROM warn
            WHERE active = true
                AND until IS NOT NULL
                AND until <= NOW();
            """;

        return DatabaseManager.getQuery().query(sql, WARN_MAPPER);
    }

    public List<WarnData> getMemberWarns(long discordId) {
        String sql = """
                SELECT * FROM warn
                WHERE discord_id = ?
                """;

        return query.query(sql, WARN_MAPPER, discordId);
    }

    public int getMemberActiveWarns(long discordId) {
        String sql = """
                SELECT COUNT(*) FROM warn
                WHERE discord_id = ?
                    AND active = true;
                """;

        return query.queryForObject(sql, Integer.class, discordId);
    }

    public void clearMemberWarns(long discordId, long guildId) {
        String sql = """
                UPDATE warn SET active = false
                WHERE discord_id = ? and guild_id = ?;
                """;

        query.update(sql, discordId, guildId);
    }

    public List<WarnData> getWarnList(long guildId) {
        String sql = """
                SELECT * FROM warn
                WHERE guild_id = ?
                """;

        return query.query(sql, WARN_MAPPER, guildId);
    }

}

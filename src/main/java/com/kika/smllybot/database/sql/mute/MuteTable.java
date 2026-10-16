package com.kika.smllybot.database.sql.mute;

import com.kika.smllybot.database.sql.DatabaseManager;
import com.kika.smllybot.database.sql.mute.dto.MuteCreateData;
import com.kika.smllybot.database.sql.mute.dto.MuteData;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.OffsetDateTime;
import java.util.List;

public class MuteTable {

    private static final RowMapper<MuteData> MUTE_MAPPER = DataClassRowMapper.newInstance(MuteData.class);

    private static final Logger log = LoggerFactory.getLogger(MuteTable.class);

    private final JdbcTemplate query;

    public MuteTable(JdbcTemplate query) {
        this.query = query;
    }

    public void createTable() {
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));

        String sql = """
                CREATE TABLE IF NOT EXISTS mute (
                    id              BIGSERIAL PRIMARY KEY,
                    mute_type       VARCHAR NOT NULL,                   -- Тип мута: TIMEOUT | ROLE
                    guild_id        BIGINT NOT NULL,
                    discord_id      BIGINT NOT NULL,                    -- Кого замутили
                    by_discord_id   BIGINT NOT NULL,                    -- Кто выдал мут
                    reason          VARCHAR(512),                       -- Причина мута
                    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(), -- Дата выдачи мута
                    until           TIMESTAMPTZ,                        -- До какого времени мут (NULL = навсегда)
                    removed_at      TIMESTAMPTZ,                        -- Когда мут сняли по факту
                    removed_by      BIGINT,                             -- Кто снял мут (NULL = снял бот по таймеру)
                    active          BOOLEAN NOT NULL DEFAULT TRUE       -- Активен ли мут сейчас
                );
                CREATE UNIQUE INDEX IF NOT EXISTS idx_mute_guild_discord_active
                ON mute (guild_id, discord_id)
                WHERE active = true;
                """;

        try {
            query.execute(sql);
            log.info("✅ Таблица MUTE успешно проверена / создана");
        } catch (Exception e) {
            log.error("❌ Ошибка создания таблицы MUTE: ", e);
        }
    }

    public void createMute(MuteCreateData data) {
        String sql = """
                INSERT INTO mute (mute_type, guild_id, discord_id, by_discord_id, reason, until)
                VALUES (?, ?, ?, ?, ?, ?)
                ON CONFLICT (guild_id, discord_id) WHERE active = true
                DO UPDATE SET
                mute_type     = EXCLUDED.mute_type,
                by_discord_id = EXCLUDED.by_discord_id,
                reason        = EXCLUDED.reason,
                until         = EXCLUDED.until,
                created_at    = NOW();
                """;

        try {
            query.update(
                    sql,
                    data.getMuteType(),
                    data.getGuildId(),
                    data.getDiscordId(),
                    data.getByDiscordId(),
                    data.getReason(),
                    data.getUntil()
            );
        } catch (Exception e) {
            log.error("❌ Возникла ошибка при попытке замутить пользователя", e);
        }
    }

    public List<MuteData> getExpiredMutes() {
        String sql = """
            SELECT * FROM mute
            WHERE active = true
              AND until IS NOT NULL
              AND until <= NOW();
            """;

        return DatabaseManager.getQuery().query(sql, MUTE_MAPPER);
    }

    public List<MuteData> getMuteList(long guildId) {
        String sql = """
                SELECT * FROM mute
                WHERE guild_id = ?
                """;

        return query.query(sql, MUTE_MAPPER, guildId);
    }

    public void markAsUnmuted(long discordId, Long removedByDiscordId, long guildId) {
        String sql = """
            UPDATE mute
            SET active = false,
                removed_at = NOW(),
                removed_by = ?
            WHERE discord_id = ? and active = true and guild_id = ?;
            """;

        try {
            query.update(sql, removedByDiscordId, discordId, guildId);
        } catch (Exception e) {
            log.error("Возникла ошибка при попытке размутить пользователя", e);
        }
    }
}

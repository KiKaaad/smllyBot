package com.kika.smllybot.database.sql.ban;

import com.kika.smllybot.database.sql.DatabaseManager;
import com.kika.smllybot.database.sql.ban.dto.BanCreateData;
import com.kika.smllybot.database.sql.ban.dto.BanData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;

public class BanTable {

    private static final Logger log = LoggerFactory.getLogger(BanTable.class);

    private static final RowMapper<BanData> BAN_MAPPER = DataClassRowMapper.newInstance(BanData.class);

    private final JdbcTemplate query;

    public BanTable(JdbcTemplate query) {
        this.query = query;
    }

    public void createTable() {
        String sql = """
                CREATE TABLE IF NOT EXISTS ban (
                    id              BIGSERIAL PRIMARY KEY,
                    guild_id        BIGINT NOT NULL,
                    discord_id      BIGINT NOT NULL,                    -- Кого замутили
                    by_discord_id   BIGINT NOT NULL,                    -- Кто выдал бан
                    reason          VARCHAR(512),                       -- Причина бана
                    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(), -- Дата выдачи бана
                    until           TIMESTAMPTZ,                        -- До какого времени бана (NULL = навсегда)
                    removed_at      TIMESTAMPTZ,                        -- Когда бан сняли по факту
                    removed_by      BIGINT,                             -- Кто снял бан (NULL = снял бот по таймеру)
                    active          BOOLEAN NOT NULL DEFAULT TRUE       -- Активен ли бан сейчас
                );
                CREATE UNIQUE INDEX IF NOT EXISTS idx_ban_guild_discord_active
                ON ban (guild_id, discord_id)
                WHERE active = true;
                """;

        try {
            query.execute(sql);
            log.info("✅ Таблица BAN успешно проверена / создана");
        } catch (Exception e) {
            log.error("❌ Ошибка создания таблицы BAN: ", e);
        }
    }

    public void createBan(BanCreateData data) {
        String sql = """
                INSERT INTO ban (guild_id, discord_id, by_discord_id, reason, until)
                VALUES (?, ?, ?, ?, ?)
                ON CONFLICT (guild_id, discord_id) WHERE active = true
                DO UPDATE SET
                by_discord_id = EXCLUDED.by_discord_id,
                reason        = EXCLUDED.reason,
                until         = EXCLUDED.until,
                created_at    = NOW();
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
            log.error("❌ Возникла ошибка при попытке забанить пользователя", e);
        }
    }

    public List<BanData> getExpiredBans() {
        String sql = """
            SELECT * FROM ban
            WHERE active = true
              AND until IS NOT NULL
              AND until <= NOW();
            """;

        return DatabaseManager.getQuery().query(sql, BAN_MAPPER);
    }

    public List<BanData> getBanList(long guildId) {
        String sql = """
                SELECT * FROM ban
                WHERE guild_id = ?
                """;

        return query.query(sql, BAN_MAPPER, guildId);
    }

    public void markAsUnbanned(long discordId, Long removedByDiscordId, long guildId) {
        String sql = """
            UPDATE ban
            SET active = false,
                removed_at = NOW(),
                removed_by = ?
            WHERE discord_id = ? and active = true and guild_id = ?;
            """;

        try {
            query.update(sql, removedByDiscordId, discordId, guildId);
        } catch (Exception e) {
            log.error("Возникла ошибка при попытке разбанить пользователя", e);
        }
    }

}

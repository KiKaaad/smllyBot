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
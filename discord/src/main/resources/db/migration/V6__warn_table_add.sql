CREATE TABLE IF NOT EXISTS warn (
    id              BIGSERIAL PRIMARY KEY,
    guild_id        BIGINT NOT NULL,
    discord_id      BIGINT NOT NULL,                    -- Кому выдан варн
    by_discord_id   BIGINT NOT NULL,                    -- Кто выдал варн
    reason          VARCHAR(512),                       -- Причина варна
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(), -- Дата выдачи варна
    until           TIMESTAMPTZ,                        -- До какого времени варна (NULL = навсегда)
    removed_at      TIMESTAMPTZ,                        -- Когда варн сняли по факту
    removed_by      BIGINT,                             -- Кто снял варн (NULL = снял бот по таймеру)
    active          BOOLEAN NOT NULL DEFAULT TRUE       -- Активен ли варн сейчас
);

CREATE UNIQUE INDEX IF NOT EXISTS idx_warn_guild_discord_active
ON warn (guild_id, discord_id)
WHERE active = true;
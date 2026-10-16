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

CREATE TABLE IF NOT EXISTS profile (
    id          BIGINT REFERENCES users(id) ON DELETE CASCADE,
    guild_id    BIGINT,
    name        VARCHAR,
    created_at  TIMESTAMPTZ,
    about_me    VARCHAR(255) DEFAULT 'Пользователь ничего не рассказал о себе.',
    PRIMARY KEY (id, guild_id)
);

CREATE TABLE IF NOT EXISTS privacy (
    id BIGINT PRIMARY KEY REFERENCES users(id) ON DELETE CASCADE,
    bag BOOLEAN DEFAULT false,
    activity BOOLEAN DEFAULT false,
    last_activity BOOLEAN DEFAULT false
);

CREATE TABLE IF NOT EXISTS bank (
    id        BIGINT PRIMARY KEY REFERENCES users(id) ON DELETE CASCADE,
    name      VARCHAR(32),
    star      INT DEFAULT 0,
    iris      BIGINT DEFAULT 0,
    iris_coin BIGINT DEFAULT 0,
    last_farm TIMESTAMP DEFAULT CURRENT_TIMESTAMP - INTERVAL '4 hours'
);

CREATE TABLE IF NOT EXISTS guild (
    id BIGINT PRIMARY KEY,
    title TEXT,
    staging BOOLEAN DEFAULT FALSE,
    mute_type VARCHAR(32) DEFAULT 'TIMEOUT',
    mute_role BIGINT
);

CREATE TABLE IF NOT EXISTS statistic (
    id              BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    guild           BIGINT NOT NULL,
    date            DATE DEFAULT CURRENT_TIMESTAMP,
    message_count   INT,
    PRIMARY KEY (id, guild, date)
);

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
CREATE TABLE guild_prefix (
    guild_id BIGINT,
    prefix VARCHAR(16) DEFAULT NULL,
    PRIMARY KEY (guild_id, prefix)
);

CREATE INDEX idx_guild_prefixes_guild_id ON guild_prefix(guild_id);
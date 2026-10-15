CREATE TABLE IF NOT EXISTS badge (
    badge_id VARCHAR(255) PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS badge_users (
    badge_id VARCHAR(255) REFERENCES badge(badge_id),
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    added TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT badge_users_pkey PRIMARY KEY (badge_id, user_id)
);

INSERT INTO badge (badge_id) VALUES ('<:alpha:1559150827032150106>'),
('<:beta:1559150765140869230>'), ('<:team:1559150762922221640>');
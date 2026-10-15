ALTER TABLE badge ADD COLUMN badge_default VARCHAR(255);

UPDATE badge SET badge_default = '🅰️'
WHERE badge_id = '<:alpha:1559150827032150106>';

UPDATE badge SET badge_default = '🅱️'
WHERE badge_id = '<:beta:1559150765140869230>';

UPDATE badge SET badge_default = '🎩'
WHERE badge_id = '<:team:1559150762922221640>';
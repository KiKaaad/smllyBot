package com.kika.smllybot.database.sql.badges.badges;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

public class BadgeTable {

    private static final Logger log = LoggerFactory.getLogger(BadgeTable.class);

    public final JdbcTemplate query;
    public BadgeTable(JdbcTemplate query) {
        this.query = query;
    }

}

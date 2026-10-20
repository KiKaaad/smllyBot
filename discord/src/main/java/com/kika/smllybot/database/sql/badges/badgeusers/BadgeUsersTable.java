package com.kika.smllybot.database.sql.badges.badgeusers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

public class BadgeUsersTable {

    private static final Logger log = LoggerFactory.getLogger(BadgeUsersTable.class);

    public final JdbcTemplate query;
    public BadgeUsersTable(JdbcTemplate query) {
        this.query = query;
    }

}

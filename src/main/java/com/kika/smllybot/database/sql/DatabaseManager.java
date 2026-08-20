package com.kika.smllybot.database.sql;

import com.kika.smllybot.Config;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseManager {

    private static final Logger log = LoggerFactory.getLogger(DatabaseManager.class);
    private static HikariDataSource dataSource;
    private static JdbcTemplate jdbcTemplate;

    public static void init() {
        if (dataSource != null) {
            return;
        }

        String host = Config.getInstance().getString("database.host");
        int port = Config.getInstance().getInt("database.port");
        String name = Config.getInstance().getString("database.name");
        String user = Config.getInstance().getString("database.user");
        String password = Config.getInstance().getString("database.password");

        HikariConfig config = new HikariConfig();

        String url = String.format("jdbc:postgresql://%s:%d/%s", host, port, name);

        config.setJdbcUrl(url);
        config.setUsername(user);
        config.setPassword(password);

        config.setMaximumPoolSize(20);
        config.setMinimumIdle(2);
        config.setIdleTimeout(30000);
        config.setConnectionTimeout(10000);

        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");

        dataSource = new HikariDataSource(config);
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public static Connection getConnection() throws SQLException {
        if (dataSource == null) {
            log.error("❌ Не удалось подключиться к базе данных. Вероятно не заполнены данные для подключение к ней");
            throw new IllegalStateException();
        }
        return dataSource.getConnection();
    }

    public static JdbcTemplate getQuery() {
        if (jdbcTemplate == null) {
            log.error("❌ Вызов без инициализации");
            throw new IllegalStateException();
        }

        return jdbcTemplate;
    }

}

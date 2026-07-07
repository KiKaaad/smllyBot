package com.kika.smllybot.database.sql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseManager {
    private static final HikariDataSource ds;

    static {
        Dotenv dotenv = Dotenv.load();
        HikariConfig config = new HikariConfig();

        String url = String.format("jdbc:postgresql://%s:%s/%s",
                dotenv.get("DB_HOST"),
                dotenv.get("DB_PORT"),
                dotenv.get("DB_NAME"));

        config.setJdbcUrl(url);
        config.setUsername(dotenv.get("DB_USER"));
        config.setPassword(dotenv.get("DB_PASS"));

        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setIdleTimeout(30000);
        config.setConnectionTimeout(10000);

        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");

        ds = new HikariDataSource(config);
    }

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    private DatabaseManager() {}
}


package com.kika.smllybot.database.sql;

import com.kika.smllybot.Config;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseManager {

    private static HikariDataSource ds;

    public static void init() {
        if (ds != null) {
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

        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setIdleTimeout(30000);
        config.setConnectionTimeout(10000);

        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");

        ds = new HikariDataSource(config);
    }

    public static Connection getConnection() throws SQLException {
        if (ds == null) {
            throw new IllegalStateException();
        }
        return ds.getConnection();
    }
}

package com.kika.smllybot.database.sql.guild;

import com.kika.smllybot.database.sql.DatabaseManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

// TODO: Таблица гильдий
public class GuildTable {

    private static final Logger log = LoggerFactory.getLogger(GuildTable.class);

    public static void createTable() {
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));

        String sql = """
                CREATE TABLE IF NOT EXISTS guild (
                id BIGINT PRIMARY KEY,
                title VARCHAR
                );
                """;

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            log.info("✅ Таблица GUILD успешно проверена / создана");
        } catch (SQLException e) {
            log.error("❌ Ошибка создания таблицы GUILD: ", e);
        }
    }

}

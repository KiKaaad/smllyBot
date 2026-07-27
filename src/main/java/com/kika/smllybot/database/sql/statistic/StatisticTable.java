package com.kika.smllybot.database.sql.statistic;

import com.kika.smllybot.database.sql.DatabaseManager;
import com.kika.smllybot.database.sql.statistic.dto.TotalStatisticUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.sql.*;

public class StatisticTable {

    private static final Logger log = LoggerFactory.getLogger(StatisticTable.class);

    public static void createTable() {
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));

        String sql = """
            CREATE TABLE IF NOT EXISTS statistic (
                id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                guild BIGINT NOT NULL,
                date DATE DEFAULT CURRENT_TIMESTAMP,
                message_count INT,
                PRIMARY KEY (id, guild, date)
            );
            """;

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            log.info("✅ Таблица STATISTIC проверена / создана");
        } catch (SQLException e) {
            log.error("❌ Ошибка создания таблицы STATISTIC: ", e);
        }
    }

    public static void createStatistic(long id, long guildId) {

        String sql = """
                INSERT INTO statistic (id, guild, date, message_count)
                VALUES (?, ?, CURRENT_DATE, 1)
                ON CONFLICT (id, guild, date)
                DO UPDATE SET message_count = statistic.message_count + 1;
                """;

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id);
            pstmt.setLong(2, guildId);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            log.error("❌ Ошибка при попытке обновить статистику пользователя: ", e);
        }
    }

    public static TotalStatisticUser getTotalUserStatistic(long id) {
        String sql = """
            SELECT
            SUM(CASE WHEN date = CURRENT_DATE THEN message_count ELSE 0 END) as total_day,
            SUM(CASE WHEN date >= CURRENT_DATE - INTERVAL '7 days' THEN message_count ELSE 0 END) as total_week,
            SUM(CASE WHEN date >= CURRENT_DATE - INTERVAL '1 month' THEN message_count ELSE 0 END) as total_month,
            SUM(CASE WHEN date >= CURRENT_DATE - INTERVAL '1 year' THEN message_count ELSE 0 END) as total
            FROM statistic
            WHERE id = ?;
            """;

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new TotalStatisticUser(
                            rs.getLong("total_day"),
                            rs.getLong("total_week"),
                            rs.getLong("total_month"),
                            rs.getLong("total")
                    );
                }
            }
        } catch (SQLException e) {
            log.error("❌ Ошибка при получении статистики: ", e);
        }
        return null;
    }
}

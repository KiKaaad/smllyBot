package com.kika.smllybot.database.sql.bank;

import com.kika.smllybot.database.sql.DatabaseManager;
import com.kika.smllybot.database.sql.bank.dto.BankAccount;
import com.kika.smllybot.database.sql.bank.dto.BankTopAmount;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BankTable {

    private static final Logger log = LoggerFactory.getLogger(BankTable.class);

    public static void createTable() {
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));

        String sql = """
                CREATE TABLE IF NOT EXISTS bank (
                id INTEGER PRIMARY KEY REFERENCES users(id) ON DELETE CASCADE,
                name VARCHAR(32),
                star INT DEFAULT 0,
                iris BIGINT DEFAULT 0,
                iris_coin BIGINT DEFAULT 0,
                last_farm TIMESTAMP DEFAULT CURRENT_TIMESTAMP - INTERVAL '4 hours'
                );
                """;

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            log.info("✅ Таблица BANK успешно проверена / создана");
        } catch (SQLException e) {
            log.error("❌ Ошибка создания таблицы BANK: {}", e.getMessage(), e);
        }
    }

    @Nullable
    public static BankAccount getOrCreateBank(int internalId, String defaultName) {
        String upsertSql = """
                INSERT INTO bank (id, name) VALUES (?, ?)
                ON CONFLICT (id) DO UPDATE SET id = EXCLUDED.id
                RETURNING id, name, star, iris, iris_coin, last_farm;
                """;

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(upsertSql)) {

            pstmt.setInt(1, internalId);
            pstmt.setString(2, defaultName);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new BankAccount(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getInt("star"),
                            rs.getLong("iris"),
                            rs.getLong("iris_coin"),
                            rs.getTimestamp("last_farm")
                    );
                }
            }
        } catch (SQLException e) {
            log.error("❌ Ошибка при получении / создании BANK: ");
        }
        return null;
    }

    public static void addIrisCoin(long internalId, long irisCoin) {
        String sql = "UPDATE bank SET iris_coin = iris_coin + ? WHERE id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, irisCoin);
            pstmt.setLong(2, internalId);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                log.info("ℹ️ Коины пользователя {} обновлены на {}", internalId, irisCoin);
            }
        } catch (SQLException e) {
            log.error("❌ Ошибка при добавлении коинов: {}", e.getMessage(), e);
        }
    }

    public static void updateLastFarm(int internalId) {
        String sql = "UPDATE bank SET last_farm = CURRENT_TIMESTAMP WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, internalId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.error("❌ Ошибка обновления времени фармы: {}", e.getMessage(), e);
        }
    }

    @NotNull
    public static List<BankTopAmount> getTopIrisCoins(int limit) {
        List<BankTopAmount> topList = new ArrayList<>();
        String sql = """
            SELECT b.id, u.name AS username, b.iris_coin
            FROM bank b
            JOIN users u ON b.id = u.id
            WHERE b.iris_coin > 0
            ORDER BY b.iris_coin DESC
            LIMIT ?
            """;

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, limit);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    topList.add(new BankTopAmount(
                            rs.getInt("id"),
                            rs.getString("username"),
                            rs.getLong("iris_coin")
                    ));
                }
            }
        } catch (SQLException e) {
            log.error("❌ Ошибка получения топа коинов: ");
        }
        return topList;
    }

    @NotNull
    public static List<BankTopAmount> getTopIris(int limit) {
        List<BankTopAmount> topList = new ArrayList<>();
        String sql = """
            SELECT b.id, u.name AS username, b.iris
            FROM bank b
            JOIN users u ON b.id = u.id
            WHERE b.iris > 0
            ORDER BY b.iris DESC
            LIMIT ?
            """;

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, limit);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    topList.add(new BankTopAmount(
                            rs.getInt("id"),
                            rs.getString("username"),
                            rs.getLong("iris")
                    ));
                }
            }
        } catch (SQLException e) {
            log.error("❌ Ошибка получения топа ирисок: ");
        }
        return topList;
    }
}


// TODO: Сделать таблицу с транзакциями пользователя
package com.kika.smllybot.database.postgresql.bank;

import com.kika.smllybot.database.postgresql.DatabaseManager;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BankTable {

    public static void createTable() {
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));

        String sql = """
                CREATE TABLE IF NOT EXISTS bank (
                id INTEGER PRIMARY KEY REFERENCES users(id) ON DELETE CASCADE,
                name VARCHAR(32),
                iris BIGINT DEFAULT 0,
                iris_coin BIGINT DEFAULT 0,
                last_farm TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                );
                """;

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("✅ DB:USERS:BANK | Таблица успешно проверена / создана");
        } catch (SQLException e) {
            System.err.println("❌ DB:USERS:BANK | Ошибка создания: " + e.getMessage());
        }
    }

    public static GetBank getOrCreateBank(int internalId, String defaultName) {
        String selectSql = "SELECT id, name, iris, iris_coin, last_farm FROM bank WHERE id = ?";
        String insertSql = "INSERT INTO bank (id, name) VALUES (?, ?) ON CONFLICT (id) DO NOTHING RETURNING id, name, iris, iris_coin, last_farm";

        try (Connection conn = DatabaseManager.getConnection()) {
            try (PreparedStatement selectPstmt = conn.prepareStatement(selectSql)) {
                selectPstmt.setInt(1, internalId);
                ResultSet rs = selectPstmt.executeQuery();
                if (rs.next()) {
                    return new GetBank(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getLong("iris"),
                            rs.getLong("iris_coin"),
                            rs.getTimestamp("last_farm")
                    );
                }
            }

            try (PreparedStatement insertPstmt = conn.prepareStatement(insertSql)) {
                insertPstmt.setInt(1, internalId);
                insertPstmt.setString(2, defaultName);
                ResultSet rs = insertPstmt.executeQuery();
                if (rs.next()) {
                    return new GetBank(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getLong("iris"),
                            rs.getLong("iris_coin"),
                            rs.getTimestamp("last_farm")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
                System.out.println("✅ Количество ирис-коинов пользователя " + internalId + " обновлена на " + irisCoin);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateLastFarm(int internalId) {
        String sql = "UPDATE bank SET last_farm = CURRENT_TIMESTAMP WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, internalId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<GetBank> getTopIrisCoins(int limit) {
        List<GetBank> topList = new ArrayList<>();
        String sql = "SELECT id, name, iris, iris_coin, last_farm FROM bank WHERE iris_coin > 0 ORDER BY iris_coin DESC LIMIT ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, limit);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    topList.add(new GetBank(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getLong("iris"),
                            rs.getLong("iris_coin"),
                            rs.getTimestamp("last_farm")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return topList;
    }

}

// TODO: Сделать директорию bank. Внутри директории банки со своими баффами и дебафами
// TODO: Сделать таблицу с транзакциями пользователя
package com.kika.smllybot.database.postgresql.user;

import com.kika.smllybot.database.postgresql.DatabaseManager;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.sql.*;

public class UserTable {

    public static void createTable() {
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
        String sql = """
            CREATE TABLE IF NOT EXISTS users (
                id SERIAL PRIMARY KEY,
                discord_id BIGINT UNIQUE NOT NULL,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                motto VARCHAR(255) DEFAULT 'Пользователь не указал описание.'
            );
            """;

        try (Connection conn = DatabaseManager.getConnection();
            Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("✅ DB:USERS | Таблица успешно проверена / создана");
        } catch (SQLException e) {
            System.err.println("❌ DB:USERS | Ошибка создания: " + e.getMessage());
        }
    }

    public static GetUsers getOrCreateUser(long discordId) {
        String selectSql = "SELECT id, " +
                "discord_id, " +
                "motto, " +
                "to_char(created_at, 'DD.MM.YYYY HH24:MI') AS created_at " +
                "FROM users WHERE discord_id = ?";
        String insertSql = "INSERT INTO users (discord_id) VALUES (?) RETURNING id, discord_id, to_char(created_at, 'DD.MM.YYYY HH24:MI') AS created_at, motto";

        try (Connection conn = DatabaseManager.getConnection()) {
            try (PreparedStatement selectPstmt = conn.prepareStatement(selectSql)) {
                selectPstmt.setLong(1, discordId);
                ResultSet rs = selectPstmt.executeQuery();
                if (rs.next()) {
                    return new GetUsers(rs.getInt("id"),
                            rs.getLong("discord_id"),
                            rs.getString("motto"),
                            rs.getString("created_at"));
                }
            }

            try (PreparedStatement insertPstmt = conn.prepareStatement(insertSql)) {
                insertPstmt.setLong(1, discordId);
                ResultSet rs = insertPstmt.executeQuery();
                if (rs.next()) {
                    return new GetUsers(rs.getInt("id"),
                            rs.getLong("discord_id"),
                            rs.getString("motto"),
                            rs.getString("created_at"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void updateMotto(long discordId, String newText) {
        String sql = "UPDATE users SET motto = ? WHERE discord_id = ?";

        try (Connection conn = DatabaseManager.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, newText);
            pstmt.setLong(2, discordId);

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("✅ Данные пользователя " + discordId + " обновлены.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
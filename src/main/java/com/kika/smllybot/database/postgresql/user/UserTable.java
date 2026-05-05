package com.kika.smllybot.database.postgresql.user;

import com.kika.smllybot.database.postgresql.DatabaseManager;
import java.sql.*;

public class UserTable {

    public static void createTable() {
        String sql = """
            CREATE TABLE IF NOT EXISTS users (
                id SERIAL PRIMARY KEY,
                discord_id BIGINT UNIQUE NOT NULL,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                about_me VARCHAR(50) DEFAULT 'Описания нет'
            );
            """;

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("✅ DB:USER | Таблица успешно проверена / создана");
        } catch (SQLException e) {
            System.err.println("❌ DB:USER | Ошибка создания: " + e.getMessage());
        }
    }

    public static User getOrCreateUser(long discordId) {
        // 1. Сначала просто пытаемся найти
        String selectSql = "SELECT id, discord_id, about_me FROM users WHERE discord_id = ?";
        String insertSql = "INSERT INTO users (discord_id) VALUES (?) RETURNING id, discord_id, about_me";

        try (Connection conn = DatabaseManager.getConnection()) {
            try (PreparedStatement selectPstmt = conn.prepareStatement(selectSql)) {
                selectPstmt.setLong(1, discordId);
                ResultSet rs = selectPstmt.executeQuery();
                if (rs.next()) {
                    return new User(rs.getInt("id"), rs.getLong("discord_id"), rs.getString("about_me"));
                }
            }

            try (PreparedStatement insertPstmt = conn.prepareStatement(insertSql)) {
                insertPstmt.setLong(1, discordId);
                ResultSet rs = insertPstmt.executeQuery();
                if (rs.next()) {
                    return new User(rs.getInt("id"), rs.getLong("discord_id"), rs.getString("about_me"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void updateAboutMe(long discordId, String newText) {
        String sql = "UPDATE users SET about_me = ? WHERE discord_id = ?";

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
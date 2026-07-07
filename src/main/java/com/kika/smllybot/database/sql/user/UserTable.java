package com.kika.smllybot.database.sql.user;

import com.kika.smllybot.database.sql.DatabaseManager;
import com.kika.smllybot.database.sql.user.dto.UserAccount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.sql.*;

public class UserTable {

    private static final Logger log = LoggerFactory.getLogger(UserTable.class);

    public static void createTable() {
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));

        String sql = """
            CREATE TABLE IF NOT EXISTS users (
                id SERIAL PRIMARY KEY,
                role VARCHAR,
                name VARCHAR,
                discord_id BIGINT UNIQUE NOT NULL,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                motto VARCHAR(255) DEFAULT 'Пользователь не указал описание.'
            );
            """;

        try (Connection conn = DatabaseManager.getConnection();
            Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            log.info("✅ Таблица USERS проверена / создана");
        } catch (SQLException e) {
            log.error("❌ Ошибка создания таблицы USERS: ");
        }
    }

    public static UserAccount getOrCreateUser(long discordId, String currentName) {
        String selectSql = """
        SELECT id, discord_id, role, name, motto, to_char(created_at, 'DD.MM.YYYY HH24:MI') AS created_at
        FROM users WHERE discord_id = ?;
        """;

        String insertSql = """
        INSERT INTO users (discord_id, name) VALUES (?, ?)
        ON CONFLICT (discord_id) DO NOTHING
        RETURNING id, discord_id, role, name, motto, to_char(created_at, 'DD.MM.YYYY HH24:MI') AS created_at;
        """;

        try (Connection conn = DatabaseManager.getConnection()) {
            try (PreparedStatement pstmt = conn.prepareStatement(selectSql)) {
                pstmt.setLong(1, discordId);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) return mapUser(rs);
                }
            }

            try (PreparedStatement pstmt = conn.prepareStatement(insertSql)) {
                pstmt.setLong(1, discordId);
                pstmt.setString(2, currentName);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) return mapUser(rs);
                }
            }

            try (PreparedStatement pstmt = conn.prepareStatement(selectSql)) {
                pstmt.setLong(1, discordId);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) return mapUser(rs);
                }
            }

        } catch (SQLException e) {
            log.error("❌ Ошибка при попытке создать / взять юзера: {}", e.getMessage(), e);
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
                log.info("✅ Данные пользователя {} обновлены.", discordId);
            }
        } catch (SQLException e) {
            log.error("❌ Возникла ошибка при обновлении девиза (discordId {}): ", discordId);
        }
    }

    public static void updateUsername(long discordId, String currentName) {
        String updateSql = "UPDATE users SET name = ? WHERE discord_id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement updatePstmt = conn.prepareStatement(updateSql)) {

            updatePstmt.setString(1, currentName);
            updatePstmt.setLong(2, discordId);

            int rowsAffected = updatePstmt.executeUpdate();

            if (rowsAffected == 0) {
                String insertSql = """
                        INSERT INTO users (discord_id, name) VALUES (?, ?)
                        ON CONFLICT (discord_id) DO NOTHING
                        """;
                try (PreparedStatement insertPstmt = conn.prepareStatement(insertSql)) {
                    insertPstmt.setLong(1, discordId);
                    insertPstmt.setString(2, currentName);
                    insertPstmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            log.error("❌ Ошибка обновления или записи юзернейма (discordId: {}): ",discordId);
        }
    }

    private static UserAccount mapUser(ResultSet rs) throws SQLException {
        return new UserAccount(
                rs.getInt("id"),
                rs.getLong("discord_id"),
                rs.getString("role"),
                rs.getString("name"),
                rs.getString("motto"),
                rs.getString("created_at")
        );
    }

}
package com.kika.smllybot.database.sql.privacy;

import com.kika.smllybot.database.sql.DatabaseManager;
import com.kika.smllybot.database.sql.privacy.dto.PrivacyAccount;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.sql.*;

public class PrivacyTable {

    private static final Logger log = LoggerFactory.getLogger(PrivacyTable.class);

    public static void createTable() {
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));

        String sql = """
                CREATE TABLE IF NOT EXISTS privacy (
                id INTEGER PRIMARY KEY REFERENCES users(id) ON DELETE CASCADE,
                bag BOOLEAN DEFAULT false,
                activity BOOLEAN DEFAULT false,
                last_activity BOOLEAN DEFAULT false
                );
                """;

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            log.info("✅ Таблица PRIVACY успешно проверена / создана");
        } catch (SQLException e) {
            log.error("❌ Ошибка создания таблицы PRIVACY: {}", e.getMessage(), e);
        }
    }

    @Nullable
    public static PrivacyAccount getOrCreatePrivacy(int internalId) {
        String selectSql = "SELECT id, bag, activity, last_activity FROM privacy WHERE id = ?";
        String insertSql = """
            INSERT INTO privacy (id) VALUES (?)
            ON CONFLICT (id) DO NOTHING
            RETURNING id, bag, activity, last_activity;
            """;

        try (Connection conn = DatabaseManager.getConnection()) {
            try (PreparedStatement pstmt = conn.prepareStatement(selectSql)) {
                pstmt.setInt(1, internalId);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return mapPrivacy(rs);
                    }
                }
            }

            try (PreparedStatement pstmt = conn.prepareStatement(insertSql)) {
                pstmt.setInt(1, internalId);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return mapPrivacy(rs);
                    }
                }
            }

            try (PreparedStatement pstmt = conn.prepareStatement(selectSql)) {
                pstmt.setInt(1, internalId);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) return mapPrivacy(rs);
                }
            }

        } catch (SQLException e) {
            log.error("❌ Ошибка PRIVACY (internalId: {}): ", internalId, e);
        }
        return null;
    }

    public static void updateBagPrivacy(int internalId, boolean visible) {
        executePrivacyUpdate("UPDATE privacy SET bag = ? WHERE id = ?", internalId, visible);
    }

    public static void updateActivityPrivacy(int internalId, boolean visible) {
        executePrivacyUpdate("UPDATE privacy SET activity = ? WHERE id = ?", internalId, visible);
    }

    public static void updateLastActivityPrivacy(int internalId, boolean visible) {
        executePrivacyUpdate("UPDATE privacy SET last_activity = ? WHERE id = ?", internalId, visible);
    }

    private static void executePrivacyUpdate(String sql, int internalId, boolean value) {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setBoolean(1, value);
            pstmt.setInt(2, internalId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.error("❌ Ошибка обновления приватности для ID {}: ", internalId);
        }
    }

    private static PrivacyAccount mapPrivacy(ResultSet rs) throws SQLException {
        return new PrivacyAccount(
                rs.getInt("id"),
                rs.getBoolean("bag"),
                rs.getBoolean("activity"),
                rs.getBoolean("last_activity")
        );
    }
}

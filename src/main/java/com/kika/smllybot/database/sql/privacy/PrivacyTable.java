package com.kika.smllybot.database.sql.privacy;

import com.kika.smllybot.database.sql.DatabaseManager;
import com.kika.smllybot.database.sql.mute.dto.MuteData;
import com.kika.smllybot.database.sql.privacy.dto.PrivacyAccount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.sql.*;

public class PrivacyTable {

    private static final Logger log = LoggerFactory.getLogger(PrivacyTable.class);

    private static final RowMapper<PrivacyAccount> PRIVACY_MAPPER = DataClassRowMapper.newInstance(PrivacyAccount.class);

    private final JdbcTemplate query;

    public PrivacyTable() {
        this.query = DatabaseManager.getQuery();
    }

    public void createTable() {
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));

        String sql = """
                CREATE TABLE IF NOT EXISTS privacy (
                    id              BIGINT PRIMARY KEY REFERENCES users(id) ON DELETE CASCADE,
                    bag             BOOLEAN DEFAULT false,
                    activity        BOOLEAN DEFAULT false,
                    last_activity   BOOLEAN DEFAULT false
                );
                """;

        try {
            query.execute(sql);
            log.info("✅ Таблица PRIVACY успешно проверена / создана");
        } catch (Exception e) {
            log.error("❌ Ошибка создания таблицы PRIVACY: ");
        }
    }

    public PrivacyAccount getOrCreatePrivacy(long id) {
        String sql = """
            INSERT INTO privacy (id)
            VALUES (?)
            ON CONFLICT (id) DO UPDATE
                SET id = EXCLUDED.id
            RETURNING id, bag, activity, last_activity;
            """;

        try {
            return query.queryForObject(sql, PRIVACY_MAPPER, id);
        } catch (Exception e) {
            log.error("❌ Ошибка PRIVACY (id: {}): ", id, e);
        }
        return null;
    }

    // Обновление приватности мешка
    public static void updateBagPrivacy(long internalId, boolean visible) {
        executePrivacyUpdate("UPDATE privacy SET bag = ? WHERE id = ?", internalId, visible);
    }

    // Обновление приватности активности (статистика сообщений)
    public static void updateActivityPrivacy(long internalId, boolean visible) {
        executePrivacyUpdate("UPDATE privacy SET activity = ? WHERE id = ?", internalId, visible);
    }

    // Обновление приватности последней активности
    public static void updateLastActivityPrivacy(long internalId, boolean visible) {
        executePrivacyUpdate("UPDATE privacy SET last_activity = ? WHERE id = ?", internalId, visible);
    }

    private static void executePrivacyUpdate(String sql, long internalId, boolean value) {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setBoolean(1, value);
            pstmt.setLong(2, internalId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.error("❌ Ошибка обновления приватности (id: {}): ", internalId, e);
        }
    }
}

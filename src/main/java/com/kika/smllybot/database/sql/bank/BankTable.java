package com.kika.smllybot.database.sql.bank;

import com.kika.smllybot.database.sql.DatabaseManager;
import com.kika.smllybot.database.sql.bank.dto.BankAccount;
import com.kika.smllybot.database.sql.bank.dto.BankTopAmount;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class BankTable {

    private static final Logger log = LoggerFactory.getLogger(BankTable.class);

    private static final RowMapper<BankAccount> BANK_MAPPER = (rs, rowNum) -> new BankAccount(
        rs.getLong("id"),
        rs.getString("name"),
        rs.getInt("star"),
        rs.getLong("iris"),
        rs.getLong("iris_coin"),
        rs.getObject("last_farm", Timestamp.class)
    );

    public static void createTable() {
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));

        String sql = """
                CREATE TABLE IF NOT EXISTS bank (
                id        BIGINT PRIMARY KEY REFERENCES users(id) ON DELETE CASCADE,
                name      VARCHAR(32),
                star      INT DEFAULT 0,
                iris      BIGINT DEFAULT 0,
                iris_coin BIGINT DEFAULT 0,
                last_farm TIMESTAMP DEFAULT CURRENT_TIMESTAMP - INTERVAL '4 hours'
                );
                """;

        try {
            DatabaseManager.getQuery().execute(sql);
            log.info("✅ Таблица BANK успешно проверена / создана");
        } catch (Exception e) {
            log.error("❌ Ошибка создания таблицы BANK: ", e);
        }
    }

    public static BankAccount getOrCreateBank(long id, String defaultName) {
        String upsertSql = """
                INSERT INTO bank (id, name) VALUES (?, ?)
                ON CONFLICT (id) DO UPDATE SET id = EXCLUDED.id
                RETURNING id, name, star, iris, iris_coin, last_farm;
                """;

        try {
            return DatabaseManager.getQuery().queryForObject(upsertSql, BANK_MAPPER, id, defaultName);
        } catch (Exception e) {
            log.error("❌ Ошибка при получении / создании BANK: ", e);
        }
        return null;
    }

    public static void addIrisCoin(long id, long irisCoin) {
        String sql = "UPDATE bank SET iris_coin = iris_coin + ? WHERE id = ?";

        try {
            DatabaseManager.getQuery().update(sql, irisCoin, id);
            log.info("ℹ️ Коины пользователя {} обновлены на {}", id, irisCoin);
        } catch (Exception e) {
            log.error("❌ Ошибка при добавлении коинов для {}: ", id, e);
        }
    }

    public static void updateLastFarm(long id) {
        String sql = "UPDATE bank SET last_farm = CURRENT_TIMESTAMP WHERE id = ?";
        try {
            DatabaseManager.getQuery().update(sql, id);
        } catch (Exception e) {
            log.error("❌ Ошибка обновления времени фармы: ", e);
        }
    }

    @NotNull
    public static List<BankTopAmount> getTopIrisCoins() {
        String sql = """
            SELECT b.id, u.name AS username, b.iris_coin
            FROM bank b
            JOIN users u ON b.id = u.id
            WHERE b.iris_coin > 0
            ORDER BY b.iris_coin DESC
            """;

        try {
            return DatabaseManager.getQuery().query(sql, (rs, rowNum) -> new BankTopAmount(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getLong("iris_coin")
            ));
        } catch (Exception e) {
            log.error("❌ Ошибка получения топа коинов: ", e);
            return new ArrayList<>();
        }
    }

    @NotNull
    public static List<BankTopAmount> getTopIris() {
        String sql = """
            SELECT b.id, u.name AS username, b.iris
            FROM bank b
            JOIN users u ON b.id = u.id
            WHERE b.iris > 0
            ORDER BY b.iris DESC
            """;

        try {
            return DatabaseManager.getQuery().query(sql, (rs, rowNum) -> new BankTopAmount(
                    rs.getLong("id"),
                    rs.getString("username"),
                    rs.getLong("iris")
            ));
        } catch (Exception e) {
            log.error("❌ Ошибка получения топа ирисок: ", e);
            return new ArrayList<>();
        }
    }
}
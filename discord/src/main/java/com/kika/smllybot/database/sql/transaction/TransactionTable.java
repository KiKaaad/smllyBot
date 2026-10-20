package com.kika.smllybot.database.sql.transaction;

import com.kika.smllybot.database.sql.DatabaseManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

// TODO: Таблица с транзакциями
public class TransactionTable {

    private static final Logger log = LoggerFactory.getLogger(TransactionTable.class);

    public static void createTable() {
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));

        String sql = """
            CREATE TABLE IF NOT EXISTS statistic (
                id          UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                guild       BIGINT,
                date        TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
                action      VARCHAR,
                value       INT
            );
            """;

        try {
            DatabaseManager.getQuery().execute(sql);

            log.info("✅ Таблица TRANSACTION проверена / создана");
        } catch (Exception e) {
            log.error("❌ Ошибка создания таблицы TRANSACTION: ", e);
        }
    }

}

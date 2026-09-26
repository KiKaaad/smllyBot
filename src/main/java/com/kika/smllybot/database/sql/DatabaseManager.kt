package com.kika.smllybot.database.sql

import com.kika.smllybot.Config
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.v1.jdbc.Database
import org.slf4j.LoggerFactory
import org.springframework.jdbc.core.JdbcTemplate
import java.sql.Connection
import java.sql.SQLException

object DatabaseManager {

    private val log = LoggerFactory.getLogger(DatabaseManager::class.java)

    @Volatile
    private var dataSource: HikariDataSource? = null

    @JvmStatic
    lateinit var query: JdbcTemplate
        private set

    @JvmStatic
    fun init() {
        if (dataSource != null) return

        val host = Config.getInstance().getString("database.host")
        val port = Config.getInstance().getInt("database.port")
        val name = Config.getInstance().getString("database.name")
        val user = Config.getInstance().getString("database.user")
        val password = Config.getInstance().getString("database.password")

        val url = "jdbc:postgresql://$host:$port/$name"

        val config = HikariConfig().apply {
            jdbcUrl = url
            username = user
            this.password = password

            maximumPoolSize = 20
            minimumIdle = 2
            idleTimeout = 30000
            connectionTimeout = 10000

            addDataSourceProperty("cachePrepStmts", "true")
            addDataSourceProperty("prepStmtCacheSize", "250")
        }

        val ds = HikariDataSource(config)
        dataSource = ds

        query = JdbcTemplate(ds)

        Database.connect(ds)

        log.info("✅ База данных успешно инициализирована")
    }

    @JvmStatic
    @Throws(SQLException::class)
    fun getConnection(): Connection {
        val ds = dataSource ?: throw IllegalStateException("Менеджер не инициализирован")
        return ds.connection
    }

    @JvmStatic
    fun close() {
        dataSource?.close()
        dataSource = null
        log.info("🔌 Пул соединений с БД закрыт")
    }
}
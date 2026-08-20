package com.kika.smllybot;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

public class Config {

    private static final Logger log = LoggerFactory.getLogger(Config.class);

    private static Config instance;
    private CommentedFileConfig config;

    public void load() {
        log.info("ℹ️ Загрузка конфигурации...");

        this.config = CommentedFileConfig.builder("config.toml")
                .defaultResource("/config.toml")
                .autosave()
                .sync()
                .build();

        this.config.load();
        log.info("✅ Конфигурация успешно загружена");
    }

    private Config() {}

    public static Config getInstance() {
        if (instance == null) {
            instance = new Config();
        }
        return instance;
    }

    public String getString(String path) {
        return config.getOrElse(path, "...");
    }

    public int getInt(String path) {
        return config.getIntOrElse(path, 0);
    }

    public long getLong(String path) {
        return config.getLongOrElse(path, 0L);
    }

    public double getFloat(String path) {
        return config.getOrElse(path, 0.0);
    }

    public boolean getBoolean(String path) {
        return config.getOrElse(path, false);
    }

    public List<?> getList(String path) {
        return config.getOrElse(path, Collections.emptyList());
    }

    public MemberCachePolicy getMemberCachePolicy(String path) {
        String policyType = getString(path).toUpperCase().trim();

        return switch (policyType) {
            case "ALL" -> MemberCachePolicy.ALL;
            case "ONLINE" -> MemberCachePolicy.ONLINE;
            case "VOICE" -> MemberCachePolicy.VOICE;
            case "OWNER" -> MemberCachePolicy.OWNER;
            case "NONE" -> MemberCachePolicy.NONE;
            default -> {
                log.warn("🟡 Неизвестная политика кэширования пользователей ({}), используется дефолтная", policyType);
                yield MemberCachePolicy.DEFAULT;
            }
        };
    }

    public void close() {
        if (config != null) {
            config.close();
        }
    }
}

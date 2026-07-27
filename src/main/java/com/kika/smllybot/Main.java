package com.kika.smllybot;

import com.kika.smllybot.database.sql.DatabaseManager;
import com.kika.smllybot.database.sql.bank.BankTable;
import com.kika.smllybot.database.sql.privacy.PrivacyTable;
import com.kika.smllybot.database.sql.statistic.StatisticTable;
import com.kika.smllybot.database.sql.user.UserTable;
import com.kika.smllybot.listeners.MessageCounter;
import com.kika.smllybot.listeners.NameSave;
import com.kika.smllybot.modules.ping.PrefixPing;
import com.kika.smllybot.modules.ping.SlashPing;
import com.kika.smllybot.other.ComponentManager;
import com.kika.smllybot.other.slashCmdInfo;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.JDAInfo;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class Main implements EventListener {


    private static final Logger log = LoggerFactory.getLogger(Main.class);
    public static String[] PREFIXES;
    public static final String VERSION = "0.5.5-beta-pw (26.07.2026)";
    public static final String OWNER = "<@683345722611073059>";

    static void main() throws InterruptedException {
        Config.getInstance().load();
        DatabaseManager.init();

        String token = Config.getInstance().getString("main.token");

        List<?> configPrefixes = Config.getInstance().getList("main.prefix");
        PREFIXES = configPrefixes.stream()
                .map(Object::toString)
                .toArray(String[]::new);

        try (Connection conn = DatabaseManager.getConnection()) {
            log.info("✅ База данных PostgreSQL подключена!");

            // Создание таблиц
            UserTable.createTable();
            BankTable.createTable();
            PrivacyTable.createTable();
            StatisticTable.createTable();

        } catch (SQLException e) {
            log.error("❌ Не удалось подключиться к базе данных: ");

            throw new RuntimeException();
        }

        Manager manager = new Manager();
        ComponentManager componentManager = new ComponentManager(manager.getCommands());
        JDA jda = JDABuilder.createDefault(token)
                // Cache & Intents
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .enableIntents(GatewayIntent.GUILD_MEMBERS)
                .enableIntents(GatewayIntent.GUILD_PRESENCES)
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .setChunkingFilter(ChunkingFilter.ALL)
                .enableCache(CacheFlag.ONLINE_STATUS)
                .setActivity(Activity.streaming("100 фактов о фембоях", "https://www.youtube.com/watch?v=o97WByHtOZM"))

                // Listeners
                .addEventListeners(new Main())
                .addEventListeners(new NameSave())

                // Ping
                .addEventListeners(new PrefixPing(), new SlashPing())

                .addEventListeners(new MessageCounter())

                // Managers
                .addEventListeners(manager)
                .addEventListeners(componentManager)
                .build();

        jda.awaitReady();
        slashCmdInfo.registerCommands(jda);

        Config.getInstance().close();
    }

    @Override
    public void onEvent(@NotNull GenericEvent event) {
        if (event instanceof ReadyEvent) {
            var jdaVersion = JDAInfo.VERSION;
            var jdaGithub = JDAInfo.GITHUB;
            var jdaShardTotal = JDA.ShardInfo.SINGLE.getShardTotal();
            var botName = event.getJDA().getSelfUser().getAsTag();
            var botId = event.getJDA().getSelfUser().getId();
            var serversCount = ((ReadyEvent) event).getGuildTotalCount();
            var userCount = event.getJDA().getUserCache().stream().count();

            log.info("""
                    \nИнформация JDA:
                    🐾 Версия: {}
                    🐾 Гитхаб: {}
                    Информация бота:
                    ℹ️ Префиксы: {}
                    💾 Версия: {}
                    📎 Гитхаб: https://github.com/KiKaaad/smllyBotDiscordJDA
                    🌚 Работает на боте: {} | 🆔 ID: {}
                    🔗 Шардов всего (1 шард = 0 - 2.500 серверов): {}
                    🌃 Серверов: {}
                    💀 Пользователей: {}
                    ✅ Успешно запущено
                    """, jdaVersion, jdaGithub, String.join("][", PREFIXES), VERSION, botName, botId, jdaShardTotal, serversCount, userCount);
        }
    }

}
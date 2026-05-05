package com.kika.smllybot;

import com.kika.smllybot.database.postgresql.DatabaseManager;
import com.kika.smllybot.database.postgresql.user.UserTable;
import com.kika.smllybot.modules.ping.PrefixPing;
import com.kika.smllybot.modules.ping.SlashPing;
import com.kika.smllybot.modules.user.AboutMe;
import com.kika.smllybot.modules.user.GlobalProfile;
import com.kika.smllybot.modules.user.ui.GlobalProfileModal;
import com.kika.smllybot.modules.user.Meow;
import com.kika.smllybot.utils.localization.I18n;
import com.kika.smllybot.utils.localization.I18nRequest;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.jetbrains.annotations.NotNull;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;

import static com.kika.smllybot.utils.formatting.Colors.GREEN;
import static com.kika.smllybot.utils.formatting.Colors.RED;
import static com.kika.smllybot.utils.formatting.Formatting.BOLD;

public class Main implements EventListener {

    public static final String[] prefixes = {"JDA!", "JAVA!"};

    public static void main(String[] args) throws InterruptedException {
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));

        try (Connection conn = DatabaseManager.getConnection()) {
            var logReq = new I18nRequest("ru", "logger", "Database", "database.connect.success");

            System.out.println(BOLD+GREEN + I18n.get(logReq));
            UserTable.createTable();
        } catch (SQLException e) {
            var logReq = new I18nRequest("ru", "logger", "Database", "database.connect.failure");

            System.err.printf(BOLD+RED + I18n.get(logReq), e.getMessage());
            throw new RuntimeException();
        }

        Dotenv dotenv = Dotenv.load();
        String token = dotenv.get("TOKEN");

        JDA jda = JDABuilder.createDefault(token)
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .enableIntents(GatewayIntent.GUILD_MEMBERS)
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .setChunkingFilter(ChunkingFilter.ALL)
                .setActivity(Activity.streaming("100 фактов о фембоях", "https://youtube.com/watch?v=o97WByHtOZM"))
                .addEventListeners(new Main())
                // Pinger
                .addEventListeners(new PrefixPing())
                .addEventListeners(new SlashPing())
                // User
                .addEventListeners(new GlobalProfile())
                .addEventListeners(new AboutMe())
                .addEventListeners(new GlobalProfileModal())
                .addEventListeners(new Meow())
                .build();

        jda.awaitReady();
        slashCmdInfo.registerCommands(jda);
    }

    @Override
    public void onEvent(@NotNull GenericEvent event) {
        if (event instanceof ReadyEvent) {
            var logReq = new I18nRequest("ru", "logger", "StartBot", "start.success");

            System.out.printf(BOLD+GREEN + I18n.get(logReq) + "\n\n");
        }
    }

}
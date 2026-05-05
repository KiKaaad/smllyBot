package com.kika.smllybot.modules.ping;

import net.dv8tion.jda.api.JDA;
import java.util.function.Consumer;

public class PingService {
    public static void fetchPing(JDA jda, Consumer<PingData> callback) {

        jda.getRestPing().queue(restPing -> {

            PingData data = new PingData(restPing, jda.getGatewayPing());

            callback.accept(data);
        });
    }
}

package com.kika.smllybot.other;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

public class slashCmdInfo {

    public static void registerCommands(JDA jda) {

        jda.updateCommands().addCommands(
                Commands.slash("ping", "проверить скорость подключение бота к API дискорда"),
                Commands.slash("farm", "зафармить ирис-коинов")
        ).queue();
    }
}
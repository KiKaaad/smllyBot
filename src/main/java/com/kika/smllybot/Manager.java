package com.kika.smllybot;

import com.kika.smllybot.modules.economy.Bag;
import com.kika.smllybot.modules.economy.Dice;
import com.kika.smllybot.modules.economy.Farm;
import com.kika.smllybot.modules.helper.GlobalHelp;
import com.kika.smllybot.modules.statistic.Statistic;
import com.kika.smllybot.modules.tops.FarmTop;
import com.kika.smllybot.modules.tops.ui.GlobalTopInteraction;
import com.kika.smllybot.modules.user.GlobalProfile;
import com.kika.smllybot.modules.user.Motto;
import com.kika.smllybot.modules.user.ui.GlobalProfileModal;
import com.kika.smllybot.modules.user.ui.GlobalProfilePrivate;
import com.kika.smllybot.other.BaseCmd;
import com.kika.smllybot.utils.PrefixUtil;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.HashMap;
import java.util.Map;

public class Manager extends ListenerAdapter {

    private final Map<String, BaseCmd> commands = new HashMap<>();

    public Manager() {
        // Экономика
        reg(new FarmTop());
        reg(new GlobalTopInteraction());
        reg(new Farm());
        reg(new Bag());
        reg(new Dice());

        // Анкета
        reg(new GlobalProfile());
        reg(new GlobalProfileModal());
        reg(new Motto());
        reg(new GlobalProfilePrivate());

        // Другое
        reg(new GlobalHelp());
        reg(new Statistic());
    }

    private void reg(BaseCmd cmd) {
        for (String alias : cmd.getAliases()) {
            commands.put(alias.toLowerCase(), cmd);
        }
    }

    public Map<String, BaseCmd> getCommands() {
        return commands;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;

        String commandBody = PrefixUtil.getCommandBody(event.getMessage().getContentRaw(), Main.PREFIXES);
        if (commandBody == null || commandBody.isEmpty()) return;

        String[] parts = commandBody.split("\\s+", 2);
        String commandName = parts[0].toLowerCase();

        // Возвращает команду без префикса
        BaseCmd cmd = commands.get(commandName);
        if (cmd != null) {
            String args = (parts.length > 1) ? parts[1] : "";

            cmd.execute(event, args);
        }
    }

}


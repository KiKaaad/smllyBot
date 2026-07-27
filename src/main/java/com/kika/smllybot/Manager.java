package com.kika.smllybot;

import com.kika.smllybot.modules.economy.Bag;
import com.kika.smllybot.modules.economy.Dice;
import com.kika.smllybot.modules.economy.Farm;
import com.kika.smllybot.modules.fun.*;
import com.kika.smllybot.modules.helper.GlobalHelp;
import com.kika.smllybot.modules.privacy.Privacy;
import com.kika.smllybot.modules.privacy.PrivacyInteraction;
import com.kika.smllybot.modules.statistic.GuildStatistic;
import com.kika.smllybot.modules.statistic.Statistic;
import com.kika.smllybot.modules.tops.Global.GlobalTop;
import com.kika.smllybot.modules.tops.Global.ui.GlobalTopInteraction;
import com.kika.smllybot.modules.user.Global.GlobalProfile;
import com.kika.smllybot.modules.user.Global.Motto;
import com.kika.smllybot.modules.user.Global.ui.GlobalProfileModal;
import com.kika.smllybot.modules.user.Global.ui.GlobalProfilePrivate;
import com.kika.smllybot.modules.user.Local.Profile;
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
        reg(new GlobalTop());
        reg(new GlobalTopInteraction());
        reg(new Farm());
        reg(new Bag());
        reg(new Dice());

        // Статистика
        reg(new Statistic());
        reg(new GuildStatistic());

        // Анкета (глобальная анкета)
        reg(new GlobalProfile());
        reg(new GlobalProfileModal());
        reg(new Motto());
        reg(new GlobalProfilePrivate());

        // Профиль (локальная анкета)
        reg(new Profile());

        // Интерактивные команды
        reg(new Hug(), new Bite(), new Burn(), new Cuddle(), new Five(), new Fuck(), new Five(), new Fuckin(),
                new Furryfication(), new GigaHit(), new Hit(), new Hold(), new Kick(), new Kill(), new Kiss(),
                new Lick(), new Pat(), new Press(), new Shoot(), new Slap(), new SlapBack(), new Spank(),
                new Tickle(), new Tie(), new Suck(), new Inseminate());

        // Другое
        reg(new GlobalHelp());
        reg(new Privacy());
        reg(new PrivacyInteraction());
    }

    private void reg(BaseCmd... cmds) {
        for (BaseCmd cmd : cmds) {
            for (String alias : cmd.getAliases()) {
                commands.put(alias.toLowerCase(), cmd);
            }
        }
    }

    public Map<String, BaseCmd> getCommands() {
        return commands;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;

        String command = event.getMessage().getContentRaw();
        String commandBody = PrefixUtil.getCommandBody(command, Main.PREFIXES);
        if (commandBody == null || commandBody.isEmpty()) return;

        String searchCommand = commandBody.trim().toLowerCase();
        BaseCmd cmd = null;
        String commandName = "";

        while (!searchCommand.isEmpty()) {
            if (commands.containsKey(searchCommand)) {
                cmd = commands.get(searchCommand);
                commandName = searchCommand;
                break;
            }

            int lastSpace = -1;
            for (int i = searchCommand.length() - 1; i >= 0; i--) {
                if (Character.isWhitespace(searchCommand.charAt(i))) {
                    lastSpace = i;
                    break;
                }
            }

            if (lastSpace == -1) break;
            searchCommand = searchCommand.substring(0, lastSpace);
        }

        if (cmd != null) {
            String args = commandBody.substring(commandName.length()).trim();
            cmd.execute(event, command, args);
        }
    }

}
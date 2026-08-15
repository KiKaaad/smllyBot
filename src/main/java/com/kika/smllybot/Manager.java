package com.kika.smllybot;

import com.kika.smllybot.handlers.ButtonHandler;
import com.kika.smllybot.handlers.ModalHandler;
import com.kika.smllybot.modules.economy.Bag;
import com.kika.smllybot.modules.economy.Dice;
import com.kika.smllybot.modules.economy.Farm;
import com.kika.smllybot.modules.fun.*;
import com.kika.smllybot.modules.helper.GlobalHelp;
import com.kika.smllybot.modules.privacy.Privacy;
import com.kika.smllybot.modules.privacy.PrivacyInteraction;
import com.kika.smllybot.modules.guild.GuildInfo;
import com.kika.smllybot.modules.statistic.StatisticBot;
import com.kika.smllybot.modules.tops.Global.GlobalTop;
import com.kika.smllybot.modules.tops.Global.ui.GlobalTopInteraction;
import com.kika.smllybot.modules.user.Global.GlobalProfile;
import com.kika.smllybot.modules.user.Global.Motto;
import com.kika.smllybot.modules.user.Global.ui.GlobalProfileModal;
import com.kika.smllybot.modules.user.Global.ui.GlobalProfilePrivate;
import com.kika.smllybot.modules.user.Local.AboutMe;
import com.kika.smllybot.modules.user.Local.Citizenship;
import com.kika.smllybot.modules.user.Local.Profile;
import com.kika.smllybot.other.BaseCmd;
import com.kika.smllybot.utils.PrefixUtil;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.HashMap;
import java.util.Map;

// !!! Регистрация кнопок ведется с учетом названия одного из алиасов
// Условно aboutMe:back:123456789 - Первым обязательно идет алиас aboutme
public class Manager extends ListenerAdapter {

    private final Map<String, BaseCmd> commands = new HashMap<>();
    private final Map<String, ButtonHandler> button = new HashMap<>();
    private final Map<String, ModalHandler> modal = new HashMap<>();

    public Manager() {
        // Экономика
        reg(new GlobalTop());
        reg(new GlobalTopInteraction());
        reg(new Farm());
        reg(new Bag());
        reg(new Dice());

        // Статистика
        reg(new StatisticBot());
        reg(new GuildInfo());

        // Анкета (глобальная анкета)
        reg(new GlobalProfile());
        reg(new GlobalProfileModal());
        reg(new Motto());
        reg(new GlobalProfilePrivate());

        // Профиль (локальная анкета)
        reg(new Profile());
        reg(new AboutMe());
        reg(new Citizenship());

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
            // Регистрация команд
            for (String alias : cmd.getAliases()) {
                commands.put(alias.toLowerCase(), cmd);
            }
            // Регистрация кнопочек
            if (cmd instanceof ButtonHandler buttonHandler) {
                button.put(buttonHandler.getButtonPrefix().toLowerCase(), buttonHandler);
            }
            // Регистрация модальных окон
            if (cmd instanceof ModalHandler modalHandler) {
                modal.put(modalHandler.getModalPrefix().toLowerCase(), modalHandler);
            }
        }
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        String[] parts = event.getComponentId().split(":");
        if (parts.length == 0) return;

        String prefix = parts[0].toLowerCase();

        ButtonHandler handler = button.get(prefix);

        if (handler != null) {
            handler.onButton(event, parts);
        }
    }

    @Override
    public void onModalInteraction(ModalInteractionEvent event) {
        String[] parts = event.getModalId().split(":");

        if (parts.length == 0) return;

        String prefix = parts[0].toLowerCase();

        ModalHandler handler = modal.get(prefix);

        if (handler != null) handler.onModal(event, parts);
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
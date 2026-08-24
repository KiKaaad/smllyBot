package com.kika.smllybot;

import com.kika.smllybot.annotations.ButtonPrefix;
import com.kika.smllybot.annotations.ModalPrefix;
import com.kika.smllybot.annotations.RegisteredButton;
import com.kika.smllybot.annotations.RegisteredModal;
import com.kika.smllybot.modules.economy.Bag;
import com.kika.smllybot.modules.economy.Dice;
import com.kika.smllybot.modules.economy.Farm;
import com.kika.smllybot.modules.fun.*;
import com.kika.smllybot.modules.guild.GuildInfo;
import com.kika.smllybot.modules.helper.GlobalHelp;
import com.kika.smllybot.modules.privacy.Privacy;
import com.kika.smllybot.modules.privacy.PrivacyInteraction;
import com.kika.smllybot.modules.statistic.StatisticBot;
import com.kika.smllybot.modules.tops.Global.GlobalTop;
import com.kika.smllybot.modules.user.Global.GlobalProfile;
import com.kika.smllybot.modules.user.Global.Motto;
import com.kika.smllybot.modules.user.Global.ui.GlobalProfileModal;
import com.kika.smllybot.modules.user.Global.ui.GlobalProfilePrivate;
import com.kika.smllybot.modules.user.Local.AboutMe;
import com.kika.smllybot.modules.user.Local.Citizenship;
import com.kika.smllybot.modules.user.Local.Profile;
import com.kika.smllybot.modules.user.Local.ui.ProfileModal;
import com.kika.smllybot.other.BaseCmd;
import com.kika.smllybot.utils.PrefixUtil;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class Manager extends ListenerAdapter {

    private static final Logger log = LoggerFactory.getLogger(Manager.class);
    private final Map<String, BaseCmd> commands = new HashMap<>();
    private final Map<String, RegisteredButton> button = new HashMap<>();
    private final Map<String, RegisteredModal> modal = new HashMap<>();

    public Manager() {
        // Экономика
        reg(new GlobalTop());
        reg(new Farm());
        reg(new Bag());
        reg(new Dice());
        log.info("✅ Модуль экономики загружен");

        // Статистика
        reg(new StatisticBot());
        reg(new GuildInfo());
        log.info("✅ Модуль статистики загружен");

        // Анкета (глобальная анкета)
        reg(new GlobalProfile());
        reg(new GlobalProfileModal());
        reg(new Motto());
        reg(new GlobalProfilePrivate());
        log.info("✅ Модуль глобальных профилей загружен");

        // Профиль (локальная анкета)
        reg(new Profile());
        reg(new ProfileModal());
        reg(new AboutMe());
        reg(new Citizenship());
        log.info("✅ Модуль локальных профилей загружен");

        // Интерактивные команды
        reg(new Hug(), new Bite(), new Burn(), new Cuddle(), new Five(), new Fuck(), new Five(), new Fuckin(),
                new Furryfication(), new GigaHit(), new Hit(), new Hold(), new Kick(), new Kill(), new Kiss(),
                new Lick(), new Pat(), new Press(), new Shoot(), new Slap(), new SlapBack(), new Spank(),
                new Tickle(), new Tie(), new Suck(), new Inseminate());
        log.info("✅ Интерактивные команды загружены");

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
            for (Method method : cmd.getClass().getDeclaredMethods()) {
                if (method.isAnnotationPresent(ButtonPrefix.class)) {
                    ButtonPrefix annotation = method.getAnnotation(ButtonPrefix.class);
                    String prefix = annotation.prefix().toLowerCase();
                    log.info("📌 Зарегистрирована кнопка '{}' -> Метод: {} (параметров: {})",
                            prefix, method.toGenericString(), method.getParameterCount());
                    method.setAccessible(true);

                    button.put(prefix, new RegisteredButton(cmd, method));
                }
            }
            // Регистрация модальных окон
            for (Method method : cmd.getClass().getDeclaredMethods()) {
                if (method.isAnnotationPresent(ModalPrefix.class)) {
                    ModalPrefix annotation = method.getAnnotation(ModalPrefix.class);
                    String prefix = annotation.prefix().toLowerCase();
                    log.debug("🪛 Получен префикс модального окна: " + prefix);
                    method.setAccessible(true);

                    modal.put(prefix, new RegisteredModal(cmd, method));
                }
            }
        }
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        String[] parts = event.getComponentId().split(":");
        if (parts.length == 0) return;

        String prefix = parts[0].toLowerCase();

        log.debug("🪛 Raw button id: {} | Ожидаемый префикс: {}", event.getComponentId(), prefix);
        log.debug("🪛 Доступные кнопки в памяти: {}", button.keySet());

        RegisteredButton handler = button.get(prefix);

        if (handler != null) {
            handler.invoke(event, parts);
        }
    }

    @Override
    public void onModalInteraction(ModalInteractionEvent event) {
        String[] parts = event.getModalId().split(":");

        if (parts.length == 0) return;

        String prefix = parts[0].toLowerCase();

        RegisteredModal handler = modal.get(prefix);

        if (handler != null) {
            handler.invoke(event, parts);
        }
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
            String arg = commandBody.substring(commandName.length()).stripLeading().trim();
            cmd.execute(event, command, arg);
        }
    }
}
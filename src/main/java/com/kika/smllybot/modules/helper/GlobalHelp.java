package com.kika.smllybot.modules.helper;

import com.kika.smllybot.other.BaseCmd;
import com.kika.smllybot.modules.helper.ui.GlobalHelpUI;
import net.dv8tion.jda.api.components.container.Container;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Set;

public class GlobalHelp extends BaseCmd {

    public GlobalHelp() {
        super(Set.of("хелпа", "хелп", "хелпер"));
    }

    @Override
    public void execute(MessageReceivedEvent event, String args) {

        if (args.isEmpty()) {
            Container response = GlobalHelpUI.defaultHelp();

            event.getChannel().sendMessageComponents(response)
                    .useComponentsV2(true)
                    .queue();
            return;
        }

        String[] parts = args.trim().split("\\s+");
        String subCommand = parts[0].toLowerCase();

        switch (subCommand) {
            case "гтоп" -> {
                Container response = GlobalHelpUI.gtop();

                event.getChannel().sendMessageComponents(response)
                        .useComponentsV2(true)
                        .queue();
            }
            case "анкета" -> {
                Container response = GlobalHelpUI.globalProfile();

                event.getChannel().sendMessageComponents(response)
                        .useComponentsV2(true)
                        .queue();
            }
            case "статбот" -> {
                Container response = GlobalHelpUI.statBot();

                event.getChannel().sendMessageComponents(response)
                        .useComponentsV2(true)
                        .queue();
            }
            default -> System.out.println("?? subCommand GlobalHelp");
        }

    }
}
// TODO: Доделать помощь по боту
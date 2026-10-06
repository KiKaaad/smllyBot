package com.kika.smllybot.modules.fun;

import com.kika.smllybot.modules.fun.ui.InfaUI;
import com.kika.smllybot.other.BaseCmd;
import net.dv8tion.jda.api.components.container.Container;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Set;

public class Infa extends BaseCmd {

    public Infa() {
        super(Set.of("инфа", "инфо"));
    }

    @Override
    public Container execute(MessageReceivedEvent event, String raw, String args) {

        var response = InfaUI.build(event.getAuthor().getEffectiveName());

        event.getMessage()
                .replyComponents(response)
                .useComponentsV2(true)
                .queue();

        return null;
    }

}

package com.kika.smllybot.modules.fun;

import com.kika.smllybot.modules.fun.ui.SoftUI;
import com.kika.smllybot.other.BaseCmd;
import net.dv8tion.jda.api.components.container.Container;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Set;

public abstract class BaseFunCmd extends BaseCmd {

    private final String emoji;
    private final String action;

    public BaseFunCmd(Set<String> aliases, String emoji, String action) {
        super(aliases);
        this.emoji = emoji;
        this.action = action;
    }

    @Override
    public Container execute(MessageReceivedEvent event, String raw, String args) {
        if (event.getMessage().getReferencedMessage() == null) {
            return null;
        }
        long author = event.getAuthor().getIdLong();

        long reply = event.getMessage().getReferencedMessage().getAuthor().getIdLong();

        String[] matches = raw.split("\\n", 2);
        String replica = null;
        if (matches.length > 1) replica = matches[1];

        String[] matchesAfterText = raw.split("\\h+", 2);
        String afterText = "";
        if (matchesAfterText.length > 1) afterText = matchesAfterText[1].trim();

        FunContext funContext = new FunContext(emoji, action, author, reply, afterText, replica);

        var response = SoftUI.buildSoftUI(funContext);

        event.getMessage().replyComponents(response)
                .useComponentsV2(true)
                .queue();

        return null;
    }

}

package com.kika.smllybot.other;

import net.dv8tion.jda.api.components.container.Container;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Set;

public abstract class BaseCmd {
    private final Set<String> aliases;

    protected BaseCmd(Set<String> aliases) { this.aliases = aliases; }

    public Set<String> getAliases() { return aliases; }

    public Container execute(MessageReceivedEvent event, String args) {
        return null;
    }
}


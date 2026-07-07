package com.kika.smllybot.other;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Set;

public abstract class BaseCmd {
    private final Set<String> aliases;

    protected BaseCmd(Set<String> aliases) { this.aliases = aliases; }

    public Set<String> getAliases() { return aliases; }

    public void execute(MessageReceivedEvent event, String args) {};
}


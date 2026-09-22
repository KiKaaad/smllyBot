package com.kika.smllybot.modules.testing;

import com.kika.smllybot.handler.ErrorThrow;
import com.kika.smllybot.other.BaseCmd;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.components.container.Container;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Set;

public class TestErrors extends BaseCmd {

    public TestErrors() {
        super(Set.of("ошибка", "error"));
    }

    @Override
    public Container execute(MessageReceivedEvent event, String raw, String args) {
        String[] arg = raw.trim().split("\\s+");

        if (arg.length < 2) return null;
        String argument = arg[1];

        switch (argument.toLowerCase()) {
            case "noaccesspermissions" -> ErrorThrow.noAccessPermission(event, Permission.BYPASS_SLOWMODE);
            case "usernotfound" -> ErrorThrow.userNotFound(event, "kef123");
            case "timeoverhead" -> ErrorThrow.timeOverhead(event);
        }

        return null;
    }

}

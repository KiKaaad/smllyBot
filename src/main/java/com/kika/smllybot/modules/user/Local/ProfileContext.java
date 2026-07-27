package com.kika.smllybot.modules.user.Local;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public record ProfileContext(
        MessageReceivedEvent event,
        long idViewer,
        User idTarget
) {}

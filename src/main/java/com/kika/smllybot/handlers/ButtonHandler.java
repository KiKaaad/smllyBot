package com.kika.smllybot.handlers;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

@Deprecated
public interface ButtonHandler {
    void onButton(ButtonInteractionEvent event, String[] args);
    String getButtonPrefix();
}

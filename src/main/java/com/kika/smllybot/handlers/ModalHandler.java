package com.kika.smllybot.handlers;

import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;

public interface ModalHandler {
    void onModal(ModalInteractionEvent event, String[] args);
}

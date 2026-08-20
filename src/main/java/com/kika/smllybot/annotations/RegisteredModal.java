package com.kika.smllybot.annotations;

import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

public record RegisteredModal(Object instance, Method method) {

    private static final Logger log = LoggerFactory.getLogger(RegisteredModal.class);

    public void invoke(ModalInteractionEvent event, String[] args) {
        try {
            method.invoke(instance, event, args);
        } catch (Exception e) {
            log.error("❌ Возникла ошибка при регистрации модалки: ", e);
        }
    }

}

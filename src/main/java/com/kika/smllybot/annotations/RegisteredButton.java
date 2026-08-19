package com.kika.smllybot.annotations;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

public record RegisteredButton(Object instance, Method method) {

    private static final Logger log = LoggerFactory.getLogger(RegisteredButton.class);

    public void invoke(ButtonInteractionEvent event, String[] args) {
        try {
            method.invoke(instance, event, args);
        } catch (Exception e) {
            log.error("❌ Возникла ошибка при регистрации кнопки: ", e);
        }
    }

}

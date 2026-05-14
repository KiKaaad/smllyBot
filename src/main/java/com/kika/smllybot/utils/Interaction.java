package com.kika.smllybot.utils;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

import java.util.concurrent.ThreadLocalRandom;

public class Interaction {


    //  Проверяет, является ли пользователь владельцем кнопки
    //  Ожидается формат: action::subAction::ownerId


    private enum DenyReason {
        MISTER_FISH("❌ Но но но мистер фиш"),
        HANDS_OFF("❌ Руки прочь!"),
        GANDALF("❌ Мне кажется тебе еще рано"),
        FURRY("❌ Завтра ночью в 3:21 к тебе приедут фурри фембойчики"),
        SUCK("❌ Сосал?");

        private final String text;
        DenyReason(String text) { this.text = text; }

        public static String getRandom() {
            DenyReason[] reasons = values();
            return reasons[ThreadLocalRandom.current().nextInt(reasons.length)].text;
        }
    }

    public static boolean checkOwner(ButtonInteractionEvent event) {
        String[] parts = event.getComponentId().split("::");

        if (parts.length >= 3) {
            String ownerId = parts[2];

            if (!event.getUser().getId().equals(ownerId)) {
                event.reply(DenyReason.getRandom())
                        .setEphemeral(true)
                        .queue();
                return false;
            }
        }
        return true;
    }
}

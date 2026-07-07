package com.kika.smllybot.utils;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadLocalRandom;

public class Interaction {

    //  Ожидается формат: action:subAction:ownerId

    private enum DenyReason {
        MISTER_FISH("\\❌ Но но но мистер фиш"),
        HANDS_OFF("\\❌ Руки прочь!"),
        GANDALF("\\❌ Мне кажется тебе еще рано"),
        FURRY("\\❌ Завтра ночью в 3:21 к тебе приедут фурри фембойчики"),
        SUCK("\\❌ Сосал?"),
        LATER("\\❌ Завтра в 3"),
        NO("\\❌ Не");

        private final String text;
        DenyReason(String text) { this.text = text; }

        public static String getRandom() {
            DenyReason[] reasons = values();
            return reasons[ThreadLocalRandom.current().nextInt(reasons.length)].text;
        }
    }

    // Последнее значение массива всегда должен быть айди
    // button:subCommand:subSubCommand:1234567890
    public static boolean checkOwner(ButtonInteractionEvent event, @NotNull String[] parts) {
        String ownerId = parts[parts.length - 1];

        if (!event.getUser().getId().equals(ownerId)) {
            event.reply(DenyReason.getRandom())
                    .setEphemeral(true)
                    .queue();
            return false;
        }
        return true;
    }
}

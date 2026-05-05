package com.kika.smllybot.modules.user;

import com.kika.smllybot.utils.Interaction;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Meow extends ListenerAdapter {

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        if (!Interaction.checkOwner(event)) return;
        if (event.getComponentId().startsWith("bag::meow")) {

            event.reply("Мяу! 🐾").queue();

        }
    }
}

package com.kika.smllybot.other;

import com.kika.smllybot.handlers.ButtonHandler;
import com.kika.smllybot.handlers.ModalHandler;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class ComponentManager extends ListenerAdapter {

    private final Map<String, BaseCmd> commands;

    public ComponentManager(Map<String, BaseCmd> commands) {
        this.commands = commands;
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        String[] parts = event.getComponentId().split(":");
        String prefix = parts[0].toLowerCase();

        BaseCmd cmd = commands.get(prefix);

        if (cmd instanceof ButtonHandler buttonHandler) {
            buttonHandler.onButton(event, parts);
        }
    }

    @Override
    public void onModalInteraction(@NotNull ModalInteractionEvent event) {
        String[] parts = event.getModalId().split(":");
        String prefix = parts[0].toLowerCase();

        BaseCmd cmd = commands.get(prefix);

        if (cmd instanceof ModalHandler modalHandler) {
            modalHandler.onModal(event, parts);
        }
    }
}

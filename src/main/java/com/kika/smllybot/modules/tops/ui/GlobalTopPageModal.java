package com.kika.smllybot.modules.tops.ui;

import com.kika.smllybot.handlers.ButtonHandler;
import com.kika.smllybot.utils.Interaction;
import net.dv8tion.jda.api.components.label.Label;
import net.dv8tion.jda.api.components.textinput.TextInput;
import net.dv8tion.jda.api.components.textinput.TextInputStyle;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.modals.Modal;
import org.jetbrains.annotations.NotNull;

// TODO: Возможность выбрать страницу в топе по нажатию на индикатор страниц
public class GlobalTopPageModal implements ButtonHandler {

    @Override
    public void onButton(@NotNull ButtonInteractionEvent event, String[] parts) {

        if (!Interaction.checkOwner(event, parts)) return;

        if (parts.length > 1 && parts[1].equals("modal")) {

            String ownerId = parts.length > 2 ? parts[2] : "";
            TextInput mottoInput = TextInput.create("motto_field", TextInputStyle.PARAGRAPH)
                    .setPlaceholder("Какой же я красавчик... 😎")
                    .setMaxLength(255)
                    .setRequired(false)
                    .build();

            Modal modal = Modal.create("profile:submit:" + ownerId, "🗿 Редактировать профиль")
                    .addComponents(
                            Label.of("Страница", mottoInput)
                    )
                    .build();

            event.replyModal(modal).queue();
        }
    }

}

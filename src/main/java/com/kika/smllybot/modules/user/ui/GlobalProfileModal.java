package com.kika.smllybot.modules.user.ui;

import com.kika.smllybot.database.DatabaseService;
import com.kika.smllybot.database.UsersData;
import com.kika.smllybot.database.postgresql.user.UserTable;
import com.kika.smllybot.modules.user.GlobalProfileContext;
import com.kika.smllybot.utils.Interaction;
import net.dv8tion.jda.api.components.textinput.TextInput;
import net.dv8tion.jda.api.components.textinput.TextInputStyle;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.modals.Modal;
import org.jetbrains.annotations.NotNull;

// ———— ———— ———— ———— ———— ———— ———— ———— ————
// Модальное окно редактирования профиля
// ———— ———— ———— ———— ———— ———— ———— ———— ————

public class GlobalProfileModal extends ListenerAdapter {

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        if (!Interaction.checkOwner(event)) return;
        if (event.getComponentId().startsWith("motto::modal")) {

            TextInput aboutMeInput = TextInput.create("motto::field", TextInputStyle.PARAGRAPH)
                    .setPlaceholder("Какой же я красавчик... 😎")
                    .setMaxLength(255)
                    .setRequired(false)
                    .build();

            Modal modal = Modal.create("motto::submit", "🗿 Редактировать профиль")
                    .addComponents(
                            net.dv8tion.jda.api.components.label.Label.of("🐾 Девиз", aboutMeInput)
                    )
                    .build();

            event.replyModal(modal).queue();
        }
    }

    @Override
    public void onModalInteraction(ModalInteractionEvent event) {
        if (event.getModalId().equals("motto::submit")) {
            var value = event.getValue("motto::field");
            if (value == null) return;

            String newAboutMe = value.getAsString();
            long discordId = event.getUser().getIdLong();
            String username = event.getUser().getName();

            UserTable.updateMotto(discordId, newAboutMe);

            UsersData data = DatabaseService.getFullData(discordId, username);

            GlobalProfileContext ctx = new GlobalProfileContext(
                    event.getUser(),
                    event.getUser(),
                    event.getMember(),
                    data
            );

            var updatedProfile = GlobalProfileUI.buildProfile(ctx);

            event.editComponents(updatedProfile)
                    .useComponentsV2(true)
                    .queue();
        }
    }
}

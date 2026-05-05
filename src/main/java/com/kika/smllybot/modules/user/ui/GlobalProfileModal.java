package com.kika.smllybot.modules.user.ui;

import com.kika.smllybot.database.postgresql.bank.Bank;
import com.kika.smllybot.database.postgresql.user.UserTable;
import com.kika.smllybot.utils.Interaction;
import net.dv8tion.jda.api.components.textinput.TextInput;
import net.dv8tion.jda.api.components.textinput.TextInputStyle;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.modals.Modal;

// ———— ———— ———— ———— ———— ———— ———— ———— ————
// Модальное окно редактирования профиля
// ———— ———— ———— ———— ———— ———— ———— ———— ————

public class GlobalProfileModal extends ListenerAdapter {

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        if (!Interaction.checkOwner(event)) return;
        if (event.getComponentId().startsWith("aboutMe::modal")) {

            TextInput aboutMeInput = TextInput.create("aboutMe::field", TextInputStyle.PARAGRAPH)
                    .setPlaceholder("Какой же я красавчик... 😎")
                    .setMinLength(1)
                    .setMaxLength(50)
                    .setRequired(true)
                    .build();

            Modal modal = Modal.create("aboutMe::submit", "🗿 Редактировать профиль")
                    .addComponents(
                            net.dv8tion.jda.api.components.label.Label.of("О себе", aboutMeInput)
                    )
                    .build();

            event.replyModal(modal).queue();
        }
    }

    @Override
    public void onModalInteraction(ModalInteractionEvent event) {
        if (event.getModalId().equals("aboutMe::submit")) {

            var value = event.getValue("aboutMe::field");
            if (value == null) return;

            String newAboutMe = value.getAsString();
            long discordId = event.getUser().getIdLong();

            UserTable.updateAboutMe(discordId, newAboutMe);

            var dbUser = UserTable.getOrCreateUser(discordId);

            var updatedProfile = GlobalProfileUI.buildProfile(event.getUser(), dbUser, event.getUser());

            event.editComponents(updatedProfile)
                    .useComponentsV2(true)
                    .queue();
        }
    }
}

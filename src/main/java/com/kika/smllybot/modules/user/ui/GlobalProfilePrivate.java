package com.kika.smllybot.modules.user.ui;

import com.kika.smllybot.database.sql.bank.BankTable;
import com.kika.smllybot.database.sql.bank.dto.BankAccount;
import com.kika.smllybot.database.sql.privacy.PrivacyTable;
import com.kika.smllybot.database.sql.privacy.dto.PrivacyAccount;
import com.kika.smllybot.database.sql.user.UserTable;
import com.kika.smllybot.database.sql.user.dto.UserAccount;
import com.kika.smllybot.handlers.ButtonHandler;
import com.kika.smllybot.handlers.ModalHandler;
import com.kika.smllybot.modules.user.GlobalProfileContext;
import com.kika.smllybot.other.BaseCmd;
import com.kika.smllybot.utils.Interaction;
import net.dv8tion.jda.api.components.label.Label;
import net.dv8tion.jda.api.components.radiogroup.RadioGroup;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.modals.ModalMapping;
import net.dv8tion.jda.api.modals.Modal;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class GlobalProfilePrivate extends BaseCmd implements ButtonHandler, ModalHandler {

    public GlobalProfilePrivate() { super(Set.of("private")); }

    @Override
    public void onButton(@NotNull ButtonInteractionEvent event, String[] parts) {

        if (!Interaction.checkOwner(event, parts)) return;

        if (parts.length > 1 && parts[1].equals("private")) {

            String ownerId = parts.length > 2 ? parts[2] : "";
            RadioGroup bagSettings = RadioGroup.create("BagSettings")
                    .addOption("Все", "false")
                    .addOption("Никто", "true")
                    .setSelectedValue("false")
                    .setRequired(true)
                    .build();
            RadioGroup activity = RadioGroup.create("ActivitySettings")
                    .addOption("Все", "false")
                    .addOption("Никто", "true")
                    .setSelectedValue("false")
                    .setRequired(true)
                    .build();
            RadioGroup activityTime = RadioGroup.create("LastActivitySettings")
                    .addOption("Все", "false")
                    .addOption("Никто", "true")
                    .setSelectedValue("false")
                    .setRequired(true)
                    .build();

            Modal modal = Modal.create("private:submit:" + ownerId, "🕶️ Настройки приватности")
                    .addComponents(
                            Label.of("Кто видит мой мешок?", bagSettings),
                            Label.of("Кто видит мою активность?", activity),
                            Label.of("Кто видит время моего последнего захода?", activityTime)
                    )
                    .build();

            event.replyModal(modal).queue();
        }
    }

    // TODO: Реализовать адекватную логику приватности
    @Override
    public void onModal(ModalInteractionEvent event, String[] parts) {
        if (parts.length > 1 && parts[1].equals("submit")) {
            long discordId = event.getUser().getIdLong();
            String username = event.getUser().getName();

            UserAccount user = UserTable.getOrCreateUser(discordId, username);

            ModalMapping bagMapping = event.getValue("BagSettings");
            PrivacyTable.updateBagPrivacy(user.internalId(), bagMapping.getAsString().equals("true"));

            ModalMapping activityMapping = event.getValue("ActivitySettings");
            PrivacyTable.updateActivityPrivacy(user.internalId(), activityMapping.getAsString().equals("true"));

            ModalMapping lastActivityMapping = event.getValue("LastActivitySettings");
            PrivacyTable.updateLastActivityPrivacy(user.internalId(), lastActivityMapping.getAsString().equals("true"));

            BankAccount bank = BankTable.getOrCreateBank(user.internalId(), username);
            PrivacyAccount privacy = PrivacyTable.getOrCreatePrivacy(user.internalId());

            GlobalProfileContext ctx = new GlobalProfileContext(
                    event.getUser(),
                    event.getUser(),
                    event.getMember(),
                    user,
                    bank,
                    privacy
            );

            var updatedProfile = GlobalProfileUI.buildProfile(ctx);

            event.editComponents(updatedProfile)
                    .useComponentsV2(true)
                    .queue();
        }
    }

}

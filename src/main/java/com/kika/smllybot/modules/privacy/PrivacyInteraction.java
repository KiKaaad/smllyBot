package com.kika.smllybot.modules.privacy;

import com.kika.smllybot.database.sql.privacy.PrivacyTable;
import com.kika.smllybot.database.sql.privacy.dto.PrivacyAccount;
import com.kika.smllybot.database.sql.user.UserTable;
import com.kika.smllybot.handlers.ButtonHandler;
import com.kika.smllybot.modules.privacy.ui.PrivacyUI;
import com.kika.smllybot.other.BaseCmd;
import com.kika.smllybot.utils.Interaction;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

import java.util.Set;

public class PrivacyInteraction extends BaseCmd implements ButtonHandler {

    public PrivacyInteraction() {
        super(Set.of("PrivacyInteraction"));
    }

    @Override
    public void onButton(ButtonInteractionEvent event, String[] parts) {

        if (!Interaction.checkOwner(event, parts)) return;

        long id = UserTable.getUserId(event.getUser().getIdLong());

        switch (parts[1]) {

            case "bag":
                if (parts[2].equals("on")) {
                    PrivacyTable.updateBagPrivacy(id, false);
                } else if (parts[2].equals("off")) PrivacyTable.updateBagPrivacy(id, true);
                break;

            case "activity":
                if (parts[2].equals("on")) {
                    PrivacyTable.updateActivityPrivacy(id, false);
                } else if (parts[2].equals("off")) PrivacyTable.updateActivityPrivacy(id, true);
                break;

            case "lastActivity":
                if (parts[2].equals("on")) {
                    PrivacyTable.updateLastActivityPrivacy(id, false);
                } else if (parts[2].equals("off")) PrivacyTable.updateLastActivityPrivacy(id, true);
                break;
        }

        PrivacyAccount privacyAccount = PrivacyTable.getOrCreatePrivacy(id);
        PrivacyContext privacy = new PrivacyContext(event.getUser().getIdLong(), privacyAccount);
        var response = PrivacyUI.buildPrivacyUI(privacy);

        event.editComponents(response)
                .useComponentsV2(true)
                .queue();
    }

}

package com.kika.smllybot.modules.privacy;

import com.kika.smllybot.database.sql.privacy.PrivacyTable;
import com.kika.smllybot.database.sql.privacy.dto.PrivacyAccount;
import com.kika.smllybot.database.sql.user.UserTable;
import com.kika.smllybot.modules.privacy.ui.PrivacyUI;
import com.kika.smllybot.other.BaseCmd;
import net.dv8tion.jda.api.components.container.Container;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Set;

public class Privacy extends BaseCmd {

    public Privacy() { super(Set.of("приватность", "privacy")); }

    @Override
    public Container execute(MessageReceivedEvent event, String raw, String args) {

        UserTable.getOrCreateUser(event.getAuthor().getIdLong(), event.getAuthor().getEffectiveName());
        long discordId = event.getAuthor().getIdLong();
        long id = UserTable.getUserId(discordId);

        PrivacyAccount privacy = PrivacyTable.getOrCreatePrivacy(id);
        PrivacyContext privacyContext = new PrivacyContext(discordId, privacy);

        var response = PrivacyUI.buildPrivacyUI(privacyContext);

        event.getChannel().sendMessageComponents(response)
                .useComponentsV2(true)
                .queue();

        return null;
    }

}

package com.kika.smllybot.modules.user;

import com.kika.smllybot.Main;
import com.kika.smllybot.database.sql.bank.BankTable;
import com.kika.smllybot.database.sql.bank.dto.BankAccount;
import com.kika.smllybot.database.sql.privacy.PrivacyTable;
import com.kika.smllybot.database.sql.privacy.dto.PrivacyAccount;
import com.kika.smllybot.database.sql.user.UserTable;
import com.kika.smllybot.database.sql.user.dto.UserAccount;
import com.kika.smllybot.handlers.ButtonHandler;
import com.kika.smllybot.modules.user.ui.GlobalProfileUI;
import com.kika.smllybot.modules.user.ui.MottoUI;
import com.kika.smllybot.other.BaseCmd;
import com.kika.smllybot.utils.Interaction;
import com.kika.smllybot.utils.PrefixUtil;
import net.dv8tion.jda.api.components.container.Container;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Set;

public class Motto extends BaseCmd implements ButtonHandler {

    public Motto() { super(Set.of("+девиз", "-девиз", "motto", "девиз")); }

    @Override
    public void execute(MessageReceivedEvent event, String args) {

        String rawMessage = event.getMessage().getContentRaw();
        String commandWord = PrefixUtil.getCommandBody(rawMessage, Main.PREFIXES).split("\\s+")[0].toLowerCase();

        char action = commandWord.charAt(0);

        long discordId = event.getAuthor().getIdLong();
        String username = event.getAuthor().getEffectiveName();

        // Удалить девиз
        if (action == '-') {
            UserTable.updateMotto(discordId, null);
            UserAccount dbUser = UserTable.getOrCreateUser(discordId, username);
            assert dbUser != null;
            Container response = MottoUI.buildMotto(
                    event.getAuthor(), dbUser, "❌ Описание удалено", false);

            event.getChannel().sendMessageComponents(response).useComponentsV2(true).queue();
            return;
        }

        // Показать девиз
        if (args.isEmpty()) {
            UserAccount dbUser = UserTable.getOrCreateUser(discordId, username);
            assert dbUser != null;
            Container response = MottoUI.buildMotto(
                    event.getAuthor(), dbUser, "Ваш текущий девиз", false);

            event.getChannel().sendMessageComponents(response).useComponentsV2(true).queue();
            return;
        }

        String aboutMeText = args.trim();

        UserTable.updateMotto(discordId, aboutMeText);
        response(event, "✅ Описание обновлено");
    }

    private void response(MessageReceivedEvent event, String title) {
        long userId = event.getAuthor().getIdLong();
        String username = event.getAuthor().getEffectiveName();
        UserAccount dbUser = UserTable.getOrCreateUser(userId, username);

        assert dbUser != null;
        Container response = MottoUI.buildMotto(event.getAuthor(), dbUser, title, true);

        event.getChannel().sendMessageComponents(response)
                .useComponentsV2(true)
                .queue();
    }

    @Override
    public void onButton(ButtonInteractionEvent event, String[] args) {
        if (!Interaction.checkOwner(event, args)) return;

        if (event.getComponentId().startsWith("motto:back:")) {
            User user = event.getUser();

            UserAccount userAccount = UserTable.getOrCreateUser(user.getIdLong(), user.getEffectiveName());
            BankAccount bank = BankTable.getOrCreateBank(userAccount.internalId(), user.getEffectiveName());
            PrivacyAccount privacy = PrivacyTable.getOrCreatePrivacy(userAccount.internalId());

            GlobalProfileContext ctx = new GlobalProfileContext(
                    user, user, event.getMember(), userAccount, bank, privacy);

            Container profile = GlobalProfileUI.buildProfile(ctx);

            event.editComponents(profile)
                    .useComponentsV2(true)
                    .queue();
        }
    }

}


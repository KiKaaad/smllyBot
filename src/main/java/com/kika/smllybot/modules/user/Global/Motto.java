package com.kika.smllybot.modules.user.Global;

import com.kika.smllybot.Main;
import com.kika.smllybot.annotations.ButtonPrefix;
import com.kika.smllybot.database.sql.bank.BankTable;
import com.kika.smllybot.database.sql.bank.dto.BankAccount;
import com.kika.smllybot.database.sql.privacy.PrivacyTable;
import com.kika.smllybot.database.sql.privacy.dto.PrivacyAccount;
import com.kika.smllybot.database.sql.users.UsersTable;
import com.kika.smllybot.database.sql.users.dto.UserAccount;
import com.kika.smllybot.modules.user.Global.ui.GlobalProfileUI;
import com.kika.smllybot.modules.user.Global.ui.MottoUI;
import com.kika.smllybot.other.BaseCmd;
import com.kika.smllybot.utils.Interaction;
import com.kika.smllybot.utils.PrefixUtil;
import net.dv8tion.jda.api.components.container.Container;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Set;

public class Motto extends BaseCmd {

    public Motto() { super(Set.of("+девиз", "-девиз", "motto", "девиз")); }

    @Override
    public Container execute(MessageReceivedEvent event, String raw, String args) {

        String command = PrefixUtil.getCommandBody(raw, Main.PREFIXES).split("\\s+")[0].toLowerCase();

        char action = command.charAt(0);

        long discordId = event.getAuthor().getIdLong();
        String username = event.getAuthor().getEffectiveName();

        // Удалить девиз
        if (action == '-') {
            UsersTable.setMotto(discordId, null);
            UserAccount dbUser = UsersTable.getOrCreateUser(discordId, username);
            Container response = MottoUI.buildMotto(
                    event.getAuthor(), dbUser, "\\❌ Описание удалено", false);

            event.getChannel().sendMessageComponents(response).useComponentsV2(true).queue();
            return response;
        }

        // Показать девиз
        if (args.isEmpty()) {
            UserAccount dbUser = UsersTable.getOrCreateUser(discordId, username);
            Container response = MottoUI.buildMotto(
                    event.getAuthor(), dbUser, "Ваш текущий девиз", false);

            event.getChannel().sendMessageComponents(response).useComponentsV2(true).queue();
            return response;
        }

        String[] parts = raw.split("\\s+");
        if (parts.length > 1) {
            String motto = parts[1];
            UsersTable.setMotto(discordId, motto);
            response(event, "\\✅ Описание обновлено");
        }

        return null;
    }

    private void response(MessageReceivedEvent event, String title) {
        long userId = event.getAuthor().getIdLong();
        String username = event.getAuthor().getEffectiveName();
        UserAccount dbUser = UsersTable.getOrCreateUser(userId, username);

        assert dbUser != null;
        Container response = MottoUI.buildMotto(event.getAuthor(), dbUser, title, true);

        event.getChannel().sendMessageComponents(response)
                .useComponentsV2(true)
                .queue();
    }

    @ButtonPrefix(prefix = "motto")
    public void onButton(ButtonInteractionEvent event, String[] args) {
        if (!Interaction.checkOwner(event, args)) return;

        if (event.getComponentId().startsWith("motto:back:")) {
            User user = event.getUser();

            UserAccount userAccount = UsersTable.getOrCreateUser(user.getIdLong(), user.getEffectiveName());
            BankAccount bank = BankTable.getOrCreateBank(userAccount.getId(), user.getEffectiveName());
            PrivacyAccount privacy = PrivacyTable.getOrCreatePrivacy(userAccount.getId());

            GlobalProfileContext ctx = new GlobalProfileContext(
                    user, user, event.getMember(), userAccount, bank, privacy);

            Container response = GlobalProfileUI.buildProfile(ctx);

            event.editComponents(response)
                    .useComponentsV2(true)
                    .queue();
        }
    }

}


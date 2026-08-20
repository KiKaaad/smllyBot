package com.kika.smllybot.modules.user.Local;

import com.kika.smllybot.Main;
import com.kika.smllybot.annotations.ButtonPrefix;
import com.kika.smllybot.database.sql.users.UsersTable;
import com.kika.smllybot.database.sql.users.dto.UserAccount;
import com.kika.smllybot.modules.user.Local.ui.CitizenshipUI;
import com.kika.smllybot.other.BaseCmd;
import com.kika.smllybot.utils.PrefixUtil;
import net.dv8tion.jda.api.components.container.Container;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Set;

public class Citizenship extends BaseCmd {

    public Citizenship() {
        super(Set.of("+гражданство", "-гражданство", "гражданство"));
    }

    @Override
    public Container execute(MessageReceivedEvent event, String raw, String args) {
        if (!event.isFromGuild()) return null;

        String command = PrefixUtil.getCommandBody(raw, Main.PREFIXES).split("\\s+")[0].toLowerCase();
        char action = command.charAt(0);

        Guild guild = event.getGuild();
        long guildId = event.getGuild().getIdLong();

        long discordId = event.getAuthor().getIdLong();
        String name = event.getAuthor().getEffectiveName();
        UserAccount user = UsersTable.getOrCreateUser(discordId, name);

        if (action == '-') {
            if (user.getCitizenship() == null) {
                var response = CitizenshipUI.buildError();

                event.getChannel().sendMessageComponents(response)
                        .useComponentsV2(true)
                        .queue();

                return null;
            }

            CitizenshipContext context = new CitizenshipContext(
                    "## \\❌ Вы уверены, что хотите удалить гражданство?", "yesminus",
                    event.getMember(), guild, user);

            var response = CitizenshipUI.buildDeleteCitizenship(context);

            event.getChannel().sendMessageComponents(response)
                    .useComponentsV2(true)
                    .queue();

            return response;
        }

        if (action == '+') {
            if (user.getCitizenship() != null) {
                CitizenshipContext context = new CitizenshipContext(
                        "## \\❌ У вас уже есть гражданство. Вы хотите его сменить?", "yesplus",
                        event.getMember(), guild, user);

                var response = CitizenshipUI.buildDeleteCitizenship(context);

                event.getChannel().sendMessageComponents(response)
                        .useComponentsV2(true)
                        .queue();

                return null;
            }
            UsersTable.setCitizenship(discordId, guildId);

            CitizenshipContext context = new CitizenshipContext("## \\🛂 Гражданство оформлено", "",
                    event.getMember(), guild, user);
            var response = CitizenshipUI.buildCitizenship(context);

            event.getChannel().sendMessageComponents(response)
                    .useComponentsV2(true)
                    .queue();

            return response;
        }

        if (args.isEmpty()) {
            CitizenshipContext context = new CitizenshipContext("## \\ℹ️ Текущее гражданство", "",
                    event.getMember(), guild, user);

            var response = CitizenshipUI.buildDefaultCitizenship(context);
            event.getChannel().sendMessageComponents(response).useComponentsV2(true).queue();

            return response;
        }

        return null;
    }

    @ButtonPrefix(prefix = "citizenship")
    public void onButton(ButtonInteractionEvent event, String[] args) {
        long discordId = event.getUser().getIdLong();

        String[] parts = event.getComponentId().split(":");

        if (parts[1].equals("yesminus")) {
            UsersTable.setCitizenship(discordId, null);

            var response = CitizenshipUI.deleteCitizenshipSuccess();

            event.editComponents(response).useComponentsV2(true).queue();
            return;
        }

        if (parts[1].equals("yesplus")) {
            UsersTable.setCitizenship(discordId, event.getGuild().getIdLong());
            UserAccount user = UsersTable.getOrCreateUser(discordId, event.getUser().getEffectiveName());

            CitizenshipContext context = new CitizenshipContext("## \\🛂 Новое гражданство оформлено", "",
                    event.getMember(), event.getGuild(), user);
            var response = CitizenshipUI.buildCitizenship(context);

            event.editComponents(response).useComponentsV2(true).queue();
        }
    }

}

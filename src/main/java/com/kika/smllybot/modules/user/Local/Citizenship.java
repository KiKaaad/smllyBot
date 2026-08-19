package com.kika.smllybot.modules.user.Local;

import com.kika.smllybot.Main;
import com.kika.smllybot.database.sql.users.UsersTable;
import com.kika.smllybot.database.sql.users.dto.UserAccount;
import com.kika.smllybot.modules.user.Local.ui.CitizenshipUI;
import com.kika.smllybot.other.BaseCmd;
import com.kika.smllybot.utils.PrefixUtil;
import net.dv8tion.jda.api.components.container.Container;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Set;

public class Citizenship extends BaseCmd {

    public Citizenship() {
        super(Set.of("+гражданство", "-гражданство", "гражданство"));
    }

    @Override
    public Container execute(MessageReceivedEvent event, String raw, String args) {

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

            CitizenshipContext context = new CitizenshipContext("",
                    event.getMember(), guild, user);

            var response = CitizenshipUI.buildDeleteCitizenship(context);

            event.getChannel().sendMessageComponents(response)
                    .useComponentsV2(true)
                    .queue();

            return response;
        }

        if (action == '+') {
            UsersTable.setCitizenship(discordId, guildId);

            CitizenshipContext context = new CitizenshipContext("## \\🛂 Гражданство оформлено",
                    event.getMember(), guild, user);
            var response = CitizenshipUI.buildCitizenship(context);

            event.getChannel().sendMessageComponents(response)
                    .useComponentsV2(true)
                    .queue();

            return response;
        }

        if (args.isEmpty()) {
            CitizenshipContext context = new CitizenshipContext("## \\ℹ️ Текущее гражданство",
                    event.getMember(), guild, user);

            var response = CitizenshipUI.buildDefaultCitizenship(context);
            event.getChannel().sendMessageComponents(response).useComponentsV2(true).queue();

            return response;
        }

        return null;
    }

}

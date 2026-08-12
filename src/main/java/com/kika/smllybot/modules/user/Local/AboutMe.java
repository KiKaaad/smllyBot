package com.kika.smllybot.modules.user.Local;

import com.kika.smllybot.Main;
import com.kika.smllybot.database.sql.bank.BankTable;
import com.kika.smllybot.database.sql.bank.dto.BankAccount;
import com.kika.smllybot.database.sql.profile.ProfileTable;
import com.kika.smllybot.database.sql.profile.dto.ProfileAccount;
import com.kika.smllybot.database.sql.statistic.StatisticTable;
import com.kika.smllybot.database.sql.statistic.dto.TotalStatisticUser;
import com.kika.smllybot.database.sql.users.UsersTable;
import com.kika.smllybot.database.sql.users.dto.UserAccount;
import com.kika.smllybot.handlers.ButtonHandler;
import com.kika.smllybot.modules.user.Local.ui.AboutMeUI;
import com.kika.smllybot.modules.user.Local.ui.ProfileUI;
import com.kika.smllybot.other.BaseCmd;
import com.kika.smllybot.utils.Interaction;
import com.kika.smllybot.utils.PrefixUtil;
import net.dv8tion.jda.api.components.container.Container;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Set;

public class AboutMe extends BaseCmd implements ButtonHandler {

    public AboutMe() {
        super(Set.of("о себе", "-о себе", "+о себе", "осебе", "-осебе", "+осебе"));
    }
    @Override
    public String getButtonPrefix() {
        return "aboutMe";
    }

    @Override
    public Container execute(MessageReceivedEvent event, String raw, String args) {

        String command = PrefixUtil.getCommandBody(raw, Main.PREFIXES).split("\\s+")[0].toLowerCase();
        char action = command.charAt(0);

        long guildId = event.getGuild().getIdLong();

        long discordId = event.getAuthor().getIdLong();
        String name = event.getAuthor().getEffectiveName();
        UserAccount user = UsersTable.getOrCreateUser(discordId, name);

        if (action == '-') {
            ProfileTable.setAboutMe(user.id(), guildId, null);

            AboutMeContext context = new AboutMeContext("## \\❌ О себе удалено", null, discordId);
            var response = AboutMeUI.buildAboutMe(context);

            event.getChannel().sendMessageComponents(response)
                    .useComponentsV2(true)
                    .queue();

            return response;
        }

        if (args.isEmpty()) {
            ProfileAccount profile = ProfileTable.getOrCreateProfile(user.id(),
                    guildId, user.name(), event.getMember().getTimeJoined());
            AboutMeContext context = new AboutMeContext("## Текущее о себе", profile.aboutMe(), discordId);

            var response = AboutMeUI.buildAboutMe(context);
            event.getChannel().sendMessageComponents(response).useComponentsV2(true).queue();

            return response;
        }

        String[] parts = raw.split("\\n+");
        if (parts.length > 1) {
            String aboutMe = parts[1];
            ProfileTable.setAboutMe(user.id(), guildId, aboutMe);

            AboutMeContext context = new AboutMeContext("## \\✅ О себе обновлено", aboutMe, discordId);
            var response = AboutMeUI.buildAboutMe(context);

            event.getChannel().sendMessageComponents(response)
                    .useComponentsV2(true)
                    .queue();

            return response;
        }

        return null;
    }

    @Override
    public void onButton(ButtonInteractionEvent event, String[] args) {
        if (!Interaction.checkOwner(event, args)) return;

        var discordId = event.getUser().getIdLong();
        var name = event.getUser().getEffectiveName();
        var guildId = event.getGuild().getIdLong();

        UserAccount users = UsersTable.getOrCreateUser(discordId, name);
        ProfileAccount profile = ProfileTable.getOrCreateProfile(users.id(),
                guildId, users.name(), event.getMember().getTimeJoined());

        if (args.length > 1 && args[1].equalsIgnoreCase("back")) {
            User user = event.getUser();

            UserAccount userAccount = UsersTable.getOrCreateUser(discordId, user.getEffectiveName());
            BankAccount bank = BankTable.getOrCreateBank(userAccount.id(), user.getEffectiveName());
            TotalStatisticUser statistic = StatisticTable.getTotalStatisticUserGuild(userAccount.id(), guildId);
            ProfileContext context = new ProfileContext(user, user, event.getMember(),
                    profile, statistic, userAccount, bank, event.getGuild().getIdLong());

            var response = ProfileUI.buildProfile(context);

            event.editComponents(response)
                    .useComponentsV2(true)
                    .queue();
        }
    }

}

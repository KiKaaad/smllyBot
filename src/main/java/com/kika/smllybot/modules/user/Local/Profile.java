package com.kika.smllybot.modules.user.Local;

import com.kika.smllybot.database.sql.bank.BankTable;
import com.kika.smllybot.database.sql.bank.dto.BankAccount;
import com.kika.smllybot.database.sql.profile.ProfileTable;
import com.kika.smllybot.database.sql.profile.dto.ProfileAccount;
import com.kika.smllybot.database.sql.statistic.StatisticTable;
import com.kika.smllybot.database.sql.statistic.dto.TotalStatisticUser;
import com.kika.smllybot.database.sql.users.UsersTable;
import com.kika.smllybot.database.sql.users.dto.UserAccount;
import com.kika.smllybot.modules.user.Local.ui.ProfileUI;
import com.kika.smllybot.other.BaseCmd;
import net.dv8tion.jda.api.components.container.Container;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.time.OffsetDateTime;
import java.util.Set;

public class Profile extends BaseCmd {

    public Profile() {
        super(Set.of("кто я", "профиль", "profile"));
    }

    private long id;
    private long guildId;
    private String author;
    private OffsetDateTime dateTime = null;

    @Override
    public Container execute(MessageReceivedEvent event, String raw, String args) {

        UsersTable.getOrCreateUser(event.getAuthor().getIdLong(), event.getAuthor().getEffectiveName());

        id = UsersTable.getUserId(event.getAuthor().getIdLong());
        guildId = event.getGuild().getIdLong();
        author = event.getAuthor().getEffectiveName();

        if (event.getMember() != null) dateTime = event.getMember().getTimeJoined();

        ProfileTable.getOrCreateProfile(id, guildId, author, dateTime);
        sendProfileResponse(event, event.getAuthor());

        return null;
    }

    private void sendProfileResponse(MessageReceivedEvent event, User target) {

        UserAccount userAccount = UsersTable.getOrCreateUser(event.getAuthor().getIdLong(),
                event.getAuthor().getEffectiveName());
        BankAccount bank = BankTable.getOrCreateBank(id, event.getAuthor().getEffectiveName());
        ProfileAccount profileAccount = ProfileTable.getOrCreateProfile(id, guildId, author, dateTime);
        TotalStatisticUser statistic = StatisticTable.getTotalStatisticUserGuild(id, guildId);
        ProfileContext context = new ProfileContext(target,
                event.getAuthor(), event.getMember(), profileAccount, statistic, userAccount, bank, event.getGuild().getIdLong());

        var response = ProfileUI.buildProfile(context);

        event.getChannel().sendMessageComponents(response)
                .useComponentsV2(true)
                .queue();

    }

}

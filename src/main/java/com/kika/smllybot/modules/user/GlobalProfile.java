package com.kika.smllybot.modules.user;

import com.kika.smllybot.database.sql.bank.BankTable;
import com.kika.smllybot.database.sql.bank.dto.BankAccount;
import com.kika.smllybot.database.sql.privacy.PrivacyTable;
import com.kika.smllybot.database.sql.privacy.dto.PrivacyAccount;
import com.kika.smllybot.database.sql.user.UserTable;
import com.kika.smllybot.database.sql.user.dto.UserAccount;
import com.kika.smllybot.modules.user.ui.GlobalProfileUI;
import com.kika.smllybot.other.BaseCmd;
import net.dv8tion.jda.api.components.container.Container;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.time.Duration;
import java.util.Set;

public class GlobalProfile extends BaseCmd {

    public GlobalProfile() {
        super(Set.of("анкета", "anketa"));
    }

    @Override
    public void execute(MessageReceivedEvent event, String args) {

        if (event.getMessage().getReferencedMessage() != null) {
            sendProfileResponse(event, event.getMessage().getReferencedMessage().getAuthor());
            return;
        }

        if (args.isEmpty()) {
            sendProfileResponse(event, event.getAuthor());
            return;
        }

        String[] parts = args.trim().split("\\s+", 1);
        String arg = parts[0];

        if (!event.getMessage().getMentions().getUsers().isEmpty()) {
            sendProfileResponse(event, event.getMessage().getMentions().getUsers().getFirst());
            return;
        }

        if (arg.matches("\\d+")) {
            event.getJDA().retrieveUserById(arg).queue(
                    targetUser -> sendProfileResponse(event, targetUser),
                    failure -> sendError(event, "❌ Упс... Пользователь с таким ID не найден")
            );
            return;
        }

        var members = event.getGuild().getMembersByName(arg, true);

        if (members.isEmpty()) {
            members = event.getGuild().getMembersByNickname(arg, true);
        }

        if (!members.isEmpty()) {
            sendProfileResponse(event, members.getFirst().getUser());
        } else {
            sendError(event, "❌ Упс... Пользователь с таким юзернеймом не найден");
        }
    }

    private void sendProfileResponse(MessageReceivedEvent event, User targetUser) {

        Member targetMember = event.isFromGuild()
                ? event.getGuild().getMember(targetUser)
                : null;

        UserAccount user = UserTable.getOrCreateUser(targetMember.getIdLong(), targetMember.getEffectiveName());
        BankAccount bank = BankTable.getOrCreateBank(user.internalId(), targetMember.getEffectiveName());
        PrivacyAccount privacy = PrivacyTable.getOrCreatePrivacy(user.internalId());

        GlobalProfileContext ctx = new GlobalProfileContext(
                targetUser,
                event.getAuthor(),
                targetMember,
                user,
                bank,
                privacy
        );

        Container response = GlobalProfileUI.buildProfile(ctx);

        event.getChannel().sendMessageComponents(response)
                .useComponentsV2(true)
                .queue();
    }

    private void sendError(MessageReceivedEvent event, String text) {
        event.getChannel().sendMessage(text)
                .delay(Duration.ofSeconds(5))
                .flatMap(Message::delete)
                .queue();
    }

}
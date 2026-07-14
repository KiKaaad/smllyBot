package com.kika.smllybot.modules.economy;

import com.kika.smllybot.database.sql.bank.BankTable;
import com.kika.smllybot.database.sql.bank.dto.BankAccount;
import com.kika.smllybot.modules.economy.ui.BagUI;
import com.kika.smllybot.other.BaseCmd;
import net.dv8tion.jda.api.components.container.Container;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.Set;

public class Bag extends BaseCmd {

    public Bag() {
        super(Set.of("мешок", "bag"));
    }

    @Override
    public Container execute(MessageReceivedEvent event, String args) {

        String[] parts = args.trim().split("\\s+");

        if (event.getMessage().getReferencedMessage() != null) {
            sendBugResponse(event, event.getMessage().getReferencedMessage().getAuthor());
            return null;
        }

        if (args.isEmpty()) {
            sendBugResponse(event, event.getAuthor());
            return null;
        };

        if (!event.getMessage().getMentions().getUsers().isEmpty()) {
            sendBugResponse(event, event.getMessage().getMentions().getUsers().getFirst());
            return null;
        }

        if (args.matches("\\d+")) {
            event.getJDA().retrieveUserById(parts[0]).queue(
                    targetUser -> sendBugResponse(event, targetUser),
                    failure -> sendError(event)
            );
            return null;
        }

        @NotNull
        var members = event.getGuild().getMembersByName(args, true);

        if (!members.isEmpty()) {
            sendBugResponse(event, members.getFirst().getUser());
        } else {
            sendError(event);
        }

        return null;
    }

    public void sendBugResponse(MessageReceivedEvent event, User targetUser) {

        BankAccount bank = BankTable.getOrCreateBank(targetUser.getIdLong(), targetUser.getName());

        BagContext ctx = new BagContext(
                targetUser,
                bank
        );

        Container response = BagUI.buildBug(ctx);

        event.getChannel().sendMessageComponents(response)
                .useComponentsV2(true)
                .queue();

    }

    public void sendError(MessageReceivedEvent event) {

        event.getChannel().sendMessage("\\❌ Увы и ах мешок этого пользователя не найден")
                .delay(Duration.ofSeconds(5))
                .flatMap(Message::delete)
                .queue();

    }

}

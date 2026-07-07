package com.kika.smllybot.modules.economy;

import com.kika.smllybot.database.DatabaseService;
import com.kika.smllybot.database.UsersData;
import com.kika.smllybot.modules.economy.ui.BagUI;
import com.kika.smllybot.other.BaseCmd;
import net.dv8tion.jda.api.components.container.Container;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Set;

public class Bag extends BaseCmd {

    public Bag() {
        super(Set.of("мешок", "bag"));
    }

    @Override
    public void execute(MessageReceivedEvent event, String args) {

        String[] parts = args.trim().split("\\s+");

        if (event.getMessage().getReferencedMessage() != null) {
            sendBugResponse(event, event.getMessage().getReferencedMessage().getAuthor());
            return;
        }

        if (args.isEmpty()) sendBugResponse(event, event.getAuthor());

        if (!event.getMessage().getMentions().getUsers().isEmpty()) {
            sendBugResponse(event, event.getMessage().getMentions().getUsers().getFirst());
            return;
        }

        if (args.matches("\\d+")) {
            event.getJDA().retrieveUserById(parts[0]).queue(
                    targetUser -> sendBugResponse(event, targetUser)
            );
            return;
        }

        var members = event.getGuild().getMembersByName(args, true);

        if (members.isEmpty()) {
            members = event.getGuild().getMembersByNickname(args, true);
        }

        if (!members.isEmpty()) {
            sendBugResponse(event, members.getFirst().getUser());
        }

    }

    public void sendBugResponse(MessageReceivedEvent event, User targetUser) {

        UsersData data = DatabaseService.getFullData(targetUser.getIdLong(), targetUser.getName());

        BagContext ctx = new BagContext(
                targetUser,
                data.dbBank()
        );

        Container response = BagUI.buildBug(ctx);

        event.getChannel().sendMessageComponents(response)
                .useComponentsV2(true)
                .queue();

    }

}

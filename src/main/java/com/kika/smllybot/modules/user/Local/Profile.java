package com.kika.smllybot.modules.user.Local;

import com.kika.smllybot.modules.user.Local.ui.ProfileUI;
import com.kika.smllybot.other.BaseCmd;
import net.dv8tion.jda.api.components.container.Container;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Set;

public class Profile extends BaseCmd {

    public Profile() {
        super(Set.of("кто я", "профиль", "profile"));
    }

    @Override
    public Container execute(MessageReceivedEvent event, String raw, String args) {

        sendProfileResponse(event, event.getAuthor());

        return null;
    }

    private void sendProfileResponse(MessageReceivedEvent event, User target) {

        ProfileContext context = new ProfileContext(event, event.getAuthor().getIdLong(), target);

        var response = ProfileUI.buildProfile(context);

        event.getChannel().sendMessageComponents(response)
                .useComponentsV2(true)
                .queue();

    }

}

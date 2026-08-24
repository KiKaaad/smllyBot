package com.kika.smllybot.modules.user.Global;

import com.kika.smllybot.database.sql.Repository;
import com.kika.smllybot.modules.user.Global.ui.GlobalProfileUI;
import com.kika.smllybot.other.BaseCmd;
import net.dv8tion.jda.api.components.container.Container;
import net.dv8tion.jda.api.components.container.ContainerChildComponent;
import net.dv8tion.jda.api.components.mediagallery.MediaGallery;
import net.dv8tion.jda.api.components.mediagallery.MediaGalleryItem;
import net.dv8tion.jda.api.components.textdisplay.TextDisplay;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class GlobalProfile extends BaseCmd {

    public GlobalProfile() {
        super(Set.of("анкета", "anketa"));
    }

    @Override
    public Container execute(MessageReceivedEvent event, String raw, String args) {

        String[] parts = raw.trim().split("\\s+", 2);

        if (event.getMessage().getReferencedMessage() != null) {
            sendAnketaResponse(event, event.getMessage().getReferencedMessage().getAuthor());
            return null;
        }

        if (parts.length < 2) {
            sendAnketaResponse(event, event.getAuthor());
            return null;
        }

        String arg = parts[1];

        if (!event.getMessage().getMentions().getUsers().isEmpty()) {
            sendAnketaResponse(event, event.getMessage().getMentions().getUsers().getFirst());
            return null;
        }

        if (arg.matches("\\d+")) {
            event.getJDA().retrieveUserById(arg).queue(
                    targetUser -> sendAnketaResponse(event, targetUser),
                    throwable -> sendError(event, "### \\❌ Упс... Пользователь с таким ID не найден")
            );
            return null;
        }

        var members = event.getGuild().getMembersByName(arg, true);

        if (members.isEmpty()) {
            members = event.getGuild().getMembersByNickname(arg, true);
        }

        if (!members.isEmpty()) {
            sendAnketaResponse(event, members.getFirst().getUser());
        } else {
            sendError(event, "### \\❌ Упс... Пользователь с таким юзернеймом не найден");
        }
        return null;
    }

    private void sendAnketaResponse(MessageReceivedEvent event, User targetUser) {
        long discordId = targetUser.getIdLong();
        String name = targetUser.getName();

        var targetMember = event.isFromGuild() ? event.getGuild().getMember(targetUser) : null;
        Repository repo = new Repository();

        GlobalProfileContext ctx = new GlobalProfileContext(
                targetUser,
                event.getAuthor(),
                targetMember,
                repo.getUser(discordId, name),
                repo.getBank(discordId, name),
                repo.getPrivacy(discordId, name)
        );

        targetUser.retrieveProfile().queue(
            profile -> {
                Container response = GlobalProfileUI.buildProfile(ctx);
                List<ContainerChildComponent> components = new ArrayList<>(response.getComponents());

                MediaGallery banner;
                if (profile.getBanner() != null) {
                    String bannerUrl = profile.getBanner().getUrl(1024);
                    banner = MediaGallery.of(MediaGalleryItem.fromUrl(bannerUrl));
                    components.addFirst(banner);
                }

                response = Container.of(components);

                event.getChannel().sendMessageComponents(response)
                        .useComponentsV2(true)
                        .queue();
            }
        );
    }

    private void sendError(MessageReceivedEvent event, String e) {
        ContainerChildComponent main = TextDisplay.of(e);

        event.getChannel().sendMessageComponents(Container.of(main))
                .useComponentsV2(true)
                .delay(Duration.ofSeconds(5))
                .flatMap(Message::delete)
                .queue();
    }

}
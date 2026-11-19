package com.kika.smllybot.modules.user.Global;

import com.kika.smllybot.database.sql.Repository;
import com.kika.smllybot.handler.ErrorThrow;
import com.kika.smllybot.modules.user.Global.ui.GlobalProfileUI;
import com.kika.smllybot.other.BaseCmd;
import net.dv8tion.jda.api.components.container.Container;
import net.dv8tion.jda.api.components.container.ContainerChildComponent;
import net.dv8tion.jda.api.components.mediagallery.MediaGallery;
import net.dv8tion.jda.api.components.mediagallery.MediaGalleryItem;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.UserContextInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class GlobalProfile extends BaseCmd {

    public GlobalProfile() {
        super(Set.of("анкета", "anketa"));
    }

    @Override
    public Container execute(MessageReceivedEvent event, String raw, String args) {

        String[] parts = args.trim().split("\\s+", 2);

        if (event.getMessage().getReferencedMessage() != null) {
            sendAnketaResponse(event, event.getMessage().getReferencedMessage().getAuthor());
            return null;
        }

        if (args.isEmpty()) {
            sendAnketaResponse(event, event.getAuthor());
            return null;
        }

        String arg = parts[0];

        if (arg.matches("\\d+")) {
            event.getJDA().retrieveUserById(arg).queue(
                    targetUser -> sendAnketaResponse(event, targetUser),
                    throwable -> ErrorThrow.userNotFound(event, arg)
            );
            return null;
        }

        if (!event.getMessage().getMentions().getUsers().isEmpty()) {
            sendAnketaResponse(event, event.getMessage().getMentions().getUsers().getLast());
            return null;
        }

        var members = event.getGuild(). getMembersByName(arg, true);

        if (members.isEmpty()) {
            members = event.getGuild().getMembersByNickname(arg, true);
        }

        if (!members.isEmpty()) {
            sendAnketaResponse(event, members.getFirst().getUser());
        } else {
            ErrorThrow.userNotFound(event, arg);
        }
        return null;
    }

    private void sendAnketaResponse(MessageReceivedEvent event, User targetUser) {
        long discordId = targetUser.getIdLong();
        String name = targetUser.getEffectiveName();

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

    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        Repository repo = new Repository();

        long discordId = event.getUser().getIdLong();
        String name = event.getUser().getEffectiveName();

        if (event.getName().equals("анкета")) {
            GlobalProfileContext ctx = new GlobalProfileContext(
                    event.getUser(),
                    event.getUser(),
                    event.getMember(),
                    repo.getUser(discordId, name),
                    repo.getBank(discordId, name),
                    repo.getPrivacy(discordId, name)
            );

            Container response = GlobalProfileUI.buildProfile(ctx);

            event.deferReply().queue();
            event.getHook().sendMessageComponents(response).useComponentsV2(true).queue();
        }
    }

    @Override
    public void onUserContextInteraction(UserContextInteractionEvent event) {
        Repository repo = new Repository();

        long discordId = event.getTarget().getIdLong();
        String name = event.getTarget().getEffectiveName();

        if (event.getName().equalsIgnoreCase("анкета")) {
            GlobalProfileContext ctx = new GlobalProfileContext(
                    event.getTarget(),
                    event.getTarget(),
                    event.getTargetMember(),
                    repo.getUser(discordId, name),
                    repo.getBank(discordId, name),
                    repo.getPrivacy(discordId, name)
            );

            Container response = GlobalProfileUI.buildProfile(ctx);

            event.deferReply().queue();
            event.getHook().sendMessageComponents(response).useComponentsV2(true).queue();
        }
    }
}
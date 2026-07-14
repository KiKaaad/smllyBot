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
    public Container execute(MessageReceivedEvent event, String args) {

        if (event.getMessage().getReferencedMessage() != null) {
            sendProfileResponse(event, event.getMessage().getReferencedMessage().getAuthor());
            return null;
        }

        if (args.isEmpty()) {
            sendProfileResponse(event, event.getAuthor());
            return null;
        }

        String[] parts = args.trim().split("\\s+", 1);
        String arg = parts[0];

        if (!event.getMessage().getMentions().getUsers().isEmpty()) {
            sendProfileResponse(event, event.getMessage().getMentions().getUsers().getFirst());
            return null;
        }

        if (arg.matches("\\d+")) {
            event.getJDA().retrieveUserById(arg).queue(
                    targetUser -> sendProfileResponse(event, targetUser),
                    throwable -> sendError(event, "### \\❌ Упс... Пользователь с таким ID не найден")
            );
            return null;
        }

        var members = event.getGuild().getMembersByName(arg, true);

        if (members.isEmpty()) {
            members = event.getGuild().getMembersByNickname(arg, true);
        }

        if (!members.isEmpty()) {
            sendProfileResponse(event, members.getFirst().getUser());
        } else {
            sendError(event, "### \\❌ Упс... Пользователь с таким юзернеймом не найден");
        }
        return null;
    }

    private void sendProfileResponse(MessageReceivedEvent event, User targetUser) {

        var targetMember = event.isFromGuild() ? event.getGuild().getMember(targetUser) : null;

        assert targetMember != null;
        UserAccount user = UserTable.getOrCreateUser(event.getAuthor().getIdLong(), targetUser.getName());
        assert user != null;
        BankAccount bank = BankTable.getOrCreateBank(user.internalId(), targetUser.getName());
        PrivacyAccount privacy = PrivacyTable.getOrCreatePrivacy(user.internalId());

        GlobalProfileContext ctx = new GlobalProfileContext(
                targetUser,
                event.getAuthor(),
                targetMember,
                user,
                bank,
                privacy
        );

        targetUser.retrieveProfile().queue(
                profile -> {
                    Container response = GlobalProfileUI.buildProfile(ctx);
                    List<ContainerChildComponent> components = new ArrayList<>(response.getComponents());

                    MediaGallery banner;
                    if (profile.getBannerUrl() != null) {
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
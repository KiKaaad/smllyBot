package com.kika.smllybot.modules.user.Local;

import com.kika.smllybot.database.sql.Repository;
import com.kika.smllybot.database.sql.bank.dto.BankAccount;
import com.kika.smllybot.database.sql.profile.dto.ProfileAccount;
import com.kika.smllybot.database.sql.statistic.dto.StatisticAccount;
import com.kika.smllybot.database.sql.users.dto.UserAccount;
import com.kika.smllybot.modules.user.Local.ui.ProfileUI;
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
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Profile extends BaseCmd {

    public Profile() {
        super(Set.of("кто я", "профиль", "profile"));
    }

    @Override
    public Container execute(MessageReceivedEvent event, String raw, String args) {
        if (event.getMember() == null) return null;

        String[] parts = raw.split("\\h+");

        if (event.getMessage().getReferencedMessage() != null) {
            sendProfileResponse(event, event.getMessage().getReferencedMessage().getAuthor());
            return null;
        }

        if (parts.length < 2) {
            sendProfileResponse(event, event.getAuthor());
            return null;
        }

        if (!event.getMessage().getMentions().getUsers().isEmpty()) {
            sendProfileResponse(event, event.getMessage().getMentions().getUsers().getFirst());
            return null;
        }

        if (parts[1].matches("\\d+")) {
            event.getJDA().retrieveUserById(parts[1]).queue(
                    user -> sendProfileResponse(event, user),
                    throwable -> sendError(event, "### \\❌ Упс... Пользователь с таким ID не найден")
            );
            return null;
        }

        var members = event.getGuild().getMembersByName(parts[1], true);

        if (members.isEmpty()) {
            members = event.getGuild().getMembersByNickname(parts[1], true);
        }

        if (!members.isEmpty()) {
            sendProfileResponse(event, members.getFirst().getUser());
        } else {
            sendError(event, "### \\❌ Упс... Пользователь с таким юзернеймом не найден");
        }

        return null;
    }

    private void sendProfileResponse(MessageReceivedEvent event, User target) {
        long discordId = target.getIdLong();
        String name = target.getEffectiveName();
        long guildId = event.getGuild().getIdLong();
        OffsetDateTime dateTime = event.getMember().getTimeJoined();

        Repository repo = new Repository();
        UserAccount user = repo.getUser(discordId, name);
        BankAccount bank = repo.getBank(discordId, name);
        ProfileAccount profileAccount = repo.getProfile(discordId, guildId, name, dateTime);
        StatisticAccount statistic = repo.getStatistic(discordId, name, guildId);
        ProfileContext context = new ProfileContext(
                target,
                event.getAuthor(),
                event.getMember(),
                profileAccount,
                statistic,
                user,
                bank,
                guildId
        );

        target.retrieveProfile().queue(
                profile -> {
                    Container response = ProfileUI.buildProfile(context);
                    List<ContainerChildComponent> components = new ArrayList<>(response.getComponents());

                    MediaGallery banner;
                    if (profile.getBanner() != null) {
                        String bannerUrl = profile.getBanner().getUrl(1024);
                        banner = MediaGallery.of(MediaGalleryItem.fromUrl(bannerUrl));
                        components.addFirst(banner);
                    }

                    response = Container.of(components);

                    event.getChannel().sendMessageComponents(response).useComponentsV2(true).queue();
                }
        );

    }

    private void sendError(MessageReceivedEvent event, String text) {
        var response = Container.of(
                TextDisplay.of(text)
        );

        event.getChannel().sendMessageComponents(response)
                .useComponentsV2(true)
                .delay(Duration.ofSeconds(5))
                .flatMap(Message::delete)
                .queue();
    }
}

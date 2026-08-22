package com.kika.smllybot.modules.user.Local.ui;

import com.kika.smllybot.annotations.ButtonPrefix;
import com.kika.smllybot.annotations.ModalPrefix;
import com.kika.smllybot.database.sql.Repository;
import com.kika.smllybot.database.sql.bank.dto.BankAccount;
import com.kika.smllybot.database.sql.profile.ProfileTable;
import com.kika.smllybot.database.sql.profile.dto.ProfileAccount;
import com.kika.smllybot.database.sql.statistic.dto.StatisticAccount;
import com.kika.smllybot.database.sql.users.UsersTable;
import com.kika.smllybot.database.sql.users.dto.UserAccount;
import com.kika.smllybot.modules.user.Local.ProfileContext;
import com.kika.smllybot.other.BaseCmd;
import com.kika.smllybot.utils.Interaction;
import net.dv8tion.jda.api.components.container.Container;
import net.dv8tion.jda.api.components.container.ContainerChildComponent;
import net.dv8tion.jda.api.components.label.Label;
import net.dv8tion.jda.api.components.mediagallery.MediaGallery;
import net.dv8tion.jda.api.components.mediagallery.MediaGalleryItem;
import net.dv8tion.jda.api.components.textinput.TextInput;
import net.dv8tion.jda.api.components.textinput.TextInputStyle;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.modals.Modal;
import org.jetbrains.annotations.NotNull;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ProfileModal extends BaseCmd {

    public ProfileModal() { super(Set.of("profile")); }

    @ButtonPrefix(prefix = "profile")
    public void onButton(@NotNull ButtonInteractionEvent event, String[] parts) {
        if (!Interaction.checkOwner(event, parts)) return;

        if (parts.length > 1 && parts[1].equals("modal")) {

            String ownerId = parts.length > 2 ? parts[2] : "";
            TextInput aboutMeField = TextInput.create("aboutMe_field", TextInputStyle.PARAGRAPH)
                    .setPlaceholder("Какой же я красавчик... 😎")
                    .setMaxLength(255)
                    .setRequired(false)
                    .build();

            Modal modal = Modal.create("profile:submit:" + ownerId, "🗿 Редактировать профиль")
                    .addComponents(
                            Label.of("🐾 О себе", aboutMeField)
                    )
                    .build();

            event.replyModal(modal).queue();
        }
    }

    @ModalPrefix(prefix = "profile")
    public void onModal(ModalInteractionEvent event, String[] parts) {
        if (parts.length > 1 && parts[1].equals("submit")) {

            var value = event.getValue("aboutMe_field");
            if (value == null) return;

            String newAboutMe = value.getAsString().isBlank() ? null : value.getAsString();
            long discordId = event.getUser().getIdLong();
            String name = event.getUser().getEffectiveName();
            long guildId = event.getGuild().getIdLong();
            OffsetDateTime dateTime = event.getMember().getTimeJoined();

            UsersTable.getOrCreateUser(discordId, name);
            long id = UsersTable.getUserId(discordId);
            ProfileTable.setAboutMe(id, event.getGuild().getIdLong(), newAboutMe);

            Repository repo = new Repository();
            UserAccount user = repo.getUser(discordId, name);
            BankAccount bank = repo.getBank(discordId, name);
            ProfileAccount profileAccount = repo.getProfile(discordId, guildId, name, dateTime);
            StatisticAccount statistic = repo.getStatistic(discordId, name, guildId);
            ProfileContext ctx = new ProfileContext(
                    event.getUser(),
                    event.getUser(),
                    event.getMember(),
                    profileAccount,
                    statistic,
                    user,
                    bank,
                    guildId
            );

            event.getUser().retrieveProfile().queue(
                    profile -> {
                        Container response = ProfileUI.buildProfile(ctx);
                        List<ContainerChildComponent> components = new ArrayList<>(response.getComponents());

                        MediaGallery banner;
                        if (profile.getBannerUrl() != null) {
                            String bannerUrl = profile.getBanner().getUrl(1024);
                            banner = MediaGallery.of(MediaGalleryItem.fromUrl(bannerUrl));
                            components.addFirst(banner);
                        }

                        response = Container.of(components);

                        event.editComponents(response)
                                .useComponentsV2(true)
                                .queue();
                    }
            );
        }
    }
}

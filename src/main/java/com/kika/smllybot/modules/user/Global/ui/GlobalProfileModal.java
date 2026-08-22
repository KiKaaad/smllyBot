package com.kika.smllybot.modules.user.Global.ui;

import com.kika.smllybot.annotations.ButtonPrefix;
import com.kika.smllybot.annotations.ModalPrefix;
import com.kika.smllybot.database.sql.bank.BankTable;
import com.kika.smllybot.database.sql.bank.dto.BankAccount;
import com.kika.smllybot.database.sql.privacy.PrivacyTable;
import com.kika.smllybot.database.sql.privacy.dto.PrivacyAccount;
import com.kika.smllybot.database.sql.users.UsersTable;
import com.kika.smllybot.database.sql.users.dto.UserAccount;
import com.kika.smllybot.modules.user.Global.GlobalProfileContext;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class GlobalProfileModal extends BaseCmd {

    public GlobalProfileModal() { super(Set.of("anketa")); }

    @ButtonPrefix(prefix = "anketa")
    public void onButton(@NotNull ButtonInteractionEvent event, String[] parts) {
        if (!Interaction.checkOwner(event, parts)) return;

        if (parts.length > 1 && parts[1].equals("modal")) {

            String ownerId = parts.length > 2 ? parts[2] : "";
            TextInput mottoInput = TextInput.create("motto_field", TextInputStyle.PARAGRAPH)
                    .setPlaceholder("Какой же я красавчик... 😎")
                    .setMaxLength(255)
                    .setRequired(false)
                    .build();

            Modal modal = Modal.create("anketa:submit:" + ownerId, "🗿 Редактировать профиль")
                    .addComponents(
                            Label.of("🐾 Девиз", mottoInput)
                    )
                    .build();

            event.replyModal(modal).queue();
        }
    }

    @ModalPrefix(prefix = "anketa")
    public void onModal(ModalInteractionEvent event, String[] parts) {
        if (parts.length > 1 && parts[1].equals("submit")) {

            var value = event.getValue("motto_field");
            if (value == null) return;

            String newAboutMe = value.getAsString().isBlank() ? null : value.getAsString();
            long discordId = event.getUser().getIdLong();
            String username = event.getUser().getName();

            UsersTable.setMotto(discordId, newAboutMe);

            UserAccount user = UsersTable.getOrCreateUser(discordId, username);
            BankAccount bank = BankTable.getOrCreateBank(user.getId(), username);
            PrivacyAccount privacy = PrivacyTable.getOrCreatePrivacy(user.getId());

            GlobalProfileContext ctx = new GlobalProfileContext(
                    event.getUser(),
                    event.getUser(),
                    event.getMember(),
                    user,
                    bank,
                    privacy
            );

            event.getUser().retrieveProfile().queue(
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

                        event.editComponents(response)
                                .useComponentsV2(true)
                                .queue();
                    }
            );
        }
    }

}

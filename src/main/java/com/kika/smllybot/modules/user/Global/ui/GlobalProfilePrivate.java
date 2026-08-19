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
import net.dv8tion.jda.api.components.radiogroup.RadioGroup;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.modals.ModalMapping;
import net.dv8tion.jda.api.modals.Modal;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class GlobalProfilePrivate extends BaseCmd {

    public GlobalProfilePrivate() { super(Set.of("private")); }

    @ButtonPrefix(prefix = "private")
    public void onButton(@NotNull ButtonInteractionEvent event, String[] parts) {
        UserAccount user = UsersTable.getOrCreateUser(event.getUser().getIdLong(), event.getUser().getName());
        PrivacyAccount privacy = PrivacyTable.getOrCreatePrivacy(user.getId());

        if (!Interaction.checkOwner(event, parts)) return;

        if (parts.length > 1 && parts[1].equals("private")) {

            String ownerId = parts.length > 2 ? parts[2] : "";
            RadioGroup bagSettings = RadioGroup.create("BagSettings")
                    .addOption("Все", "false")
                    .addOption("Никто", "true")
                    .setSelectedValue("%s".formatted(privacy.getBag()))
                    .setRequired(true)
                    .build();
            RadioGroup activity = RadioGroup.create("ActivitySettings")
                    .addOption("Все", "false")
                    .addOption("Никто", "true")
                    .setSelectedValue("%s".formatted(privacy.getActivity()))
                    .setRequired(true)
                    .build();
            RadioGroup activityTime = RadioGroup.create("LastActivitySettings")
                    .addOption("Все", "false")
                    .addOption("Никто", "true")
                    .setSelectedValue("%s".formatted(privacy.getLastActivity()))
                    .setRequired(true)
                    .build();

            Modal modal = Modal.create("private:submit:" + ownerId, "🕶️ Настройки приватности")
                    .addComponents(
                            Label.of("Кто видит мой мешок?", bagSettings),
                            Label.of("Кто видит мою активность?", activity),
                            Label.of("Кто видит время моего последнего захода?", activityTime)
                    )
                    .build();

            event.replyModal(modal).queue();
        }
    }

    @ModalPrefix(prefix = "private")
    public void onModal(ModalInteractionEvent event, String[] parts) {
        if (parts[1].equals("submit")) {
            long discordId = event.getUser().getIdLong();
            String username = event.getUser().getName();

            UserAccount user = UsersTable.getOrCreateUser(discordId, username);

            ModalMapping bagMapping = event.getValue("BagSettings");
            PrivacyTable.updateBagPrivacy(user.getId(), bagMapping.getAsString().equals("true"));

            ModalMapping activityMapping = event.getValue("ActivitySettings");
            PrivacyTable.updateActivityPrivacy(user.getId(), activityMapping.getAsString().equals("true"));

            ModalMapping lastActivityMapping = event.getValue("LastActivitySettings");
            PrivacyTable.updateLastActivityPrivacy(user.getId(), lastActivityMapping.getAsString().equals("true"));

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

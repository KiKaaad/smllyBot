package com.kika.smllybot.modules.user.Global;

import com.kika.smllybot.Main;
import com.kika.smllybot.annotations.ButtonPrefix;
import com.kika.smllybot.database.sql.bank.BankTable;
import com.kika.smllybot.database.sql.bank.dto.BankAccount;
import com.kika.smllybot.database.sql.privacy.PrivacyTable;
import com.kika.smllybot.database.sql.privacy.dto.PrivacyAccount;
import com.kika.smllybot.database.sql.users.UsersTable;
import com.kika.smllybot.database.sql.users.dto.UserAccount;
import com.kika.smllybot.modules.user.Global.ui.GlobalProfileUI;
import com.kika.smllybot.modules.user.Global.ui.MottoUI;
import com.kika.smllybot.other.BaseCmd;
import com.kika.smllybot.utils.Interaction;
import com.kika.smllybot.utils.PrefixUtil;
import net.dv8tion.jda.api.components.container.Container;
import net.dv8tion.jda.api.components.container.ContainerChildComponent;
import net.dv8tion.jda.api.components.mediagallery.MediaGallery;
import net.dv8tion.jda.api.components.mediagallery.MediaGalleryItem;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Motto extends BaseCmd {

    public Motto() { super(Set.of("+девиз", "-девиз", "motto", "девиз")); }

    @Override
    public Container execute(MessageReceivedEvent event, String raw, String args) {

        String command = PrefixUtil.getCommandBody(raw, Main.PREFIXES).split("\\s+")[0].toLowerCase();

        char action = command.charAt(0);

        long discordId = event.getAuthor().getIdLong();
        String username = event.getAuthor().getEffectiveName();

        // Удалить девиз
        if (action == '-') {
            UsersTable.setMotto(discordId, null);
            UserAccount dbUser = UsersTable.getOrCreateUser(discordId, username);
            Container response = MottoUI.buildMotto(
                    event.getAuthor(), dbUser, "\\❌ Описание удалено", false);

            event.getChannel().sendMessageComponents(response).useComponentsV2(true).queue();
            return response;
        }

        // Показать девиз
        if (args.isEmpty()) {
            UserAccount dbUser = UsersTable.getOrCreateUser(discordId, username);
            Container response = MottoUI.buildMotto(
                    event.getAuthor(), dbUser, "Ваш текущий девиз", false);

            event.getChannel().sendMessageComponents(response).useComponentsV2(true).queue();
            return response;
        }

        String[] parts = raw.split("\\n+");
        if (parts.length > 1) {
            String motto = parts[1].replace("@", "\\@");
            UsersTable.setMotto(discordId, motto);
            response(event, "\\✅ Описание обновлено");
        }

        return null;
    }

    private void response(MessageReceivedEvent event, String title) {
        long discordId = event.getAuthor().getIdLong();
        String username = event.getAuthor().getEffectiveName();
        UserAccount dbUser = UsersTable.getOrCreateUser(discordId, username);

        event.getAuthor().retrieveProfile().queue(
            profile -> {
                Container response = MottoUI.buildMotto(event.getAuthor(), dbUser, title, true);
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

    @ButtonPrefix(prefix = "motto")
    public void onButton(ButtonInteractionEvent event, String[] args) {
        if (!Interaction.checkOwner(event, args)) return;

        if (event.getComponentId().startsWith("motto:back:")) {
            User user = event.getUser();

            UserAccount userAccount = UsersTable.getOrCreateUser(user.getIdLong(), user.getEffectiveName());
            BankAccount bank = BankTable.getOrCreateBank(userAccount.getId(), user.getEffectiveName());
            PrivacyAccount privacy = PrivacyTable.getOrCreatePrivacy(userAccount.getId());

            GlobalProfileContext ctx = new GlobalProfileContext(
                    user, user, event.getMember(), userAccount, bank, privacy);

            event.getUser().retrieveProfile().queue(
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

                    event.editComponents(response).useComponentsV2(true).queue();
                }
            );
        }
    }

}


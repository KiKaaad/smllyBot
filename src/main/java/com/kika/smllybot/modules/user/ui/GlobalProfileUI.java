package com.kika.smllybot.modules.user.ui;

import com.kika.smllybot.database.postgresql.bank.Bank;
import com.kika.smllybot.database.postgresql.user.User;
import net.dv8tion.jda.api.components.actionrow.ActionRow;
import net.dv8tion.jda.api.components.buttons.Button;
import net.dv8tion.jda.api.components.container.Container;
import net.dv8tion.jda.api.components.mediagallery.MediaGallery;
import net.dv8tion.jda.api.components.mediagallery.MediaGalleryItem;
import net.dv8tion.jda.api.components.section.Section;
import net.dv8tion.jda.api.components.separator.Separator;
import net.dv8tion.jda.api.components.textdisplay.TextDisplay;
import net.dv8tion.jda.api.components.thumbnail.Thumbnail;

import java.time.format.DateTimeFormatter;

public class GlobalProfileUI {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    public static Container buildProfile(
            net.dv8tion.jda.api.entities.User targetUser,
            User dbUser,
            net.dv8tion.jda.api.entities.User viewer) {
        boolean isOwner = targetUser.getIdLong() == viewer.getIdLong();
        String discordCreated = targetUser.getTimeCreated().format(formatter);
        // Аватар
        String avatarUrl = targetUser.getEffectiveAvatarUrl();
        // Баннер
        net.dv8tion.jda.api.entities.User.Profile profile = targetUser.retrieveProfile().complete();
        String bannerUrl = null;
        MediaGallery banner = null;
        if (profile.getBanner() != null) {
            bannerUrl = profile.getBanner().getUrl(480);
            banner = MediaGallery.of(MediaGalleryItem.fromUrl(bannerUrl));
        }

        Section main = Section.of(
                Thumbnail.fromUrl(avatarUrl),
                TextDisplay.of("# 🗿 Это %s".formatted(targetUser.getName())),
                TextDisplay.of("### О себе:"),
                TextDisplay.of(dbUser.aboutMe()
                ));

        Section economy = Section.of(
                Button.primary("bag::meow::" + targetUser.getId(), "мяу"),
                TextDisplay.of("### 💰 Мешок"),
                TextDisplay.of("🌼 %d ирисок"),
                TextDisplay.of("📻 %d i¢")
        );

        // Мне в целом не нравится как я реализовал проверку наличия баннера у пользователя,
        // предложите, как это можно сделать лучше

        // Если баннер есть
        if (bannerUrl != null) {
            if (isOwner) {
                return Container.of(
                        banner,
                        main,
                        Separator.createDivider(Separator.Spacing.SMALL),
                        TextDisplay.of("🕐 Во вселенной дискорда с **%s**".formatted(discordCreated)),
                        TextDisplay.of("⌛ Во вселенной бота с **{created_at_in_bot}**"),
                        TextDisplay.of("🆔 IDB `%d`".formatted(dbUser.internalId())),
                        TextDisplay.of("🆔 IDD `%d`".formatted(dbUser.discordId())),
                        Separator.createDivider(Separator.Spacing.SMALL),
                        economy,
                        TextDisplay.of("### Кнопки управления профилем"),
                        Separator.createDivider(Separator.Spacing.SMALL),
                        ActionRow.of(Button.primary("aboutMe::modal::" + targetUser.getId(), "Редактировать профиль"))
                );
            } else {
                return Container.of(
                        banner,
                        main,
                        Separator.createDivider(Separator.Spacing.SMALL),
                        TextDisplay.of("🕐 Во вселенной дискорда с **%s**".formatted(discordCreated)),
                        TextDisplay.of("⌛ Во вселенной бота с **{created_at_in_bot}**"),
                        TextDisplay.of("🆔 IDB `%d`".formatted(dbUser.internalId())),
                        TextDisplay.of("🆔 IDD `%d`".formatted(dbUser.discordId())),
                        Separator.createDivider(Separator.Spacing.SMALL),
                        economy
                );
            }
        // Если баннера нет
        } else {
            if (isOwner) {
                return Container.of(
                        main,
                        Separator.createDivider(Separator.Spacing.SMALL),
                        TextDisplay.of("🕐 Во вселенной дискорда с **%s**".formatted(discordCreated)),
                        TextDisplay.of("⌛ Во вселенной бота с **{created_at_in_bot}**"),
                        TextDisplay.of("🆔 IDB `%d`".formatted(dbUser.internalId())),
                        TextDisplay.of("🆔 IDD `%d`".formatted(dbUser.discordId())),
                        Separator.createDivider(Separator.Spacing.SMALL),
                        economy,
                        TextDisplay.of("### Кнопки управления профилем"),
                        Separator.createDivider(Separator.Spacing.SMALL),
                        ActionRow.of(Button.primary("aboutMe::modal::" + targetUser.getId(), "Редактировать профиль"))
                );
            } else {
                return Container.of(
                        main,
                        Separator.createDivider(Separator.Spacing.SMALL),
                        TextDisplay.of("🕐 Во вселенной дискорда с **%s**".formatted(discordCreated)),
                        TextDisplay.of("⌛ Во вселенной бота с **{created_at_in_bot}**"),
                        TextDisplay.of("🆔 IDB `%d`".formatted(dbUser.internalId())),
                        TextDisplay.of("🆔 IDD `%d`".formatted(dbUser.discordId())),
                        Separator.createDivider(Separator.Spacing.SMALL),
                        economy
                );
            }
        }
    }
}

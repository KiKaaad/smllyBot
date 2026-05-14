package com.kika.smllybot.modules.user.ui;

import com.kika.smllybot.modules.user.GlobalProfileContext;
import com.kika.smllybot.utils.TimeUtil;

import net.dv8tion.jda.api.components.actionrow.ActionRow;
import net.dv8tion.jda.api.components.buttons.Button;
import net.dv8tion.jda.api.components.container.Container;
import net.dv8tion.jda.api.components.container.ContainerChildComponent;
import net.dv8tion.jda.api.components.mediagallery.MediaGallery;
import net.dv8tion.jda.api.components.mediagallery.MediaGalleryItem;
import net.dv8tion.jda.api.components.section.Section;
import net.dv8tion.jda.api.components.separator.Separator;
import net.dv8tion.jda.api.components.textdisplay.TextDisplay;
import net.dv8tion.jda.api.components.thumbnail.Thumbnail;
import net.dv8tion.jda.api.entities.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public abstract class GlobalProfileUI {

    public static Container buildProfile(GlobalProfileContext ctx) {
        boolean isOwner = ctx.target().getIdLong() == ctx.viewer().getIdLong();
        // Красивое отображение ирис-коинов: 123.456.789
        String irisCoin = String.format(Locale.GERMAN, "%,d", ctx.data().dbBank().irisCoin());

        // Даты
        String discordCreated = TimeUtil.getDiscordTimestamp(ctx.target().getTimeCreated());
        String botCreated = TimeUtil.getBotTimestamp(ctx.data().dbUser().createdAt());

        // Аватар
        String avatarUrl = ctx.target().getEffectiveAvatarUrl();

        // Баннер
        User.Profile profile = ctx.target().retrieveProfile().complete();
        String bannerUrl = null;
        if (profile.getBanner() != null) {
            bannerUrl = profile.getBanner().getUrl(1024);
        }

        // О себе
        String aboutMe = ctx.data().dbUser().motto();
        if (aboutMe == null || aboutMe.isBlank()) {
            aboutMe = "Пользователь не указал описание.";
        }

        // Статус пользователя (онлайн / оффлайн)
        String status = UserStatus.getEmoji(ctx.member());

        // Главная секция (самая верхняя)
        Section main = Section.of(
                Thumbnail.fromUrl(avatarUrl),
                TextDisplay.of("# 🗿 Это [%s](discord://discord.com/users/%s)%s"
                        .formatted(ctx.target().getEffectiveName(), ctx.target().getIdLong(), status)),
                TextDisplay.of("### 🐾 Девиз:"),
                TextDisplay.of(aboutMe)
        );

        // Секция экономики (самая нижняя)
        Section economy = Section.of(
                Button.primary("bag::meow::" + ctx.target().getId(), "мяу"),
                TextDisplay.of("### 💰 Мешок"),
                TextDisplay.of("🌼 **%d** ирисок".formatted(ctx.data().dbBank().iris())),
                TextDisplay.of("☢️ **%s** i¢".formatted(irisCoin))
        );

        // Средняя секция (посередине)
        ContainerChildComponent mid0 = TextDisplay.of("🕐 Во вселенной дискорд с **%s**".formatted(discordCreated));
        ContainerChildComponent mid1 = TextDisplay.of("⌛ Во вселенной бота с **%s**".formatted(botCreated));
        ContainerChildComponent mid2 = TextDisplay.of("🆔 IDB `%d`".formatted(ctx.data().dbUser().internalId()));
        ContainerChildComponent mid3 = TextDisplay.of("🆔 IDD `%d`".formatted(ctx.data().dbUser().discordId()));

        // Подвал
        ContainerChildComponent down0 = TextDisplay.of("-# **IDB** - айди внутри бота. **IDD** - айди внутри дискорд.");

        List<ContainerChildComponent> components = new ArrayList<>(15);

        // Баннер если есть
        if (profile.getBanner() != null) {
            components.add(MediaGallery.of(MediaGalleryItem.fromUrl(bannerUrl)));
        }

        // Главная секция
        components.add(main);
        components.add(Separator.createDivider(Separator.Spacing.SMALL));

        // Средняя секция
        components.add(mid0);
        components.add(mid1);
        components.add(mid2);
        components.add(mid3);
        components.add(Separator.createDivider(Separator.Spacing.SMALL));

        components.add(economy);

        // Управление (пред нижняя)
        if (isOwner) {
            components.add(TextDisplay.of("### Кнопки управления профилем"));
            components.add(Separator.createDivider(Separator.Spacing.SMALL));
            components.add(ActionRow.of(Button.primary("motto::modal::" + ctx.target().getId(), "Редактировать профиль")));
        }

        // Подвал
        components.add(down0);

        return Container.of(components);
    }
}

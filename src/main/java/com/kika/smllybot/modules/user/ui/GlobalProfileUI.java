package com.kika.smllybot.modules.user.ui;

import com.kika.smllybot.modules.user.GlobalProfileContext;
import com.kika.smllybot.utils.Formatter;
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

public abstract class GlobalProfileUI {

    public static Container buildProfile(GlobalProfileContext ctx) {
        boolean isOwner = ctx.target().getIdLong() == ctx.viewer().getIdLong();

        String idUser = Formatter.germanNum(ctx.user().internalId());
        long discordId = ctx.user().discordId();

        // Красивое отображение ирис-коинов: 123.456.789
        String irisCoin = Formatter.germanNum(ctx.bank().irisCoin());
        String iris = Formatter.germanNum(ctx.bank().iris());
        String star = Formatter.germanNum(ctx.bank().star());

        // Даты
        String discordCreated = TimeUtil.getDiscordTimestamp(ctx.target().getTimeCreated());
        String botCreated = TimeUtil.getBotTimestamp(ctx.user().createdAt());

        // Аватар
        String avatarUrl = ctx.target().getEffectiveAvatarUrl();

        // Баннер
        User.Profile profile = ctx.target().retrieveProfile().complete();
        String bannerUrl = null;
        if (profile.getBanner() != null) {
            bannerUrl = profile.getBanner().getUrl(1024);
        }

        // О себе
        String aboutMe = ctx.user().motto();
        if (aboutMe == null || aboutMe.isBlank()) {
            aboutMe = "Пользователь не указал описание.";
        }

        // Статус пользователя (онлайн / оффлайн)
        String status = UserStatus.getEmoji(ctx.member());

        // Главная секция
        Section main = Section.of(
                Thumbnail.fromUrl(avatarUrl),
                TextDisplay.of("# \\🗿 Это %s%s".formatted(ctx.target().getEffectiveName(), status)),
                TextDisplay.of("### Девиз:"),
                TextDisplay.of(aboutMe)
        );

        // Средняя секция
        ContainerChildComponent headerMid = TextDisplay.of("## \\👀 Краткая информация: ");
        ContainerChildComponent midRole = TextDisplay.of("\\👑 Создатель бота");
        String activityVisible = "";
        if (ctx.privacy().activity()) activityVisible = "-# Скрыто (видно только вам)";
        ContainerChildComponent mid = TextDisplay.of("""
                \\📊 **Активность** (день | нед | мес | всего): %s | %s | %s | %s
                %s
                """.formatted(-1, -1, -1, -1, activityVisible));
        ContainerChildComponent mid0 = TextDisplay.of("\\🕐 Во вселенной дискорд с **%s**".formatted(discordCreated));
        ContainerChildComponent mid1 = TextDisplay.of("\\⌛ Во вселенной бота с **%s**".formatted(botCreated));
        ContainerChildComponent mid2 = TextDisplay.of("IDB `%s`".formatted(idUser));
        ContainerChildComponent mid3 = TextDisplay.of("IDD `%d`".formatted(discordId));

        // Секция экономики (самая нижняя)
        String bagVisible = "";
        if (ctx.privacy().bag()) bagVisible = "-# Скрыто (видно только вам)";
        ContainerChildComponent economy0 = TextDisplay.of("""
                ### \\💰 Мешок
                %s
                """.formatted(bagVisible));
        ContainerChildComponent economy1 = TextDisplay.of("\\🍬 **%s** ирисок | \\⭐ **%s** звездочек".formatted(iris, star));
        ContainerChildComponent economy2 = TextDisplay.of("\\☢️ **%s** i¢".formatted(irisCoin));

        // Футер
        ContainerChildComponent footer = TextDisplay.of("-# **IDB** - айди внутри бота. **IDD** - айди внутри дискорд.");

        List<ContainerChildComponent> components = new ArrayList<>(15);

        // Баннер если есть
        if (profile.getBanner() != null) components.add(MediaGallery.of(MediaGalleryItem.fromUrl(bannerUrl)));

        // Главная секция
        components.add(main);
        components.add(Separator.createDivider(Separator.Spacing.SMALL));

        // Средняя секция
        components.add(headerMid);
        if (ctx.user().role() != null && ctx.user().role()
                .equalsIgnoreCase("owner")) components.add(midRole);

        // Если не скрыто - показать активность
        if (!ctx.privacy().bag()) components.add(mid);
        else if (isOwner) components.add(mid);

        components.add(mid0); components.add(mid1); components.add(mid2); components.add(mid3);

        // Если не скрыто - показать мешок
        if (!ctx.privacy().bag()) {
            components.add(Separator.createDivider(Separator.Spacing.SMALL));
            components.add(economy0); components.add(economy1); components.add(economy2);
        } else if (isOwner) {
            components.add(Separator.createDivider(Separator.Spacing.SMALL));
            components.add(economy0); components.add(economy1); components.add(economy2);
        }


        // Управление
        if (isOwner) {
            components.add(TextDisplay.of("### Кнопки управления профилем"));
            components.add(Separator.createDivider(Separator.Spacing.SMALL));
            components.add(ActionRow.of(
                    Button.primary("profile:modal:" + ctx.target().getId(), "ℹ️ Редактировать профиль"),
                    Button.secondary("private:private:" + ctx.target().getId(), "🕶️ Приватность")
            ));
        }

        // Подвал
        components.add(footer);

        return Container.of(components);
    }
}

// TODO: Это вообще пиздец, полностью переделать

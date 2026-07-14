package com.kika.smllybot.modules.user.ui;

import com.kika.smllybot.modules.user.GlobalProfileContext;
import com.kika.smllybot.utils.Formatter;
import com.kika.smllybot.utils.TimeUtil;
import net.dv8tion.jda.api.components.actionrow.ActionRow;
import net.dv8tion.jda.api.components.buttons.Button;
import net.dv8tion.jda.api.components.container.Container;
import net.dv8tion.jda.api.components.container.ContainerChildComponent;
import net.dv8tion.jda.api.components.section.Section;
import net.dv8tion.jda.api.components.separator.Separator;
import net.dv8tion.jda.api.components.textdisplay.TextDisplay;
import net.dv8tion.jda.api.components.thumbnail.Thumbnail;

import java.util.ArrayList;
import java.util.List;

public abstract class GlobalProfileUI {

    public static Container buildProfile(GlobalProfileContext ctx) {
        List<ContainerChildComponent> components = new ArrayList<>(15);

        String invisible = "-# Скрыто (видно только вам)";

        boolean isOwner = ctx.target().getIdLong() == ctx.viewer().getIdLong();

        String idUser = Formatter.germanNum(ctx.user().internalId());
        long discordId = ctx.user().discordId();

        // Красивое отображение ирис-коинов: 123.456.789
        String irisCoin = Formatter.germanNum(ctx.bank().irisCoin());
        String iris = Formatter.germanNum(ctx.bank().iris());
        String star = Formatter.germanNum(ctx.bank().star());

        // Даты
        String discordCreated = TimeUtil.getTimestamp(ctx.target().getTimeCreated());
        String discordCreatedRelative = TimeUtil.getTimestampRelative(ctx.target().getTimeCreated());
        String botCreated = TimeUtil.getTimestamp(ctx.user().createdAt());
        String botCreatedRelative = TimeUtil.getTimestampRelative(ctx.user().createdAt());

        // Аватар
        String avatarUrl = ctx.target().getEffectiveAvatarUrl();

        // Девиз
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
        // Краткая информация:
        ContainerChildComponent headerMid = TextDisplay.of("## \\👀 Краткая информация: ");
        // Активность
        String activityVisible = "";
        if (ctx.privacy().activity()) activityVisible = invisible;
        ContainerChildComponent mid = TextDisplay.of("""
                \\📊 **Активность** (день | нед | мес | всего): %s | %s | %s | %s
                %s
                """.formatted(-1, -1, -1, -1, activityVisible));

        // Во вселенной дискорда с ...
        ContainerChildComponent mid0 = TextDisplay.of("\\🕐 Во вселенной дискорд с **%s** (**%s**)"
                .formatted(discordCreated, discordCreatedRelative));

        // Во вселенной бота с ...
        ContainerChildComponent mid1 = TextDisplay.of("\\⌛ Во вселенной бота с **%s** (**%s**)"
                .formatted(botCreated, botCreatedRelative));

        // Айдишечки
        ContainerChildComponent mid2 = TextDisplay.of("IDB `%s`".formatted(idUser));
        ContainerChildComponent mid3 = TextDisplay.of("IDD `%d`".formatted(discordId));

        // Секция экономики (самая нижняя)
        String bagVisible = "";
        if (ctx.privacy().bag()) bagVisible = invisible;
        ContainerChildComponent economyHeader = TextDisplay.of("""
                ### \\💰 Мешок
                %s
                """.formatted(bagVisible));
        ContainerChildComponent economy1 = TextDisplay.of("\\🍬 **%s** ирисок | \\⭐ **%s** звездочек".formatted(iris, star));
        ContainerChildComponent economy2 = TextDisplay.of("\\☢️ **%s** i¢".formatted(irisCoin));

        // Футер
        ContainerChildComponent footer = TextDisplay.of("-# **IDB** - айди внутри бота. **IDD** - айди внутри дискорд.");

        // Это ...
        // Девиз: ...
        components.add(main);
        components.add(Separator.createDivider(Separator.Spacing.SMALL));

        // Краткая информация:
        components.add(headerMid);

        // Если не скрыто - показать активность
        if (!ctx.privacy().bag()) components.add(mid);
        else if (isOwner) components.add(mid);

        // Во вселенной дискорда с ...
        components.add(mid0);
        // Во вселенной бота с ...
        components.add(mid1);
        // Айди в дискорде и боте
        components.add(mid2);
        components.add(mid3);

        // Если не скрыто - показать мешок
        if (!ctx.privacy().bag()) {
            components.add(Separator.createDivider(Separator.Spacing.SMALL));
            // Мешок
            components.add(economyHeader);
            // ... ирисок | ... звездочек
            components.add(economy1);
            // ... i¢
            components.add(economy2);
        } else if (isOwner) {
            components.add(Separator.createDivider(Separator.Spacing.SMALL));
            components.add(economyHeader);
            components.add(economy1);
            components.add(economy2);
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

        // IDB - айди внутри бота. IDD - айди внутри дискорд.
        components.add(footer);

        return Container.of(components);
    }

}

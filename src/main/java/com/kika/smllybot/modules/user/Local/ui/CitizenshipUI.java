package com.kika.smllybot.modules.user.Local.ui;

import com.kika.smllybot.modules.user.Local.CitizenshipContext;
import com.kika.smllybot.utils.TimeUtil;
import net.dv8tion.jda.api.components.buttons.Button;
import net.dv8tion.jda.api.components.container.Container;
import net.dv8tion.jda.api.components.container.ContainerChildComponent;
import net.dv8tion.jda.api.components.section.Section;
import net.dv8tion.jda.api.components.separator.Separator;
import net.dv8tion.jda.api.components.textdisplay.TextDisplay;
import net.dv8tion.jda.api.entities.Guild;

import java.util.ArrayList;
import java.util.List;

public class CitizenshipUI {

    public static Container buildCitizenship(CitizenshipContext ctx) {
        List<ContainerChildComponent> components = new ArrayList<>();

        ContainerChildComponent header = TextDisplay.of(ctx.text());
        ContainerChildComponent separator = Separator.createDivider(Separator.Spacing.SMALL);
        ContainerChildComponent main = TextDisplay.of("Ваше гражданство оформлено в **%s**".formatted(ctx.guild().getName()));

        components.add(header);
        components.add(separator);
        components.add(main);

        return Container.of(components);
    }

    public static Container buildDeleteCitizenship(CitizenshipContext ctx) {
        List<ContainerChildComponent> components = new ArrayList<>();
        Long citizenship = ctx.user().getCitizenship();
        Guild guild = ctx.guild();
        String mainText;

        String time = TimeUtil.getTimestamp(ctx.user().getCitizenshipData());
        String timeRelative = TimeUtil.getTimestampRelative(ctx.user().getCitizenshipData());

        ContainerChildComponent header = Section.of(
                Button.danger("citizenship:yes:discordId", "Подтвердить"),
                TextDisplay.of("## \\❌ Вы уверены, что хотите удалить гражданство?")
        );

        if (guild.getJDA().getGuildById(citizenship) != null) {
            mainText = "Ваше гражданство в **%s** оформлено уже %s (%s)"
                    .formatted(guild.getJDA().getGuildById(citizenship).getName(), timeRelative, time);
        } else {
            mainText = "Ваше гражданство в (ID: **%d**) оформлено уже %s (%s)"
                    .formatted(citizenship, timeRelative, time);
        }
        ContainerChildComponent main = TextDisplay.of(mainText);
        ContainerChildComponent footer = TextDisplay.of("-# Из-за особенностей Discord вместо названия гильдии может быть айди");

        components.add(header);
        components.add(main);
        components.add(footer);

        return Container.of(components);
    }

    public static Container buildError() {
        List<ContainerChildComponent> components = new ArrayList<>();

        ContainerChildComponent header = TextDisplay.of("## \\❌ У вас и так нет гражданства");

        components.add(header);

        return Container.of(components);
    }

    public static Container buildDefaultCitizenship(CitizenshipContext ctx) {
        List<ContainerChildComponent> components = new ArrayList<>();

        Long citizenship = ctx.user().getCitizenship();

        String mainText;

        if (citizenship == null) {
            mainText = "У вас нет гражданства \\😭";
        } else {
            Guild targetGuild = ctx.guild().getJDA().getGuildById(citizenship);

            if (targetGuild != null) {
                mainText = "Ваше гражданство оформлено в **%s**".formatted(targetGuild.getName());
            } else {
                mainText = "Сервер гражданства (**ID:** `%d`) не найден в кэше бота".formatted(citizenship);
            }
        }

        ContainerChildComponent header = TextDisplay.of("## \\🛂 Текущее гражданство");
        ContainerChildComponent separator = Separator.createDivider(Separator.Spacing.SMALL);
        ContainerChildComponent main = TextDisplay.of(mainText);

        components.add(header);
        components.add(separator);
        components.add(main);

        return Container.of(components);
    }


}

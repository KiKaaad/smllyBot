package com.kika.smllybot.modules.user.Local.ui;

import com.kika.smllybot.modules.user.Local.CitizenshipContext;
import net.dv8tion.jda.api.components.container.Container;
import net.dv8tion.jda.api.components.container.ContainerChildComponent;
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

    public static Container buildDeleteCitizenship() {
        List<ContainerChildComponent> components = new ArrayList<>();

        ContainerChildComponent header = TextDisplay.of("## \\❌ Гражданство удалено");

        components.add(header);

        return Container.of(components);
    }

    public static Container buildDefaultCitizenship(CitizenshipContext ctx) {
        List<ContainerChildComponent> components = new ArrayList<>();

        Long citizenship = ctx.user().citizenship();

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

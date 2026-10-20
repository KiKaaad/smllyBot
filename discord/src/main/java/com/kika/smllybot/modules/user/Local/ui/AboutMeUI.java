package com.kika.smllybot.modules.user.Local.ui;

import com.kika.smllybot.modules.user.Local.AboutMeContext;
import net.dv8tion.jda.api.components.buttons.Button;
import net.dv8tion.jda.api.components.container.Container;
import net.dv8tion.jda.api.components.container.ContainerChildComponent;
import net.dv8tion.jda.api.components.section.Section;
import net.dv8tion.jda.api.components.separator.Separator;
import net.dv8tion.jda.api.components.textdisplay.TextDisplay;

import java.util.ArrayList;
import java.util.List;

public class AboutMeUI {

    public static Container buildAboutMe(AboutMeContext ctx) {
        List<ContainerChildComponent> components = new ArrayList<>();

        String aboutMe = "Пользователь не рассказал о себе";
        if (ctx.aboutMe() != null) aboutMe = ctx.aboutMe();

        ContainerChildComponent main = Section.of(
                Button.primary("aboutMe:back:" + ctx.discordId(), "◀️ Назад"),
                TextDisplay.of(ctx.main())
        );
        ContainerChildComponent separator = Separator.createDivider(Separator.Spacing.SMALL);
        ContainerChildComponent content = TextDisplay.of("""
                ### О себе:
                %s
                """.formatted(aboutMe));

        components.add(main);
        components.add(separator);
        components.add(content);
        return Container.of(components);
    }

}

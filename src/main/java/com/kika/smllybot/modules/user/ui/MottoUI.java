package com.kika.smllybot.modules.user.ui;

import com.kika.smllybot.database.sql.user.dto.UserAccount;
import net.dv8tion.jda.api.components.buttons.Button;
import net.dv8tion.jda.api.components.container.Container;
import net.dv8tion.jda.api.components.section.Section;
import net.dv8tion.jda.api.components.separator.Separator;
import net.dv8tion.jda.api.components.textdisplay.TextDisplay;
import net.dv8tion.jda.api.entities.User;

public class MottoUI {

    public static Container buildMotto(User user, UserAccount dbUser, String title, boolean showHeader) {
        String mottoDisplay = dbUser.motto() == null ? "Пользователь не указал описание." : dbUser.motto();

        Section topSection = Section.of(
                Button.primary("motto:back:" + user.getId(), "◀️ Назад"),
                TextDisplay.of("## " + title)
        );

        if (showHeader) {
            return Container.of(
                    topSection,
                    Separator.createDivider(Separator.Spacing.SMALL),
                    TextDisplay.of("**Описание:**"),
                    TextDisplay.of(mottoDisplay)
            );
        } else {
            return Container.of(
                    topSection,
                    Separator.createDivider(Separator.Spacing.SMALL),
                    TextDisplay.of(mottoDisplay)
            );
        }
    }
}

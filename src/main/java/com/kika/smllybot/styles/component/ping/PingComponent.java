package com.kika.smllybot.styles.component.ping;

import com.kika.smllybot.modules.ping.PingData;
import net.dv8tion.jda.api.components.container.Container;
import net.dv8tion.jda.api.components.separator.Separator;
import net.dv8tion.jda.api.components.textdisplay.TextDisplay;

public class PingComponent {

    public static Container render(PingData data) {
        return Container.of(
                TextDisplay.of("## 🏓 Понг!"),
                TextDisplay.of("Задержка **Rest**: `%d мс`".formatted(data.rest())),
                TextDisplay.of("Задержка **Gateway**: `%d мс`".formatted(data.gateway())),
                Separator.createDivider(Separator.Spacing.SMALL),
                TextDisplay.of("-# **Rest** - скорость обработки действия Discord API. **Gateway** - скорость реакции Discord API")
        );
    }

}

package com.kika.smllybot.modules.privacy.ui;

import com.kika.smllybot.modules.privacy.PrivacyContext;
import net.dv8tion.jda.api.components.buttons.Button;
import net.dv8tion.jda.api.components.container.Container;
import net.dv8tion.jda.api.components.container.ContainerChildComponent;
import net.dv8tion.jda.api.components.section.Section;
import net.dv8tion.jda.api.components.separator.Separator;
import net.dv8tion.jda.api.components.textdisplay.TextDisplay;

import java.util.ArrayList;
import java.util.List;

public class PrivacyUI {

    public static Container buildPrivacyUI(PrivacyContext ctx) {

        List<ContainerChildComponent> components = new ArrayList<>(10);

        Button buttonBag;
        Button buttonActivity;
        Button buttonLastActivity;
        if (!ctx.privacy().bag()) {
            buttonBag = Button.success("PrivacyInteraction:bag:off:" + ctx.discordId(), "✅");
        } else {
            buttonBag = Button.danger("PrivacyInteraction:bag:on:" + ctx.discordId(), "❌");
        }
        if (!ctx.privacy().activity()) {
            buttonActivity = Button.success("PrivacyInteraction:activity:off:" + ctx.discordId(), "✅");
        } else {
            buttonActivity = Button.danger("PrivacyInteraction:activity:on:" + ctx.discordId(), "❌");
        }
        if (!ctx.privacy().lastActivity()) {
            buttonLastActivity = Button.success("PrivacyInteraction:lastActivity:off:" + ctx.discordId(), "✅");
        } else {
            buttonLastActivity = Button.danger("PrivacyInteraction:lastActivity:on:" + ctx.discordId(), "❌");
        }


        ContainerChildComponent header = TextDisplay.of("# \\👀 Настройки приватности");
        ContainerChildComponent separator = Separator.createDivider(Separator.Spacing.SMALL);
        ContainerChildComponent bag = Section.of(
                buttonBag,
                TextDisplay.of("## \\💰 Могут ли видеть ваш мешок?"),
                TextDisplay.of("""
                        Мешок отображает баланс ирисок, ирис-коинов и звездочек.
                        Если выбрано, мешок будет виден только вам""")
        );
        ContainerChildComponent activity = Section.of(
                buttonActivity,
                TextDisplay.of("## \\📊 Могут ли видеть вашу активность?"),
                TextDisplay.of("""
                        Активность отображает ваш онлайн за день, неделю, месяц и все время
                        Если выбрано, активность будет видна только вам""")
        );
        ContainerChildComponent lastActivity = Section.of(
                buttonLastActivity,
                TextDisplay.of("## \\🟢 Могут ли видеть время вашей последней активности?"),
                TextDisplay.of("""
                        Последняя активность отображает время, когда вы последний раз писали сообщения
                        Если выбрать, последняя активность будет заменена на «Был(а) недавно», «Был(а) давно»""")
        );

        components.add(header);
        components.add(separator);
        components.add(bag);
        components.add(separator);
        components.add(activity);
        components.add(separator);
        components.add(lastActivity);

        return Container.of(components);
    }

}

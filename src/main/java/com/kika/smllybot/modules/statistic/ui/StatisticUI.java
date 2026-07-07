package com.kika.smllybot.modules.statistic.ui;

import com.kika.smllybot.modules.statistic.StatisticContext;
import com.sun.management.OperatingSystemMXBean;
import net.dv8tion.jda.api.components.container.Container;
import net.dv8tion.jda.api.components.container.ContainerChildComponent;
import net.dv8tion.jda.api.components.mediagallery.MediaGallery;
import net.dv8tion.jda.api.components.mediagallery.MediaGalleryItem;
import net.dv8tion.jda.api.components.section.Section;
import net.dv8tion.jda.api.components.separator.Separator;
import net.dv8tion.jda.api.components.textdisplay.TextDisplay;
import net.dv8tion.jda.api.components.thumbnail.Thumbnail;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.kika.smllybot.Main.VERSION;
import static com.kika.smllybot.Main.OWNER;
import static com.kika.smllybot.listeners.MessageCounter.messageCount;

public abstract class StatisticUI {

    public static Container buildStatistic(StatisticContext ctx) {
        List<ContainerChildComponent> components = new ArrayList<>(10);

        // TODO: 90% информации здесь должно быть в контексте, а не в самом файле, это портит логику гуи
        OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
        long RAM = osBean.getTotalMemorySize() / 1048576;
        String RAMUserFriendly = String.format(Locale.GERMAN, "%,d", RAM);
        long freeRAM = RAM - osBean.getFreeMemorySize() / 1048576;
        String freeRAMUserFriendly = String.format(Locale.GERMAN, "%,d", freeRAM);
        double systemLoad = osBean.getCpuLoad();
        double systemLoadVerified = systemLoad * 100;
        String systemLoadUserFriendly = String.format(Locale.US, "%.2f", systemLoadVerified);

        double diffRAM = (double) freeRAM / RAM * 100;
        String diffRAMUserFriendly = String.format(Locale.US, "%.2f", diffRAM);

        String systemLoadEmoji = null;
        if (systemLoadVerified <= 25) systemLoadEmoji = "⚡";
        if (systemLoadVerified > 25) systemLoadEmoji = "🟢";
        if (systemLoadVerified >= 50) systemLoadEmoji = "🟡";
        if (systemLoadVerified >= 75) systemLoadEmoji = "🟠";
        if (systemLoadVerified > 95) systemLoadEmoji = "🔴";
        if (systemLoadVerified > 99) systemLoadEmoji = "💀 ВСЁ";

        // Шапка
        Section header = Section.of(
                Thumbnail.fromUrl(ctx.botAvatarUrl()),
                TextDisplay.of("# \\📊 Статистика бота"),
                TextDisplay.of("\\%s Нагрузка: **%s%%**".formatted(systemLoadEmoji, systemLoadUserFriendly)),
                TextDisplay.of("ОЗУ: **%s / %s МБ** (занято %s%%)".formatted(freeRAMUserFriendly, RAMUserFriendly, diffRAMUserFriendly))
        );
        ContainerChildComponent headerOther = TextDisplay.of("## \\🌃 Прочая статистика");
        ContainerChildComponent servers = TextDisplay.of("Серверов: **%d**".formatted(ctx.serversCount()));
        ContainerChildComponent users = TextDisplay.of("Пользователей: **%d** | Учтено сообщений: **%d**".formatted(ctx.userCount(), messageCount));
        ContainerChildComponent shards = TextDisplay.of("Количество шардов: **%d**".formatted(ctx.shardTotal()));
        ContainerChildComponent version = TextDisplay.of("Версия JDA **%s** | Версия бота **%s**".formatted(ctx.jdaVersion(), VERSION));
        ContainerChildComponent copyright = TextDisplay.of("-# \\©️ 2026 [KiKa](https://t.me/KiKaaad) | " + OWNER);

        if (ctx.botAvatarUrl() != null) components.add(MediaGallery.of(MediaGalleryItem.fromUrl(ctx.botBannerUrl())));

        components.add(header);
        components.add(Separator.createDivider(Separator.Spacing.SMALL));

        components.add(headerOther);

        components.add(servers);
        components.add(users);
        components.add(shards);
        components.add(version);

        components.add(copyright);

        return Container.of(components);
    }

}

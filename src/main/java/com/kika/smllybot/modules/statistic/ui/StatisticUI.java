package com.kika.smllybot.modules.statistic.ui;

import com.kika.smllybot.database.sql.user.UserTable;
import com.kika.smllybot.modules.statistic.StatisticContext;
import com.kika.smllybot.utils.Formatter;
import com.sun.management.OperatingSystemMXBean;
import net.dv8tion.jda.api.components.container.Container;
import net.dv8tion.jda.api.components.container.ContainerChildComponent;
import net.dv8tion.jda.api.components.mediagallery.MediaGallery;
import net.dv8tion.jda.api.components.mediagallery.MediaGalleryItem;
import net.dv8tion.jda.api.components.section.Section;
import net.dv8tion.jda.api.components.separator.Separator;
import net.dv8tion.jda.api.components.textdisplay.TextDisplay;
import net.dv8tion.jda.api.components.thumbnail.Thumbnail;
import net.dv8tion.jda.api.entities.User;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;

import static com.kika.smllybot.Main.OWNER;
import static com.kika.smllybot.Main.VERSION;
import static com.kika.smllybot.listeners.MessageCounter.messageCount;

public abstract class StatisticUI {

    public static Container buildStatistic(StatisticContext ctx) {
        List<ContainerChildComponent> components = new ArrayList<>(12);


        User.Profile user = ctx.user().retrieveProfile().complete();
        String banner = null;
        if (user.getBanner() != null) banner = user.getBanner().getUrl(1024);

        OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
        // Получение общего количества памяти и свободного
        long RAM = osBean.getTotalMemorySize() / 1048576;
        long freeRAM = RAM - osBean.getFreeMemorySize() / 1048576;

        // Загрузка ЦП: 12.34%
        double systemLoad = osBean.getCpuLoad() * 100;

        // Красивое форматирование: 123456 -> 123.456
        // ОЗУ
        String RAMFriendly = Formatter.germanNum(RAM);
        String freeRAMFriendly = Formatter.germanNum(freeRAM);
        // ЦП
        String systemLoadFriendly = Formatter.usNum(systemLoad);

        double diffRAM = (double) freeRAM / RAM * 100;
        String diffRAMFriendly = Formatter.usNum(diffRAM);

        String systemLoadEmoji = null;
        if (systemLoad <= 25) systemLoadEmoji = "⚡";
        if (systemLoad > 25) systemLoadEmoji = "🟢";
        if (systemLoad >= 50) systemLoadEmoji = "🟡";
        if (systemLoad >= 75) systemLoadEmoji = "🟠";
        if (systemLoad > 95) systemLoadEmoji = "🔴";
        if (systemLoad > 99) systemLoadEmoji = "💀 ВСЁ";

        // Шапка
        Section header = Section.of(
                Thumbnail.fromUrl(ctx.botAvatarUrl()),
                TextDisplay.of("# \\📊 Статистика бота"),
                TextDisplay.of("\\%s Использовани ЦП **%s%%**".formatted(systemLoadEmoji, systemLoadFriendly)),
                TextDisplay.of("Использование ОЗУ **%s / %s МБ** (занято %s%%)"
                        .formatted(freeRAMFriendly, RAMFriendly, diffRAMFriendly))
        );
        ContainerChildComponent headerOther = TextDisplay.of("## \\🌃 Прочая статистика");
        ContainerChildComponent servers = TextDisplay.of("Серверов: **%d**".formatted(ctx.serversCount()));
        ContainerChildComponent users = TextDisplay.of("Пользователей (в кэше / бд): **%d** / **%d** | Учтено сообщений: **%d**"
                .formatted(ctx.userCount(), UserTable.getTotalUsers().Total(), messageCount));
        ContainerChildComponent shards = TextDisplay.of("Количество шардов: **%d**".formatted(ctx.shardTotal()));
        ContainerChildComponent version = TextDisplay.of("Версия JDA **%s** | Версия бота **%s**"
                .formatted(ctx.jdaVersion(), VERSION));
        ContainerChildComponent copyright = TextDisplay.of("-# \\©️ 2026 [KiKa](https://t.me/KiKaaad) | " + OWNER);

        if (user.getBanner() != null) components.add(MediaGallery.of(MediaGalleryItem.fromUrl(banner)));

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

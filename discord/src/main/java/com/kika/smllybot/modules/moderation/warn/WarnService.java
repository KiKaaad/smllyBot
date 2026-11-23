package com.kika.smllybot.modules.moderation.warn;

import com.kika.smllybot.database.sql.guild.warn.WarnTable;
import com.kika.smllybot.database.sql.guild.warn.dto.WarnCreateData;
import com.kika.smllybot.modules.moderation.TargetUser;
import com.kika.smllybot.modules.moderation.warn.ui.WarnUI;
import com.kika.smllybot.utils.TimeUtil;
import net.dv8tion.jda.api.components.container.Container;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.UserSnowflake;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.time.OffsetDateTime;

public class WarnService {

    public final WarnTable warnTable;
    public WarnService(WarnTable warnTable) {
        this.warnTable = warnTable;
    }

    protected void warn(MessageReceivedEvent event, Member moderator, TargetUser target) {
        if (target.getUser().getIdLong() == 0) return;
        long targetId = target.getUser().getIdLong();
        int targetWarns = warnTable.getMemberActiveWarns(targetId);

        Guild guild = event.getGuild();

        OffsetDateTime until = TimeUtil.calculateUntil(target.getDuration(), target.getUnit());

        if (targetWarns < 2) {
            WarnCreateData data = new WarnCreateData(
                    guild.getIdLong(),
                    targetId,
                    moderator.getIdLong(),
                    target.getReason(),
                    until
            );

            warnTable.createWarn(data);

            int targetWarnBefore = warnTable.getMemberActiveWarns(targetId);
            Container response = WarnUI.build(data, until, targetWarnBefore);

            event.getChannel().sendMessageComponents(response).useComponentsV2(true).queue();
        } else {
            guild.kick(target.getUser()).reason(target.getReason()).queue();
            warnTable.clearMemberWarns(targetId, guild.getIdLong());

            Container response = WarnUI.buildKick(targetId);
            event.getChannel().sendMessageComponents(response).useComponentsV2().queue();
        }
    }

    protected void unWarnAll(MessageReceivedEvent event, Member moderator, UserSnowflake target) {
        warnTable.clearMemberWarns(target.getIdLong(), event.getGuild().getIdLong());

        Container response = WarnUI.buildUnWarn(target.getIdLong());
        event.getChannel().sendMessageComponents(response).useComponentsV2(true).queue();
    }

}

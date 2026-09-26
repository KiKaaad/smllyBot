package com.kika.smllybot.modules.moderation.mute;

import com.kika.smllybot.database.sql.mute.MuteTable;
import com.kika.smllybot.database.sql.mute.dto.MuteCreateData;
import com.kika.smllybot.handler.ErrorThrow;
import com.kika.smllybot.modules.moderation.mute.ui.MuteUI;
import com.kika.smllybot.utils.TimeUtil;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.time.OffsetDateTime;

public class MuteService {

    protected static void mute(MessageReceivedEvent event, Member target,
                               Member moderator, String rawAmount, String unit, String reason) {
        Guild guild = event.getGuild();

        if (!guild.getSelfMember().canInteract(target)) {
            ErrorThrow.smallPermission(event);
            return;
        }

//        if (!moderator.canInteract(target)) {
//            log.error("❌ Вы не можете замутить этого пользователя: Роль пользователя выше вашей роли.");
//            return;
//        }

        long amount;
        try {
            amount = Long.parseLong(rawAmount);
        } catch (NumberFormatException e) {
            ErrorThrow.notNumber(event, rawAmount);
            return;
        }

        OffsetDateTime until = TimeUtil.calculateUntil(amount, unit);

        target.timeoutUntil(until).reason(reason).queue(
                success -> {
                    MuteCreateData data = new MuteCreateData(
                            "TIMEOUT",
                            guild.getIdLong(),
                            target.getIdLong(),
                            moderator.getIdLong(),
                            reason,
                            until
                    );
                    MuteTable.createMute(data);

                    var response = MuteUI.mute(target.getIdLong(), moderator.getIdLong(), until, reason);
                    event.getChannel().sendMessageComponents(response).useComponentsV2(true).queue();
                },
                error -> ErrorThrow.undefined(event, error.getMessage())
        );
    }

    protected static void unMute(MessageReceivedEvent event, Member target, Member moderator) {
        Guild guild = event.getGuild();

        if (!guild.getSelfMember().canInteract(target)) {
            ErrorThrow.smallPermission(event);
            return;
        }

//        if (!moderator.canInteract(target)) {
//            log.error("❌ Вы не можете замутить этого пользователя: Роль пользователя выше вашей роли.");
//            return;
//        }

        var response = MuteUI.unmute(target.getIdLong());

        MuteTable.markAsUnmuted(target.getIdLong(), moderator.getIdLong(), target.getGuild().getIdLong());
        target.removeTimeout().queue();
        event.getChannel().sendMessageComponents(response).useComponentsV2(true).queue();
    }

}

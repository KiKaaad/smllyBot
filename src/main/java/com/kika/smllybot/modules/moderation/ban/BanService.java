package com.kika.smllybot.modules.moderation.ban;

import com.kika.smllybot.database.sql.ban.BanTable;
import com.kika.smllybot.database.sql.ban.dto.BanCreateData;
import com.kika.smllybot.handler.ErrorThrow;
import com.kika.smllybot.modules.moderation.ban.ui.BanUI;
import com.kika.smllybot.modules.moderation.mute.ui.MuteUI;
import com.kika.smllybot.utils.TimeUtil;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.time.OffsetDateTime;
import java.util.concurrent.TimeUnit;

public class BanService {

    private final BanTable banTable;
    public BanService(BanTable banTable) {
        this.banTable = banTable;
    }

    protected void ban(MessageReceivedEvent event, Member target,
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

        Long amount = null;
        if (rawAmount != null && unit != null) {
            try {
                amount = Long.parseLong(rawAmount);
            } catch (NumberFormatException e) {
                ErrorThrow.notNumber(event, rawAmount);
                return;
            }
        }

        OffsetDateTime until = TimeUtil.calculateUntil(amount, unit);

        target.ban(0, TimeUnit.MINUTES).reason(reason).queue(
                success -> {
                    BanCreateData data = new BanCreateData(
                            guild.getIdLong(),
                            target.getIdLong(),
                            moderator.getIdLong(),
                            reason,
                            until
                    );
                    banTable.createBan(data);

                    var response = BanUI.ban(target.getIdLong(), moderator.getIdLong(), until, reason);
                    event.getChannel().sendMessageComponents(response).useComponentsV2(true).queue();
                },
                error -> ErrorThrow.undefined(event, error.getMessage())
        );
    }

    protected void unban(MessageReceivedEvent event, User target, Member moderator) {
        Guild guild = event.getGuild();

//        if (!moderator.canInteract(target)) {
//            log.error("❌ Вы не можете замутить этого пользователя: Роль пользователя выше вашей роли.");
//            return;
//        }

        var response = BanUI.unban(target.getIdLong());

        banTable.markAsUnbanned(target.getIdLong(), moderator.getIdLong(), guild.getIdLong());
        guild.unban(target).queue();
        event.getChannel().sendMessageComponents(response).useComponentsV2(true).queue();
    }

}

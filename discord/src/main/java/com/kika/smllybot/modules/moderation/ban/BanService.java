package com.kika.smllybot.modules.moderation.ban;

import com.kika.smllybot.database.sql.guild.ban.BanTable;
import com.kika.smllybot.database.sql.guild.ban.dto.BanCreateData;
import com.kika.smllybot.handler.ErrorThrow;
import com.kika.smllybot.modules.moderation.TargetUser;
import com.kika.smllybot.modules.moderation.ban.ui.BanUI;
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

    protected void ban(MessageReceivedEvent event, Member moderator, TargetUser target) {
        if (target.getUser().getIdLong() == 0) return;
        long targetId = target.getUser().getIdLong();

        Guild guild = event.getGuild();

        guild.ban(target.getUser(), 0, TimeUnit.MINUTES).reason(target.getReason()).queue(
                success -> {
                    OffsetDateTime until = TimeUtil.calculateUntil(target.getDuration(), target.getUnit());

                    BanCreateData data = new BanCreateData(
                            guild.getIdLong(),
                            targetId,
                            moderator.getIdLong(),
                            target.getReason(),
                            until
                    );
                    banTable.createBan(data);

                    var response = BanUI.ban(targetId, moderator.getIdLong(), until, target.getReason());

                    event.getChannel().sendMessageComponents(response).useComponentsV2(true).queue();
                    }, throwable -> ErrorThrow.userNotFound(event, String.valueOf(targetId))
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
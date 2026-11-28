package com.kika.smllybot.modules.moderation.mute;

import com.kika.smllybot.database.sql.guild.mute.MuteTable;
import com.kika.smllybot.database.sql.guild.mute.dto.MuteCreateData;
import com.kika.smllybot.handler.ErrorThrow;
import com.kika.smllybot.modules.moderation.TargetUser;
import com.kika.smllybot.modules.moderation.mute.ui.MuteUI;
import com.kika.smllybot.utils.SendComponents;
import com.kika.smllybot.utils.TimeUtil;
import net.dv8tion.jda.api.components.container.Container;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class MuteService {

    private final MuteTable muteTable;

    public MuteService(MuteTable muteTable) {
        this.muteTable = muteTable;
    }

    protected CompletableFuture<List<Container>> mute(Guild guild, Member moderator, TargetUser targetUser) {

//        if (!moderator.canInteract(target)) {
//            log.error("❌ Вы не можете замутить этого пользователя: Роль пользователя выше вашей роли.");
//            return;
//        }

        OffsetDateTime until = TimeUtil.calculateUntil(targetUser.getDuration(), targetUser.getUnit());

        return guild.timeoutUntil(targetUser.getUser(), until).reason(targetUser.getReason()).submit().thenApply(
                success -> {
                    MuteCreateData data = new MuteCreateData(
                            "TIMEOUT",
                            guild.getIdLong(),
                            targetUser.getUser().getIdLong(),
                            moderator.getIdLong(),
                            targetUser.getReason(),
                            until
                    );
                    muteTable.createMute(data);

                    return List.of(MuteUI.mute(targetUser.getUser().getIdLong(), moderator.getIdLong(), until, targetUser.getReason()));
                }).exceptionally(error -> ErrorThrow.undefined(error.getMessage()));
    }

    protected void unMute(MessageReceivedEvent event, Member target, Member moderator) {
        Guild guild = event.getGuild();

        if (!guild.getSelfMember().canInteract(target)) {
            SendComponents.sendTo15(event, ErrorThrow.smallPermission());
            return;
        }

//        if (!moderator.canInteract(target)) {
//            log.error("❌ Вы не можете замутить этого пользователя: Роль пользователя выше вашей роли.");
//            return;
//        }

        var response = MuteUI.unmute(target.getIdLong());

        muteTable.markAsUnmuted(target.getIdLong(), moderator.getIdLong(), target.getGuild().getIdLong());
        target.removeTimeout().queue();
        event.getChannel().sendMessageComponents(response).useComponentsV2(true).queue();
    }

}

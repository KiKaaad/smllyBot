package com.kika.smllybot.modules.moderation.mute;

import com.kika.smllybot.database.sql.mute.MuteTable;
import com.kika.smllybot.database.sql.mute.dto.MuteCreateData;
import com.kika.smllybot.handler.ErrorThrow;
import com.kika.smllybot.modules.moderation.mute.ui.MuteUI;
import com.kika.smllybot.other.BaseCmd;
import com.kika.smllybot.utils.TimeUtil;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.components.container.Container;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.time.OffsetDateTime;
import java.util.Set;

public class Mute extends BaseCmd {

    public Mute() {
        super(Set.of("мут", "мьют"));
    }

    @Override
    public Container execute(MessageReceivedEvent event, String raw, String args) {
        if (!event.isFromGuild()) return null;
        if (!event.getMember().hasPermission(Permission.MODERATE_MEMBERS)) {
            ErrorThrow.noAccessPermission(event, Permission.MODERATE_MEMBERS);

            return null;
        }

        long author = event.getAuthor().getIdLong();
        long guildId = event.getGuild().getIdLong();

        String[] matches = raw.trim().split("\\n", 2);

        String firstLine = matches[0].trim();
        String[] parts = firstLine.split("\\h+", 4);

        if (event.getMessage().getReferencedMessage() != null) {
            long discordId = event.getMessage().getReferencedMessage().getAuthor().getIdLong();
            long untilRaw;
            Member member = event.getMessage().getReferencedMessage().getMember();

            OffsetDateTime until;
            
            if (parts.length > 1 && !parts[1].isBlank()) {
                untilRaw = Long.parseLong(parts[1]);
                until = TimeUtil.calculateUntil(untilRaw, parts[2]);
            } else {
                ErrorThrow.timeOverhead(event);
                return null;
            }

            String reason = null;
            if (matches.length >= 2 && !matches[1].isBlank()) reason = matches[1];

            MuteCreateData data = new MuteCreateData(
                    "TIMEOUT",
                    guildId,
                    discordId,
                    author,
                    reason,
                    until
            );
            MuteTable.createMute(data);

            member.timeoutUntil(until).reason(reason).queue();

            var response = MuteUI.build(discordId, author, until, reason);
            event.getChannel().sendMessageComponents(response).useComponentsV2(true).queue();

            return null;
        }

        String arg = parts[1];

        if (!event.getMessage().getMentions().getUsers().isEmpty()) {
            long discordId = event.getMessage().getMentions().getUsers().getFirst().getIdLong();

            OffsetDateTime until = TimeUtil.calculateUntil(Long.parseLong(parts[2]), parts[3]);

            String reason = null;
            if (matches.length > 1 && !matches[1].isBlank()) reason = matches[1];

            MuteCreateData data = new MuteCreateData(
                    "TIMEOUT",
                    guildId,
                    discordId,
                    author,
                    reason,
                    until
            );
            MuteTable.createMute(data);

            var response = MuteUI.build(discordId, author, until, reason);
            event.getChannel().sendMessageComponents(response).useComponentsV2(true).queue();

            return null;
        }

//        if (arg.matches("\\d+")) {
//            event.getJDA().retrieveUserById(arg).queue(
//                    targetUser -> sendAnketaResponse(event, targetUser),
//                    throwable -> sendError(event, "### \\❌ Упс... Пользователь с таким ID не найден")
//            );
//            return null;
//        }

//        var members = event.getGuild().getMembersByName(arg, true);
//
//        if (members.isEmpty()) {
//            members = event.getGuild().getMembersByNickname(arg, true);
//        }
//
//        if (!members.isEmpty()) {
//            sendMuteResponse(event, members.getFirst().getUser());
//        } else {
//            sendError(event, "### \\❌ Упс... Пользователь с таким юзернеймом не найден");
//        }
        return null;
    }

}

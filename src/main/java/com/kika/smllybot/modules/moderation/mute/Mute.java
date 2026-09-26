package com.kika.smllybot.modules.moderation.mute;

import com.kika.smllybot.database.sql.guild.GuildTable;
import com.kika.smllybot.database.sql.guild.dto.GuildData;
import com.kika.smllybot.handler.ErrorThrow;
import com.kika.smllybot.other.BaseCmd;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.components.container.Container;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public class Mute extends BaseCmd {

    private static final Logger log = LoggerFactory.getLogger(Mute.class);

    public Mute() {
        super(Set.of("мут", "мьют", "unmute"));
    }

    @Override
    public Container execute(MessageReceivedEvent event, String raw, String args) {
        if (!event.isFromGuild()) return null;
        GuildData guildData = GuildTable.getOrCreateGuild(event.getGuild().getIdLong(), event.getGuild().getName());
        if (!guildData.getStaging()) {
            ErrorThrow.onlyOnStaging(event);
            return null;
        }

        Guild guild = event.getGuild();
        Member moderator = event.getMember();

        // Проверка прав модератора и бота
        if (moderator == null || !moderator.hasPermission(Permission.MODERATE_MEMBERS)) {
            ErrorThrow.noAccessPermission(event, Permission.MODERATE_MEMBERS);
            return null;
        }

        if (!guild.getSelfMember().hasPermission(Permission.MODERATE_MEMBERS)) {
            ErrorThrow.noAccessPermissionBot(event, Permission.MODERATE_MEMBERS);
            return null;
        }

        String[] matches = raw.trim().split("\\n", 2);
        String firstLine = matches[0].trim();
        String[] parts = firstLine.split("\\h+");

        String reason = (matches.length >= 2 && !matches[1].isBlank()) ? matches[1].trim() : null;

        // Ответом на сообщение
        if (event.getMessage().getReferencedMessage() != null) {
            Member target = event.getMessage().getReferencedMessage().getMember();
            if (target == null) {
                log.error("❌ Не удалось получить участника из отвеченного сообщения.");
                return null;
            }

            if (parts.length < 3) {
                ErrorThrow.timeoutOverhead(event);
                return null;
            }

            MuteService.mute(event, target, moderator, parts[1], parts[2], reason);
            return null;
        }

        // Упоминание -> @username
        if (!event.getMessage().getMentions().getMembers().isEmpty()) {
            Member target = event.getMessage().getMentions().getMembers().getFirst();

            if (parts.length < 4) {
                ErrorThrow.timeoutOverhead(event);
                return null;
            }

            MuteService.mute(event, target, moderator, parts[2], parts[3], reason);
            return null;
        }

        if (parts.length < 2) {
            return null;
        }

        String arg = parts[1];

        // ID -> 12345678910121314
        if (arg.matches("\\d+")) {
            long targetId = Long.parseLong(arg);
            guild.retrieveMemberById(targetId).queue(
                    target -> {
                        if (parts.length < 4) {
                            ErrorThrow.timeoutOverhead(event);
                            return;
                        }
                        MuteService.mute(event, target, moderator, parts[2], parts[3], reason);
                    },
                    failure -> ErrorThrow.userNotFound(event, arg)
            );
            return null;
        }

        // Никнейм -> kefichik.
        var members = guild.getMembersByName(arg, true);
        if (members.isEmpty()) {
            members = guild.getMembersByNickname(arg, true);
        }

        if (!members.isEmpty()) {
            if (parts.length < 4) {
                ErrorThrow.timeoutOverhead(event);
                return null;
            }
            MuteService.mute(event, members.getFirst(), moderator, parts[2], parts[3], reason);
        } else {
            ErrorThrow.userNotFound(event, members.getFirst().getNickname());
        }

        return null;
    }

}

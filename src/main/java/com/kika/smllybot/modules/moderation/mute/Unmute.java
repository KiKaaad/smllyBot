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

import java.util.Set;

public class Unmute extends BaseCmd {

    public Unmute() {
        super(Set.of("размут", "размьют"));
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

        // Ответом на сообщение
        if (event.getMessage().getReferencedMessage() != null) {
            Member target = event.getMessage().getReferencedMessage().getMember();
            if (target == null) {
                return null;
            }

            MuteService.unMute(event, target, moderator);
            return null;
        }

        // Упоминание -> @username
        if (!event.getMessage().getMentions().getMembers().isEmpty()) {
            Member target = event.getMessage().getMentions().getMembers().getFirst();

            MuteService.unMute(event, target, moderator);
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
                        MuteService.unMute(event, target, moderator);
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
            MuteService.unMute(event, members.getFirst(), moderator);
        } else {
            ErrorThrow.userNotFound(event, members.getFirst().getNickname());
        }

        return null;
    }

}

package com.kika.smllybot.modules.moderation.ban;

import com.kika.smllybot.database.sql.guild.guild.GuildTable;
import com.kika.smllybot.database.sql.guild.guild.dto.GuildData;
import com.kika.smllybot.handler.ErrorThrow;
import com.kika.smllybot.other.BaseCmd;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.components.container.Container;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Set;

public class Unban extends BaseCmd {

    private final BanService banService;
    public Unban(BanService banService) {
        super(Set.of("разбан", "unban"));
        this.banService = banService;
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
        if (moderator == null || !moderator.hasPermission(Permission.BAN_MEMBERS)) {
            ErrorThrow.noAccessPermission(event, Permission.BAN_MEMBERS);
            return null;
        }

        if (!guild.getSelfMember().hasPermission(Permission.BAN_MEMBERS)) {
            ErrorThrow.noAccessPermissionBot(event, Permission.BAN_MEMBERS);
            return null;
        }

        String[] matches = raw.trim().split("\\n", 2);
        String firstLine = matches[0].trim();
        String[] parts = firstLine.split("\\h+");

        // Ответом на сообщение
        if (event.getMessage().getReferencedMessage() != null) {
            User target = event.getMessage().getReferencedMessage().getAuthor();

            banService.unban(event, target, moderator);
            return null;
        }

        // Упоминание -> @username
        if (!event.getMessage().getMentions().getUsers().isEmpty()) {
            User target = event.getMessage().getMentions().getUsers().getFirst();

            banService.unban(event, target, moderator);
            return null;
        }

        if (parts.length < 2) {
            return null;
        }

        String arg = parts[1];

        // ID -> 12345678910121314
        if (arg.matches("\\d+")) {
            long targetId = Long.parseLong(arg);
            event.getJDA().retrieveUserById(targetId).queue(
                    target -> banService.unban(event, target, moderator),
                    failure -> ErrorThrow.userNotFound(event, arg)
            );
            return null;
        }

        return null;
    }

}

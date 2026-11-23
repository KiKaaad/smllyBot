package com.kika.smllybot.modules.moderation.ban;

import com.kika.smllybot.database.sql.guild.guild.GuildTable;
import com.kika.smllybot.database.sql.guild.guild.dto.GuildData;
import com.kika.smllybot.handler.ErrorThrow;
import com.kika.smllybot.modules.moderation.FindUser;
import com.kika.smllybot.other.BaseCmd;
import com.kika.smllybot.utils.SendComponents;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.components.container.Container;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.UserSnowflake;
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
            SendComponents.sendTo15(event, ErrorThrow.onlyOnStaging());
            return null;
        }

        Guild guild = event.getGuild();
        Member moderator = event.getMember();

        // Проверка прав модератора и бота
        if (moderator == null || !moderator.hasPermission(Permission.BAN_MEMBERS)) {
            SendComponents.sendTo15(event, ErrorThrow.noAccessPermission(Permission.BAN_MEMBERS));
            return null;
        }

        if (!guild.getSelfMember().hasPermission(Permission.BAN_MEMBERS)) {
            SendComponents.sendTo15(event, ErrorThrow.noAccessPermissionBot(Permission.BAN_MEMBERS));
            return null;
        }

        FindUser findUser = new FindUser();
        UserSnowflake userSnowflake = findUser.find(event.getMessage(), args);

        event.getJDA().retrieveUserById(userSnowflake.getIdLong()).queue(
                success -> banService.unban(event, success, moderator),
                throwable -> SendComponents.sendTo15(event, ErrorThrow.userNotFound(userSnowflake.toString()))
        );

        return null;
    }

}

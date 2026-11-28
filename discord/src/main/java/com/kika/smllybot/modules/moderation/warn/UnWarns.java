package com.kika.smllybot.modules.moderation.warn;

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

public class UnWarns extends BaseCmd {

    public final WarnService warnService;
    public UnWarns(WarnService warnService) {
        super(Set.of("-варны", "снять варны"));
        this.warnService = warnService;
    }

    @Override
    public Container execute(MessageReceivedEvent event, String raw, String args) {
        if (!event.isFromGuild()) return null;

        GuildData guildData = GuildTable.getOrCreateGuild(event.getGuild().getIdLong(), event.getGuild().getName());
        if (!guildData.getStaging()) {
            SendComponents.sendTo15(event, ErrorThrow.onlyOnStaging());
            return null;
        }

        if (!event.getMessage().getMember().hasPermission(Permission.KICK_MEMBERS)) {
            SendComponents.sendTo15(event, ErrorThrow.noAccessPermission(Permission.KICK_MEMBERS));
            return null;
        }

        Guild guild = event.getGuild();
        Member moderator = event.getMember();

        if (!guild.getSelfMember().hasPermission(Permission.KICK_MEMBERS)) {
            SendComponents.sendTo15(event, ErrorThrow.noAccessPermissionBot(Permission.KICK_MEMBERS));
            return null;
        }

        FindUser findUser = new FindUser();
        UserSnowflake userSnowflake = findUser.find(event.getMessage(), args);

        warnService.unWarnAll(event, moderator, userSnowflake);

        return null;
    }

}

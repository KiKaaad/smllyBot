package com.kika.smllybot.modules.moderation.ban;

import com.kika.smllybot.database.sql.guild.guild.GuildTable;
import com.kika.smllybot.database.sql.guild.guild.dto.GuildData;
import com.kika.smllybot.handler.ErrorThrow;
import com.kika.smllybot.modules.moderation.TargetUser;
import com.kika.smllybot.modules.moderation.Typification;
import com.kika.smllybot.other.BaseCmd;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.components.container.Container;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Set;

public class Ban extends BaseCmd {

    private final BanService banService;
    public Ban(BanService banService) {
        super(Set.of("бан"));
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

        // Если бот не имеет прав
        if (!guild.getSelfMember().hasPermission(Permission.BAN_MEMBERS)) {
            ErrorThrow.noAccessPermissionBot(event, Permission.BAN_MEMBERS);
            return null;
        }

        Typification typification = new Typification();
        TargetUser context = typification.find(event, raw);

        banService.ban(event, moderator, context);

        return null;
    }

}
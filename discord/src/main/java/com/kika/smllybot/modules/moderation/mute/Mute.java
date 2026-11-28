package com.kika.smllybot.modules.moderation.mute;

import com.kika.smllybot.database.sql.guild.guild.GuildTable;
import com.kika.smllybot.database.sql.guild.guild.dto.GuildData;
import com.kika.smllybot.handler.ErrorThrow;
import com.kika.smllybot.modules.moderation.TargetUser;
import com.kika.smllybot.modules.moderation.Typification;
import com.kika.smllybot.other.BaseCmd;
import com.kika.smllybot.utils.SendComponents;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.components.container.Container;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.UserSnowflake;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public class Mute extends BaseCmd {

    private static final Logger log = LoggerFactory.getLogger(Mute.class);

    private final MuteService muteService;

    public Mute(MuteService muteService) {
        super(Set.of("мут", "мьют", "mute"));
        this.muteService = muteService;
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
        if (moderator == null || !moderator.hasPermission(Permission.MODERATE_MEMBERS)) {
            SendComponents.sendTo15(event, ErrorThrow.noAccessPermission(Permission.MODERATE_MEMBERS));
            return null;
        }

        if (!guild.getSelfMember().hasPermission(Permission.MODERATE_MEMBERS)) {
            SendComponents.sendTo15(event, ErrorThrow.noAccessPermission(Permission.MODERATE_MEMBERS));
            return null;
        }

        Typification typification = new Typification();
        TargetUser targetUser = typification.find(event, args);

        muteService.mute(event.getGuild(), moderator, targetUser)
                .thenAccept(containers -> {
                    event.getChannel().sendMessageComponents(containers).useComponentsV2(true).queue();
                });

        return null;
    }

    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("мут")) {
            event.deferReply().queue();
            Member moderator = event.getMember();

            UserSnowflake user = event.getOptionsByName("юзер").getFirst().getAsUser();
            Long duration = event.getOptionsByName("время").getFirst().getAsLong();
            String unit = event.getOptionsByName("интервал").getFirst().getAsString();
            String reason = event.getOptionsByName("причина").getFirst().getAsString();

            TargetUser targetUser = new TargetUser(user, duration, unit, reason);

            muteService.mute(event.getGuild(), moderator, targetUser)
                    .thenAccept(containers -> event.getHook()
                            .sendMessageComponents(containers)
                            .useComponentsV2(true)
                            .queue());
        }
    }

}

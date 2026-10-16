package com.kika.smllybot.modules.moderation.ban;

import com.kika.smllybot.annotations.ButtonPrefix;
import com.kika.smllybot.annotations.ModalPrefix;
import com.kika.smllybot.database.sql.ban.BanTable;
import com.kika.smllybot.database.sql.ban.dto.BanData;
import com.kika.smllybot.database.sql.guild.GuildTable;
import com.kika.smllybot.database.sql.guild.dto.GuildData;
import com.kika.smllybot.handler.ErrorThrow;
import com.kika.smllybot.modules.moderation.ban.ui.BanListUI;
import com.kika.smllybot.modules.moderation.mute.MuteListContext;
import com.kika.smllybot.modules.moderation.mute.ui.MuteListUI;
import com.kika.smllybot.other.BaseCmd;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.components.container.Container;
import net.dv8tion.jda.api.components.label.Label;
import net.dv8tion.jda.api.components.textinput.TextInput;
import net.dv8tion.jda.api.components.textinput.TextInputStyle;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.modals.Modal;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class BanList extends BaseCmd {

    private final BanTable banTable;

    public BanList(BanTable banTable) {
        super(Set.of("банлист", "banlist"));
        this.banTable = banTable;
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

        List<BanData> banData = banTable.getBanList(guild.getIdLong());
        BanListContext context = new BanListContext(banData, moderator.getIdLong());
        var response = BanListUI.build(context, 1);

        event.getChannel().sendMessageComponents(response).useComponentsV2(true)
                .setAllowedMentions(Collections.emptyList())
                .queue();

        return null;
    }

    @ButtonPrefix(prefix = "ban_list")
    public void onButton(ButtonInteractionEvent event, String[] args) {
        String[] parts = event.getComponentId().split(":");
        long owner = event.getUser().getIdLong();

        // Сначала модалочка
        if (parts[1].equals("select")) {
            TextInput pageInput = TextInput.create("page_num", TextInputStyle.SHORT)
                    .setPlaceholder("Введите страницу")
                    .setMinLength(1)
                    .setMaxLength(4)
                    .setRequired(true)
                    .build();

            Modal modal = Modal.create("ban_list_modal:" + ":" + owner, "🔍 Перейти на страницу")
                    .addComponents(Label.of("Страница", pageInput))
                    .build();

            event.replyModal(modal).queue();
            return;
        }

        Container response;
        // И уже потом парсим в инт, а то все сломается
        int page = Integer.parseInt(parts[1]);

        List<BanData> banData = banTable.getBanList(event.getGuild().getIdLong());
        BanListContext ctx = new BanListContext(banData, owner);
        response = BanListUI.build(ctx, page);

        event.editComponents(response).useComponentsV2(true).queue();
    }

    @ModalPrefix(prefix = "ban_list_modal")
    public void onModal(ModalInteractionEvent event, String[] args) {
        String[] parts = event.getModalId().split(":");
        long owner = Long.parseLong(parts[2]);

        String inputPage = event.getValue("page_num").getAsString();
        int targetPage = 1;

        try {
            targetPage = Integer.parseInt(inputPage.trim());
        } catch (NumberFormatException ignored) {}

        Container response = buildResponse(event.getGuild().getIdLong(), targetPage, owner);

        event.editComponents(response).useComponentsV2(true).queue();
    }

    private Container buildResponse(long guildId, int page, long owner) {
        List<BanData> banData = banTable.getBanList(guildId);

        BanListContext ctx = new BanListContext(banData, owner);
        return BanListUI.build(ctx, page);
    }

}

package com.kika.smllybot.modules.moderation.mypunishments;

import com.kika.smllybot.annotations.ButtonPrefix;
import com.kika.smllybot.annotations.ModalPrefix;
import com.kika.smllybot.database.sql.guild.mute.MuteTable;
import com.kika.smllybot.database.sql.guild.mute.dto.MuteData;
import com.kika.smllybot.modules.moderation.mypunishments.ui.MyMutesUI;
import com.kika.smllybot.other.BaseCmd;
import net.dv8tion.jda.api.components.container.Container;
import net.dv8tion.jda.api.components.label.Label;
import net.dv8tion.jda.api.components.textinput.TextInput;
import net.dv8tion.jda.api.components.textinput.TextInputStyle;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.modals.Modal;

import java.util.List;
import java.util.Set;

public class MyMutes extends BaseCmd {

    private final MuteTable muteTable;
    public MyMutes(MuteTable muteTable) {
        super(Set.of("мои муты", "my mutes"));
        this.muteTable = muteTable;
    }

    @Override
    public Container execute(MessageReceivedEvent event, String raw, String args) {
        long discordId = event.getAuthor().getIdLong();

        List<MuteData> muteData = muteTable.getMemberMutes(discordId);

        Container response = MyMutesUI.build(muteData, 1, discordId);

        event.getChannel().sendMessageComponents(response).useComponentsV2(true).queue();

        return null;
    }

    @ButtonPrefix(prefix = "my_mute_list")
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

            Modal modal = Modal.create("my_ban_list_modal:" + ":" + owner, "🔍 Перейти на страницу")
                    .addComponents(Label.of("Страница", pageInput))
                    .build();

            event.replyModal(modal).queue();
            return;
        }

        Container response;
        // И уже потом парсим в инт, а то все сломается
        int page = Integer.parseInt(parts[1]);

        List<MuteData> muteData = muteTable.getMemberMutes(event.getUser().getIdLong());

        response = MyMutesUI.build(muteData, page, owner);

        event.editComponents(response).useComponentsV2(true).queue();
    }

    @ModalPrefix(prefix = "my_mute_list_modal")
    public void onModal(ModalInteractionEvent event, String[] args) {
        String[] parts = event.getModalId().split(":");
        long owner = Long.parseLong(parts[2]);

        String inputPage = event.getValue("page_num").getAsString();
        int targetPage = 1;

        try {
            targetPage = Integer.parseInt(inputPage.trim());
        } catch (NumberFormatException ignored) {}

        Container response = buildResponse(event.getGuild().getIdLong(), targetPage, owner, event);

        event.editComponents(response).useComponentsV2(true).queue();
    }

    private Container buildResponse(long guildId, int page, long owner, ModalInteractionEvent event) {
        List<MuteData> muteData = muteTable.getMemberMutes(guildId);

        return MyMutesUI.build(muteData, page, owner);
    }
}

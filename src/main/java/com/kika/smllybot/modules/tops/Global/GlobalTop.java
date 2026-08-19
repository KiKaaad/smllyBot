package com.kika.smllybot.modules.tops.Global;

import com.kika.smllybot.annotations.ButtonPrefix;
import com.kika.smllybot.annotations.ModalPrefix;
import com.kika.smllybot.database.sql.bank.BankTable;
import com.kika.smllybot.database.sql.bank.dto.BankTopAmount;
import com.kika.smllybot.modules.tops.Global.ui.GlobalTopUI;
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

public class GlobalTop extends BaseCmd {

    public GlobalTop() {
        super(Set.of("гтоп", "gtop"));
    }

    @Override
    public Container execute(MessageReceivedEvent event, String raw, String args) {

        String[] parts = raw.trim().split("\\h+", 2);
        String value = parts[1];
        long owner = event.getAuthor().getIdLong();

        switch (value) {
            case "ириски" -> {
                List<BankTopAmount> topAmount = BankTable.getTopIris();
                GlobalTopContext ctx = new GlobalTopContext(topAmount, "iris", owner);
                var response = GlobalTopUI.build(ctx, 1);

                event.getChannel().sendMessageComponents(response).useComponentsV2(true).queue();
            }
            case "коины" -> {
                List<BankTopAmount> topAmount = BankTable.getTopIrisCoins();
                GlobalTopContext ctx = new GlobalTopContext(topAmount, "coin", owner);
                var response = GlobalTopUI.build(ctx, 1);

                event.getChannel().sendMessageComponents(response).useComponentsV2(true).queue();
            }
            default -> {
                return null;
            }
        }

        return null;
    }

    @ButtonPrefix(prefix = "gtop")
    public void onButton(ButtonInteractionEvent event, String[] args) {
        String[] parts = event.getComponentId().split(":");
        long owner = event.getUser().getIdLong();

        // Сначала модалочка
        if (parts[1].equals("select")) {
            String type = parts[2];

            TextInput pageInput = TextInput.create("page_num", TextInputStyle.SHORT)
                    .setPlaceholder("Введите страницу")
                    .setMinLength(1)
                    .setMaxLength(4)
                    .setRequired(true)
                    .build();

            Modal modal = Modal.create("gtop_modal:" + type + ":" + owner, "🔍 Перейти на страницу")
                    .addComponents(Label.of("Страница", pageInput))
                    .build();

            event.replyModal(modal).queue();
            return;
        }

        Container response;
        // И уже потом парсим в инт, а то все сломается
        int page = Integer.parseInt(parts[1]);

        switch (parts[2]) {
            case "coin" -> {
                List<BankTopAmount> topAmount = BankTable.getTopIrisCoins();
                GlobalTopContext ctx = new GlobalTopContext(topAmount, "coin", owner);
                response = GlobalTopUI.build(ctx, page);
            }
            case "iris" -> {
                List<BankTopAmount> topAmount = BankTable.getTopIris();
                GlobalTopContext ctx = new GlobalTopContext(topAmount, "iris", owner);
                response = GlobalTopUI.build(ctx, page);
            }
            default -> {
                return;
            }
        }

        event.editComponents(response).useComponentsV2(true).queue();
    }

    @ModalPrefix(prefix = "gtop_modal")
    public void onModal(ModalInteractionEvent event, String[] args) {
        String[] parts = event.getModalId().split(":");
        String type = parts[1];
        long owner = Long.parseLong(parts[2]);

        String inputPage = event.getValue("page_num").getAsString();
        int targetPage = 1;

        try {
            targetPage = Integer.parseInt(inputPage.trim());
        } catch (NumberFormatException ignored) {}

        Container response = buildResponse(type, targetPage, owner);

        event.editComponents(response).useComponentsV2(true).queue();
    }

    private Container buildResponse(String type, int page, long owner) {
        List<BankTopAmount> topAmount = type.equals("coin")
                ? BankTable.getTopIrisCoins()
                : BankTable.getTopIris();

        GlobalTopContext ctx = new GlobalTopContext(topAmount, type, owner);
        return GlobalTopUI.build(ctx, page);
    }
}
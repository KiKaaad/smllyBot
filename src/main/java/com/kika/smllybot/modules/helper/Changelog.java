package com.kika.smllybot.modules.helper;

import com.kika.smllybot.modules.helper.ui.GlobalHelpUI;
import com.kika.smllybot.other.BaseCmd;
import net.dv8tion.jda.api.components.container.Container;
import net.dv8tion.jda.api.components.textdisplay.TextDisplay;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Set;

public class Changelog extends BaseCmd {

    public Changelog() {
        super(Set.of("чендж-лог", "ченджлог", "changelog"));
    }

    @Override
    public Container execute(MessageReceivedEvent event, String raw, String args) {

        var response = Container.of(
                TextDisplay.of("""
                        # 0.7.1-beta (30.09.2026)
                        ## ✨ Новое
                        - Команда `ченджлог` - показывает последние обновления дискорд-бота.
                        - Иллюстрации прямиком в `хелп`. Для удобства пользователей
                        теперь есть гифки с гайдом как пользоваться некоторыми командами
                        
                        ## 🐛 Исправления
                        - Теперь у мутов адекватные склонения: 1 час, 10 дней,\s
                        вместо "юзер лишается права слова на через 1 час"
                        - Т. к бот сейчас более заточен под русскоязычный сегмент, теперь используются
                        форматирования чисел как раз под ру-регион. То есть вместо 123.456 -> 123 456
                        
                        ## ⚙️ Внутренние изменения
                        - Муты стали более расширяемы за счет использования конструкторов\s
                        вместо статичных полей
                        """)
        );

        event.getChannel().sendMessageComponents(response)
                .useComponentsV2(true)
                .queue();

        return null;
    }
}

package com.kika.smllybot.modules.helper;

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
                        # 0.8.0-beta (06.10.2026)
                        ## ✨ Новое
                        - Команда `инфа` - позволяет узнать процент чего-то там. Любого вопроса который вы зададите
                        - Команда `данет` - позволяет узнать, правда ли то что вы сказали или ложь
                        - Команда `бан` - позволяет заблокировать пользователя. Поддерживает неограниченные сроки!
                        - Команда `разбан` - позволяет разблокировать пользователя
                        - Команда `банлист` - история всех блокировок в гильдии. Показывать дату бана, срок, причину бана и если пользователь был досрочно разблокирован, показывает кто его разбанил
                        
                        ## 🔄️ Изменено
                        - Новые алиасы к командам, например: `помощь`, `mute`, `unmute`
                        - Теперь по умолчанию ВСЕ пинги запрещены. Сделано во избежание уязвимостей в боте
                        
                        ## 🐛 Исправления
                        - Бот пытался конвертировать тип String в long из-за чего `статбот` не работала
                        
                        ## ⚙️ Внутренние изменения
                        - Для селф-хостеров я теперь делаю миграции. То есть, если вы хостите бота самостоятельно, при обновлении базы данных вам не придется вручную изменять свою базу
                        - SQL написано более "по-людски". С комментариями!!
                        - Теперь бот имеет красивые цветные логи
                        - Логи теперь еще и сохранаются в папку logs
                        - Автомаперы. Мне лень вручную делать 3 одних и тех же списка данных
                        """)
        );

        event.getChannel().sendMessageComponents(response)
                .useComponentsV2(true)
                .queue();

        return null;
    }
}

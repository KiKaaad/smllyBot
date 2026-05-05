package com.kika.smllybot.styles.text.ping;

import com.kika.smllybot.modules.ping.PingData;
import com.kika.smllybot.utils.localization.I18n;
import com.kika.smllybot.utils.localization.I18nRequest;

public class PingText {

    public static String render(PingData data) {
        var req = new I18nRequest("ru", "modules", "ping", "ping", "ping.response");
        return I18n.get(req).formatted(data.rest(), data.gateway());
    }

}

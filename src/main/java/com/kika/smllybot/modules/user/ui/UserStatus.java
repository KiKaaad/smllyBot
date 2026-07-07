package com.kika.smllybot.modules.user.ui;

import net.dv8tion.jda.api.entities.Member;

public enum UserStatus {

    ONLINE("<:online:1502685517349916703>"),
    IDLE("<:idle:1502685519308521522>"),
    DO_NOT_DISTURB("<:dnd:1502685514963357847>"),
    OFFLINE("<:invisible:1502685512220147854>"),
    BOT(" <:bot:1502710577435381913>");

    private final String emoji;

    UserStatus(String emoji) {
        this.emoji = emoji;
    }

    public static String getEmoji(Member member) {
        if (member == null) return " ...";
        if (member.getUser().isBot()) return BOT.emoji;

        return switch (member.getOnlineStatus()) {
            case ONLINE -> ONLINE.emoji;
            case IDLE -> IDLE.emoji;
            case DO_NOT_DISTURB -> DO_NOT_DISTURB.emoji;
            default -> OFFLINE.emoji;
        };
    }

}

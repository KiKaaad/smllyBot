package com.kika.smllybot.modules.user.Global.ui;

import net.dv8tion.jda.api.entities.Member;

public enum UserStatus {

    ONLINE("\\🟢"),
    IDLE("\\🌙"),
    DO_NOT_DISTURB("\\🔴"),
    OFFLINE("\\💤"),
    BOT("<:bot:1502710577435381913>");

    private final String emoji;

    UserStatus(String emoji) {
        this.emoji = emoji;
    }

    public static String getEmoji(Member member) {
        if (member == null) return "\\❔";
        if (member.getUser().isBot()) return BOT.emoji;

        return switch (member.getOnlineStatus()) {
            case ONLINE -> ONLINE.emoji;
            case IDLE -> IDLE.emoji;
            case DO_NOT_DISTURB -> DO_NOT_DISTURB.emoji;
            default -> OFFLINE.emoji;
        };
    }

}

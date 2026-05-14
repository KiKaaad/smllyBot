package com.kika.smllybot.utils;

import net.dv8tion.jda.api.utils.TimeFormat;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

import static com.kika.smllybot.utils.Plural.getTimeType;

public class TimeUtil {

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    // Правильные склонения для фермы
    public static String formatTimeLeft(long timeLeft) {
        long h = TimeUnit.MILLISECONDS.toHours(timeLeft);
        long m = TimeUnit.MILLISECONDS.toMinutes(timeLeft) % 60;
        long s = TimeUnit.MILLISECONDS.toSeconds(timeLeft) % 60;

        StringBuilder sb = new StringBuilder();
        if (h > 0) {
            sb.append(h).append(" ").append(getTimeType(h, "час", "часа", "часов")).append(" ");
        }
        if (m > 0) {
            sb.append(m).append(" ").append(getTimeType(m, "минута", "минуты", "минут")).append(" ");
        }
        if (h == 0 && (s > 0 || (m == 0))) {
            sb.append(s).append(" ").append(getTimeType(s, "секунду", "секунды", "секунд"));
        }

        return sb.toString().trim();
    }

    // Таймстамп для профиля
    public static String getDiscordTimestamp(OffsetDateTime time) {
        return TimeFormat.DATE_TIME_SHORT.atTimestamp(time.toInstant().toEpochMilli()).toString();
    }

    public static String getBotTimestamp(String date) {
        if (date == null || date.isBlank()) return "Неизвестно ?";

        LocalDateTime dateTime = LocalDateTime.parse(date, DATE_TIME_FORMATTER);
        long millis = dateTime.toInstant(ZoneOffset.UTC).toEpochMilli();
        return TimeFormat.DATE_TIME_SHORT.atTimestamp(millis).toString();
    }

}

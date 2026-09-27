package com.kika.smllybot.utils;

import net.dv8tion.jda.api.utils.TimeFormat;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static com.kika.smllybot.utils.Plural.getTimeType;

public class TimeUtil {

    // Правильные склонения для времени
    public static String formatTimeLeft(OffsetDateTime until) {
        if (until == null) {
            return "навсегда";
        }

        long timeLeftMillis = Duration.between(Instant.now(), until.toInstant()).toMillis();
        return formatTimeLeft(timeLeftMillis);
    }

    public static String formatTimeLeft(Long timeLeft) {
        if (timeLeft == null || timeLeft <= 0) {
            return "0 секунд";
        }

        long totalSeconds = timeLeft / 1000;

        long days = totalSeconds / (24 * 3600);
        long remainingSecAfterDays = totalSeconds % (24 * 3600);

        long hours = remainingSecAfterDays / 3600;
        long remainingSecAfterHours = remainingSecAfterDays % 3600;

        long minutes = remainingSecAfterHours / 60;
        long seconds = remainingSecAfterHours % 60;

        if (days >= 30) {
            long months = days / 30;
            long remDays = days % 30;
            if (remDays > 0) {
                return months + " " + getTimeType(months, "месяц", "месяца", "месяцев") + " " +
                        remDays + " " + getTimeType(remDays, "день", "дня", "дней");
            }
            return months + " " + getTimeType(months, "месяц", "месяца", "месяцев");
        }

        if (days >= 7) {
            long weeks = days / 7;
            long remDays = days % 7;
            if (remDays > 0) {
                return weeks + " " + getTimeType(weeks, "неделю", "недели", "недель") + " " +
                        remDays + " " + getTimeType(remDays, "день", "дня", "дней");
            }
            return weeks + " " + getTimeType(weeks, "неделю", "недели", "недель");
        }

        if (days > 0) {
            if (hours > 0) {
                return days + " " + getTimeType(days, "день", "дня", "дней") + " " +
                        hours + " " + getTimeType(hours, "час", "часа", "часов");
            }
            return days + " " + getTimeType(days, "день", "дня", "дней");
        }

        if (hours > 0) {
            if (minutes > 0) {
                return hours + " " + getTimeType(hours, "час", "часа", "часов") + " " +
                        minutes + " " + getTimeType(minutes, "минуту", "минуты", "минут");
            }
            return hours + " " + getTimeType(hours, "час", "часа", "часов");
        }

        if (minutes > 0) {
            if (seconds > 0) {
                return minutes + " " + getTimeType(minutes, "минуту", "минуты", "минут") + " " +
                        seconds + " " + getTimeType(seconds, "секунду", "секунды", "секунд");
            }
            return minutes + " " + getTimeType(minutes, "минуту", "минуты", "минут");
        }

        return seconds + " " + getTimeType(seconds, "секунду", "секунды", "секунд");
    }

    // Таймстампы
    public static String getTimestamp(OffsetDateTime time) {
        return TimeFormat.DATE_TIME_SHORT.atTimestamp(time.toInstant().toEpochMilli()).toString();
    }

    public static String getTimestampRelative(OffsetDateTime time) {
        return TimeFormat.RELATIVE.atTimestamp(time.toInstant().toEpochMilli()).toString();
    }

    public static OffsetDateTime calculateUntil(long rawTime, String type) {
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);

        return switch (type.toLowerCase().trim()) {
            case "год", "года", "лет", "г", "л" -> now.plusYears(rawTime);
            case "месяц", "месяца", "месяцев", "мес" -> now.plusMonths(rawTime);
            case "неделю", "недели", "недель", "нед" -> now.plusWeeks(rawTime);
            case "день", "дня", "дней", "д" -> now.plusDays(rawTime);
            case "час", "часа", "часов", "ч" -> now.plusHours(rawTime);
            case "минуту", "минуты", "минут", "м" -> now.plusMinutes(rawTime);
            case "секунду", "секунды", "секунд", "с" -> now.plusSeconds(rawTime);
            case null, default -> null;
        };
    }

}

package study.bulletin_board.util;

import java.time.Duration;
import java.time.LocalDateTime;

public class TimeAgoUtil {

    public static String formatTimeAgo(LocalDateTime dateTime) {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(dateTime, now);

        long seconds = duration.getSeconds();

        if (seconds < 60) {
            return seconds + "초 전";
        } else if (seconds < 3600) {
            long minutes = seconds / 60;
            return minutes + "분 전";
        } else if (seconds < 86400) {
            long hours = seconds / 3600;
            return hours + "시간 전";
        } else {
            long days = seconds / 86400;
            return days + "일 전";
        }
    }
}

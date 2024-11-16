import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTime {

    public static String getDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.now();
        return dateTime.format(formatter);
    }
}

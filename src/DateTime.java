import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTime {

    public static String GetDateTime() {
        DateTimeFormatter _formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime _dateTime = LocalDateTime.now();
        return _dateTime.format(_formatter);
    }
}

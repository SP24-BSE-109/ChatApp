import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utility class to fetch the current date and time.
 */
public class DateTime {

    /**
     * Returns the current date and time in the format "yyyy-MM-dd HH:mm:ss".
     *
     * @return A formatted timestamp string.
     */
    public static String GetDateTime() {
        DateTimeFormatter _formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime _dateTime = LocalDateTime.now();
        return _dateTime.format(_formatter);
    }
}

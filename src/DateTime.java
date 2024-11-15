import java.time.LocalDateTime;
public class DateTime {

    private String dateTime;

    public String getDateTime() {
        LocalDateTime DateTime = LocalDateTime.now();
        return DateTime.toString();
    }
}

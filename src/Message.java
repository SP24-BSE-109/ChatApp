import java.util.Scanner;
public class Message {

    private String message;

    public Message GetMessage()
    {
        String dateTime;
        DateTime DateTime = new DateTime();
        dateTime = DateTime.getDateTime();

        Scanner msg = new Scanner(System.in);

        message = msg.nextLine();
        message = message + ("[")+dateTime+("]");
        return null;
    }

}

package il.cshaifasweng.OCSFMediatorExample.client;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import javafx.geometry.Pos;

public class PostNotifications {

    private static PostNotifications instance;

    private PostNotifications() {
        // Private constructor to prevent instantiation
    }

    public static synchronized PostNotifications getInstance() {
        if (instance == null) {
            instance = new PostNotifications();
        }
        return instance;
    }
    public void TaskNotification(UsersNotificationEvent event)
    {
        if (event.getNotification().getNotification().equals("Task Accepted")) {
            Notifications.create()
                    .title("Task Accepted!")
                    .text("Hooray " + event.getNotification().getAddressee().getGivenName() + "!\n" +
                            "The community manager has approved your task")
                    .hideAfter(Duration.seconds(5))
                    .position(Pos.TOP_LEFT)
                    .showConfirm();
        } else if(event.getNotification().getNotification().equals("Task Rejected")) {
            Notifications.create()
                    .title("Task Rejected")
                    .text("Sorry " + event.getNotification().getAddressee().getGivenName() + "...\n" +
                         "The community manager has rejected your task")
                    .hideAfter(Duration.seconds(5))
                    .position(Pos.TOP_LEFT)
                    .showError();

        }
    }
}

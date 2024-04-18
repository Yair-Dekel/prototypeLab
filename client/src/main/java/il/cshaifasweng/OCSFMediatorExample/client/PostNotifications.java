package il.cshaifasweng.OCSFMediatorExample.client;
import javafx.application.Platform;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import javafx.geometry.Pos;

import java.io.File;

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
        Platform.runLater(() -> {
            // Create a media file for the notification sound
            Media errorsound = new Media(new File("sorry_sound.wav").toURI().toString());
            MediaPlayer mediaPlayer1 = new MediaPlayer(errorsound);
            Media confirmsound = new Media(new File("yay_sound.mp3").toURI().toString());
            MediaPlayer mediaPlayer2 = new MediaPlayer(confirmsound);
            System.out.println("in notification");
            if (event.getNotification().getNotification().equals("Task Accepted")) {
                Notifications.create()
                        .title("Task Accepted!")
                        .text("Hooray " + event.getNotification().getAddressee().getGivenName() + "!\n" +
                                "The community manager has approved your task")
                        .hideAfter(Duration.seconds(5))
                        .position(Pos.TOP_LEFT)
                        .showInformation();
                mediaPlayer2.play();
            } else if(event.getNotification().getNotification().equals("Task Rejected")) {
                Notifications.create()
                        .title("Task Rejected")
                        .text("Sorry " + event.getNotification().getAddressee().getGivenName() + "...\n" +
                                "The community manager has rejected your task")
                        .hideAfter(Duration.seconds(5))
                        .position(Pos.TOP_LEFT)
                        .showError();
                mediaPlayer1.play();
            } else if (event.getNotification().getNotification().equals("Volunteer Found")) {
                Notifications.create()
                        .title("Volunteer found")
                        .text("Lucky you! "+  event.getNotification().getUserInvolved().getUsername()+ " volunteered for your task at this moment ")
                        .hideAfter(Duration.seconds(5))
                        .position(Pos.TOP_LEFT)
                        .showInformation();
                mediaPlayer2.play();

            }
        });
    }


}

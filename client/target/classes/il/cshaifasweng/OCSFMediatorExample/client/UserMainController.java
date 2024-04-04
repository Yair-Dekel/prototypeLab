
package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.MessageOfStatus;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

import static il.cshaifasweng.OCSFMediatorExample.client.SimpleChatClient.setRoot;
import static il.cshaifasweng.OCSFMediatorExample.client.UserClient.getClient;

public class UserMainController {

    @FXML
    private Button volunteering;

    @FXML
    private AnchorPane btn1;
    @FXML
    private Label welcome_label; //after rina and malek do login we'll change it to welcome + user name

    @FXML
    private Button MY_REQUSTED_TASKS;
    String SaveUserName;

    private static Scene scene;
    private static Stage appStage;


    public void setAppStage(Stage appStage) {
        this.appStage = appStage;
    }
    @FXML
    private void initialize() {
        // Assuming you have the username stored in a variable named 'username'
        String username = UserClient.getLoggedInUser().getGivenName();
        SaveUserName=UserClient.getLoggedInUser().getUsername();
        // Set the text of the welcome_label to the username
        welcome_label.setText("Welcome " + username);
        welcome_label.setAlignment(Pos.CENTER);

        try {
            MessageOfStatus message=new MessageOfStatus("add user client",SaveUserName);
            getClient().sendToServer(message);
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }
    }
    @FXML
    void switchToemergency(ActionEvent event) {
        Platform.runLater(() -> {
            try {
                setRoot("Emergency");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @FXML
    void switchToNewTask(ActionEvent event) throws IOException {
        Platform.runLater(() -> {
            try {
                setRoot("new_task");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @FXML
    void showRequstedTasks(ActionEvent event) {
        Platform.runLater(() -> {
            try {
                setRoot("requestedTasksPage");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

    }

    @FXML
    void switchToVolunteer(ActionEvent event){
        Platform.runLater(() -> {
            try {
                setRoot("showMyVolunteeredTasks");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @FXML
    void turnToVolunteeringPage(ActionEvent event) {
        Platform.runLater(() -> {
            try {
                setRoot("VolunteeringPage");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });



    }
}

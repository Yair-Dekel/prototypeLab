
package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

import static il.cshaifasweng.OCSFMediatorExample.client.SimpleChatClient.setRoot;

public class UserMainController {

    @FXML
    private AnchorPane btn1;
    @FXML
    private Label welcome_label; //after rina and malek do login we'll change it to welcome + user name

    @FXML
    private void initialize() {
        // Assuming you have the username stored in a variable named 'username'
        String username = UserClient.getLoggedInUser().getGivenName();
        // Set the text of the welcome_label to the username
        welcome_label.setText("Welcome " + username);
        welcome_label.setAlignment(Pos.CENTER);
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
}

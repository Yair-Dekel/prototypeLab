
package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Registered_user;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

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


    @FXML
    void Log_Out(ActionEvent event) throws IOException{
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
        UserClient.getClient().sendToServer("Log_Out");
        // Open a new login window
        try {
            Parent root = FXMLLoader.load(getClass().getResource("log_in.fxml"));
            Stage loginStage = new Stage();
            loginStage.setScene(new Scene(root));
            loginStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}

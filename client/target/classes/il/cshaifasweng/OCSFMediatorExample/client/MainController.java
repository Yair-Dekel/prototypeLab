package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.DisplayTasksMassage;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.Task;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static il.cshaifasweng.OCSFMediatorExample.client.SimpleChatClient.setRoot;
import static il.cshaifasweng.OCSFMediatorExample.client.UserClient.getClient;

public class MainController {

    @FXML
    private AnchorPane btn1;

    @FXML
    private Button see_all_task_btn;


    @FXML
    private TextField TF_forErrro;

    @FXML
    private TextField Username_TF;

    @FXML
    private Label user_name_label;


    @FXML
    private Button login_btn;

    @FXML
    private PasswordField password_TF;
    private int msgId;



private void showAlert(String title, String content) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle(title);
    alert.setHeaderText(null);

    // Custom GridPane to allow for more content
    GridPane gridPane = new GridPane();
    Label label = new Label(content);
    label.setWrapText(true); // Allow text wrapping
    gridPane.add(label, 0, 0);
    alert.getDialogPane().setContent(gridPane);

    alert.showAndWait();
}
    private void showErrorDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    @FXML
    void LogIN_check_info(ActionEvent event) throws IOException {
        String password = password_TF.getText();
        String username = Username_TF.getText();
        System.out.println(password + "   " + username);
        password_TF.clear();
        Username_TF.clear();
        Message message = new Message("Confirm information", username, password);
        UserClient.getClient().sendToServer(message);

    }


    @Subscribe
    public void result_user_input(NewVerifiedInformationEvent event) {
        System.out.println("in client/after event/result_user_input");
        if (event.getMessage().getMessage().equals("correct")) {
            UserClient.setLoggedInUser(event.getMessage().getUser());
            if(UserClient.getLoggedInUser().getPermission())//1 for manager
            {
                System.out.println("result_user_input in manager");
                switchToMainOfManager();
            }else
                switchToMainOfUser();

        } else if (event.getMessage().getMessage().equals("wrongPassword")) {

            TF_forErrro.setText("you try to write a wrong password,please try againe");
            Platform.runLater(() -> {
            showErrorDialog("Wrong Password \n you try to write a wrong password, please try again");
            });
        } else if (event.getMessage().getMessage().equals("user is not exist")) {
            TF_forErrro.setText("you try to write a wrong username, please try again");
            Platform.runLater(() -> {
            showErrorDialog("wrong User Name \n you try to write a wrong username, please try again");
            });
        }
    }


    void switchToMainOfUser() {
        Platform.runLater(() -> {
            try {
                setRoot("user_main");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

    }
    void switchToMainOfManager()
    {
        System.out.println("switchToMainOfManager in manager");

        Platform.runLater(() -> {
            try {
                setRoot("manager_main");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }


    @FXML
    void initialize() {
        EventBus.getDefault().register(this);
        msgId = 0;
    }
}
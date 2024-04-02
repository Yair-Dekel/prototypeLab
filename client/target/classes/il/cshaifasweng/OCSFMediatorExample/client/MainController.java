package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.Registered_user;
import il.cshaifasweng.OCSFMediatorExample.entities.Task;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
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
    private TextField Username_TF;

    @FXML
    private Label user_name_label;


    @FXML
    private Button login_btn;

    @FXML
    private PasswordField password_TF;
    private int msgId;

    @FXML
    private Button Emergency_btn;

    private static Scene scene;
    private static Stage appStage;


    public void setAppStage(Stage appStage) {
        this.appStage = appStage;
    }


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
    String saveUserName;

    @FXML
    void LogIN_check_info(ActionEvent event) throws IOException {
        String password = password_TF.getText();
        String username = Username_TF.getText();
        saveUserName=username;
        System.out.println(password + "   " + username);
        password_TF.clear();
        Username_TF.clear();
        Message message = new Message("Confirm information", username, password);
        UserClient.getClient().sendToServer(message);

    }


    @Subscribe
    public void result_user_input(NewVerifiedInformationEvent event) throws IOException {
        System.out.println("in client/after event/result_user_input");
        if (event.getMessage().getMessage().equals("correct"))
        {
            if(event.getMessage().getUser().getPermission())//1 for manager
            {
                ManagerClient.getClient().openConnection();
                 ManagerClient.setManagerClient(event.getMessage().getUser());
                switchToMainOfManager();
            }else {
                UserClient.setLoggedInUser(event.getMessage().getUser());
                switchToMainOfUser();
            }

        } else if (event.getMessage().getMessage().equals("wrongPassword")) {

            Platform.runLater(() -> {
            showErrorDialog("Wrong Password \n you try to write a wrong password, please try again");
            });
        } else if (event.getMessage().getMessage().equals("user is not exist")) {
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





    @FXML
    void switchToemergency(ActionEvent event) {
        System.out.println("here");
        Platform.runLater(() -> {
            try {
                setRoot("Emergency");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    void switchToMainOfManager()
    {
        System.out.println("switchToMainOfManager in manager");
        System.out.println(saveUserName + "999999999999999999999999999999999999999999999");


        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("manager_main.fxml"));
                Parent root = loader.load();
                Manager managerController = loader.getController();
                managerController.initialize(saveUserName); // Pass the username to initialize method

                // Show the scene
                Scene scene = new Scene(root);
                appStage.setScene(scene);
                appStage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
//                setRoot("manager_main");
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        });
    }



    @FXML
    void initialize() {
        EventBus.getDefault().register(this);
        msgId = 0;
        // Set initial focus to the Username_TF TextField
        Username_TF.requestFocus();
    }


}
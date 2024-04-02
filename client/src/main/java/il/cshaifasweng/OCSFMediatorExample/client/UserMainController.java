/*
package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.DisplayTasksMassage;
import il.cshaifasweng.OCSFMediatorExample.entities.MessageOfStatus;
import il.cshaifasweng.OCSFMediatorExample.entities.Task;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    @FXML
    private Button log_out;

    @FXML
    private Button volunteer_taks;


    String SaveUserName;

    private static Scene scene;
    private static Stage appStage;

    @FXML
    void log_Out(ActionEvent event) throws IOException {
       /* UserClient.getClient().closeConnection();
        Platform.runLater(() -> {
            try {
                setRoot("log_in");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });*/


   /* private void showAlert(String title, String header, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType, content);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.show();
    }

/*
   @FXML
   void log_Out(ActionEvent event) throws IOException  {
       UserClient.getClient().closeConnection();

       // Load the log_in.fxml file
       FXMLLoader loader = new FXMLLoader(getClass().getResource("log_in.fxml"));
       Parent root = loader.load();

       // Set the scene
       Scene scene = new Scene(root);
       Stage stage = (Stage) log_out.getScene().getWindow(); // Get the current stage
       stage.setScene(scene);
       stage.show();
   }*/
  /*  public void setAppStage(Stage appStage) {
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
    void switchToVolunteer(ActionEvent event) {

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
                FXMLLoader loader = new FXMLLoader(getClass().getResource("requestedTasksPage.fxml"));
                Parent root = loader.load();
                ShowRequestedTasksPage RequestController = loader.getController();
                RequestController.initialize(SaveUserName); // Pass the username to initialize method

                // Show the scene
                Scene scene = new Scene(root);
                appStage.setScene(scene);
                appStage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

    }



    @FXML
    void turnToVolunteeringPage(ActionEvent event) {
        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("VolunteeringPage.fxml"));
                Parent root = loader.load();
                VolunteeringPage VolunteerController = loader.getController();
               VolunteerController.initialize(SaveUserName); // Pass the username to initialize method

                // Show the scene
                Scene scene = new Scene(root);
                appStage.setScene(scene);
                appStage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });


    }
}
*/


package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.DisplayTasksMassage;
import il.cshaifasweng.OCSFMediatorExample.entities.MessageOfStatus;
import il.cshaifasweng.OCSFMediatorExample.entities.Task;
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
import java.util.ArrayList;
import java.util.List;

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
                FXMLLoader loader = new FXMLLoader(getClass().getResource("requestedTasksPage.fxml"));
                Parent root = loader.load();
                ShowRequestedTasksPage RequestController = loader.getController();
                RequestController.initialize(SaveUserName); // Pass the username to initialize method

                // Show the scene
                Scene scene = new Scene(root);
                appStage.setScene(scene);
                appStage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

    }



    @FXML
    void turnToVolunteeringPage(ActionEvent event) {
        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("VolunteeringPage.fxml"));
                Parent root = loader.load();
                VolunteeringPage VolunteerController = loader.getController();
                VolunteerController.initialize(SaveUserName); // Pass the username to initialize method

                // Show the scene
                Scene scene = new Scene(root);
                appStage.setScene(scene);
                appStage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });


    }
    @FXML
    private Button log_out;

    @FXML
    private Button volunteer_taks;
    @FXML
    void log_Out(ActionEvent event) {

    }
    @FXML
    void switchToVolunteer(ActionEvent event) {

    }
}
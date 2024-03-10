package il.cshaifasweng.OCSFMediatorExample.client;
import javafx.scene.input.MouseEvent;
import il.cshaifasweng.OCSFMediatorExample.client.ocsf.AbstractClient;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;


import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;





import il.cshaifasweng.OCSFMediatorExample.entities.MessageOfStatus;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.text.Font;
import javafx.util.Duration;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import static il.cshaifasweng.OCSFMediatorExample.client.ManagerClient.getManagerClient;



public class Manager  {

    @FXML
    private Button Accept;

    @FXML
    private Button EmergencyButton;

    @FXML
    private Button EmergencyCalls;

    @FXML
    private ListView<Task> ListOfTasks;

    @FXML
    private Button Members;

    @FXML
    private TextField Reason;

    @FXML
    private Button Reject;

    @FXML
    private TextArea Request;

    @FXML
    private Button Send;

    @FXML
    private Button Tasks;

    @FXML
    private Label WriteReason;


    private int msgId;

    // Define the font size and family you want to use
    double fontSize = 15.0; // Example font size
    String fontFamily = "Arial"; // Example font family

    // Create a Font object with the desired font size and family
    Font font = Font.font(fontFamily, fontSize);

    @Subscribe
    public void taskAccepted(NewDetailsEvent event) {


        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION,
                    String.format("Task : %d accepted",
                            event.getMessage().getTask().getId()));
            alert.setTitle("Task Accepted");
            alert.setHeaderText("Task Accepted:");
            alert.show();
            for (Task task : ListOfTasks.getItems()) {
                if (task.getId() == event.getMessage().getTask().getId()) {
                    // Remove the selected item from the ListView
                    ListOfTasks.getItems().remove(task);

                    ListOfTasks.scrollTo(task);

                    // Break out of the loop since we found and removed the item
                    break;
                }
            }
            Request.setText("");
        });
    }



    @Subscribe
    public void taskRejected(TaskRejectEvent event) {


        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION,
                    String.format("Task : %d rejected",
                            event.getMessage().getTask().getId()));
            alert.setTitle("Task rejected");
            alert.setHeaderText("Task rejected:");
            alert.show();
            for (Task task : ListOfTasks.getItems()) {
                if (task.getId() == event.getMessage().getTask().getId()) {
                    // Remove the selected item from the ListView
                    ListOfTasks.getItems().remove(task);

                    ListOfTasks.scrollTo(task);
                    // Break out of the loop since we found and removed the item
                    break;
                }
            }
            Request.setText("");
        });
    }



    @FXML
    void AcceptRequest(ActionEvent event) throws IOException {
        Task task = ListOfTasks.getSelectionModel().getSelectedItem();
        if (task != null) {
            MessageOfStatus message = new MessageOfStatus(task, "accept");
            getManagerClient().sendToServer(message);
        }
        else {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR,
                        String.format("you have not select a task."));
                alert.setTitle("ERROR");
                alert.setHeaderText("First select a task");
                alert.show();

            });
        }
    }

    @FXML
    void EmergencyCall(ActionEvent event) {

    }

    @FXML
    void RejectRequest(ActionEvent event) throws IOException {
        Task task = ListOfTasks.getSelectionModel().getSelectedItem();
        if (task != null) {
            Send.setVisible(true);
            Reason.setVisible(true);
            WriteReason.setVisible(true);
        }
        else{

            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR,
                        String.format("you have not select a task."));
                alert.setTitle("ERROR");
                alert.setHeaderText("First select a task");
                alert.show();

            });
        }
    }

    @FXML
    void SendReason(ActionEvent event) throws IOException {
        Task task = ListOfTasks.getSelectionModel().getSelectedItem();

        String reason = Reason.getText();

        if (!reason.equals("")) {
            MessageOfStatus message = new MessageOfStatus(task, "reject: "+reason);

            getManagerClient().sendToServer(message);

            Send.setVisible(false);
            Reason.setVisible(false);
            WriteReason.setVisible(false);
        }
        else
        {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR,
                        String.format("you have to write a reason."));
                alert.setTitle("ERROR");
                alert.setHeaderText("There is no reason");
                alert.show();

            });
        }
    }


    @FXML
    void ShowEmergency(ActionEvent event) {

    }

    @FXML
    void ShowList(MouseEvent event) {
        Send.setVisible(false);
        Reason.setVisible(false);
        WriteReason.setVisible(false);
        Task tempTask = ListOfTasks.getSelectionModel().getSelectedItem();
        if (tempTask != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDeadline = tempTask.getDeadline().format(formatter);
            String taskDetails = String.format("Task ID: %d\n\nType: %s\n\nDeadline: %s\n\nStatus: %s\n\nName: %s %s",
                    tempTask.getId(), tempTask.getType_of_task(), formattedDeadline, tempTask.getStatus(),
                    tempTask.getRegistered_user().getGivenName(), tempTask.getRegistered_user().getFamilyName());
            // Update the TextArea with task details
            Request.setText(taskDetails);
            Request.setVisible(true);
            Request.setWrapText(true);
            Request.setFont(font);
        } else {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR,
                        String.format("you have not select a task."));
                alert.setTitle("ERROR");
                alert.setHeaderText("First select a task");
                alert.show();

            });
        }
    }


    @FXML
    void ShowMembers(ActionEvent event) {

    }

    @FXML
    void ShowTasks(ActionEvent event) {

    }

    @FXML
    void WriteReason(ActionEvent event) {

    }



    //ObservableList<Task> observableTasks = FXCollections.observableArrayList();
    @Subscribe
    public void ShowListView(TasksMessageEvent event) {

        Platform.runLater(() -> {
            //tasksContaine.getChildren().clear(); // Clear existing content

            if (event != null) {
                List<Task> tasks = event.getTasksE().getTasks();
                if (tasks != null && !tasks.isEmpty()) {
                    ObservableList<Task> observableTasks = FXCollections.observableArrayList(tasks);

                    // Create ListView to display tasks
                    ListOfTasks.setItems(observableTasks);
                }
                else {
                    showAlert("Requests Information", "Requests Information", "There is no requests.", Alert.AlertType.INFORMATION);
                }
            }
            else {
                showAlert("Error", "Error", "Invalid event received.", Alert.AlertType.ERROR);
            }

            //Scene scene = new Scene(ListOfTasks, 300, 250);

            // Set stage title and scene, then show the stage
            //Manager.setTitle("Tasks Waiting for Approval");
            // Manager.setScene(scene);
            //  Manager.show();
        });
    }

    private void showAlert(String title, String header, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType, content);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.show();
    }

    @FXML
    void initialize() {
        EventBus.getDefault().register(this);
        try {
            getManagerClient().sendToServer("list view");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        msgId=0;

        try {
            Message message = new Message(msgId, "add client");
            getManagerClient().sendToServer(message);
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }
    }

}

package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;

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

public class Volunteering {

    @FXML
    private Button Volunteer;

    @FXML
    private ListView<Task> VolunteeringList;

    @FXML
    private TextArea VolunteeringTaskDetails;


    private int msgId;

    // Define the font size and family you want to use
    double fontSize = 15.0; // Example font size
    String fontFamily = "Arial"; // Example font family

    // Create a Font object with the desired font size and family
    Font font = Font.font(fontFamily, fontSize);


    @FXML
    void DisplayForVolunteering(MouseEvent event) {


        Task tempTask = VolunteeringList.getSelectionModel().getSelectedItem();
        if (tempTask != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDeadline = tempTask.getDeadline().format(formatter);
            String taskDetails = String.format("Task ID: %d\n\nType: %s\n\nDeadline: %s\n\nStatus: %s\n\nName: %s %s",
                    tempTask.getId(), tempTask.getType_of_task(), formattedDeadline, tempTask.getStatus(),
                    tempTask.getRegistered_user().getGivenName(), tempTask.getRegistered_user().getFamilyName());
            // Update the TextArea with task details
            VolunteeringTaskDetails.setText(taskDetails);
            VolunteeringTaskDetails.setVisible(true);
            VolunteeringTaskDetails.setWrapText(true);
            VolunteeringTaskDetails.setFont(font);
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
    void Volunteering(ActionEvent event) {

    }

    private void showAlert(String title, String header, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType, content);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.show();
    }


    @Subscribe
    public void ShowListViewVolunteer(VolunteeringListEvent event) {

        Platform.runLater(() -> {
            //tasksContaine.getChildren().clear(); // Clear existing content


            if (event != null) {
                List<Task> tasks = event.getTasksE().getTasks();
                /*for (Task task : tasks)
                {
                    if (task.getRegistered_user().getId()==UserClient.getLoggedInUser().getId())
                    {
                        tasks.remove(task);
                    }
                }*/

                if (tasks != null && !tasks.isEmpty()) {
                    ObservableList<Task> observableTasks = FXCollections.observableArrayList(tasks);

                    // Create ListView to display tasks
                    VolunteeringList.setItems(observableTasks);

                }
                else {
                    showAlert("Requests Information", "Requests Information", "There is no tasks.", Alert.AlertType.INFORMATION);
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

    @FXML
    void initialize() throws IOException {
        EventBus.getDefault().register(this);
        try {
            UserClient.getClient().sendToServer("list view for volunteering");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        msgId=0;

       try {
            Message message = new Message(msgId, "add client");
            UserClient.getClient().sendToServer(message);
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }
    }



}

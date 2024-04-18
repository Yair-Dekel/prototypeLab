package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.MessageOfStatus;
import il.cshaifasweng.OCSFMediatorExample.entities.Task;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.text.Font;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import org.greenrobot.eventbus.EventBus;


import java.io.IOException;

import static il.cshaifasweng.OCSFMediatorExample.client.SimpleChatClient.setRoot;
import static il.cshaifasweng.OCSFMediatorExample.client.UserClient.getLoggedInUser;

public class ShowRequestedTasksPage {

    @FXML
    private Button emergency_button;

    @FXML
    private Button BACK;

    @FXML
    private Label welcome;

    @FXML
    private TextArea RequestedTaskDetails;

    @FXML
    private ListView<Task> requestedTaskList;

    private int msgId;

    // Define the font size and family you want to use
    double fontSize = 15.0; // Example font size
    String fontFamily = "Arial"; // Example font family

    // Create a Font object with the desired font size and family
    Font font = Font.font(fontFamily, fontSize);

    @FXML
    void displayRequest(MouseEvent event) {
        RequestedTaskDetails.setText("");
        Task tempTask = requestedTaskList.getSelectionModel().getSelectedItem();
        if (tempTask != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDeadline = tempTask.getDeadline().format(formatter);
            String moreDetails = tempTask.getMoredetails();

            if (moreDetails != null) {
                String taskDetails = String.format("Task ID: %d\n\nType: %s\n\nDeadline: %s\n\nStatus: %s\n\nName: %s %s\n\nMore Details: %s",
                        tempTask.getId(), tempTask.getType_of_task(), formattedDeadline, tempTask.getStatus(),
                        tempTask.getRegistered_user().getGivenName(), tempTask.getRegistered_user().getFamilyName(), moreDetails);
                System.out.println(taskDetails);
                RequestedTaskDetails.setText(taskDetails);

            } else {
                String taskDetailsWithoutMoreDetails = String.format("Task ID: %d\n\nType: %s\n\nDeadline: %s\n\nStatus: %s\n\nName: %s %s",
                        tempTask.getId(), tempTask.getType_of_task(), formattedDeadline, tempTask.getStatus(),
                        tempTask.getRegistered_user().getGivenName(), tempTask.getRegistered_user().getFamilyName());
                System.out.println(taskDetailsWithoutMoreDetails);
                RequestedTaskDetails.setText(taskDetailsWithoutMoreDetails);
            }
            // Update the TextArea with task details
            RequestedTaskDetails.setVisible(true);
            RequestedTaskDetails.setWrapText(true);
            RequestedTaskDetails.setFont(font);
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


    @Subscribe
    public void ShowListViewRequestedTasks(RequestedTasksShowEvent event) {
        System.out.println("firstttttttttttttttttttttttttttttttttt");
        Platform.runLater(() -> {
            RequestedTaskDetails.setText("");

            //tasksContaine.getChildren().clear(); // Clear existing content


            if (event != null) {
                List<Task> tasks = event.getTasksE().getTasks();
                System.out.println("inside");
                /*for (Task task : tasks)
                {
                    if (task.getRegistered_user().getId()==UserClient.getLoggedInUser().getId())
                    {
                        tasks.remove(task);
                    }
                }*/

                if (tasks != null && !tasks.isEmpty()) {
                    System.out.println("inside if  omg");
                    ObservableList<Task> observableTasks = FXCollections.observableArrayList(tasks);

                    // Create ListView to display tasks
                   requestedTaskList.setItems(observableTasks);

                }
                else {
                    System.out.println("inside it inside it");
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
    private void showAlert(String title, String header, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType, content);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.show();
    }

    @FXML
    void Back(ActionEvent event) {

        Platform.runLater(() -> {
            try {
                setRoot("user_main");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        EventBus.getDefault().unregister(this);


    }

    @FXML
    void initialize() throws IOException {
        String username=UserClient.getLoggedInUser().getUsername();
        System.out.println(username+" userclient???????????????????????????????????/");

        welcome.setText( UserClient.getLoggedInUser().getGivenName()+"'s Tasks");
        EventBus.getDefault().register(this);
        UserClient userClient = UserClient.getClient();
        try {
            // Open connection to the server
            userClient.openConnection();
        } catch (IOException e) {
            // Handle connection error
            System.err.println("Error: Unable to connect to the server");
            e.printStackTrace();
            return; // Stop initialization if connection fails
        }
        try {
            Message message4 = new Message("list view for requestedTasks",username);
            System.out.println("sending mesaage to server to request list");
            userClient.sendToServer(message4);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        msgId = 0;

        try {
            Message message = new Message(msgId, "add client");
            UserClient.getClient().sendToServer(message);
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }

    }
    @Subscribe
    public void TaskNotification(UsersNotificationEvent event)
    {
        Platform.runLater(() -> {
            PostNotifications.getInstance().TaskNotification(event);
        });
        if (PostNotifications.unregeister)
        {
            System.out.println("got inside");
            EventBus.getDefault().unregister(this);
            System.out.println("unregistered");
        }
    }

    public void switchToemergency(ActionEvent actionEvent)
    {
        System.out.println("here");
        Platform.runLater(() -> {
            try {
                UserClient.setLast_fxml("requestedTasksPage");
                setRoot("Emergency");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            EventBus.getDefault().unregister(this);
        });

    }
}

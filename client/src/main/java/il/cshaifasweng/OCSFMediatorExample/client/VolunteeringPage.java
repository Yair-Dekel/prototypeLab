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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.text.Font;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import javafx.scene.control.Label;

import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static il.cshaifasweng.OCSFMediatorExample.client.SimpleChatClient.setRoot;


public class VolunteeringPage {


    @FXML
    private ListView<Task> VolunteeringList;

    @FXML
    private TextArea VolunteeringTaskDetails;

    @FXML
    private Button volunteer;

    @FXML
    private Button back_button;

    @FXML
    private Button emergency;

    @FXML
    private Label welcome;



    private int msgId;

    // Define the font size and family you want to use
    double fontSize = 15.0; // Example font size
    String fontFamily = "Arial"; // Example font family

    // Create a Font object with the desired font size and family
    Font font = Font.font(fontFamily, fontSize);

    String Saveuser;


    @FXML
    void volunteeringTaskShow(MouseEvent event) {

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
    void Volunteering(ActionEvent event) throws IOException {
        Task task = VolunteeringList.getSelectionModel().getSelectedItem();
        if (task != null) {
            MessageOfStatus message = new MessageOfStatus(task, "volunteering",Saveuser);
            UserClient.getClient().sendToServer(message);
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

    @Subscribe
    public void VolunteerComplete(PersonVolunteering event) {


        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION,
                    String.format("Task : %d ,thanks for volunteering",
                            event.getMessage().getTask().getId()));
            alert.setTitle("Volunteering ");
            alert.setHeaderText("Volunteering:");
            alert.show();
            for (Task task : VolunteeringList.getItems()) {
                if (task.getId() == event.getMessage().getTask().getId()) {
                    // Remove the selected item from the ListView
                    VolunteeringList.getItems().remove(task);

                    VolunteeringList.scrollTo(task);

                    // Break out of the loop since we found and removed the item
                    break;
                }
            }
            VolunteeringTaskDetails.setText("");
        });
    }
    @Subscribe
    public void ShowListViewVolunteer(VolunteeringEvent event) {
        System.out.println("firstttttttttttttttttttttttttttttttttt");
        Platform.runLater(() -> {
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
                    VolunteeringList.setItems(observableTasks);

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
    void back(ActionEvent event) {
        Platform.runLater(() -> {
            try {
                setRoot("user_main");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

    }


    @FXML
    void initialize() throws IOException {
        String username=UserClient.getLoggedInUser().getUsername();
        EventBus.getDefault().register(this);
        Saveuser=username;
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
            Message message3 = new Message("list view for volunteering",username);
            userClient.sendToServer(message3);
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

    public void switchToemergency(ActionEvent actionEvent)
    {
        Platform.runLater(() -> {
            try {
                UserClient.setLast_fxml("VolunteeringPage");
                setRoot("Emergency");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

}
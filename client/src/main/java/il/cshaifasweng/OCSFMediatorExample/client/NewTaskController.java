package il.cshaifasweng.OCSFMediatorExample.client;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import il.cshaifasweng.OCSFMediatorExample.entities.NewTaskMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.TaskType;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import static il.cshaifasweng.OCSFMediatorExample.client.SimpleChatClient.setRoot;

public class NewTaskController {

    @FXML
    private TextArea detailsTxt;

    @FXML
    private DatePicker deadlineDp;

    @FXML
    private Button confirm_and_displayBtn;

    @FXML
    private ComboBox<TaskType> taskTypeComboBox;



    @FXML
    private void check_confirm_display_task() throws IOException {
        LocalDateTime deadline = deadlineDp.getValue() == null ? null : deadlineDp.getValue().atStartOfDay();
        TaskType selectedTaskType = taskTypeComboBox.getValue();
        String details = detailsTxt.getText();

        // Check if deadline is null or not
        if (selectedTaskType == null) {
            showErrorDialog("Please select a task type.");
        } else if (deadline == null) {
            showErrorDialog("Please select a deadline.");
        } else {
            // Calculate remaining days only if the deadline is not null
            int remaining = (int) ChronoUnit.DAYS.between(LocalDateTime.now(), deadline);
            details = details == null ? "" : details;
            remaining++; // Increment remaining days by one

            if (remaining <= 0) {
                showErrorDialog("Please select a future date.");
            } else {
                showAlert("Task Information",
                        "Your task type is: " + selectedTaskType + "\n\n"
                                + "Time of the task creation: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                                + "\n\nThe task needs to be completed in " + (remaining) + " days\n\n"
                                + "Task additional Details: " + details
                                + "\n\n\nPlease note: The task will be published after the approval of the manager"
                                + "\n\nThe system uploads your task...");
                UserClient.getClient().sendToServer(new NewTaskMessage(deadline, details, selectedTaskType, UserClient.getLoggedInUser())); //here i want to get to the relevant client - it can be more than one- so we need to change getclient method.
            }
        }
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

    @Subscribe
    public void informNTuploaded(NewTaskEvent event) {
        Platform.runLater(() -> {

                    showAlert("Atis saved your task!", "Your task has been included in the queue of tasks awaiting approval.");
                    try {
                        setRoot("user_main");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
    }




    // Helper method to show an error dialog
    private void showErrorDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    // Initialize the ComboBox with task types when the controller is created
    @FXML
    private void initialize() {
        EventBus.getDefault().register(this);
        ObservableList<TaskType> taskTypes = FXCollections.observableArrayList(TaskType.values());
        /*ObservableList for the case in which the manager want to add or remove a task type*/
        taskTypeComboBox.setItems(taskTypes);
//        taskTypeComboBox.setValue(TaskType.valueOf("select type")); // Set default value

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

    // Helper method to show an error dialog
    @FXML
    void back(ActionEvent event) throws IOException {
        SimpleChatClient.setRoot("show_tasks");

    }

}

package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.DisplayDataMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.Registered_user;
import il.cshaifasweng.OCSFMediatorExample.entities.Task;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;

import static il.cshaifasweng.OCSFMediatorExample.client.SimpleChatClient.setRoot;

public class UserTasksController {
    @FXML
    private Button Backbtn;
    @FXML
    private Button emmergencyBtn;
    private Registered_user user;

    @FXML
    private TableView<Task> TasksDone;

    @FXML
    private TableColumn<Registered_user, String> idTaskd;

    @FXML
    private TableColumn<Registered_user, String> creationtimed;
    @FXML
    private TableColumn<Registered_user, String> completiontimed;
    @FXML
    private TableColumn<Registered_user, String> Type_of_taskd;
    @FXML
    private TableColumn<Registered_user, String> moredetailsd;
    @FXML
    private TableColumn<Registered_user, String> statusu;


    @FXML
    private TableView<Task> Tasksuploded;

    @FXML
    private TableColumn<Registered_user, String> idTasku;

    @FXML
    private TableColumn<Registered_user, String> creationtimeu;
    @FXML
    private TableColumn<Registered_user, String> Type_of_tasku;
    @FXML
    private TableColumn<Registered_user, String> moredetailsu;
    @FXML
    private TableColumn<Registered_user, String> deadlineu;
    @FXML
    private Label donenotdone;

    public Registered_user getUser() {
        return user;
    }

    public void setUser(Registered_user user) {
        this.user = user;
    }

    public void initialize() {
        System.out.println("Initializing UserTasksController");
        EventBus.getDefault().register(this);

        // Check if user is null
        if (user != null) {
            // Set the text of the welcome_label to the username
            donenotdone.setText(user.getGivenName() + "'s Tasks");
            donenotdone.setAlignment(Pos.CENTER);

            // Initialize columns
            idTasku.setCellValueFactory(new PropertyValueFactory<>("Id"));
            Type_of_tasku.setCellValueFactory(new PropertyValueFactory<>("Type_of_task"));
            creationtimeu.setCellValueFactory(new PropertyValueFactory<>("Creation_time"));
            deadlineu.setCellValueFactory(new PropertyValueFactory<>("Deadline"));
            statusu.setCellValueFactory(new PropertyValueFactory<>("Status"));
            moredetailsu.setCellValueFactory(new PropertyValueFactory<>("Moredetails"));

            idTaskd.setCellValueFactory(new PropertyValueFactory<>("Id"));
            Type_of_taskd.setCellValueFactory(new PropertyValueFactory<>("Type_of_task"));
            creationtimed.setCellValueFactory(new PropertyValueFactory<>("Creation_time"));
            completiontimed.setCellValueFactory(new PropertyValueFactory<>("Completiontime"));
//            deadlineu.setCellValueFactory(new PropertyValueFactory<>("Deadline"));
//            statusu.setCellValueFactory(new PropertyValueFactory<>("Status"));
            moredetailsd.setCellValueFactory(new PropertyValueFactory<>("Moredetails"));
                // Your action code here
            try {
                Backbtn.setOnAction(event -> BackToMembers(event));
                emmergencyBtn.setOnAction(event -> switchToemergency(event));
                ManagerClient.getClient().sendToServer(new DisplayDataMessage(user, "uploaded"));
                ManagerClient.getClient().sendToServer(new DisplayDataMessage(user, "performed"));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        } else {
            System.err.println("User is null. Cannot initialize UI components.");
        }
    }
    @FXML
    void switchToemergency(ActionEvent event) {
        Platform.runLater(() -> {
            try {
                setRoot("Emergency");
                Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                currentStage.close();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Subscribe
    public void displayTasks(UserTasksDisplayEvent event) {
        Platform.runLater(() -> {
            try {
                if (!event.getDis().getTasks().isEmpty()) {
//                    System.out.println(event.getDis().getTasks().getFirst().getId());
                    System.out.println("eventrecognized");
                    ObservableList<Task> observableTasks = FXCollections.observableArrayList(event.getDis().getTasks());
                    if (!observableTasks.isEmpty()) {
                        Tasksuploded.setItems(observableTasks);

                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Subscribe
    public void displayCTasks(CompletedEvent event) {
        System.out.println("comoleted controller");
        Platform.runLater(() -> {
            try {
                if (!event.getDis().getTasks().isEmpty()) {
//                    System.out.println(event.getDis().getTasks().getFirst().getId());
                    System.out.println("eventrecognizedC");
                    ObservableList<Task> observableTasks = FXCollections.observableArrayList(event.getDis().getTasks());
                    TasksDone.setItems(observableTasks);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    //    @Subscribe
//    public void TaskNotification(UsersNotificationEvent event)
//    {
//        PostNotifications.getInstance().TaskNotification(event);
//    }

    @FXML
    void BackToMembers(ActionEvent event) {
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
        System.out.println("in back to members");
                Platform.runLater(() -> {
                    try {
                        setRoot("members");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

}


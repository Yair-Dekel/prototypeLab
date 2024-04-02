package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.client.ManagerClient;
import il.cshaifasweng.OCSFMediatorExample.client.CompletedEvent;
import il.cshaifasweng.OCSFMediatorExample.client.UserTasksDisplayEvent;
import il.cshaifasweng.OCSFMediatorExample.entities.DisplayDataMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.Registered_user;
import il.cshaifasweng.OCSFMediatorExample.entities.Task;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;

public class UserTasksController {


    private Registered_user user;

    @FXML
    private TableView<Registered_user> TasksDone;

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
//            deadlineu.setCellValueFactory(new PropertyValueFactory<>("Deadline"));
//            statusu.setCellValueFactory(new PropertyValueFactory<>("Status"));
            moredetailsd.setCellValueFactory(new PropertyValueFactory<>("Moredetails"));
            try {
                ManagerClient.getClient().sendToServer(new DisplayDataMessage(user, "uploaded"));
                ManagerClient.getClient().sendToServer(new DisplayDataMessage(user, "performed"));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        } else {
            System.err.println("User is null. Cannot initialize UI components.");
        }
    }

    ;


    @Subscribe
    public void displayTasks(UserTasksDisplayEvent event) {
        Platform.runLater(() -> {
            try {
                if (!event.getDis().getTasks().isEmpty()){
                    System.out.println(event.getDis().getTasks().getFirst().getId());
                    System.out.println("eventrecognized");
                    ObservableList<Task> observableTasks = FXCollections.observableArrayList(event.getDis().getTasks());
                    if (!observableTasks.isEmpty()) {
                        Tasksuploded.setItems(observableTasks);

                }}
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Subscribe
    public void displayCTasks(CompletedEvent event) {
        Platform.runLater(() -> {
            try {
                if (!event.getDis().getTasks().isEmpty())
                {System.out.println(event.getDis().getTasks().getFirst().getId());
                System.out.println("eventrecognizedC");
                ObservableList<Task> observableTasks = FXCollections.observableArrayList(event.getDis().getTasks());
                    Tasksuploded.setItems(observableTasks);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}


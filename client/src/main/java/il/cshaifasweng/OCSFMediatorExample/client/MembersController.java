package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Communities;
import il.cshaifasweng.OCSFMediatorExample.entities.DisplayDataMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.Registered_user;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;

import static il.cshaifasweng.OCSFMediatorExample.client.SimpleChatClient.setRoot;

public class MembersController {
    @FXML
    private Button emmergencyBtn;
    @FXML
    private TableView<Registered_user> tableView;

    @FXML
    private TableColumn<Registered_user, String> usernameColumn;

    @FXML
    private TableColumn<Registered_user, String> phoneNumber;
    @FXML
    private TableColumn<Registered_user, String> givenName;
    @FXML
    private TableColumn<Registered_user, String> familyName;
    @FXML
    private TableColumn<Registered_user, String> id;

    @FXML
    private Label communityM;

    @FXML
    private Button Backbtn;
    public Registered_user userdisplay = null;

    public void setAppStage(Stage appStage) {
        this.appStage = appStage;
    }

    private static Stage appStage;
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
    public void initialize() throws IOException {
        System.out.println("hi");
        EventBus.getDefault().register(this);
        // Assuming you have the username stored in a variable named 'username'
        Communities community = ManagerClient.getManagerClient().getCommunity();
        // Set the text of the welcome_label to the username
        communityM.setText(community + " Members");
        communityM.setAlignment(Pos.CENTER);
        // Initialize columns
        id.setCellValueFactory(new PropertyValueFactory<>("Id"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("Username"));
        phoneNumber.setCellValueFactory(new PropertyValueFactory<>("Phone_number"));
        givenName.setCellValueFactory(new PropertyValueFactory<>("GivenName"));
        familyName.setCellValueFactory(new PropertyValueFactory<>("FamilyName"));
        ManagerClient.getClient().sendToServer(new DisplayDataMessage(community, "members"));
    }


    @Subscribe
    public void displayMembers(MembersDisplayEvent event) {
        ObservableList<Registered_user> observableMembers = FXCollections.observableArrayList(event.getDis().getMembers());
        populateTableM(observableMembers);


    }

    @FXML
    void BackToTheMain(ActionEvent event) {
//        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//        currentStage.close();
            Platform.runLater(() -> {
                try {
                    setRoot("manager_main");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
    }



    @FXML
    private void addRowAction(Registered_user user, MouseEvent event) throws IOException {
        System.out.println("addrowaction");

        Platform.runLater(() -> {
            try {
//                Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//                currentStage.close();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("UserTasks.fxml"));

                // Create an instance of the UserTasksController and pass the user to its constructor
                UserTasksController controller = new UserTasksController();
                controller.setUser(user);
                // Set the controller to the FXMLLoader
                fxmlLoader.setController(controller);

                // Load the FXML
                Parent root = (Parent) fxmlLoader.load();

                // Assuming you have a stage object available
                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void populateTableM(ObservableList<Registered_user> users) {
        Platform.runLater(() -> {
            // Populate TableView with data
            tableView.setItems(users);
            tableView.setRowFactory(tv -> {
                TableRow<Registered_user> row = new TableRow<>();
                row.setOnMouseClicked(event -> {
                    if (!row.isEmpty() && event.getClickCount() == 1) {
                        Registered_user user = row.getItem();
                        try {
                            addRowAction(user, event);
                        } catch (IOException e) {
                            e.printStackTrace(); // Handle the exception appropriately
                        }
                    }
                });
                return row;
            });
        });
    }
}

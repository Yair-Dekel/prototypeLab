package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.client.Emergency_Call_Event;
import il.cshaifasweng.OCSFMediatorExample.entities.Emergency_call;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.greenrobot.eventbus.EventBus;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import static il.cshaifasweng.OCSFMediatorExample.client.ManagerClient.getClient;
import static il.cshaifasweng.OCSFMediatorExample.client.SimpleChatClient.setRoot;


public class show_emergencyCall {

    @FXML
    private Button EmergencyButton;

    @FXML
    private TableColumn<Emergency_call, String> nameColumn;

    @FXML
    private TableColumn<Emergency_call, String> phoneColumn;
    @FXML
    private TableView<Emergency_call> ListOfCalls;


    @FXML
    private Button back_to_main;

    @FXML
    private TableColumn<Emergency_call, LocalDateTime> creationtimeColumn;

    @FXML
    private TableColumn<Emergency_call, Integer> idColumn;


    @FXML
    void Back_to_main(ActionEvent event) throws IOException {
        SimpleChatClient.setRoot("manager_main");

    }


    @FXML
    void ShowList(MouseEvent event) {

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


    @Subscribe
    public void ShowList_Emergency_call(Emergency_Call_Event event) {////////////////////////////////////////////////////////
        System.out.println("in ShowList_Emergency_call*************");
        Platform.runLater(() -> {
            if (event != null) {
                List<Emergency_call> calls = event.getCalls().getCalls();
                if (calls != null && !calls.isEmpty()) {
                    for (Emergency_call call : calls) {
                        System.out.println(call.getGiven_name());
                    }
                    ObservableList<Emergency_call> observableTasks = FXCollections.observableArrayList(calls);

                    // Create ListView to display tasks
                    ListOfCalls.setItems(observableTasks);
                } else {
                    showAlert("Requests Information", "Requests Information", "There is no requests.", Alert.AlertType.INFORMATION);
                }
            } else {
                showAlert("Error", "Error", "Invalid event received.", Alert.AlertType.ERROR);
            }

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
        // Register this class as a subscriber to EventBus
        EventBus.getDefault().register(this);

        try {
            getClient().sendToServer("ShowEmergency");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Set cell value factories for table columns
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("given_name"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone_number"));
        creationtimeColumn.setCellValueFactory(new PropertyValueFactory<>("creation_time"));
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        // Set cell factory to center-align values
        nameColumn.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                    setAlignment(Pos.CENTER);
                }
            }
        });

        phoneColumn.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                    setAlignment(Pos.CENTER);
                }
            }
        });

        creationtimeColumn.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd  HH:mm:ss");
                    String formattedDateTime = item.format(formatter);
                    setText(formattedDateTime);
                    setAlignment(Pos.CENTER);
                }
            }
        });

        idColumn.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.toString());
                    setAlignment(Pos.CENTER);
                }
            }
        });
    }

}

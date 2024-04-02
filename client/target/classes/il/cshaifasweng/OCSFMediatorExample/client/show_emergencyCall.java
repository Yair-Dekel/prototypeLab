package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.client.Emergency_Call_Event;
import il.cshaifasweng.OCSFMediatorExample.entities.Communities;
import il.cshaifasweng.OCSFMediatorExample.entities.Emergency_call;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import static il.cshaifasweng.OCSFMediatorExample.client.ManagerClient.getClient;
import static il.cshaifasweng.OCSFMediatorExample.client.SimpleChatClient.setRoot;


public class show_emergencyCall {
    // Declare fields to store the last selected community and date range
    private String lastSelectedCommunity = "All";
    private LocalDate lastStartDate;
    private LocalDate lastEndDate;

    @FXML
    private Button EmergencyButton;
    @FXML
    private BarChart<?, ?> Calls_chart;
    @FXML
    private DatePicker To_date;
    @FXML
    private DatePicker From_Date;

    // Declare a field to store the previous list of emergency calls
    private List<Emergency_call> previousCalls;
    @FXML
    private TableColumn<Emergency_call, String> nameColumn;

    @FXML
    private TableColumn<Emergency_call, String> phoneColumn;
    @FXML
    private TableView<Emergency_call> ListOfCalls;

    @FXML
    private MenuButton comunity_choose;

    @FXML
    private Button back_to_main;

    @FXML
    private TableColumn<Emergency_call, LocalDateTime> creationtimeColumn;

    @FXML
    private TableColumn<Emergency_call, Integer> idColumn;

    private static Scene scene;
    private static Stage appStage;


    public void setAppStage(Stage appStage) {
        this.appStage = appStage;
    }

    @FXML
    void Back_to_main(ActionEvent event) throws IOException {
//        SimpleChatClient.setRoot("manager_main");
        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("manager_main.fxml"));
                Parent root = loader.load();
                Manager ManagerController = loader.getController();
                ManagerController.initialize(ManagerClient.getManagerClient().getUsername()); // Pass the username to initialize method

                // Show the scene
                Scene scene = new Scene(root);
                if (appStage == null) {
                    appStage = new Stage();
                }

                appStage.setScene(scene);
                appStage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

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
    public void ShowList_Emergency_call(Emergency_Call_Event event) {
        System.out.println("in ShowList_Emergency_call*************");
        Platform.runLater(() -> {
            if (event != null) {
                List<Emergency_call> allCalls = event.getCalls().getCalls();
                previousCalls=allCalls;
                System.out.println(" (event != null)");
                if (allCalls != null && !allCalls.isEmpty()) {
                    // Get the selected community from the MenuButton
                    List<Emergency_call> filteredCalls=allCalls;
                    if(!comunity_choose.getText().equals("All"))
                    {

                        Communities selectedCommunity = Communities.valueOf(comunity_choose.getText());

                        // Filter calls based on the selected community
                         filteredCalls = allCalls.stream()
                                 .filter(call -> call.getRegistered_user() != null && call.getRegistered_user().getCommunity() == selectedCommunity)
                                 .collect(Collectors.toList());
                    }

                    // Check if there are any filtered calls
                    if (!filteredCalls.isEmpty()) {
                        for (Emergency_call call : filteredCalls) {
                            System.out.println(call.getGiven_name());
                        }
                        ObservableList<Emergency_call> observableTasks = FXCollections.observableArrayList(filteredCalls);
                        ListOfCalls.setItems(observableTasks);
                    } else {
                        ListOfCalls.getItems().clear();
                    }
                } else {
                    ListOfCalls.getItems().clear();
                }
            } else

            {


                System.out.println(" (event == null)");
                List<Emergency_call> allCalls = previousCalls;
                if (allCalls != null && !allCalls.isEmpty()) {
                    // Get the selected community from the MenuButton
                    List<Emergency_call> filteredCalls=allCalls;
                    if(!comunity_choose.getText().equals("All"))
                    {

                        Communities selectedCommunity = Communities.valueOf(comunity_choose.getText());

                        // Filter calls based on the selected community
                        filteredCalls = allCalls.stream()
                                .filter(call -> call.getRegistered_user() != null && call.getRegistered_user().getCommunity() == selectedCommunity)
                                .collect(Collectors.toList());
                    }

                    // Check if there are any filtered calls
                    if (!filteredCalls.isEmpty()) {
                        for (Emergency_call call : filteredCalls) {
                            System.out.println(call.getGiven_name());
                        }
                        ObservableList<Emergency_call> observableTasks = FXCollections.observableArrayList(filteredCalls);
                        ListOfCalls.setItems(observableTasks);
                    } else {
                        ListOfCalls.getItems().clear();
                    }
                }  else {
                    ListOfCalls.getItems().clear();
                }

            }
        });
    }




    private void showAlert(String title, String header, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType, content);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.show();
    }


    private void updateListOfCalls() {
        System.out.println("updated");
        // Get the selected start and end dates
        LocalDate startDate = lastStartDate != null ? lastStartDate : From_Date.getValue();
        LocalDate endDate = lastEndDate != null ? lastEndDate : To_date.getValue();

        // Validate that the start date is before the end date
        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            showAlert("Error", "Invalid Date Range", "From date must be before To date", Alert.AlertType.ERROR);
            return;
        }

        // Get the selected community
        String selectedCommunity = comunity_choose.getText();

        // Filter the list of emergency calls based on the selected time period and community
        ObservableList<Emergency_call> filteredCalls = FXCollections.observableArrayList();
        for (Emergency_call call : previousCalls) {
            LocalDateTime creationTime = call.getCreation_time();
            LocalDate callDate = creationTime.toLocalDate();
            boolean isInDateRange = (startDate == null || callDate.isEqual(startDate) || callDate.isAfter(startDate)) &&
                    (endDate == null || callDate.isEqual(endDate) || callDate.isBefore(endDate));

            // Add a null check for registered user
            if (call.getRegistered_user() != null) {
                boolean isMatchingCommunity = selectedCommunity.equals("All") ||
                        call.getRegistered_user().getCommunity().toString().equals(selectedCommunity);

                if (isInDateRange && isMatchingCommunity) {
                    filteredCalls.add(call);
                }
            } else {
                // If the community is "All" and the call falls within the selected date range, add it to the filtered list
                if (selectedCommunity.equals("All") && isInDateRange) {
                    filteredCalls.add(call);
                }
            }
        }

        // Update the TableView with the filtered list of emergency calls
        ListOfCalls.setItems(filteredCalls);
        // Update the last selected community and date range
        lastSelectedCommunity = comunity_choose.getText();
        lastStartDate = startDate;
        lastEndDate = endDate;
    }



    @FXML
    void initialize() {
        System.out.println("here");
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


        comunity_choose.getItems().clear();

// Add "All" MenuItem
        MenuItem allMenuItem = new MenuItem("All");
        allMenuItem.setOnAction(event -> {
            // Handle the action when "All" is selected
            comunity_choose.setText("All");
            updateListOfCalls(); // Pass null as the event parameter for simplicity
        });
        comunity_choose.getItems().add(allMenuItem);

// Add action handlers for each community MenuItem
        for (Communities community : Communities.values()) {
            MenuItem menuItem = new MenuItem(community.toString());
            menuItem.setOnAction(event -> {
                // Handle the action when the menu item is clicked
                String selectedCommunity = community.toString();
                System.out.println("Selected community: " + selectedCommunity);
                // Update the text of the comunity_choose MenuButton
                comunity_choose.setText(selectedCommunity);

                updateListOfCalls(); // Pass null as the event parameter for simplicity
            });

            comunity_choose.getItems().add(menuItem);

        }



        // Register event listeners for the DatePickers
        From_Date.setOnAction(event -> {
            lastStartDate = From_Date.getValue();
            updateListOfCalls();
        });
        To_date.setOnAction(event -> {
            lastEndDate = To_date.getValue();
            updateListOfCalls();
        });


    }



}

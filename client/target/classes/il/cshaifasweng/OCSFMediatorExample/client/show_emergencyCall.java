package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.client.Emergency_Call_Event;
import il.cshaifasweng.OCSFMediatorExample.entities.Communities;
import il.cshaifasweng.OCSFMediatorExample.entities.Emergency_call;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
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
import java.util.stream.Collectors;

import static il.cshaifasweng.OCSFMediatorExample.client.ManagerClient.getClient;
import static il.cshaifasweng.OCSFMediatorExample.client.SimpleChatClient.setRoot;


public class show_emergencyCall {

    @FXML
    private Button EmergencyButton;
    @FXML
    private BarChart<?, ?> Calls_chart;

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


/*    @Subscribe
    public void ShowList_Emergency_call(Emergency_Call_Event event) {////////////////////////////////////////////////////////
        System.out.println("in ShowList_Emergency_call*************");
        Platform.runLater(() -> {
            if (event != null) {
                List<Emergency_call> calls = event.getCalls().getCalls();
                previousCalls=calls;
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
            }
            else {
                List<Emergency_call> calls = previousCalls;
                if (calls != null && !calls.isEmpty()) {
                    for (Emergency_call call : calls) {
                        System.out.println(call.getGiven_name());
                    }
                    ObservableList<Emergency_call> observableTasks = FXCollections.observableArrayList(calls);

                    // Create ListView to display tasks
                    ListOfCalls.setItems(observableTasks);
                } else {
                    showAlert("Requests Information", "Requests Information", "There is no requests.", Alert.AlertType.INFORMATION);
                    ListOfCalls.getItems().clear();
                }
            }

        });
    }*/


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
                        showAlert("Requests Information", "Requests Information", "There are no requests for the selected community.", Alert.AlertType.INFORMATION);
                    }
                } else {
                    ListOfCalls.getItems().clear();
                    showAlert("Error Information", "Error Information", "There are no Calls.", Alert.AlertType.INFORMATION);
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
                        showAlert("Requests Information", "Requests Information", "There are no requests for the selected community.", Alert.AlertType.INFORMATION);
                    }
                }  else {
                    ListOfCalls.getItems().clear();
                    showAlert("Error Information", "Error Information", "There are no Calls.", Alert.AlertType.INFORMATION);
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


    private void initializeCallsChart() {
        // Initialize the bar chart
     /*   Calls_chart.setTitle("Emergency Calls by Day and Month");

        // Set up the x-axis
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Day/Month");
        Calls_chart.setCategoryAxis(xAxis);

        // Set up the y-axis
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Number of Calls");
        Calls_chart.setValueAxis(yAxis);

        // Get the current date
        LocalDateTime currentDate = LocalDateTime.now();

        // Populate the bar chart (dummy data for demonstration)
        ObservableList<XYChart.Data<String, Number>> data = FXCollections.observableArrayList();
        for (int i = 1; i <= currentDate.getDayOfMonth(); i++) {
            String day = String.format("%02d", i);
            data.add(new XYChart.Data<>("Day " + day, (int) (Math.random() * 10))); // Dummy data, replace with your logic
        }
        for (int i = 1; i <= currentDate.getMonth().maxLength(); i++) {
            String month = String.format("%02d", i);
            data.add(new XYChart.Data<>("Month " + month, (int) (Math.random() * 10))); // Dummy data, replace with your logic
        }

        XYChart.Series<String, Number> series = new XYChart.Series<>(data);
        Calls_chart.getData().add(series);*/
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


        comunity_choose.getItems().clear();

// Add "All" MenuItem
        MenuItem allMenuItem = new MenuItem("All");
        allMenuItem.setOnAction(event -> {
            // Handle the action when "All" is selected
            comunity_choose.setText("All");
            ShowList_Emergency_call(null); // Pass null as the event parameter for simplicity
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

                ShowList_Emergency_call(null); // Pass null as the event parameter for simplicity
            });

            comunity_choose.getItems().add(menuItem);

        }


        // Initialize the bar chart
        initializeCallsChart();
    }



}

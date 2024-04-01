package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.client.Emergency_Call_Event;
import il.cshaifasweng.OCSFMediatorExample.entities.Communities;
import il.cshaifasweng.OCSFMediatorExample.entities.Emergency_call;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.greenrobot.eventbus.EventBus;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
        initializeCallsChart();
    }




    private void showAlert(String title, String header, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType, content);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.show();
    }


    private void updateListOfCalls() {
        // Get the selected start and end dates
        LocalDate startDate = From_Date.getValue();
        LocalDate endDate = To_date.getValue();

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
    }






    private void initializeCallsChart() {
        // Initialize the bar chart
        Calls_chart.setTitle("Emergency Calls by Day and Month");

        // Set up the x-axis
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Creation Time");

        // Set up the y-axis
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Number of Calls");

        // Create the bar chart with the specified axes
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);

        // Create series for the chart
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Emergency Calls");

        // Bind the chart's data to the ListOfCalls table's items
        series.setData(getChartData());

        // Add the series to the chart
        barChart.getData().add(series);

        // Set the bar chart to your existing Calls_chart property
        Calls_chart = barChart;
    }

    // Method to get chart data from the ListOfCalls table's items
    private ObservableList<XYChart.Data<String, Number>> getChartData() {
        ObservableList<XYChart.Data<String, Number>> data = FXCollections.observableArrayList();
        for (Emergency_call call : ListOfCalls.getItems()) {
            // Add data to the series
            data.add(new XYChart.Data<>(call.getCreation_time().toString(), 1)); // Assuming each call counts as 1
        }
        return data;
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



        // Register event listeners for the DatePickers
        From_Date.setOnAction(event -> updateListOfCalls());
        To_date.setOnAction(event -> updateListOfCalls());



        // Initialize the bar chart
        initializeCallsChart();
    }



}

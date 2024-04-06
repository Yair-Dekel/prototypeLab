package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.client.Emergency_Call_Event;
import il.cshaifasweng.OCSFMediatorExample.entities.Communities;
import il.cshaifasweng.OCSFMediatorExample.entities.Emergency_call;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;
import javafx.util.StringConverter;
import org.greenrobot.eventbus.EventBus;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static il.cshaifasweng.OCSFMediatorExample.client.ManagerClient.getClient;
import static il.cshaifasweng.OCSFMediatorExample.client.SimpleChatClient.setRoot;


public class show_emergencyCall {
    // Declare fields to store the last selected community and date range
    private String lastSelectedCommunity = "All";


    private int x=0;
    private LocalDate lastStartDate;
    private LocalDate lastEndDate;
    // Declare a field to store the filtered list of emergency calls
    private ObservableList<Emergency_call> filteredCalls = FXCollections.observableArrayList();

    @FXML
    private Button EmergencyButton;
    @FXML
    private LineChart<String, Number> Calls_chart;
    @FXML
    private DatePicker To_date;
    @FXML
    private DatePicker From_Date;

    // Declare a field to store the previous list of emergency calls
    private List<Emergency_call> previousCalls;
    @FXML
    private TableColumn<Emergency_call, String> nameColumn;
    @FXML
    private TableColumn<Emergency_call, String> ipColumn;

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
        Platform.runLater(() -> {
            try {
                setRoot("manager_main");
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
        Platform.runLater(() -> {
            try {
                ManagerClient.setLast_fxml("show_emergencyCall");
                setRoot("Emergency");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        updateListOfCalls();
        // updateChartData();
    }

    @Subscribe
    public void New_Emergency_call(Emergency_Call_Event event)

    {
        previousCalls=event.getCalls().getCalls();
        updateListOfCalls();

    }



    private void showAlert(String title, String header, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType, content);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.show();
    }


    private void updateListOfCalls() {
        Platform.runLater(() -> {
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

            // Clear the previous filtered list of emergency calls
            filteredCalls.clear();

            // Filter the list of emergency calls based on the selected time period and community
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

            // Update the line chart data
            updateChartData();

            // Update the last selected community and date range
            lastSelectedCommunity = selectedCommunity;
            lastStartDate = startDate;
            lastEndDate = endDate;

            updateChartData();

        });
        // updateChartData();
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
        ipColumn.setCellValueFactory(new PropertyValueFactory<>("Host"));

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
        // Set cell value factory for ipColumn to include the host value
        ipColumn.setCellFactory(tc -> new TableCell<>() {
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


        // Configure X-axis (category axis)
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Date");
        // Calls_chart.setBarGap(20); // Set the gap between bars
        //Calls_chart.setCategoryGap(10); // Set the gap between categories

        Calls_chart.getXAxis().setTickLabelRotation(90);
        Calls_chart.getXAxis().setTickLabelGap(10);

        // Configure Y-axis (number axis)
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Number of Calls");
        yAxis.setAutoRanging(false); // Disable auto-ranging
        yAxis.setLowerBound(0); // Set lower bound to 0

        // Get the maximum count of calls from the filteredCalls list
        int maxCount = filteredCalls.stream()
                .mapToInt(call -> 1) // Assuming each call counts as 1
                .max()
                .orElse(0);

        // Calculate a suitable upper bound for the axis
        double upperBound = Math.max(10, maxCount + 1); // Adjust the lower bound for desired granularity

        // Calculate a suitable tick unit based on the maximum count of calls
        double tickUnit = Math.max(1, upperBound / 10); // Adjust the divisor for desired granularity

        yAxis.setUpperBound(upperBound); // Set upper bound dynamically
        yAxis.setTickUnit(tickUnit); // Set tick unit dynamically
        yAxis.setTickLabelFormatter(new StringConverter<Number>() {
            @Override
            public String toString(Number object) {
                return String.format("%d", object.intValue());
            }

            @Override
            public Number fromString(String string) {
                return null; // Not needed for our case
            }
        });

        // Set chart axes
        // Calls_chart.setBarGap(0);
        //Calls_chart.setCategoryGap(0);
        Calls_chart.setHorizontalGridLinesVisible(false);
        Calls_chart.setVerticalGridLinesVisible(true);
        Calls_chart.setVerticalZeroLineVisible(false);

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
            menuItem.setOnAction(event -> {                             //---------------------------menu on action
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

            //updateChartData();
        });
        To_date.setOnAction(event -> {
            lastEndDate = To_date.getValue();
            updateListOfCalls();

            // updateChartData();
        });

        updateListOfCalls();
    }

    private void updateListOfCallsTwice() {
        // Trigger the update process twice with a delay in between
        updateListOfCalls();

        // Create a PauseTransition with a 1-second delay
        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished(event -> {
            // After the delay, trigger the update process again
            updateListOfCalls();
        });
        pause.play(); // Start the pause transition
        updateListOfCalls();
    }






    private void updateChartData() {
        // Clear existing data in the chart
        Calls_chart.getData().clear();
        x=0;

        // Create a new series
        XYChart.Series<String, Number> series = new XYChart.Series<>();

        // Count the number of calls for each date
        Map<String, Integer> callCountMap = new HashMap<>();
        for (Emergency_call call : filteredCalls) {
            LocalDateTime creationTime = call.getCreation_time();
            String creationTimeStr = creationTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")); // Format creation time as needed
            // Increment the call count for the corresponding date
            callCountMap.put(creationTimeStr, callCountMap.getOrDefault(creationTimeStr, 0) + 1);
        }

        // Add data to the series for points where the Y-value is greater than 0
        for (Map.Entry<String, Integer> entry : callCountMap.entrySet()) {
            int callCount = entry.getValue();
            if (callCount > 0) {
                x++;
                series.getData().add(new XYChart.Data<>(entry.getKey(), callCount));
            }
        }

        System.out.println(x);

        // Sort the series data based on X-axis values
        series.getData().sort(Comparator.comparing(data -> ((String) data.getXValue())));

        // Add the series to the chart
        Calls_chart.getData().add(series);

        // Set fixed width for the columns and rotate labels
        Calls_chart.getXAxis().setTickLabelRotation(0); // Setting rotation angle to 0 to display labels horizontally

        // Configure chart
        Calls_chart.setCreateSymbols(true); // Show symbols for each data point

        // Adjust chart settings if necessary
        Calls_chart.setAnimated(false); // Enable animation

        // Set chart title
        Calls_chart.setTitle("Emergency Calls");

        // Set Y-axis label
        Calls_chart.getYAxis().setLabel("Number of Calls");

        // Hide legend
        Calls_chart.setLegendVisible(false);

        // Adjust chart layout
        Calls_chart.setHorizontalGridLinesVisible(false); // Hide horizontal grid lines
        Calls_chart.setVerticalGridLinesVisible(true); // Show vertical grid lines
        Calls_chart.setVerticalZeroLineVisible(false); // Hide zero line

        // Set tick unit for x-axis to 1
        Calls_chart.getXAxis().setTickLength(1);

    }









}

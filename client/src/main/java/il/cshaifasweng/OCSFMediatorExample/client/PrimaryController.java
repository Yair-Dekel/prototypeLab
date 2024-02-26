package il.cshaifasweng.OCSFMediatorExample.client;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.MessageOfStatus;
import il.cshaifasweng.OCSFMediatorExample.entities.Task;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.util.Duration;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import static il.cshaifasweng.OCSFMediatorExample.client.SimpleClient.getClient;

public class PrimaryController {

	@FXML
	public VBox tasksContainer;
	@FXML
	public TextArea DataFromServerTF;
	@FXML
	public VBox newVbox;
	@FXML
	public AnchorPane btn1;
	public VBox tasksContaine;

	@FXML
	private Button displayTasks;
	@FXML
	private VBox showingVbox;

	private int msgId;


	// Define the font size and family you want to use
	double fontSize = 15.0; // Example font size
	String fontFamily = "Arial"; // Example font family

	// Create a Font object with the desired font size and family
	Font font = Font.font(fontFamily, fontSize);




	@FXML
	void changeRequest(Task task)
	{
		try {
			MessageOfStatus message = new MessageOfStatus(task, "change status");
			SimpleClient.getClient().sendToServer(message);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	/*@Subscribe
	public void setDataFromServerTF(MessageEvent event) {
		DataFromServerTF.setText(event.getMessage().getMessage());
	}//////////////////////////////////////////////////////////////////////////////////*/
	@Subscribe
	public void showNewRequest(NewDetailsEvent event) {

		DateTimeFormatter creationTime = DateTimeFormatter.ofPattern("HH:mm:ss");
		DateTimeFormatter deadlineTime = DateTimeFormatter.ofPattern("HH:mm:ss");

		Platform.runLater(() -> {
			Alert alert = new Alert(Alert.AlertType.INFORMATION,
					String.format("Task ID: %d\n\nType of task: %s\n\nCreation time: %s\n\n" +
									"\n\nDeadline time: %s\n\nStatus: %s",
							event.getMessage().getTask().getId(),
							event.getMessage().getTask().getType_of_task(),
							event.getMessage().getTask().getCreation_time().format(creationTime),
							event.getMessage().getTask().getDeadline().format(deadlineTime),
							event.getMessage().getTask().getStatus())
			);
			alert.setTitle("Task Information");
			alert.setHeaderText("Task Information:");
			alert.show();
		});
	}

	@FXML
	void displayTasks() {
		try {
			System.out.println("get into display controller1");
			SimpleClient.getClient().sendToServer("display tasks");
			System.out.println("sended to server");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

/************************************************************************************************************/
	@Subscribe
	public void displayTasks(TasksMessageEvent event) {/////////////////////////////////////////////////////////////////////////////////////////////
		System.out.println("got into displaytasks func2");
		tasksContaine.getChildren().clear(); // Clear existing content

		//for(Task T:tasks) System.out.println(T.getType_of_task());
		if (event != null) {
			System.out.println("evet is not null");

			List<Task> tasks = event.getTasksE().getTasks();
			if (tasks != null && !tasks.isEmpty()) {
				System.out.println("tasks!=null");
				for (Task task : tasks) {
					Button taskButton = createTaskButton(task);
					tasksContaine.getChildren().add(taskButton);
				}
			} else {
				showAlert("Tasks Information", "Tasks Information", "No tasks found in the data base.", Alert.AlertType.INFORMATION);

			}
		} else {
			showAlert("Error", "Error", "Invalid event received.", Alert.AlertType.ERROR);
		}
		// Set the vertical spacing between buttons
		tasksContaine.setSpacing(10); // Adjust the spacing as neede
	showingVbox.setSpacing(10);
	}
	private void showAlert(String title, String header, String content, Alert.AlertType alertType) {
		Alert alert = new Alert(alertType, content);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.show();
	}


	private Button createTaskButton(Task task) {
		Button button = new Button(String.format("Task %d", task.getId()));
		// Set the button to use its preferred width
		button.setMaxWidth(Double.MAX_VALUE);
		button.setPrefWidth(Control.USE_PREF_SIZE);
		// Set the preferred height of the button
		button.setPrefHeight(40); // Adjust the height as needed

		/*we'll think as a group what information we'll show here, before displaying tasks*/

		button.setOnAction(event -> handleTaskButtonClick(task));

		return button;
	}
/**********************************************************************************************************/






	private void handleTaskButtonClick(Task task
	){
		// Construct detailed task information

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); // Define your desired date format
		String formattedDeadline = task.getDeadline().format(formatter); // Format the deadline date

		String taskDetails = String.format("Task ID: %d\n\nType: %s\n\nDeadline: %s\n\nStatus: %s",
				task.getId(), task.getType_of_task(), formattedDeadline, task.getStatus());

		// Update the TextArea with task details
		DataFromServerTF.setText(taskDetails);
		DataFromServerTF.setVisible(true);
		// Enable text wrapping
		DataFromServerTF.setWrapText(true);
		// Set the font to the TextArea
		DataFromServerTF.setFont(font);
		Button button = new Button(String.format("Change Status"));


		button.setPrefHeight(40); // Adjust the height as needed

		// Apply CSS to change the text color of the button
		button.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
		button.setFont(font);
		// Remove the second element from showingVbox if it exists
		if (showingVbox.getChildren().size() > 1) {
			showingVbox.getChildren().remove(1);
		}

	    showingVbox.getChildren().add(button);
		// Set the button to use its preferred width
		button.setMaxWidth(Double.MAX_VALUE);
		button.setPrefWidth(Control.USE_PREF_SIZE);
		// add button of change status
		//button.setOnAction(event -> changeRequest(task));
	}


	@Subscribe
	public void getStarterData(NewSubscriberEvent event) {
		try {
			Message message = new Message(msgId, "send Submitters IDs");
			SimpleClient.getClient().sendToServer(message);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



	@Subscribe
	public void errorEvent(ErrorEvent event){
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
		Platform.runLater(() -> {
			Alert alert = new Alert(Alert.AlertType.ERROR,
					String.format("Message:\nId: %d\nData: %s\nTimestamp: %s\n",
							event.getMessage().getId(),
							event.getMessage().getMessage(),
							event.getMessage().getTimeStamp().format(dtf))
			);
			alert.setTitle("Error!");
			alert.setHeaderText("Error:");
			alert.show();
		});
	}

	@FXML
	void initialize() {
		EventBus.getDefault().register(this);
        try {
            getClient().sendToServer("display tasks");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //	MessageTF.clear();
	//	DataFromServerTF.clear();
		msgId=0;
	/*	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
		Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
			LocalTime currentTime = LocalTime.now();
			timeTF.setText(currentTime.format(dtf));
		}),
				new KeyFrame(Duration.seconds(1))
		)*
		clock.setCycleCount(Animation.INDEFINITE);
		clock.play();*/
		try {
			Message message = new Message(msgId, "add client");
			SimpleClient.getClient().sendToServer(message);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}

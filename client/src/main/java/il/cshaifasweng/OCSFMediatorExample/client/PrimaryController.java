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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class PrimaryController {

	@FXML
	private Button displayTasks;


	@FXML
	private VBox tasksContainer;


	private int msgId;


	@FXML
	void changeRequest(Task task) {
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
					String.format("Task ID: %d\nType of task: %s\nCreation time: %s\n" +
									"\nDeadline time: %s\nStatus: %s\n",
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
		tasksContainer.getChildren().clear(); // Clear existing content

		if (event != null) {
			List<Task> tasks = event.getTasks();
			System.out.println("recognized event");

			if (tasks != null && !tasks.isEmpty()) {
				System.out.println("tasks!=null");
				for (Task task : tasks) {
					Button taskButton = createTaskButton(task);
					tasksContainer.getChildren().add(taskButton);
				}
			} else {
				showAlert("Tasks Information", "Tasks Information", "No tasks found in the data base.", Alert.AlertType.INFORMATION);

			}
		} else {
			showAlert("Error", "Error", "Invalid event received.", Alert.AlertType.ERROR);
		}
	}
	private void showAlert(String title, String header, String content, Alert.AlertType alertType) {
		Alert alert = new Alert(alertType, content);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.show();
	}


	private Button createTaskButton(Task task) {
		Button button = new Button(String.format("Task %d", task.getId()));

		/*we'll think as a group what information we'll show here, before displaying tasks*/

//		button.setOnAction(event -> handleTaskButtonClick(task));

		return button;
	}
/**********************************************************************************************************/






/*	private void handleTaskButtonClick(Task task){
		// Construct detailed task information

		String taskDetails = String.format("Task ID: %d\nType: %s\nDeadline: %s\nStatus: %s",
				task.getId(), task.getType_of_task(), task.getDeadline(), task.getStatus());

		// Update the TextArea with task details
		DataFromServerTF.setText(taskDetails);
		DataFromServerTF.setVisible(true);
		Button button = new Button(String.format("Change Status"));
		// add button of change status
		button.setOnAction(event -> changeRequest(task));
	}*/


	/*@Subscribe
	public void setSubmittersTF(UpdateMessageEvent event) {
		submitterID1.setText(event.getMessage().getData().substring(0,9));
		submitterID2.setText(event.getMessage().getData().substring(11,20));
	}*/

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

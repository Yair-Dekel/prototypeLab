package il.cshaifasweng.OCSFMediatorExample.client;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.Task;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.util.Duration;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;
import javafx.scene.control.TextArea;




public class registered_user_Controller {

    @FXML
    private TextField another_info;
    @FXML
    private TextField TF_deadline;



/*
    @FXML
    private TextArea Tf_show_task;
    @FXML
    private Button btn_back;*/



    @FXML
    private AnchorPane btn1;

    @FXML
    private Button btn2;

    @FXML
    private Button btn3;

    private int msgId;
    @FXML
    void check_choose(ActionEvent event) throws IOException {

        Task newTask;
        Button clickedButton = (Button) event.getSource();

        int deadline= Integer.parseInt(TF_deadline.getText());
        try {
            TF_deadline.clear();
        //    newTask=new Task(another_info.getText(),deadline);
            Message message = new Message(msgId++,clickedButton.getText());
            SimpleClient.getClient().sendToServer(message);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        switchTonewTask(clickedButton.getText(),deadline);
    }



    @FXML
    void send_free_choose(ActionEvent event) throws IOException {


        int deadline= Integer.parseInt(TF_deadline.getText());
        //Task newTask=new Task(another_info.getText(),deadline) ;
        try {
            Message message = new Message(msgId++, another_info.getText());
            //newTask=message.getTask();
            SimpleClient.getClient().sendToServer(message);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        switchTonewTask(another_info.getText(),deadline);
    }

    private void switchTonewTask(String newTask,int deadline) throws IOException {

        // Switch to the new_task view
        SimpleChatClient.setRoot("new_task");
        show_new_task_Controller controller = (show_new_task_Controller) SimpleChatClient.getLoader().getController();
        controller.Display_newTask(newTask,deadline);



    }



}

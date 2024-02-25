
package il.cshaifasweng.OCSFMediatorExample.client;
import java.time.format.DateTimeFormatter;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import java.time.LocalDateTime;
import java.io.IOException;

public class show_new_task_Controller {

    @FXML
    private TextArea Tf_show_task;

    @FXML
    private AnchorPane btn1;

    @FXML
    private Button btn_back;

    @FXML
    void back(ActionEvent event) throws IOException {
        SimpleChatClient.setRoot("show_tasks");

    }
    public void Display_newTask(String msg,int deadline)
    {
        LocalDateTime Time=LocalDateTime.now();
        Tf_show_task.setText("Your task is: "+msg+"\n\n"+"Time of the task creation:"+ Time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))+"\n\n"+
                "The deadline for your task "+Time.plusDays(deadline).format(DateTimeFormatter.ofPattern("yyyy-MM-dd "))
                +"\n\n\n Please note: the task will be published after the approval of the manager");
    }

}

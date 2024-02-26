
package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.DisplayTasksMassage;
import il.cshaifasweng.OCSFMediatorExample.entities.Task;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static il.cshaifasweng.OCSFMediatorExample.client.SimpleChatClient.setRoot;
import static il.cshaifasweng.OCSFMediatorExample.client.SimpleClient.getClient;

public class MainController {

    @FXML
    private AnchorPane btn1;

    @FXML
    private Button see_all_task_btn;



    @FXML
    void switchToAllTask(ActionEvent event) throws IOException {
   /*     Task T1=new Task("help");
        Task T2=new Task("clean");*/
//        Task T1=new Task();
//        Task T2=new Task();

//        List <Task> mylist = new ArrayList<>();
//        mylist.add(T1);
//        mylist.add(T2);
//        DisplayTasksMassage dis= new DisplayTasksMassage(mylist);
//
//        TasksMessageEvent Event=new TasksMessageEvent(dis);

        Platform.runLater(() -> {
            try {
                setRoot("All_tasks_fxml");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}

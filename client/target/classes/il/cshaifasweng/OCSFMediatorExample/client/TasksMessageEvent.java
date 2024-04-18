package il.cshaifasweng.OCSFMediatorExample.client;
import il.cshaifasweng.OCSFMediatorExample.entities.DisplayDataMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.DisplayDataMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.Task;

import java.util.List;

public class TasksMessageEvent {


    private DisplayDataMessage dis;



    public TasksMessageEvent(DisplayDataMessage dis) {
        this.dis = dis;
    }

    public DisplayDataMessage getTasksE() {
            return this.dis;
        }
    }


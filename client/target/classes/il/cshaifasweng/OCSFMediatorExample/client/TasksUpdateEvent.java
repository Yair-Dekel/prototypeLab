package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.DisplayDataMessage;

public class TasksUpdateEvent {

    private DisplayDataMessage dis;



    public TasksUpdateEvent(DisplayDataMessage dis) {
        this.dis = dis;
    }

    public DisplayDataMessage getDis() {
        return this.dis;
    }

}


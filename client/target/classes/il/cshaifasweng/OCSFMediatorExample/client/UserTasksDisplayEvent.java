package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.DisplayDataMessage;

public class UserTasksDisplayEvent {


    private DisplayDataMessage dis;



    public UserTasksDisplayEvent(DisplayDataMessage dis) {
        this.dis = dis;
    }

    public DisplayDataMessage getDis() {
        return this.dis;
    }

}


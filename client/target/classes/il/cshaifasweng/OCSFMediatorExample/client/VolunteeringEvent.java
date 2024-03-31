package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.DisplayTasksMassage;
import il.cshaifasweng.OCSFMediatorExample.entities.DisplayTasksMassage;
import il.cshaifasweng.OCSFMediatorExample.entities.Task;

import java.util.List;

public class VolunteeringEvent {
    private DisplayTasksMassage dis;

    public VolunteeringEvent(DisplayTasksMassage dis) {
        this.dis = dis;
    }
    public DisplayTasksMassage getTasksE() {
        return this.dis;
    }


}


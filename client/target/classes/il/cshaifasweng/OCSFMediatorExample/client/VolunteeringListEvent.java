package il.cshaifasweng.OCSFMediatorExample.client;
import il.cshaifasweng.OCSFMediatorExample.entities.DisplayTasksMassage;
import il.cshaifasweng.OCSFMediatorExample.entities.Task;

import java.util.List;

public class VolunteeringListEvent {

    private DisplayTasksMassage dis;



    public VolunteeringListEvent(DisplayTasksMassage dis) {
        this.dis = dis;
    }

    public DisplayTasksMassage getTasksE() {
        return this.dis;
    }
}


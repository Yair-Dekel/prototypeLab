package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.DisplayDataMessage;

public class VolunteeredTasksShowEvent
{
    private DisplayDataMessage dis;

    public VolunteeredTasksShowEvent(DisplayDataMessage dis) {
        this.dis = dis;
    }
    public DisplayDataMessage getTasksE() {
        return this.dis;
    }
}

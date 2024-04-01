package il.cshaifasweng.OCSFMediatorExample.client;
import il.cshaifasweng.OCSFMediatorExample.entities.DisplayTasksMassage;
import il.cshaifasweng.OCSFMediatorExample.entities.MessageOfStatus;

public class RequestedTasksShowEvent {
    private DisplayTasksMassage dis;

    public RequestedTasksShowEvent(DisplayTasksMassage dis) {
        this.dis = dis;
    }
    public DisplayTasksMassage getTasksE() {
        return this.dis;
    }
}


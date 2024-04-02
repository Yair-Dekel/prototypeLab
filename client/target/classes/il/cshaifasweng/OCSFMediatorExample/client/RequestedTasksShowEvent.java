package il.cshaifasweng.OCSFMediatorExample.client;
import il.cshaifasweng.OCSFMediatorExample.entities.DisplayDataMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.DisplayDataMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.MessageOfStatus;

public class RequestedTasksShowEvent {
    private DisplayDataMessage dis;

    public RequestedTasksShowEvent(DisplayDataMessage dis) {
        this.dis = dis;
    }
    public DisplayDataMessage getTasksE() {
        return this.dis;
    }
}


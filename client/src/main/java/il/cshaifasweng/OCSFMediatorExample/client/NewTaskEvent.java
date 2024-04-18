package il.cshaifasweng.OCSFMediatorExample.client;
import il.cshaifasweng.OCSFMediatorExample.entities.MessageOfStatus;
import il.cshaifasweng.OCSFMediatorExample.entities.NewTaskMessage;

public class NewTaskEvent {

    private NewTaskMessage message;

    public il.cshaifasweng.OCSFMediatorExample.entities.NewTaskMessage getMessage() {
        return message;
    }

    public NewTaskEvent(NewTaskMessage message) {
        this.message = message;
    }
}

package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.MessageOfStatus;


public class NewDetailsEvent {

    private MessageOfStatus message;

    public il.cshaifasweng.OCSFMediatorExample.entities.MessageOfStatus getMessage() {
        return message;
    }

    public NewDetailsEvent(MessageOfStatus message) {
        this.message = message;
    }
}

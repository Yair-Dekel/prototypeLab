package il.cshaifasweng.OCSFMediatorExample.client;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.MessageOfStatus;
public class PersonVolunteering {
    private MessageOfStatus message;

    public il.cshaifasweng.OCSFMediatorExample.entities.MessageOfStatus getMessage() {
        return message;
    }

    public PersonVolunteering(MessageOfStatus message) {
        this.message = message;
    }
}

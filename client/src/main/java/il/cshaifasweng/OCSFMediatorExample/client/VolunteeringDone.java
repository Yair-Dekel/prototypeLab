package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.MessageOfStatus;

public class VolunteeringDone {
    private MessageOfStatus message;

    public il.cshaifasweng.OCSFMediatorExample.entities.MessageOfStatus getMessage() {
        return message;
    }

    public VolunteeringDone(MessageOfStatus message) {
        this.message = message;
    }
}

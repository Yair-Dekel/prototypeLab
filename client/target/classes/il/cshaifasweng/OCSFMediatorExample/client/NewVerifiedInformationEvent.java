package il.cshaifasweng.OCSFMediatorExample.client;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.Registered_user;

public class NewVerifiedInformationEvent {
    private Message message;
    private Registered_user user;

    public NewVerifiedInformationEvent(Message message) {
        this.message = message;
    }

    public Registered_user getUser() {
        return user;
    }

    public void setUser(Registered_user user) {
        this.user = user;
    }

    public Message getMessage() {
        return message;
}
}

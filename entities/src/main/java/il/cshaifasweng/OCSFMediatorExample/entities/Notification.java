package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;

public class Notification implements Serializable {
    private String notification;
    private Registered_user addressee;

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public Registered_user getAddressee() {
        return addressee;
    }

    public void setAddressee(Registered_user addressee) {
        this.addressee = addressee;
    }

    public Notification(String notification, Registered_user addressee) {
        this.notification = notification;
        this.addressee = addressee;
    }
}

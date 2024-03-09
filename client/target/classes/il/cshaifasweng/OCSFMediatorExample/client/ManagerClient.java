package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.client.ocsf.AbstractClient;

import java.io.IOException;
import il.cshaifasweng.OCSFMediatorExample.entities.DisplayTasksMassage;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.MessageOfStatus;
import il.cshaifasweng.OCSFMediatorExample.entities.Task;
import org.greenrobot.eventbus.EventBus;

import il.cshaifasweng.OCSFMediatorExample.client.ocsf.AbstractClient;

import java.io.IOException;
import java.util.List;
import javafx.collections.ObservableList;

public class ManagerClient extends AbstractClient {

    private static ManagerClient managerClient = null;


    /**
     * Constructs the client.
     *
     * @param host the server's host name.
     * @param port the port number.
     */
    private ManagerClient(String host, int port) {
        super(host, port);
    }

    @Override
    protected void handleMessageFromServer(Object msg) throws IOException {

        if (msg instanceof DisplayTasksMassage) {
            DisplayTasksMassage dis = (DisplayTasksMassage) msg;
            EventBus.getDefault().post(new TasksMessageEvent(dis));
            System.out.println("recognized massage as a list of tasks");
        }
        else if (msg instanceof MessageOfStatus) {
            MessageOfStatus message1 = (MessageOfStatus) msg;
            if (message1.getChangeStatus().equals("task accepted")) {
                EventBus.getDefault().post(new NewDetailsEvent(message1));
            } else if (message1.getChangeStatus().equals("task rejected")) {
                EventBus.getDefault().post(new TaskRejectEvent(message1));
            }
        }




    }

    public static ManagerClient getManagerClient() {
        if (managerClient == null) {
            managerClient = new ManagerClient("localhost", 3000);
        }
        return managerClient;
    }

}

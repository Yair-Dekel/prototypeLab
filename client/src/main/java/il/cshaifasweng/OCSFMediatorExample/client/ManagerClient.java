package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.client.ocsf.AbstractClient;

import java.io.IOException;

import il.cshaifasweng.OCSFMediatorExample.entities.*;
import org.greenrobot.eventbus.EventBus;

public class ManagerClient extends AbstractClient {

    private static Registered_user managerClient=null;
    private static ManagerClient client = null;
    private static Registered_user loggedInUser=null;


    /**
     * Constructs the client.
     *
     * @param host the server's host name.
     * @param port the port number.
     */
    private ManagerClient(String host, int port) {
        super(host, port);
    }


    public static Registered_user getLoggedInUser() {
        return loggedInUser;
    }

    public static void setLoggedInUser(Registered_user user) {
        if (loggedInUser == null) {
            System.out.println("client created");
            loggedInUser = user;
        }
    }



    @Override
    protected void handleMessageFromServer(Object msg) throws IOException {

        if (msg instanceof DisplayTasksMassage) {
            DisplayTasksMassage dis = (DisplayTasksMassage) msg;
            EventBus.getDefault().post(new TasksMessageEvent(dis));
            System.out.println("recognized massage as a list of tasks");
        } else if (msg instanceof MessageOfStatus) {
            MessageOfStatus message1 = (MessageOfStatus) msg;
            if (message1.getChangeStatus().equals("task accepted")) {
                EventBus.getDefault().post(new NewDetailsEvent(message1));
            } else if (message1.getChangeStatus().equals("task rejected")) {
                EventBus.getDefault().post(new TaskRejectEvent(message1));
            }
        }

    }



    public static void setManagerClient(Registered_user managerClient) {
        ManagerClient.managerClient = managerClient;
    }

    public static Registered_user getManagerClient() {
        return managerClient;
    }

    public static ManagerClient getClient() {
        if (client == null) {
            client = new ManagerClient("localhost", 3000);
        }
        return client;
    }


}

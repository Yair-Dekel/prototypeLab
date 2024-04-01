package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.client.ocsf.AbstractClient;

import java.io.IOException;
import java.util.List;

import il.cshaifasweng.OCSFMediatorExample.entities.*;
import org.greenrobot.eventbus.EventBus;

public class ManagerClient extends AbstractClient {

    private static Registered_user managerClient=null;
    private static ManagerClient client = null;


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
        } else if (msg instanceof DisplayCalls) {
            List <Emergency_call> calls=((DisplayCalls) msg).getCalls();
            for (Emergency_call call : calls) {
                System.out.println(call.getGiven_name());
            }
            EventBus.getDefault().post(new Emergency_Call_Event((DisplayCalls) msg));

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

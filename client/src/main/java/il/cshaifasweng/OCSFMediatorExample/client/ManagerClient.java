package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.client.Emergency_Call_Event;
import il.cshaifasweng.OCSFMediatorExample.client.*;
import il.cshaifasweng.OCSFMediatorExample.client.ocsf.AbstractClient;

import java.io.IOException;
import java.util.List;

import il.cshaifasweng.OCSFMediatorExample.entities.*;
import org.greenrobot.eventbus.EventBus;

public class ManagerClient extends AbstractClient {

    private static Registered_user managerClient = null;
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

    @Override
    protected void handleMessageFromServer(Object msg) throws IOException {

        if (msg instanceof DisplayDataMessage) {
            DisplayDataMessage dis = (DisplayDataMessage) msg;
            if (dis.getDataType().equals("tasks")) {
                EventBus.getDefault().post(new TasksMessageEvent(dis));
//            } else if (dis.getDataType().equals("all tasks")) {
//                ObservableList<Task> ALLTask_ = FXCollections.observableArrayList(dis.getTasks());
//                TasksOb.getInstance().setObservableTasks(ALLTask_);
            } else if (dis.getDataType().equals("members")) {
                System.out.println("c");
                EventBus.getDefault().post(new MembersDisplayEvent(dis));
            } else if (dis.getDataType().equals("uploaded")) {
                System.out.println("client");
                EventBus.getDefault().post(new UserTasksDisplayEvent(dis));
            } else if (dis.getDataType().equals("preformed")) {
                System.out.println("clientp");
                EventBus.getDefault().post(new CompletedEvent(dis));
            }

        }
//        else if (msg instanceof ) {
////            List <Emergency_call> calls=((DisplayCalls) msg).getCalls();
//            UserTasksController.class.
//        }
        else if (msg instanceof DisplayCalls) {
            List <Emergency_call> calls=((DisplayCalls) msg).getCalls();
            for (Emergency_call call : calls) {
                System.out.println(call.getGiven_name());
            }
            EventBus.getDefault().post(new Emergency_Call_Event((DisplayCalls) msg));

        }
//        else if (msg instanceof AddToObMessage) {
//                AddToObMessage NT = (AddToObMessage) msg;
//                TasksOb.getInstance().observableTasks.add(NT.getNewtask());
//        }
         else if (msg instanceof MessageOfStatus) {
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

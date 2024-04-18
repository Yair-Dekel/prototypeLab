package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.*;

import il.cshaifasweng.OCSFMediatorExample.client.ocsf.AbstractClient;
import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

public class UserClient extends AbstractClient {

    /*just for the running check*/
    private static Registered_user loggedInUser=null;
    private static UserClient client = null;

    //    public static void login(String username, String password) { //rina and malek
//        // Perform login logic and set the loggedInUser
//        // ...
//
//        // Example: Setting the loggedInUser
//        // here it need to return from server with the user instance
//        //loggedInUser = new Registered_user(username, /* other user details */);
//        /*just for the running*/
////        loggedInUser = new Registered_user("Rom", "Levi", "rom_levi1", "123", false, "0507773121", "Haifa");
//    }
    private UserClient(String host, int port) {
        super(host, port);
    }

    public static Registered_user getLoggedInUser() {
        return loggedInUser;
    }

    public static void setLoggedInUser(Registered_user user) {

            loggedInUser = user;

    }

    @Override
    protected void handleMessageFromServer(Object msg) throws IOException {
        System.out.println("got into handleMessageFromServer ");
        if (msg instanceof NewTaskMessage) {      //new task info
            NewTaskMessage ntm = (NewTaskMessage) msg;
            EventBus.getDefault().post(new NewTaskEvent(ntm));

        }
        else if (msg instanceof Message) {      ///of login
            System.out.println("in client/handlefrom serverr /in message inst");
            Message message=(Message)msg;
            System.out.println(message.getMessage());
            EventBus.getDefault().post(new NewVerifiedInformationEvent(message));
        } else if (msg instanceof DisplayDataMessage) {
            System.out.println("userclient dis");
            DisplayDataMessage dis = (DisplayDataMessage) msg;
            System.out.println("8888888888888888888888888888888888888888888888888");
            if(dis.getDataType().equals("Requested Tasks"))
            {
                System.out.println("**********************************************");
                EventBus.getDefault().post(new RequestedTasksShowEvent(dis));
                System.out.println("recognized massage as a list of requested tasks in userclient");
            }else if(dis.getDataType().equals("Volunteered Tasks")) {
                System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++");
                EventBus.getDefault().post(new VolunteeredTasksShowEvent(dis));
                System.out.println("recognized massage as a list of volunteered tasks in userclient");
            }
            else {
                System.out.println("correct inside ========");
                EventBus.getDefault().post(new VolunteeringEvent(dis));
                System.out.println("recognized massage as a list of tasks in userclient");
            }

        }else if(msg instanceof MessageOfStatus) {
            System.out.println("thanks");
            MessageOfStatus message1 = (MessageOfStatus) msg;
            if (message1.getChangeStatus().equals("Thanks for volunteering")) {
                System.out.println("thanks");
                EventBus.getDefault().post(new PersonVolunteering(message1));
            }else if (message1.getChangeStatus().equals("volunteering done")) {
                System.out.println("thanks for doning");
                EventBus.getDefault().post(new VolunteeringDone(message1));
            }

        }
        else if (msg instanceof Notification)
        {
            Notification notification = (Notification) msg;
            EventBus.getDefault().post(new UsersNotificationEvent(notification));

        }
    }

    public static UserClient getClient() {
        if (client == null) {
            System.out.println("client created");
            client = new UserClient("localhost", 3000);
        }
        return client;
    }
}
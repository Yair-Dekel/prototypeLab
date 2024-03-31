package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.*;

import il.cshaifasweng.OCSFMediatorExample.client.ocsf.AbstractClient;
import org.greenrobot.eventbus.EventBus;
import org.hibernate.SessionFactory;

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
        if (loggedInUser == null) {
            System.out.println("client created");
            loggedInUser = user;
        }
    }

    @Override
    protected void handleMessageFromServer(Object msg) throws IOException {
        System.out.println("got into handleMessageFromServer ");
        if (msg instanceof NewTaskMessage) {
            NewTaskMessage ntm = (NewTaskMessage) msg;
            EventBus.getDefault().post(new NewTaskEvent(ntm));

        } else if (msg instanceof Message) {
            System.out.println("in client/handlefrom serverr /in message inst");
            Message message = (Message) msg;
            System.out.println(message.getMessage());
            EventBus.getDefault().post(new NewVerifiedInformationEvent(message));
        } else if (msg instanceof DisplayTasksMassage) {
            System.out.println("userclient dis");
            DisplayTasksMassage dis = (DisplayTasksMassage) msg;
            EventBus.getDefault().post(new VolunteeringEvent(dis));
            System.out.println("recognized massage as a list of tasks in userclient");

        }else if(msg instanceof MessageOfStatus) {
            MessageOfStatus message1 = (MessageOfStatus) msg;
            if (message1.getChangeStatus().equals("Thanks for volunteering")) {
                EventBus.getDefault().post(new PersonVolunteering(message1));
            }
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
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
        if (loggedInUser == null) {
            System.out.println("client created");
            loggedInUser = user;
        }
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
        }


        else if (msg.equals("Log_Out")){
            System.out.println("we are in the UserClient Log_Out section ");
            loggedInUser=null;
            System.out.println("we are in the UserClient Log_Out section and we succeed to disconnect the user from the server");

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
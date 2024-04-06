package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.NewEmergencyCall;
import il.cshaifasweng.OCSFMediatorExample.entities.Registered_user;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

import static il.cshaifasweng.OCSFMediatorExample.client.SimpleChatClient.setRoot;

public class EmergencyController {

    @FXML
    private AnchorPane btn1;

    @FXML
    private Label info_label;

    @FXML
    void BackToTheMain(ActionEvent event)
    {
       if( (UserClient.getLoggedInUser()== null) && (ManagerClient.getManagerClient()==null))
       {
           Platform.runLater(() -> {
               try {
                   setRoot("log_in");
               } catch (IOException e) {
                   throw new RuntimeException(e);
               }
           });
       }
       else if((UserClient.getLoggedInUser()== null))
       {

               Platform.runLater(() -> {
                   try {
                       if(ManagerClient.getLast_fxml().equals("show_emergencyCall"))
                       {
                           System.out.println("in fxml show");
                           setRoot("show_emergencyCall");
                       } else if (ManagerClient.getLast_fxml().equals("members")) {
                           setRoot("members");

                       } else if (ManagerClient.getLast_fxml().equals("UserTasks")) {
                           setRoot("members");
                       } else {

                           setRoot("manager_main");
                       }
                   } catch (IOException e) {
                       throw new RuntimeException(e);
                   }
               });

       }else
       {
           Platform.runLater(() -> {
               try {
                   if(UserClient.getLast_fxml().equals("new_task"))
                   {
                       setRoot("new_task");
                   } else if (UserClient.getLast_fxml().equals("VolunteeringPage")) {
                       setRoot("VolunteeringPage");
                   } else if (UserClient.getLast_fxml().equals("showMyVolunteeredTasks")) {
                       setRoot("showMyVolunteeredTasks");
                   } else if (UserClient.getLast_fxml().equals("requestedTasksPage")) {
                       setRoot("requestedTasksPage");
                   } else
                   {

                       setRoot("user_main");
                   }
               } catch (IOException e) {
                   throw new RuntimeException(e);
               }
           });
       }

    }


    @FXML
    private void initialize() throws IOException {
        if ((UserClient.getLoggedInUser() == null) && (ManagerClient.getManagerClient() == null)) {
            System.out.println("1");
        String host= UserClient.getClient().getHost();
            info_label.setText("To make it easier for us to identify you,\nplease log in and press the emergency button again\n Your IP Address: "+host);
            UserClient.getClient().sendToServer(new NewEmergencyCall("Unknown User",null,null, host));
        } else if ((UserClient.getLoggedInUser() == null)) {
            System.out.println("2");
            Registered_user user = ManagerClient.getManagerClient();
            info_label.setText("Your firstName : " + user.getGivenName() + "\nyour lastName : " + user.getFamilyName()
                    + "\nYour phoneNumber to contact : " + user.getPhone_number() + "\nYour Community : " + user.getCommunity());
            ManagerClient.getClient().sendToServer(new NewEmergencyCall(user.getGivenName(),user.getPhone_number(),ManagerClient.getManagerClient(), null));
        } else {
            System.out.println("3");
            Registered_user user = UserClient.getLoggedInUser();
            info_label.setText("Your firstName : " + user.getGivenName() + "\nyour lastName : " + user.getFamilyName()
                    + "\nYour phoneNumber to contact : " + user.getPhone_number() + "\nYour Community : " + user.getCommunity());

            UserClient.getClient().sendToServer(new NewEmergencyCall(user.getGivenName(),user.getPhone_number(),UserClient.getLoggedInUser(), null));
        }

    }
}

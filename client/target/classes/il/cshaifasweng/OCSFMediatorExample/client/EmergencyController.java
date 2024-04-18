package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.NewEmergencyCall;
import il.cshaifasweng.OCSFMediatorExample.entities.Registered_user;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;

import static il.cshaifasweng.OCSFMediatorExample.client.SimpleChatClient.setRoot;

public class EmergencyController {

    @FXML
    private AnchorPane btn1;

    @FXML
    private Label info_label;
    private static Scene scene;
    private static Stage appStage;


    public void setAppStage(Stage appStage) {
        this.appStage = appStage;
    }


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
//           Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//           currentStage.close();
           Platform.runLater(() -> {
               try {
                   setRoot("manager_main");
               } catch (IOException e) {
                   throw new RuntimeException(e);
               }
           });

       }else
       {
           Platform.runLater(() -> {
               try {
                   setRoot("user_main");
               } catch (IOException e) {
                   throw new RuntimeException(e);
               }
           });
       }

    }
//    @Subscribe
//    public void TaskNotification(UsersNotificationEvent event)
//    {
//        PostNotifications.getInstance().TaskNotification(event);
//    }

    @FXML
    private void initialize() throws IOException {
        if ((UserClient.getLoggedInUser() == null) && (ManagerClient.getManagerClient() == null)) {
//        if (UnknownUserClient.getClient().isConnected())
        String host= UserClient.getClient().getHost();
            info_label.setText("To make it easier for us to identify you,\nplease log in and press the emergency button again\n Your IP Address: "+host);
            UserClient.getClient().sendToServer(new NewEmergencyCall("Unknown User",null,null, host));
        } else if ((UserClient.getLoggedInUser() == null)) {
            Registered_user user = ManagerClient.getManagerClient();
            info_label.setText("Your firstName : " + user.getGivenName() + "\nyour lastName : " + user.getFamilyName()
                    + "\nYour phoneNumber to contact : " + user.getPhone_number() + "\nYour Community : " + user.getCommunity());
            ManagerClient.getClient().sendToServer(new NewEmergencyCall(user.getGivenName(),user.getPhone_number(),ManagerClient.getManagerClient(), null));
        } else {
            Registered_user user = UserClient.getLoggedInUser();
            info_label.setText("Your firstName : " + user.getGivenName() + "\nyour lastName : " + user.getFamilyName()
                    + "\nYour phoneNumber to contact : " + user.getPhone_number() + "\nYour Community : " + user.getCommunity());

            UserClient.getClient().sendToServer(new NewEmergencyCall(user.getGivenName(),user.getPhone_number(),UserClient.getLoggedInUser(), null));
        }

    }
}

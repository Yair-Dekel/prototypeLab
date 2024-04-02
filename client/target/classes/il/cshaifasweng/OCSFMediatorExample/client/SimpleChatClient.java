package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.net.URI;
import java.util.concurrent.CountDownLatch;


import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.CountDownLatch;

/**
 * JavaFX App
 */
public class SimpleChatClient extends Application {

    private static Scene scene;
    private static Stage appStage;
    private UserClient client;
    private ManagerClient managerClient;///////////////////????????????????????????????????????????????????????????
    private static FXMLLoader fxmlLoader;

    public static Stage getAppStage() {
        return appStage;
    }

    @Override
    public void start(Stage stage) throws IOException {
        EventBus.getDefault().register(this);
        appStage = stage;
        client = UserClient.getClient();
        client.openConnection();
        scene = new Scene(loadFXML("log_in"), 600, 600);
        stage.setScene(scene);
        stage.show();

    }



    static void setRoot(String fxml) throws IOException {
        Parent root = loadFXML(fxml);
        scene = new Scene(root);
        appStage.setScene(scene);
        appStage.show();
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SimpleChatClient.class.getResource(fxml + ".fxml"));
        Parent root= fxmlLoader.load();
        Object controller=fxmlLoader.getController();
        if (controller instanceof MainController) {
            MainController mainController = (MainController) controller;
            mainController.setAppStage(appStage);
        }
        else if(controller instanceof UserMainController){
            UserMainController UserController = (UserMainController) controller;
            UserController.setAppStage(appStage);
        }
        else if(controller instanceof EmergencyController) {
            EmergencyController emergebcyController = (EmergencyController) controller;
            emergebcyController.setAppStage(appStage);
        }
        return root;


    }

    public static FXMLLoader getLoader() {
        return fxmlLoader;
    }




    @Override
    public void stop() throws Exception {
        // TODO Auto-generated method stub
        EventBus.getDefault().unregister(this);
        super.stop();
    }


    @Subscribe
    public void onMessageEvent(MessageEvent message) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
        Platform.runLater(() -> {
            Alert alert = new Alert(AlertType.INFORMATION,
                    String.format("Message:\nId: %d\nData: %s\nTimestamp: %s\n",
                            message.getMessage().getId(),
                            message.getMessage().getMessage(),
                            message.getMessage().getTimeStamp().format(dtf))
            );
            alert.setTitle("new message");
            alert.setHeaderText("New Message:");
            alert.show();
        });
    }

    public static void main(String[] args) {
        launch();
    }

}
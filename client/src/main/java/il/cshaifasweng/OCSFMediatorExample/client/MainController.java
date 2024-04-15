package il.cshaifasweng.OCSFMediatorExample.client;

import java.sql.*;
import java.util.*;


import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.Registered_user;
import il.cshaifasweng.OCSFMediatorExample.entities.SecureUtils;
import il.cshaifasweng.OCSFMediatorExample.entities.Task;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;


import java.io.IOException;
import java.util.TimerTask;

import static il.cshaifasweng.OCSFMediatorExample.client.SimpleChatClient.setRoot;
import static il.cshaifasweng.OCSFMediatorExample.client.UserClient.getClient;

public class MainController {

    @FXML
    private Stage loginStage;

    @FXML
    private AnchorPane btn1;

    @FXML
    private Button see_all_task_btn;


    @FXML
    private TextField Username_TF;

    @FXML
    private Label user_name_label;


    @FXML
    private Button login_btn;

    @FXML
    private PasswordField password_TF;
    private int msgId;

    @FXML
    private Button Emergency_btn;



    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);

        // Custom GridPane to allow for more content
        GridPane gridPane = new GridPane();
        Label label = new Label(content);
        label.setWrapText(true); // Allow text wrapping
        gridPane.add(label, 0, 0);
        alert.getDialogPane().setContent(gridPane);

        alert.showAndWait();
    }

    private void showErrorDialog(String message) {
        System.out.println("error");
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    String saveUserName;

    @FXML
    void LogIN_check_info(ActionEvent event) throws IOException {
        if (!loginButtonEnabled) {
            return; // Don't proceed if login button is disabled
        }
        String password = password_TF.getText();
        String username = Username_TF.getText();
        saveUserName = username;
        System.out.println(password + "   " + username);
        password_TF.clear();
        Username_TF.clear();

//        byte[] salt = retrieveSaltForUser(username);
//        if (salt == null) {
//            // Salt not found for the user
//            showErrorDialog("User not found or salt not available");
//            return;
//        }

//        String hashedPassword = SecureUtils.getSecurePassword(password,salt);
//        System.out.println("Hashed password: " + hashedPassword);

        Message message = new Message("Confirm information", username, password);
        UserClient.getClient().sendToServer(message);

    }

//    private byte[] retrieveSaltForUser(String username) {
//        byte[] salt = null;
//        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Atis?serverTimezone=UTC", "root", "050898")) {
//            String query = "SELECT salt FROM Users WHERE username = ?";
//            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
//                preparedStatement.setString(1, username);
//                try (ResultSet resultSet = preparedStatement.executeQuery()) {
//                    if (resultSet.next()) {
//                        // Retrieve the salt string from the database
//                        String saltString = resultSet.getString("salt");
//                        // Decode the salt string from Base64 to bytes
//                        salt = Base64.getDecoder().decode(saltString);
//                        System.out.println("Retrieved salt for user " + username + ": " + Arrays.toString(salt));
//                    } else {
//                        System.out.println("Salt not found for user: " + username);
//                    }
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//            System.err.println("Error retrieving salt for user " + username + ": " + e.getMessage());
//            // Handle any potential exceptions
//        }
//
//        return salt;
//    }




    public static int loginAttempts = 1;
    public static final int MAX_LOGIN_ATTEMPTS = 3;
    public static final long LOCKOUT_DURATION = 60000; // 1 minute in milliseconds
    private static boolean loginButtonEnabled = true;

    private void disableLoginButtonForDuration(long duration) {
        System.out.println("inside disable#####################3");
        loginButtonEnabled = false;
        login_btn.setDisable(true);

        // Schedule a task to enable the login button after the specified duration
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                loginButtonEnabled = true;
                loginAttempts = 0; // Reset login attempts after lockout duration
                Platform.runLater(() -> {
                    login_btn.setDisable(false);
                });
            }
        }, duration);
    }

    @Subscribe
    public void result_user_input(NewVerifiedInformationEvent event) throws IOException {
        System.out.println("in client/after event/result_user_input");
        System.out.println("counter" + loginAttempts);
        if (loginAttempts == MAX_LOGIN_ATTEMPTS) {
            System.out.println("inside attemps if");
            //disableLoginButtonForDuration(LOCKOUT_DURATION);
            Platform.runLater(() -> {
                showErrorDialog("please try again after 1 minute.");
            });
            disableLoginButtonForDuration(LOCKOUT_DURATION);
            return;
        } else {
            if (event.getMessage().getMessage().equals("correct")) {
                /************UnknownUserClient.getClient().closeConnection();*/
                if (event.getMessage().getUser().getPermission())//1 for manager
                {
                    ManagerClient.getClient().openConnection();
                    ManagerClient.setManagerClient(event.getMessage().getUser());
                    switchToMainOfManager();
                } else {
                    UserClient.setLoggedInUser(event.getMessage().getUser());
                    switchToMainOfUser();
                }

            } else if (event.getMessage().getMessage().equals("wrongPassword")) {
                loginAttempts++;
                System.out.println(loginAttempts);

                Platform.runLater(() -> {
                    showErrorDialog("Wrong Password \n you try to write a wrong password, please try again");
                });
            } else if (event.getMessage().getMessage().equals("userNotExist")) {
                loginAttempts++;
                System.out.println(loginAttempts);
                System.out.println("inside wrong wrong");
                Platform.runLater(() -> {
                    showErrorDialog("wrong User Name and password \n you try to write a wrong username and password, please try again");
                });
            } else if (event.getMessage().getMessage().equals("exists")) {
                Platform.runLater(() -> {
                    showErrorDialog("you are already connected.");
                });
            }
        }

    }

    void switchToMainOfUser() {
        Platform.runLater(() -> {
            try {
                setRoot("user_main");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        EventBus.getDefault().unregister(this);
    }


    @FXML
    void switchToemergency(ActionEvent event) {
        System.out.println("here");
        Platform.runLater(() -> {
            try {
                setRoot("Emergency");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        EventBus.getDefault().unregister(this);
    }


    void switchToMainOfManager() throws IOException {
        System.out.println("here");
        Platform.runLater(() -> {
            try {
                setRoot("manager_main");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        EventBus.getDefault().unregister(this);
    }

/*
    void switchToMainOfManager()
    {
        System.out.println("switchToMainOfManager in manager");
        System.out.println(saveUserName + "999999999999999999999999999999999999999999999");


        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("manager_main.fxml"));
                Parent root = loader.load();
                Manager managerController = loader.getController();
                managerController.initialize(saveUserName); // Pass the username to initialize method

                // Show the scene
                Scene scene = new Scene(root);
                appStage.setScene(scene);
                appStage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
//                setRoot("manager_main");
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        });
    }

*/

    @FXML
    void initialize() throws IOException {
        System.out.println("initialized main controller");
        EventBus.getDefault().register(this);
        msgId = 0;
        try {
            UserClient.getClient().sendToServer("deadline check");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Set initial focus to the Username_TF TextField
        Username_TF.requestFocus();
        System.out.println("initialized");
    }


}
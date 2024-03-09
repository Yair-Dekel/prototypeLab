package il.cshaifasweng.OCSFMediatorExample.client;

import java.io.IOException;
import javafx.fxml.FXML;

public class SecondaryController {

    @FXML
     void switchToPrimary() throws IOException {
        SimpleChatClient.setRoot("primary");
    }
}
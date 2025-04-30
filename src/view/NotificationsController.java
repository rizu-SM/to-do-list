package view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import java.net.URL;
import java.util.ResourceBundle;

public class NotificationsController extends BaseController implements Initializable {
    @FXML
    private VBox notificationsContainer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        updateUserInfo();
        displayNoNotifications();
    }

    private void displayNoNotifications() {
        if (notificationsContainer != null) {
            notificationsContainer.getChildren().clear();
            
            Label noNotificationsLabel = new Label("No notifications today");
            noNotificationsLabel.getStyleClass().add("no-notifications-label");
            notificationsContainer.getChildren().add(noNotificationsLabel);
        }
    }
} 
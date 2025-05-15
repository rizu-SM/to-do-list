package view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import util.UserSession;
import javafx.stage.Stage;
import javafx.scene.control.ProgressIndicator;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import java.io.IOException;

public class BaseController {
    @FXML
    protected Label userNameLabel;
    
    @FXML
    protected Label userEmailLabel;

    @FXML
    private ProgressIndicator completedProgress, inProgressProgress, notStartedProgress;
    @FXML
    private Label completedPercent, inProgressPercent, notStartedPercent;

    protected void updateUserInfo() {
        UserSession session = UserSession.getInstance();
        if (userNameLabel != null) {
            userNameLabel.setText(session.getFullName());
        }
        if (userEmailLabel != null) {
            userEmailLabel.setText(session.getEmail());
        }
    }

    protected void updateUserInfo(String fullName, String email) {
        UserSession session = UserSession.getInstance();
        session.setFullName(fullName);
        session.setEmail(email);
        
        if (userNameLabel != null) {
            userNameLabel.setText(fullName);
        }
        if (userEmailLabel != null) {
            userEmailLabel.setText(email);
        }
    }

    protected void showError(String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    protected void storeCurrentWindowDimensions(Stage stage) {
        double currentWidth = stage.getWidth();
        double currentHeight = stage.getHeight();
    }

    public void updateTaskStatus(int completed, int inProgress, int notStarted) {
        int total = completed + inProgress + notStarted;
        double completedRatio = total == 0 ? 0 : (double) completed / total;
        double inProgressRatio = total == 0 ? 0 : (double) inProgress / total;
        double notStartedRatio = total == 0 ? 0 : (double) notStarted / total;

        completedProgress.setProgress(completedRatio);
        inProgressProgress.setProgress(inProgressRatio);
        notStartedProgress.setProgress(notStartedRatio);

        completedPercent.setText((int)(completedRatio * 100) + "%");
        inProgressPercent.setText((int)(inProgressRatio * 100) + "%");
        notStartedPercent.setText((int)(notStartedRatio * 100) + "%");
    }

    @FXML
    protected void handleLogout(javafx.event.ActionEvent event) {
        try {
            // Clear the user session
            UserSession.getInstance().clearSession();
            
            // Load the SignIn view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("SignIn.fxml"));
            Parent root = loader.load();
            
            // Get the current scene and update it
            Scene scene = ((Node) event.getSource()).getScene();
            if (scene != null) {
                scene.setRoot(root);
            }
        } catch (IOException e) {
            showError("Failed to logout: " + e.getMessage());
        }
    }
} 
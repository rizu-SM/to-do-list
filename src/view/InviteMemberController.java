package view;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.event.ActionEvent;

public class InviteMemberController {

    @FXML
    private TextField emailField;

    @FXML
    private Button sendInviteButton;

    @FXML
    private Label dateLabel;

    @FXML
    private Label goBackLabel;

    @FXML
    private Label statusLabel;

    @FXML
    private void initialize() {
        updateDate();
        statusLabel.setText("");
    }

    @FXML
    private void handleSendInvite() {
        String email = emailField.getText().trim();

        if (email.isEmpty()) {
            showStatusMessage("Please enter an email address", false);
        } else {
            showStatusMessage("Invitation sent to: " + email, true);
            emailField.clear();
        }
    }

    @FXML
    private void updateDate() {
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMM dd yyyy", Locale.ENGLISH);
        dateLabel.setText(today.format(formatter));
    }

    @FXML
    private void handleGoBack(MouseEvent event) {
        try {
            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/dashboard.fxml"));
            Parent dashboardRoot = loader.load();
            
            Scene scene = new Scene(dashboardRoot);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDashboardButton(ActionEvent event) {
        try {
            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/dashboard.fxml"));
            Parent dashboardRoot = loader.load();
            
            Scene scene = new Scene(dashboardRoot);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogoutButton(ActionEvent event) {
        try {
            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/SignIn.fxml"));
            Parent signInRoot = loader.load();
            
            Scene scene = new Scene(signInRoot);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSettingsButton(ActionEvent event) {
        try {
            Node source = (Node) event.getSource();
            Scene scene = source.getScene();
            BorderPane borderPane = (BorderPane) scene.getRoot();
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Settings.fxml"));
            Parent settingsRoot = loader.load();
            
            borderPane.setCenter(settingsRoot);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleTaskCategoriesButton(ActionEvent event) {
        try {
            Node source = (Node) event.getSource();
            Scene scene = source.getScene();
            BorderPane borderPane = (BorderPane) scene.getRoot();
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/TaskCategories.fxml"));
            Parent taskCategoriesRoot = loader.load();
            
            borderPane.setCenter(taskCategoriesRoot);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showStatusMessage(String message, boolean isSuccess) {
        statusLabel.setText(message);
        if (isSuccess) {
            statusLabel.setTextFill(Color.GREEN);
        } else {
            statusLabel.setTextFill(Color.RED);
        }
    }
}

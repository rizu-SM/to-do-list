package Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import util.UserSession;
import Model.User;
import Controller.AuthController;

public class SettingsController {
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField emailField;
    @FXML private TextField contactField;
    @FXML private TextField positionField;
    @FXML private Button updateInfoButton;
    @FXML private Label messageLabel;
    @FXML private Hyperlink goBackLink;
    @FXML private RadioButton maleRadio;
    @FXML private RadioButton femaleRadio;
    @FXML private ToggleGroup genderGroup;

    // Référence au contrôleur du dashboard
    private DashboardController dashboardController;

    @FXML
    public void initialize() {
        // Charger les informations actuelles de l'utilisateur
        loadUserInfo();
    }

    public void setDashboardController(DashboardController controller) {
        this.dashboardController = controller;
    }

    private void loadUserInfo() {
        User currentUser = UserSession.getInstance().getCurrentUser();
        if (currentUser != null) {
            firstNameField.setText(currentUser.getPrenom());
            lastNameField.setText(currentUser.getNom());
            emailField.setText(currentUser.getEmail());
        }
    }

    @FXML
    private void handleUpdateInfo(ActionEvent event) {
        System.out.println("clicked");
        try {
            // Get the current user from UserSession
            User currentUser = UserSession.getInstance().getCurrentUser();
            if (currentUser == null) {
                showError("No user is currently logged in");
                return;
            }

            // Get values from the form fields
            String nom = lastNameField.getText();
            String prenom = firstNameField.getText();
            String email = emailField.getText();
            
            // Get gender from radio buttons
            char sex = maleRadio.isSelected() ? 'M' : 'F';

            // Validate input
            if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty()) {
                showError("Please fill in all fields");
                return;
            }

            // Create an instance of AuthController
            AuthController authController = new AuthController();

            // Call the updateProfile method with the correct parameters
            boolean success = authController.updateProfile(currentUser.getId(), nom, prenom, email, sex);

            if (success) {
                // Update the current user's information in the session
                currentUser.setNom(nom);
                currentUser.setPrenom(prenom);
                currentUser.setEmail(email);
                currentUser.setSex(sex);
                
                showSuccess("Profile updated successfully!");
                // Update the welcome label with new user info
                if (dashboardController != null) {
                    dashboardController.updateUserInfo();
                }
            } else {
                showError("Failed to update profile");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showError("An error occurred while updating profile");
        }
    }

    private void showSuccess(String message) {
        messageLabel.setTextFill(javafx.scene.paint.Color.GREEN);
        messageLabel.setText(message);
    }

    private void showError(String message) {
        messageLabel.setTextFill(javafx.scene.paint.Color.RED);
        messageLabel.setText(message);
    }

    @FXML
    private void goBackToDashboard(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/dashboard.fxml"));
            Parent dashboardRoot = loader.load();
            
            // Obtenir la scène actuelle
            Scene currentScene = ((Node) event.getSource()).getScene();
            
            // Remplacer la scène entière
            currentScene.setRoot(dashboardRoot);
            
        } catch (Exception e) {
            e.printStackTrace();
            messageLabel.setTextFill(javafx.scene.paint.Color.RED);
            messageLabel.setText("Error returning to dashboard: " + e.getMessage());
        }
    }
}
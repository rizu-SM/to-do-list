package view;

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
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class SettingsController {
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField emailField;
    @FXML private TextField contactField;
    @FXML private TextField positionField;
    @FXML private Button updateInfoButton;
    @FXML private Label messageLabel;
    @FXML private Hyperlink goBackLink;

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
        // TODO: Charger les informations depuis une base de données ou un fichier
        // Pour l'instant, on met des valeurs par défaut
        firstNameField.setText("Sundar");
        lastNameField.setText("Gurung");
        emailField.setText("sundargurung360@gmail.com");
        contactField.setText("");
        positionField.setText("");
    }

    @FXML
    private void handleUpdateInfo(ActionEvent event) {
        // Vérifier que les champs requis sont remplis
        if (firstNameField.getText().trim().isEmpty() || 
            lastNameField.getText().trim().isEmpty() || 
            emailField.getText().trim().isEmpty()) {
            messageLabel.setTextFill(javafx.scene.paint.Color.RED);
            messageLabel.setText("Please fill in all required fields (First Name, Last Name, Email)");
            return;
        }

        // TODO: Sauvegarder les informations dans une base de données ou un fichier
        
        // Mettre à jour le dashboard
        if (dashboardController != null) {
            String fullName = firstNameField.getText() + " " + lastNameField.getText();
            dashboardController.updateUserInfo(fullName, emailField.getText());
        }

        messageLabel.setTextFill(javafx.scene.paint.Color.GREEN);
        messageLabel.setText("Information updated successfully!");
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
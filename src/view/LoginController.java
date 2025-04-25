package view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import Model.User;
import Controller.AuthController;

public class LoginController {
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    private AuthController authController = new AuthController();

    @FXML
    private void handleLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();

        User user = authController.login(email, password);
        if (user != null) {
            try {
                // Charger le dashboard
                FXMLLoader loader = new FXMLLoader(getClass().getResource("dashboard.fxml"));
                Parent dashboard = loader.load();

                // Obtenir le contrôleur et initialiser l'utilisateur
                DashboardController dashboardController = loader.getController();
                dashboardController.setCurrentUserId(user.getId());
                dashboardController.updateUserInfo(user.getNom(), user.getEmail());

                // Afficher le dashboard
                Stage stage = (Stage) emailField.getScene().getWindow();
                Scene scene = new Scene(dashboard);
                stage.setScene(scene);
                stage.show();
            } catch (Exception e) {
                errorLabel.setText("Erreur lors du chargement du dashboard: " + e.getMessage());
                errorLabel.setVisible(true);
            }
        } else {
            errorLabel.setText("Email ou mot de passe incorrect");
            errorLabel.setVisible(true);
        }
    }
} 
package Controller;

import java.io.IOException;

import Controller.AuthController;
import Model.User;
import util.UserSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import Controller.DashboardController;
import Model.DatabaseManager;
import Model.Task;

public class MainController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label label;

    @FXML
    private Button button;

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    public void afficherSignInForm(ActionEvent e) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/SignIn.fxml"));
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();

        double width = stage.getScene().getWidth();
        double height = stage.getScene().getHeight();

        scene = new Scene(root, width, height);

        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void afficherLogInForm(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/SignUp.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        
        // Get the current window dimensions
        double width = stage.getScene().getWidth();
        double height = stage.getScene().getHeight();
        
        // Create new scene with the same dimensions
        Scene scene = new Scene(root, width, height);
        
        // Set window properties
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void afficherDashboard(ActionEvent event) {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Please fill in all fields.", Alert.AlertType.WARNING);
            return;
        }

        // Use AuthController.login to check if the user exists
        User user = AuthController.login(email, password);

        if (user != null) {
            try {
                // Set user info in UserSession
                UserSession.getInstance().setUser(user);
                
                // Load the dashboard
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Dashboard.fxml"));
                Parent root = loader.load();
                
                // Get the dashboard controller
                DashboardController dashboardController = loader.getController();
                dashboardController.updateUserInfo();
                
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                
                // Get the current window dimensions
                double width = stage.getScene().getWidth();
                double height = stage.getScene().getHeight();
                
                // Create new scene with the same dimensions
                Scene scene = new Scene(root, width, height);
                
                // Set window properties
                stage.setResizable(false);
                stage.setScene(scene);
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Error", "Failed to load the dashboard.", Alert.AlertType.ERROR);
            }
        } else {
            showAlert("Login Failed", "Invalid email or password.", Alert.AlertType.WARNING);
        }
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
} 
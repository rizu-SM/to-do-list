package view;

import java.io.IOException;

import Controller.AuthController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class SignUpController {

    @FXML
    private TextField nomField;

    @FXML
    private TextField prenomField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField sexField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    public void handleSignUp(ActionEvent event) {
        // Get user input from the form
        String nom = nomField.getText();
        String prenom = prenomField.getText();
        String email = emailField.getText();
        String sex = sexField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        // Validate input fields
        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || sex.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showAlert("Error", "Please fill in all fields.", Alert.AlertType.WARNING);
            return;
        }

        if (!password.equals(confirmPassword)) {
            showAlert("Error", "Passwords do not match.", Alert.AlertType.WARNING);
            return;
        }

        char sexChar = sex.equalsIgnoreCase("Male") ? 'M' : 'F';

        // Call the signup function in AuthController
        AuthController authController = new AuthController();
        boolean success = authController.signup(nom, prenom, sexChar, email, password);

        if (success) {
            showAlert("Success", "Account created successfully!", Alert.AlertType.INFORMATION);
        } else {
            showAlert("Error", "Failed to create account. Please try again.", Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void afficherSignInForm(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/SignIn.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
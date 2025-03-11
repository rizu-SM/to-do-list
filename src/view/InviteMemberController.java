package view;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

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
    private void initialize() {
        updateDate();
    }

    @FXML
    private void handleSendInvite() {
        String email = emailField.getText().trim();

        if (email.isEmpty()) {
            showAlert("Error", "Please enter an email address.");
        } else {
            showAlert("Invitation Sent", "An invitation has been sent to: " + email);
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
        System.out.println("Go Back clicked!");
        // Ajoute ici la logique pour revenir à l'écran précédent
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

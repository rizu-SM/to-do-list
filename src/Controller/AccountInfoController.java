package Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AccountInfoController {

    @FXML
    private VBox formContainer;

    @FXML
    private Label dateLabel;

    @FXML
    private void handleGoBack() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Go Back");
        alert.setHeaderText(null);
        alert.setContentText("Go Back clicked!");
        alert.showAndWait();
    }

    public void initialize() {
        updateDate();
    }

    private void updateDate() {
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMM dd yyyy"); // Exemple : "Friday, Mar 08 2025"
        dateLabel.setText(today.format(formatter));
    }
} 
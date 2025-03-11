package view;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label; // ✅ Correct import
import javafx.scene.layout.VBox;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AccountInfoController {

    @FXML
    private VBox formContainer; // Lien avec le FXML

    @FXML
    private Label dateLabel; // ✅ S'assurer que cet ID correspond à celui du FXML

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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE,"
        		                                                   + " MMM dd yyyy"); // Exemple : "Friday, Mar 08 2025"
        dateLabel.setText(today.format(formatter));
    }
}

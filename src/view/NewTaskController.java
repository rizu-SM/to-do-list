package view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class NewTaskController {
    @FXML private TextField titleField;
    @FXML private DatePicker datePicker;
    @FXML private ChoiceBox<String> category;
    @FXML private TextArea descriptionArea;
    @FXML private Button doneButton;
    @FXML private Hyperlink goBackLink;
    @FXML private CheckBox extremePriority;
    @FXML private CheckBox moderatePriority;
    @FXML private CheckBox lowPriority;

	  @FXML
	  public void initialize() {
		  category.getItems().addAll("Personal", "Work", "Chores", "Religious", "Others");
		  category.setValue("Personal");
	  }

    @FXML
    private void goBackToDashboard(ActionEvent event) {
        try {
            // Charger le dashboard.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/dashboard.fxml"));
            Parent dashboardRoot = loader.load();
            
            // Obtenir la scène actuelle
            Scene currentScene = ((Node) event.getSource()).getScene();
            
            // Remplacer la scène entière au lieu de juste le centre
            currentScene.setRoot(dashboardRoot);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDoneButton(ActionEvent event) {
        // Validation des champs
        StringBuilder errorMessage = new StringBuilder();
        
        if (titleField.getText().trim().isEmpty()) {
            errorMessage.append("• Title is required\n");
        }
        
        if (datePicker.getValue() == null) {
            errorMessage.append("• Date is required\n");
        }
        
        if (descriptionArea.getText().trim().isEmpty()) {
            errorMessage.append("• Description is required\n");
        }
        
        if (!extremePriority.isSelected() && !moderatePriority.isSelected() && !lowPriority.isSelected()) {
            errorMessage.append("• Please select a priority\n");
        }
        
        // Si des erreurs sont trouvées, afficher l'alerte
        if (errorMessage.length() > 0) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Form Error");
            alert.setHeaderText("Please complete all required fields");
            alert.setContentText(errorMessage.toString());
            alert.showAndWait();
            return;
        }
        
        // Si tout est valide, procéder à la sauvegarde et retourner au dashboard
        String title = titleField.getText();
        String description = descriptionArea.getText();
        String selectedCategory = category.getValue();
        // ... autres champs

        // Après avoir sauvegardé, retourner au tableau de bord
        goBackToDashboard(event);
    }
}

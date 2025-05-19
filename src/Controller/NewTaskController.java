package Controller;

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
import java.io.IOException;
import Model.Task;
import util.UserSession;
import java.time.LocalDate;
import Controller.TaskController;
import javafx.collections.FXCollections;

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
        // Use CategoryManager for categories
        CategoryManager categoryManager = CategoryManager.getInstance();
        category.setItems(FXCollections.observableArrayList("Routin", "Work", "Study", "Sport", "other"));
        category.setValue("Routin");

        // Set up priority checkbox listeners for mutual exclusivity
        extremePriority.setOnAction(e -> {
            if (extremePriority.isSelected()) {
                moderatePriority.setSelected(false);
                lowPriority.setSelected(false);
            }
        });

        moderatePriority.setOnAction(e -> {
            if (moderatePriority.isSelected()) {
                extremePriority.setSelected(false);
                lowPriority.setSelected(false);
            }
        });

        lowPriority.setOnAction(e -> {
            if (lowPriority.isSelected()) {
                extremePriority.setSelected(false);
                moderatePriority.setSelected(false);
            }
        });
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
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Form Error");
            alert.setHeaderText("Please complete all required fields");
            alert.setContentText(errorMessage.toString());
            alert.showAndWait();
            return;
        }
        
        try {
            // Get task information
            String titre = titleField.getText();
            String description = descriptionArea.getText();
            String categorie = this.category.getValue();
            LocalDate dateLimite = datePicker.getValue();
            
            // Determine priority using correct ENUM values
            String priorite;
            if (extremePriority.isSelected()) {
                priorite = "Haute";
            } else if (moderatePriority.isSelected()) {
                priorite = "Moyenne";
            } else {
                priorite = "Faible";
            }

            // Get current user ID
            int userId = UserSession.getInstance().getCurrentUser().getId();

            // Create new task with the correct constructor and ENUM values
            Task newTask = new Task(userId, titre, description, dateLimite, "À faire", priorite, categorie);

            // Add the task to the database using TaskController
            TaskController taskController = new TaskController();
            if (taskController.addTask(newTask)) {
                // Add the task to the static list in DashboardController
                DashboardController.getAllTasks().add(0, newTask);
                
                // Navigate back to dashboard
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Dashboard.fxml"));
                    Parent dashboardRoot = loader.load();
                    
                    Scene scene = ((Node) event.getSource()).getScene();
                    scene.setRoot(dashboardRoot);
                    
                } catch (IOException e) {
                    e.printStackTrace();
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Navigation Error");
                    alert.setHeaderText("Could not return to dashboard");
                    alert.setContentText("An error occurred while trying to navigate back.");
                    alert.showAndWait();
                }
            } else {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Could not create task");
                alert.setContentText("Failed to save the task to the database.");
                alert.showAndWait();
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not create task");
            alert.setContentText("An error occurred while trying to save the task.");
            alert.showAndWait();
        }
    }
}
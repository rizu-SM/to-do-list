package view;

import Model.User;
import Model.Task;
import Controller.TaskController;
import util.UserSession;
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
    private TextArea descriptionField;

    @FXML
    private ChoiceBox<String> priorityComboBox;

    @FXML
    private DatePicker dueDatePicker;

    @FXML
    private ChoiceBox<String> categoryComboBox;

    @FXML
    public void initialize() {
        // Use CategoryManager for categories
        CategoryManager categoryManager = CategoryManager.getInstance();
        category.setItems(categoryManager.getCategories());
        category.setValue(categoryManager.getCategories().get(0));

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

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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
        try {
            // Récupérer l'utilisateur connecté
            User currentUser = UserSession.getInstance().getCurrentUser();
            if (currentUser == null) {
                showError("No user is currently logged in.");
                return;
            }

            // Récupérer les données du formulaire
            String title = titleField.getText();
            String description = descriptionArea.getText();
            LocalDate dueDate = datePicker.getValue();
            String priority = null;
            if (extremePriority.isSelected()) {
                priority = "Haute";
            } else if (moderatePriority.isSelected()) {
                priority = "Moyenne";
            } else if (lowPriority.isSelected()) {
                priority = "Faible";
            }
            String selectedCategory = category.getValue(); // Renommé pour éviter le conflit

            // Vérifier que tous les champs obligatoires sont remplis
            if (title == null || title.isEmpty() || dueDate == null || priority == null || selectedCategory == null) {
                showError("Please fill in all required fields.");
                return;
            }

            // Créer un nouvel objet Task
            Task newTask = new Task(
                currentUser.getId(), // userId
                title,               // titre
                description,         // description
                dueDate,             // dateLimite
                "À faire",           // statut par défaut
                priority,            // priorite
                selectedCategory     // categorie
            );

            // Ajouter la tâche à la base de données
            TaskController taskController = new TaskController();
            boolean success = taskController.addTask(newTask);

            if (success) {
                showSuccess("Task added successfully!");
                // Retourner au tableau de bord ou mettre à jour la liste des tâches
                goBackToDashboard(event);
            } else {
                showError("Failed to add the task.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showError("An error occurred: " + e.getMessage());
        }
    }
}

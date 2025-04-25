package view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.net.URL;

public class DashboardController {
	  private Stage stage;
	  private Scene scene; 
	  private Parent root;
    @FXML private Label dayLabel;
    @FXML private Label dateLabel;
    @FXML private VBox toDoListContainer;
    @FXML private VBox taskStatusContainer;
    @FXML private PieChart taskStatusChart;
    @FXML private VBox completedTaskContainer;
    @FXML private VBox taskContainer;
    @FXML private Button addTaskButton;
    
    // Ajout des labels pour les informations utilisateur
    @FXML private Label userNameLabel;
    @FXML private Label userEmailLabel;

    public void initialize() {
        // Afficher la date actuelle
        LocalDate today = LocalDate.now();
        dayLabel.setText(formatDay(today.getDayOfWeek().toString()));
        dateLabel.setText(today.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        // Ajuster la largeur des sections
        if (toDoListContainer != null && taskStatusContainer != null) {
            HBox.setHgrow(toDoListContainer, Priority.ALWAYS);
            HBox.setHgrow(taskStatusContainer, Priority.NEVER);
        }

        // Configurer le PieChart des statuts des tâches
        if (taskStatusChart != null) {
            PieChart.Data completed = new PieChart.Data("Completed", 0);
            PieChart.Data inProgress = new PieChart.Data("In Progress", 0);
            PieChart.Data pending = new PieChart.Data("Pending", 0);

            taskStatusChart.getData().addAll(completed, inProgress, pending);

         // Appliquer les couleurs une fois que les nodes sont créés
            taskStatusChart.getData().forEach(data -> {
                data.nodeProperty().addListener((obs, oldNode, newNode) -> {
                    if (newNode != null) {
                        String color = switch (data.getName()) {
                            case "Pending" -> "#E67E22"; // Orange
                            case "In Progress" -> "#3498DB"; // Bleu
                            case "Completed" -> "#2ECC71"; // Vert
                            default -> "#95A5A6"; // Gris par défaut
                        };
                        newNode.setStyle("-fx-pie-color: " + color + ";");
                    }
                });
            });
        }

        // Initialiser les informations utilisateur
        if (userNameLabel != null) {
            userNameLabel.setText("Sundar Gurung");
        }
        if (userEmailLabel != null) {
            userEmailLabel.setText("sundargurung360@gmail.com");
        }
    }

    // Méthode pour mettre à jour les informations utilisateur
    public void updateUserInfo(String fullName, String email) {
        if (userNameLabel != null) {
            userNameLabel.setText(fullName);
        }
        if (userEmailLabel != null) {
            userEmailLabel.setText(email);
        }
    }

    private String formatDay(String day) {
        return day.substring(0, 1).toUpperCase() + day.substring(1).toLowerCase();
    }

 // Ajouter une tâche avec priorité et statut
    private void addTask(String title, String description, String priority, String status) {
        if (taskContainer != null) {
            Label titleLabel = new Label(title);
            titleLabel.getStyleClass().add("task-title");

            Label descLabel = new Label(description);
            descLabel.getStyleClass().add("task-desc");

            // Création des labels pour Priority et Status
            Label priorityLabel = new Label("Priority: " + priority);
            priorityLabel.getStyleClass().add("task-priority");

            Label statusLabel = new Label(status);
            updateTaskStatus(statusLabel, status); // Appliquer la bonne couleur

            // HBox pour aligner Priority et Status sur la même ligne
            HBox taskDetails = new HBox(15, priorityLabel, statusLabel);
            taskDetails.getStyleClass().add("task-details");

            // Création du conteneur de la tâche
            VBox taskCard = new VBox(10, titleLabel, descLabel, taskDetails);
            taskCard.getStyleClass().add("task-card");

            taskContainer.getChildren().add(taskCard);
        }
    }

    // Ajouter une tâche complétée
    private void addCompletedTask(String title, String description) {
        if (completedTaskContainer != null) {
            Label titleLabel = new Label(title);
            titleLabel.getStyleClass().add("task-title");

            Label descLabel = new Label(description);
            descLabel.getStyleClass().add("task-desc");

            VBox taskCard = new VBox(titleLabel, descLabel);
            taskCard.getStyleClass().add("completed-task-card");

            completedTaskContainer.getChildren().add(taskCard);
        }
    }

    // Mise à jour des couleurs du statut de la tâche
    public void updateTaskStatus(Label statusLabel, String status) {
        // Appliquer directement le texte
        statusLabel.setText(status);

        // Supprimer toutes les classes existantes
        statusLabel.getStyleClass().removeAll("status-not-started", "status-in-progress", "status-completed");

        // Ajouter la bonne classe pour la couleur du texte
        switch (status) {
            case "Not Started":
                statusLabel.getStyleClass().add("status-not-started");
                break;
            case "In Progress":
                statusLabel.getStyleClass().add("status-in-progress");
                break;
            case "Completed":
                statusLabel.getStyleClass().add("status-completed");
                break;
        }
    }

    @FXML
    private void openSettings(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Settings.fxml"));
            Parent settingsRoot = loader.load();
            
            // Obtenir le contrôleur des paramètres
            SettingsController settingsController = loader.getController();
            settingsController.setDashboardController(this);
            
            // Remplacer le contenu actuel par les paramètres
            BorderPane borderPane = (BorderPane) ((Node) event.getSource()).getScene().getRoot();
            borderPane.setCenter(settingsRoot);
            
        } catch (IOException ex) {
            ex.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Impossible de charger les paramètres");
            alert.setContentText("Détails de l'erreur : " + ex.getMessage());
            alert.showAndWait();
        }
    }

    public void afficheNewTask(ActionEvent e) {
        try {
            // Vérifier que le fichier FXML existe
            URL fxmlUrl = getClass().getResource("/view/NewTask.fxml");
            if (fxmlUrl == null) {
                throw new IOException("Fichier NewTask.fxml non trouvé dans le chemin /view/");
            }

            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent newTaskRoot = loader.load();
            
            // Remplacer le contenu actuel par le formulaire de nouvelle tâche
            BorderPane borderPane = (BorderPane) ((Node) e.getSource()).getScene().getRoot();
            borderPane.setCenter(newTaskRoot);
            
        } catch (IOException ex) {
            ex.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Impossible de charger le formulaire de nouvelle tâche");
            alert.setContentText("Détails de l'erreur : " + ex.getMessage());
            alert.showAndWait();
        } catch (Exception ex) {
            ex.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Une erreur inattendue s'est produite");
            alert.setContentText("Détails de l'erreur : " + ex.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void showDashboard(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/dashboard.fxml"));
            Parent dashboardRoot = loader.load();
            
            // Obtenir la scène actuelle
            Scene currentScene = ((Node) event.getSource()).getScene();
            
            // Remplacer la scène entière
            currentScene.setRoot(dashboardRoot);
            
        } catch (IOException ex) {
            ex.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Impossible de charger le tableau de bord");
            alert.setContentText("Détails de l'erreur : " + ex.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void showTaskCategories(ActionEvent event) {
        try {
            // Get the current BorderPane
            BorderPane borderPane = (BorderPane) ((Node) event.getSource()).getScene().getRoot();
            
            // Load the TaskCategories.fxml using a different approach
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("TaskCategories.fxml"));
            Parent taskCategoriesRoot = loader.load();
            
            // Replace only the center content
            borderPane.setCenter(taskCategoriesRoot);
            
        } catch (IOException ex) {
            ex.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not load task categories");
            alert.setContentText("Details: " + ex.getMessage());
            alert.showAndWait();
        } catch (Exception ex) {
            ex.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("An unexpected error occurred");
            alert.setContentText("Details: " + ex.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            // Load the sign in page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/SignIn.fxml"));
            Parent signInRoot = loader.load();
            
            // Get the current stage
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            
            // Store current window dimensions
            double currentWidth = stage.getWidth();
            double currentHeight = stage.getHeight();
            
            // Create a new scene with the sign in page
            Scene scene = new Scene(signInRoot);
            
            // Set the new scene
            stage.setScene(scene);
            
            // Restore window dimensions
            stage.setWidth(currentWidth);
            stage.setHeight(currentHeight);
            
            stage.show();
            
        } catch (IOException ex) {
            ex.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not load sign in page");
            alert.setContentText("Details: " + ex.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void handleInviteButton(ActionEvent event) {
        try {
            // Load the invite member page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/InviteMember.fxml"));
            Parent inviteMemberRoot = loader.load();
            
            // Get the current stage
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            
            // Store current window dimensions
            double currentWidth = stage.getWidth();
            double currentHeight = stage.getHeight();
            
            // Create a new scene with the invite member page
            Scene scene = new Scene(inviteMemberRoot);
            
            // Set the new scene
            stage.setScene(scene);
            
            // Restore window dimensions
            stage.setWidth(currentWidth);
            stage.setHeight(currentHeight);
            
            stage.show();
            
        } catch (IOException ex) {
            ex.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not load invite member page");
            alert.setContentText("Details: " + ex.getMessage());
            alert.showAndWait();
        }
    }
}


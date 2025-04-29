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
import java.util.ArrayList;
import java.util.List;
import util.UserSession;
import Model.Task;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.control.ProgressIndicator;
import javafx.geometry.Pos;
import javafx.scene.text.Text;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ButtonType;
import java.util.Optional;

public class DashboardController extends BaseController implements Initializable {
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
    @FXML private Text userNameText;

    // Static list to store all tasks
    private static List<Task> allTasks = new ArrayList<>();

    // Public static method to access tasks
    public static List<Task> getAllTasks() {
        return allTasks;
    }

    @FXML
    private VBox taskStatsContainer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Update user info from session
        updateUserInfo();

        // Display current date
        LocalDate today = LocalDate.now();
        dayLabel.setText(formatDay(today.getDayOfWeek().toString()));
        dateLabel.setText(today.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        // Adjust section widths
        if (toDoListContainer != null && taskStatusContainer != null) {
            HBox.setHgrow(toDoListContainer, Priority.ALWAYS);
            HBox.setHgrow(taskStatusContainer, Priority.NEVER);
        }

        // Make task container scrollable
        if (taskContainer != null) {
            taskContainer.setSpacing(15);
            taskContainer.setPadding(new javafx.geometry.Insets(10));
            
            // Enable vertical growth
            VBox.setVgrow(taskContainer, Priority.ALWAYS);
            
            // Display all existing tasks
            displayAllTasks();
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

        updateTaskStatistics();
    }

    // Method to update user info and welcome message
    @Override
    public void updateUserInfo() {
        UserSession session = UserSession.getInstance();
        if (userNameLabel != null) {
            userNameLabel.setText(session.getFirstName());
        }
        if (userEmailLabel != null) {
            userEmailLabel.setText(session.getEmail());
        }
        if (userNameText != null) {
            userNameText.setText(session.getFirstName());
        }
        
        // Update welcome message in the main content area
        if (toDoListContainer != null && toDoListContainer.getParent() instanceof HBox) {
            HBox mainContent = (HBox) toDoListContainer.getParent();
            mainContent.lookupAll(".welcome-text").forEach(node -> {
                if (node instanceof Label) {
                    ((Label) node).setText("Welcome Back, " + session.getFirstName() + " 👋");
                }
            });
        }
    }

    private String formatDay(String day) {
        return day.substring(0, 1).toUpperCase() + day.substring(1).toLowerCase();
    }

    // Method to display all tasks
    private void displayAllTasks() {
        taskContainer.getChildren().clear();
        for (Task task : allTasks) {
            createTaskCard(task);
        }
    }

    // Method to create a task card
    private void createTaskCard(Task task) {
        // Create date header
        String dateStr = task.getCreatedDate().format(DateTimeFormatter.ofPattern("dd MMMM"));
        Label dateLabel = new Label(dateStr + (task.getCreatedDate().equals(LocalDate.now()) ? " • Today" : ""));
        dateLabel.getStyleClass().add("task-date");

        // Create title with options menu
        HBox titleBox = new HBox();
        titleBox.setSpacing(10);
        titleBox.getStyleClass().add("task-title-box");

        Label titleLabel = new Label(task.getTitle());
            titleLabel.getStyleClass().add("task-title");

        javafx.scene.layout.Region spacer = new javafx.scene.layout.Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        // Create status ComboBox
        ComboBox<String> statusComboBox = new ComboBox<>();
        statusComboBox.getItems().addAll("Not Started", "In Progress", "Completed");
        statusComboBox.setValue(task.getStatus());
        statusComboBox.getStyleClass().add("status-combo-box");
        
        // Handle status change
        statusComboBox.setOnAction(event -> {
            String newStatus = statusComboBox.getValue();
            task.setStatus(newStatus);
            updateTaskStatus(statusComboBox, newStatus);
            updateTaskStatistics();
        });
        
        // Set initial status style
        updateTaskStatus(statusComboBox, task.getStatus());

        // Create delete button
        Button deleteButton = new Button("🗑");
        deleteButton.getStyleClass().add("delete-task-button");
        deleteButton.setOnAction(e -> confirmAndDeleteTask(task));
        
        titleBox.getChildren().addAll(titleLabel, spacer, statusComboBox, deleteButton);

        // Description
        Label descLabel = new Label(task.getDescription());
            descLabel.getStyleClass().add("task-desc");
        descLabel.setWrapText(true);

        // Status and Priority row
        HBox detailsBox = new HBox();
        detailsBox.setSpacing(15);
        detailsBox.getStyleClass().add("task-details");

        Label priorityLabel = new Label("Priority: " + task.getPriority());
        priorityLabel.getStyleClass().add("task-priority");

        // Created date
        Label createdLabel = new Label("Created on " + task.getCreatedDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        createdLabel.getStyleClass().add("task-created-date");

        detailsBox.getChildren().addAll(priorityLabel, createdLabel);
        HBox.setHgrow(createdLabel, Priority.ALWAYS);

        // Main task card
        VBox taskCard = new VBox(10);
        taskCard.getStyleClass().add("task-card");
        taskCard.getChildren().addAll(dateLabel, titleBox, descLabel, detailsBox);

        // Add task card to container
        taskContainer.getChildren().add(taskCard);
        }

    private void confirmAndDeleteTask(Task task) {
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Delete Task");
        confirmDialog.setHeaderText("Delete Task");
        confirmDialog.setContentText("Are you sure you want to delete this task?");
        
        Optional<ButtonType> result = confirmDialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            allTasks.remove(task);
            displayAllTasks();
            updateTaskStatistics();
        }
    }

    private void updateTaskStatus(ComboBox<String> statusComboBox, String status) {
        // Remove all status-related style classes
        statusComboBox.getStyleClass().removeAll("status-not-started", "status-in-progress", "status-completed");

        // Add appropriate style class based on status
        switch (status) {
            case "Not Started":
                statusComboBox.getStyleClass().add("status-not-started");
                break;
            case "In Progress":
                statusComboBox.getStyleClass().add("status-in-progress");
                break;
            case "Completed":
                statusComboBox.getStyleClass().add("status-completed");
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

    @FXML
    private void showNotes(ActionEvent event) {
        try {
            // Get the current BorderPane
            BorderPane borderPane = (BorderPane) ((Node) event.getSource()).getScene().getRoot();
            
            // Load the Notes.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Notes.fxml"));
            Parent notesRoot = loader.load();
            
            // Replace only the center content
            borderPane.setCenter(notesRoot);
            
            // Update button styles
            Node sourceButton = (Node) event.getSource();
            if (sourceButton.getParent() instanceof VBox) {
                VBox sidebar = (VBox) sourceButton.getParent();
                
                // Reset all buttons to default style
                sidebar.getChildren().forEach(node -> {
                    if (node instanceof Button) {
                        node.getStyleClass().remove("sidebar-button-selected");
                        node.getStyleClass().add("sidebar-button");
                    }
                });
                
                // Set the Notes button to selected
                sourceButton.getStyleClass().remove("sidebar-button");
                sourceButton.getStyleClass().add("sidebar-button-selected");
            }
            
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Navigation Error");
            alert.setContentText("Could not load the Notes page. Please try again.");
            alert.showAndWait();
        }
    }

    @FXML
    public void showNewTask(ActionEvent event) {
        try {
            // Get the current BorderPane
            BorderPane borderPane = (BorderPane) ((Node) event.getSource()).getScene().getRoot();
            
            // Load the MyTasks.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MyTasks.fxml"));
            Parent myTasksRoot = loader.load();
            
            // Replace only the center content
            borderPane.setCenter(myTasksRoot);
            
            // Update button styles
            Node sourceButton = (Node) event.getSource();
            if (sourceButton.getParent() instanceof VBox) {
                VBox sidebar = (VBox) sourceButton.getParent();
                
                // Reset all buttons to default style
                sidebar.getChildren().forEach(node -> {
                    if (node instanceof Button) {
                        node.getStyleClass().remove("sidebar-button-selected");
                        node.getStyleClass().add("sidebar-button");
                    }
                });
                
                // Set the My Task button to selected
                sourceButton.getStyleClass().remove("sidebar-button");
                sourceButton.getStyleClass().add("sidebar-button-selected");
            }
            
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Navigation Error");
            alert.setContentText("Could not load the My Tasks page. Please try again.");
            alert.showAndWait();
        }
    }

    private void updateTaskStatistics() {
        taskStatsContainer.getChildren().clear();
        
        if (allTasks.isEmpty()) {
            Label noTasksLabel = new Label("No tasks available");
            noTasksLabel.getStyleClass().add("task-status-title");
            taskStatsContainer.getChildren().add(noTasksLabel);
            return;
        }

        Label titleLabel = new Label("Task Status");
        titleLabel.getStyleClass().add("task-status-title");
        taskStatsContainer.getChildren().add(titleLabel);

        int totalTasks = allTasks.size();
        int completedTasks = (int) allTasks.stream().filter(task -> task.getStatus().equals("Completed")).count();
        int inProgressTasks = (int) allTasks.stream().filter(task -> task.getStatus().equals("In Progress")).count();
        int notStartedTasks = (int) allTasks.stream().filter(task -> task.getStatus().equals("Not Started")).count();

        createStatusRow("Completed", completedTasks, totalTasks, "completed-progress");
        createStatusRow("In Progress", inProgressTasks, totalTasks, "in-progress-progress");
        createStatusRow("Not Started", notStartedTasks, totalTasks, "not-started-progress");
    }

    private void createStatusRow(String status, int count, int total, String styleClass) {
        HBox statusRow = new HBox();
        statusRow.getStyleClass().add("status-row");
        
        Label statusLabel = new Label(status);
        statusLabel.getStyleClass().add("status-label");
        
        ProgressIndicator progressIndicator = new ProgressIndicator();
        double percentage = total > 0 ? (double) count / total : 0;
        progressIndicator.setProgress(percentage);
        progressIndicator.getStyleClass().add(styleClass);
        
        Label percentageLabel = new Label(String.format("%.0f%%", percentage * 100));
        percentageLabel.getStyleClass().add("percentage-label");
        
        statusRow.getChildren().addAll(statusLabel, progressIndicator, percentageLabel);
        taskStatsContainer.getChildren().add(statusRow);
    }

    // Method to add a new task
    public void addTask(String title, String description, String priority, String status) {
        Task newTask = new Task(title, description, priority, status);
        allTasks.add(0, newTask);
        displayAllTasks();
        updateTaskStatistics();
    }

    public void refreshTasks() {
        // ... existing refresh code ...
        updateTaskStatistics();
    }
}


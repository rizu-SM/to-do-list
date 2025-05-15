package view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Insets;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import Model.Task;
import Model.User;
import Controller.TaskController;
import util.UserSession;
import java.io.IOException;
import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javafx.scene.Node;
import javafx.geometry.Pos;
import javafx.scene.shape.Circle;

public class SharedTasksController extends BaseController {
    @FXML private VBox tasksContainer;
    @FXML private Label headerText;
    @FXML private Label dayLabel;
    @FXML private Label dateLabel;
    @FXML private TextField searchField;
    @FXML private Label coinsAmount;
    
    private User taskOwner;
    private TaskController taskController;
    
    public void initialize() {
        taskController = new TaskController();
        
        // Set current date
        LocalDate today = LocalDate.now();
        dayLabel.setText(formatDay(today.getDayOfWeek().toString()));
        dateLabel.setText(today.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        
        // Setup search functionality
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterTasks(newValue);
        });
    }
    
    private String formatDay(String day) {
        return day.substring(0, 1).toUpperCase() + day.substring(1).toLowerCase();
    }
    
    private void filterTasks(String searchText) {
        tasksContainer.getChildren().clear();
        List<Task> sharedTasks = taskController.getTasksByUserId(taskOwner.getId());
        
        if (sharedTasks.isEmpty()) {
            Label noTasksLabel = new Label("No shared tasks available");
            noTasksLabel.getStyleClass().add("no-tasks-label");
            tasksContainer.getChildren().add(noTasksLabel);
            return;
        }
        
        for (Task task : sharedTasks) {
            if (searchText.isEmpty() || 
                task.getTitre().toLowerCase().contains(searchText.toLowerCase()) ||
                task.getDescription().toLowerCase().contains(searchText.toLowerCase())) {
                VBox taskCard = createTaskCard(task);
                tasksContainer.getChildren().add(taskCard);
            }
        }
    }
    
    public void setTaskOwner(User owner) {
        System.out.println("Setting task owner: " + owner.getEmail() + " (ID: " + owner.getId() + ")");
        this.taskOwner = owner;
        headerText.setText("Tasks shared by " + owner.getPrenom() + " " + owner.getNom());
        loadSharedTasks();
    }
    
    private void loadSharedTasks() {
        System.out.println("Loading shared tasks for user ID: " + taskOwner.getId());
        tasksContainer.getChildren().clear();
        List<Task> sharedTasks = taskController.getTasksByUserId(taskOwner.getId());
        System.out.println("Found " + sharedTasks.size() + " shared tasks");
        
        if (sharedTasks.isEmpty()) {
            System.out.println("No shared tasks available");
            Label noTasksLabel = new Label("No shared tasks available");
            noTasksLabel.getStyleClass().add("no-tasks-label");
            tasksContainer.getChildren().add(noTasksLabel);
            return;
        }
        
        for (Task task : sharedTasks) {
            System.out.println("Creating card for task: " + task.getTitre());
            VBox taskCard = createTaskCard(task);
            tasksContainer.getChildren().add(taskCard);
        }
    }
    
    private VBox createTaskCard(Task task) {
        VBox card = new VBox(10);
        card.getStyleClass().add("task-card");
        card.setMaxWidth(Double.MAX_VALUE);
        card.setStyle("-fx-background-color: white; -fx-padding: 15; -fx-background-radius: 8; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 0);");
        
        // Title with priority indicator
        HBox titleBox = new HBox(10);
        titleBox.setAlignment(Pos.CENTER_LEFT);
        
        // Priority indicator
        Circle priorityIndicator = new Circle(5);
        switch(task.getPriorite().toLowerCase()) {
            case "high":
                priorityIndicator.setFill(Color.RED);
                break;
            case "medium":
                priorityIndicator.setFill(Color.ORANGE);
                break;
            case "low":
                priorityIndicator.setFill(Color.GREEN);
                break;
            default:
                priorityIndicator.setFill(Color.GRAY);
        }
        
        Label titleLabel = new Label(task.getTitre());
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        titleBox.getChildren().addAll(priorityIndicator, titleLabel);
        
        // Description
        Label descriptionLabel = new Label(task.getDescription());
        descriptionLabel.setStyle("-fx-text-fill: #666666; -fx-font-size: 14px;");
        descriptionLabel.setWrapText(true);
        
        // Details Box
        HBox detailsBox = new HBox(15);
        detailsBox.setStyle("-fx-padding: 10 0 0 0;");
        
        // Due Date with icon
        HBox dueDateBox = new HBox(5);
        dueDateBox.setAlignment(Pos.CENTER_LEFT);
        Label dueDateIcon = new Label("📅");
        Label dueDateLabel = new Label(task.getDateLimite().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        dueDateLabel.setStyle("-fx-text-fill: #666666;");
        dueDateBox.getChildren().addAll(dueDateIcon, dueDateLabel);
        
        // Status with icon
        HBox statusBox = new HBox(5);
        statusBox.setAlignment(Pos.CENTER_LEFT);
        Label statusIcon = new Label("📊");
        Label statusLabel = new Label(task.getStatut());
        statusLabel.setStyle("-fx-text-fill: #666666;");
        statusBox.getChildren().addAll(statusIcon, statusLabel);
        
        // Category with icon
        HBox categoryBox = new HBox(5);
        categoryBox.setAlignment(Pos.CENTER_LEFT);
        Label categoryIcon = new Label("🏷️");
        Label categoryLabel = new Label(task.getCategorie());
        categoryLabel.setStyle("-fx-text-fill: #666666;");
        categoryBox.getChildren().addAll(categoryIcon, categoryLabel);
        
        detailsBox.getChildren().addAll(dueDateBox, statusBox, categoryBox);
        
        // Add all elements to the card
        card.getChildren().addAll(titleBox, descriptionLabel, detailsBox);
        
        // Add hover effect
        card.setOnMouseEntered(e -> card.setStyle("-fx-background-color: #f8f9fa; -fx-padding: 15; -fx-background-radius: 8; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 10, 0, 0, 0);"));
        card.setOnMouseExited(e -> card.setStyle("-fx-background-color: white; -fx-padding: 15; -fx-background-radius: 8; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 0);"));
        
        return card;
    }
    
    @FXML
    private void handleBackButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/IncomingInvitations.fxml"));
            Parent root = loader.load();
            Scene scene = tasksContainer.getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
            showError("Failed to return to invitations: " + e.getMessage());
        }
    }
    
    private BorderPane getRootBorderPane(Node source) {
        return (BorderPane) source.getScene().getRoot();
    }

    @FXML
    private void showNotifications(javafx.event.ActionEvent event) {
        try {
            // Load only the center content of Notifications view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Notifications.fxml"));
            Parent notificationsContent = loader.load();
            
            // Extract only the center content if it's a BorderPane
            if (notificationsContent instanceof BorderPane) {
                notificationsContent = (Parent) ((BorderPane) notificationsContent).getCenter();
            }
            
            // Get the current BorderPane and update its center
            BorderPane borderPane = getRootBorderPane((Node) event.getSource());
            if (borderPane != null) {
                borderPane.setCenter(notificationsContent);
            }
        } catch (IOException e) {
            showError("Failed to load notifications: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleLogout(javafx.event.ActionEvent event) {
        try {
            // Clear the user session
            UserSession.getInstance().clearSession();
            
            // Load the SignIn view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/SignIn.fxml"));
            Parent root = loader.load();
            
            // Get the current scene and update it
            Scene scene = ((Node) event.getSource()).getScene();
            if (scene != null) {
                scene.setRoot(root);
            }
        } catch (IOException e) {
            showError("Failed to logout: " + e.getMessage());
        }
    }
    
    @FXML
    private void showDashboard(javafx.event.ActionEvent event) {
        try {
            // Load only the center content of Dashboard view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Dashboard.fxml"));
            Parent dashboardContent = loader.load();
            
            // Extract only the center content if it's a BorderPane
            if (dashboardContent instanceof BorderPane) {
                dashboardContent = (Parent) ((BorderPane) dashboardContent).getCenter();
            }
            
            // Get the current BorderPane and update its center
            BorderPane borderPane = getRootBorderPane((Node) event.getSource());
            if (borderPane != null) {
                borderPane.setCenter(dashboardContent);
            }
        } catch (IOException e) {
            showError("Failed to load dashboard: " + e.getMessage());
        }
    }
    
    @FXML
    private void showMyTasks(javafx.event.ActionEvent event) {
        try {
            // Load only the center content of MyTasks view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MyTasks.fxml"));
            Parent myTasksContent = loader.load();
            
            // Extract only the center content if it's a BorderPane
            if (myTasksContent instanceof BorderPane) {
                myTasksContent = (Parent) ((BorderPane) myTasksContent).getCenter();
            }
            
            // Get the current BorderPane and update its center
            BorderPane borderPane = getRootBorderPane((Node) event.getSource());
            if (borderPane != null) {
                borderPane.setCenter(myTasksContent);
            }
        } catch (IOException e) {
            showError("Failed to load my tasks: " + e.getMessage());
        }
    }
    
    @FXML
    private void showNotes(javafx.event.ActionEvent event) {
        try {
            // Load only the center content of Notes view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Notes.fxml"));
            Parent notesContent = loader.load();
            
            // Extract only the center content if it's a BorderPane
            if (notesContent instanceof BorderPane) {
                notesContent = (Parent) ((BorderPane) notesContent).getCenter();
            }
            
            // Get the current BorderPane and update its center
            BorderPane borderPane = getRootBorderPane((Node) event.getSource());
            if (borderPane != null) {
                borderPane.setCenter(notesContent);
            }
        } catch (IOException e) {
            showError("Failed to load notes: " + e.getMessage());
        }
    }
    
    @FXML
    private void openSettings(javafx.event.ActionEvent event) {
        try {
            // Load only the center content of Settings view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Settings.fxml"));
            Parent settingsContent = loader.load();
            
            // Extract only the center content if it's a BorderPane
            if (settingsContent instanceof BorderPane) {
                settingsContent = (Parent) ((BorderPane) settingsContent).getCenter();
            }
            
            // Get the current BorderPane and update its center
            BorderPane borderPane = getRootBorderPane((Node) event.getSource());
            if (borderPane != null) {
                borderPane.setCenter(settingsContent);
            }
        } catch (IOException e) {
            showError("Failed to load settings: " + e.getMessage());
        }
    }
} 
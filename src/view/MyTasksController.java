package view;

import javafx.scene.control.Label;
import Controller.TaskController;
import Model.DatabaseManager;
import Model.Task;
import Model.User;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import java.io.IOException;
import java.lang.classfile.Label;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import Model.Task;
import view.DashboardController;
import javafx.geometry.Pos;

public class MyTasksController extends BaseController implements Initializable {
    @FXML
    private VBox tasksContainer;
    @FXML
    private ComboBox<String> priorityFilter;
    @FXML
    private ComboBox<String> statusFilter;
    @FXML
    private DatePicker dateFilter;
    
    private List<Task> allTasks;
    private TaskController taskController = new TaskController();
    private User loggedInUser;
    public void setLoggedInUser(User user) {
        this.loggedInUser = user;
    }

    public List<Task> getTasksByUserId(int userId) {
        return DatabaseManager.getTasksByUserId(userId); // Assurez-vous que cette méthode fonctionne correctement
    }

    public void loadUserTasks(int userId) {
        tasksContainer.getChildren().clear();

        List<Task> tasks = taskController.getTasksByUserId(userId);

        if (tasks == null || tasks.isEmpty()) {
            Label noTasksLabel = new Label("No tasks available.");
            noTasksLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #888;");
            tasksContainer.getChildren().add(noTasksLabel);
            return;
        }

        for (Task task : tasks) {
            VBox taskCard = new VBox();
            taskCard.setSpacing(10);
            taskCard.setStyle("-fx-background-color: #f4f4f4; -fx-padding: 10; -fx-border-color: #ddd; -fx-border-radius: 5; -fx-background-radius: 5;");

            Label titleLabel = new Label(task.getTitre());
            titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

            Label descriptionLabel = new Label(task.getDescription());
            descriptionLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #555;");

            Label dateLabel = new Label("Due: " + task.getDateLimite());
            dateLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #888;");

            Label statusLabel = new Label("Status: " + task.getStatut());
            statusLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #888;");

            taskCard.getChildren().addAll(titleLabel, descriptionLabel, dateLabel, statusLabel);
            tasksContainer.getChildren().add(taskCard);
        }
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        updateUserInfo();
        setupFilters();
        loadTasks();
    }

    private void setupFilters() {
        // Setup priority filter
        priorityFilter.getItems().addAll("All Priorities", "Haute", "Moyenne", "Basse");
        priorityFilter.setValue("All Priorities");
        priorityFilter.setOnAction(e -> applyFilters());

        // Setup status filter
        statusFilter.getItems().addAll("All Statuses", "À faire", "En cours", "Terminé");
        statusFilter.setValue("All Statuses");
        statusFilter.setOnAction(e -> applyFilters());

        // Setup date filter
        dateFilter.setOnAction(e -> applyFilters());
    }

    @FXML
    private void clearFilters() {
        priorityFilter.setValue("All Priorities");
        statusFilter.setValue("All Statuses");
        dateFilter.setValue(null);
        loadTasks();
    }

    private void loadTasks() {
        tasksContainer.getChildren().clear();
        allTasks = DashboardController.getAllTasks();
        displayFilteredTasks(allTasks);
    }

    private void applyFilters() {
        List<Task> filteredTasks = allTasks.stream()
            .filter(task -> filterByPriority(task))
            .filter(task -> filterByStatus(task))
            .filter(task -> filterByDate(task))
            .collect(Collectors.toList());
        
        displayFilteredTasks(filteredTasks);
    }

    private boolean filterByPriority(Task task) {
        String priority = priorityFilter.getValue();
        return priority.equals("All Priorities") || task.getPriorite().equals(priority);
    }

    private boolean filterByStatus(Task task) {
        String status = statusFilter.getValue();
        return status.equals("All Statuses") || task.getStatut().equals(status);
    }

    private boolean filterByDate(Task task) {
        LocalDate selectedDate = dateFilter.getValue();
        return selectedDate == null || task.getDateLimite().equals(selectedDate);
    }

    private void displayFilteredTasks(List<Task> tasks) {
        tasksContainer.getChildren().clear();
        tasks.forEach(this::createTaskCard);
    }

    private void createTaskCard(Task task) {
        // Create card container
        VBox card = new VBox();
        card.getStyleClass().add("task-card");
        card.setSpacing(10);
        
        // Date header
        String dateStr = task.getDateLimite().format(DateTimeFormatter.ofPattern("dd MMMM"));
        Label dateLabel = new Label(dateStr + (task.getDateLimite().equals(java.time.LocalDate.now()) ? " • Today" : ""));
        dateLabel.getStyleClass().add("task-date");

        // Title with options
        HBox titleBox = new HBox();
        titleBox.setSpacing(10);
        titleBox.setAlignment(Pos.CENTER_LEFT);
        titleBox.getStyleClass().add("task-title-box");

        Label titleLabel = new Label(task.getTitre());
        titleLabel.getStyleClass().add("task-title");
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        // Create status options
        ComboBox<String> statusComboBox = new ComboBox<>();
        statusComboBox.getItems().addAll("À faire", "En cours", "Terminé");
        statusComboBox.setValue(task.getStatut() != null ? task.getStatut() : "À faire");
        statusComboBox.getStyleClass().add("status-combo-box");
        
        // Handle status change
        statusComboBox.setOnAction(event -> {
            String newStatus = statusComboBox.getValue();
            task.setStatut(newStatus);
            updateTaskStatus(statusComboBox, newStatus);
        });
        
        // Set initial status style
        updateTaskStatus(statusComboBox, statusComboBox.getValue());

        // Create delete button
        Button deleteButton = new Button("🗑");
        deleteButton.getStyleClass().add("delete-task-button");
        deleteButton.setOnAction(e -> confirmAndDeleteTask(task, card));
        
        titleBox.getChildren().addAll(titleLabel, spacer, statusComboBox, deleteButton);

        // Description
        Label descLabel = new Label(task.getDescription());
        descLabel.getStyleClass().add("task-desc");
        descLabel.setWrapText(true);

        // Status and Priority row
        HBox detailsBox = new HBox();
        detailsBox.setSpacing(15);
        detailsBox.getStyleClass().add("task-details");

        Label priorityLabel = new Label("Priority: " + task.getPriorite());
        priorityLabel.getStyleClass().add("task-priority");

        // Due date
        Label dueDateLabel = new Label("Due on " + task.getDateLimite().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        dueDateLabel.getStyleClass().add("task-due-date");

        detailsBox.getChildren().addAll(priorityLabel, dueDateLabel);
        HBox.setHgrow(dueDateLabel, Priority.ALWAYS);

        // Add all elements to card
        card.getChildren().addAll(dateLabel, titleBox, descLabel, detailsBox);
        
        // Add card to container
        tasksContainer.getChildren().add(card);
    }

    private void confirmAndDeleteTask(Task task, VBox card) {
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Delete Task");
        confirmDialog.setHeaderText("Delete Task");
        confirmDialog.setContentText("Are you sure you want to delete this task?");
        
        Optional<ButtonType> result = confirmDialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            DashboardController.getAllTasks().remove(task);
            tasksContainer.getChildren().remove(card);
        }
    }

    private void updateTaskStatus(ComboBox<String> statusComboBox, String status) {
        // Remove all status-related style classes
        statusComboBox.getStyleClass().removeAll("status-not-started", "status-in-progress", "status-completed");
        
        // Add appropriate style class based on status
        switch (status) {
            case "À faire":
                statusComboBox.getStyleClass().add("status-not-started");
                break;
            case "En cours":
                statusComboBox.getStyleClass().add("status-in-progress");
                break;
            case "Terminé":
                statusComboBox.getStyleClass().add("status-completed");
                break;
        }
    }

    @FXML
    private void createNewTask(ActionEvent event) {
        try {
            BorderPane borderPane = (BorderPane) ((Node) event.getSource()).getScene().getRoot();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/NewTask.fxml"));
            Parent newTaskRoot = loader.load();
            borderPane.setCenter(newTaskRoot);
        } catch (IOException e) {
            e.printStackTrace();
            showError("Error loading new task view");
        }
    }
}
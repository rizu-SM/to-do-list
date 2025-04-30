package view;

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
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import Model.Task;
import view.DashboardController;
import javafx.geometry.Pos;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import Model.User;
import Controller.TaskController;

public class MyTasksController extends BaseController implements Initializable {
    @FXML
    private VBox tasksContainer;
    @FXML
    private ComboBox<String> priorityFilter;
    @FXML
    private ComboBox<String> statusFilter;
    @FXML
    private DatePicker dateFilter;
    @FXML
    private TableView<Task> taskTableView;
    @FXML
    private TableColumn<Task, String> titleColumn;
    @FXML
    private TableColumn<Task, String> descriptionColumn;
    @FXML
    private TableColumn<Task, String> dueDateColumn;
    @FXML
    private TableColumn<Task, String> priorityColumn;
    @FXML
    private TableColumn<Task, String> statusColumn;
    @FXML
    private TextField titleField;
    @FXML
    private TextArea descriptionField;
    @FXML
    private DatePicker dueDatePicker;
    @FXML
    private ComboBox<String> categoryFilter;
    @FXML
    private Button addButton;
    @FXML
    private Button updateButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button logoutButton;
    @FXML
    private Label welcomeLabel;
    
    private List<Task> allTasks;
    private ObservableList<Task> taskList;
    private TaskController taskController;
    private User currentUser;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupFilters();
        // Don't load tasks here, they will be loaded when setUser is called
    }

    private void setupFilters() {
        // Setup priority filter
        priorityFilter.getItems().addAll("All Priorities", "Faible", "Moyenne", "Haute");
        priorityFilter.setValue("All Priorities");
        priorityFilter.setOnAction(e -> applyFilters());

        // Setup status filter
        statusFilter.getItems().addAll("All Statuses", "À faire", "En cours", "Terminé");
        statusFilter.setValue("All Statuses");
        statusFilter.setOnAction(e -> applyFilters());

        // Setup date filter
        dateFilter.setOnAction(e -> applyFilters());

        // Setup priority combo box
        priorityFilter.getItems().addAll("Faible", "Moyenne", "Haute");
        priorityFilter.setValue("Moyenne");

        // Setup category filter
        categoryFilter.getItems().addAll("Default");
        categoryFilter.setValue("Default");

        // Setup status combo box
        statusFilter.getItems().addAll("À faire", "En cours", "Terminé");
        statusFilter.setValue("À faire");
    }

    @FXML
    private void clearFilters() {
        priorityFilter.setValue("All Priorities");
        statusFilter.setValue("All Statuses");
        dateFilter.setValue(null);
        loadTasks();
    }

    private void loadTasks() {
        if (currentUser == null) {
            System.out.println("No current user, cannot load tasks");
            return;
        }
        System.out.println("Loading tasks for user ID: " + currentUser.getId());
        tasksContainer.getChildren().clear();
        taskController = new TaskController();
        allTasks = taskController.getTasksByUserId(currentUser.getId());
        System.out.println("Retrieved " + allTasks.size() + " tasks from database");
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
        System.out.println("Displaying " + tasks.size() + " tasks");
        tasksContainer.getChildren().clear();
        tasks.forEach(task -> {
            System.out.println("Creating card for task: " + task.getTitre());
            createTaskCard(task);
        });
    }

    private void createTaskCard(Task task) {
        System.out.println("Creating card for task: " + task.getTitre());
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
            
            // Save the status change to the backend
            if (taskController == null) {
                taskController = new TaskController();
            }
            if (taskController.editTask(task)) {
                System.out.println("Task status updated successfully");
                // Refresh the task list to ensure consistency
                allTasks = taskController.getTasksByUserId(currentUser.getId());
                displayFilteredTasks(allTasks);
            } else {
                System.out.println("Failed to update task status");
                showError("Failed to update task status");
                // Revert the status change if it failed
                statusComboBox.setValue(task.getStatut());
            }
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
        System.out.println("Card added to container");
    }

    private void confirmAndDeleteTask(Task task, VBox card) {
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Delete Task");
        confirmDialog.setHeaderText("Delete Task");
        confirmDialog.setContentText("Are you sure you want to delete this task?");
        
        Optional<ButtonType> result = confirmDialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            taskController.deleteTask(task.getId());
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

    public void setUser(User user) {
        this.currentUser = user;
        if (user != null) {
            if (welcomeLabel != null) {
                welcomeLabel.setText("Welcome, " + user.getPrenom() + " " + user.getNom());
            }
            loadTasks();
        }
    }

    @FXML
    private void handleAddTask() {
        if (!validateInput()) {
            System.out.println("Input validation failed");
            return;
        }

        String title = titleField.getText();
        String description = descriptionField.getText();
        LocalDate dueDate = dueDatePicker.getValue();
        String priority = priorityFilter.getValue();
        String status = statusFilter.getValue();
        String category = "Default"; // You can add category selection later

        System.out.println("Creating new task with details:");
        System.out.println("Title: " + title);
        System.out.println("Description: " + description);
        System.out.println("Due Date: " + dueDate);
        System.out.println("Priority: " + priority);
        System.out.println("Status: " + status);
        System.out.println("User ID: " + currentUser.getId());

        Task task = new Task(
            currentUser.getId(),
            title,
            description,
            dueDate,
            status,
            priority,
            category
        );

        // Initialize taskController if not already done
        if (taskController == null) {
            taskController = new TaskController();
        }

        if (taskController.addTask(task)) {
            System.out.println("Task added successfully through TaskController");
            clearForm();
            // Reload all tasks from the database
            allTasks = taskController.getTasksByUserId(currentUser.getId());
            // Display all tasks
            displayFilteredTasks(allTasks);
            showSuccess("Task added successfully!");
        } else {
            System.out.println("Failed to add task through TaskController");
            showError("Failed to add task");
        }
    }

    @FXML
    private void handleUpdateTask() {
        Task selectedTask = taskTableView.getSelectionModel().getSelectedItem();
        if (selectedTask == null) {
            showError("Please select a task to update");
            return;
        }

        if (!validateInput()) {
            return;
        }

        selectedTask.setTitre(titleField.getText());
        selectedTask.setDescription(descriptionField.getText());
        selectedTask.setDateLimite(dueDatePicker.getValue());
        selectedTask.setPriorite(priorityFilter.getValue());
        selectedTask.setStatut(statusFilter.getValue());

        if (taskController.editTask(selectedTask)) {
            loadTasks();
            clearForm();
            showSuccess("Task updated successfully!");
        } else {
            showError("Failed to update task. The task might be completed.");
        }
    }

    @FXML
    private void handleDeleteTask() {
        Task selectedTask = taskTableView.getSelectionModel().getSelectedItem();
        if (selectedTask != null) {
            if (taskController.deleteTask(selectedTask.getId())) {
                taskList.remove(selectedTask);
                clearForm();
                showSuccess("Task deleted successfully!");
            } else {
                showError("Failed to delete task");
            }
        }
    }

    private boolean validateInput() {
        if (titleField.getText().isEmpty()) {
            showError("Title is required");
            return false;
        }
        if (dueDatePicker.getValue() == null) {
            showError("Due date is required");
            return false;
        }
        return true;
    }

    private void clearForm() {
        titleField.clear();
        descriptionField.clear();
        dueDatePicker.setValue(null);
        priorityFilter.setValue("Moyenne");
        statusFilter.setValue("À faire");
    }

    @Override
    protected void showError(String message) {
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
    private void handleLogout(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/FirstPage.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            
            // Store current window dimensions
            double currentWidth = stage.getWidth();
            double currentHeight = stage.getHeight();
            
            Scene scene = new Scene(root);
            stage.setScene(scene);
            
            // Restore window dimensions
            stage.setWidth(currentWidth);
            stage.setHeight(currentHeight);
            
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showError("Error during logout");
        }
    }

    @Override
    protected void updateUserInfo() {
        if (currentUser != null && welcomeLabel != null) {
            welcomeLabel.setText("Welcome, " + currentUser.getPrenom() + " " + currentUser.getNom());
        }
    }
} 
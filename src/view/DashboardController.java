package view;

import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DashboardController {

    @FXML private Label dayLabel;
    @FXML private Label dateLabel;
    @FXML private VBox toDoListContainer;
    @FXML private VBox taskStatusContainer;
    @FXML private PieChart taskStatusChart;
    @FXML private VBox completedTaskContainer;
    @FXML private VBox taskContainer;
    @FXML private Button addTaskButton;

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
            PieChart.Data completed = new PieChart.Data("Completed", 4);
            PieChart.Data inProgress = new PieChart.Data("In Progress", 3);
            PieChart.Data pending = new PieChart.Data("Pending", 2);

            taskStatusChart.getData().addAll(completed, inProgress, pending);

            // Appliquer les couleurs une fois que les nodes sont créés
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

        // Ajouter des tâches avec priorité et statut
        addTask("📝 Finish JavaFX Project", "Deadline: 12th March", "High", "Not Started");
        addTask("📞 Call Client", "Discuss project specifications", "Moderate", "In Progress");
        addTask("📧 Respond to Emails", "Reply to pending messages", "Low", "Completed");

        // Ajouter des tâches complétées
        addCompletedTask("🎯 Submit Project", "Final submission of JavaFX To-Do List App.");
        addCompletedTask("📢 Team Meeting", "Discussion on project roadmap.");

        // Action du bouton "+ Add Task"
        if (addTaskButton != null) {
            addTaskButton.setOnAction(e -> addTask("📌 New Task", "Description here...", "Moderate", "Not Started"));
        }
    }

    private String formatDay(String day) {
        return day.substring(0, 1).toUpperCase() + day.substring(1).toLowerCase();
    }

    // Ajouter une tâche avec priorité et statut
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
 // Mise à jour des couleurs du texte du statut
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

}


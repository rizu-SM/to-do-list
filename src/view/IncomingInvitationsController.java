package view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;
import javafx.scene.layout.Priority;
import java.util.List;
import util.UserSession;
import Model.User;
import Controller.TaskController;
import javafx.scene.control.Alert;
import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ResourceBundle;
import java.io.IOException;
import javafx.scene.layout.BorderPane;
import view.SharedTasksController;

public class IncomingInvitationsController extends BaseController implements Initializable {
    @FXML private VBox invitationsContainer;
    private TaskController taskController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        taskController = new TaskController();
        loadIncomingInvitations();
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

    @FXML
    private void handleLogout(javafx.event.ActionEvent event) {
        try {
            UserSession.getInstance().clearSession();
            // For logout, we want to completely change the scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/SignIn.fxml"));
            Parent root = loader.load();
            Scene scene = ((Node) event.getSource()).getScene();
            if (scene != null) {
                scene.setRoot(root);
            }
        } catch (IOException e) {
            showError("Failed to logout: " + e.getMessage());
        }
    }

    private void loadIncomingInvitations() {
        invitationsContainer.getChildren().clear();
        
        // Get users who have shared tasks with the current user
        List<User> owners = taskController.getOwnersWhoSharedWith(UserSession.getInstance().getCurrentUser().getId());
        
        if (owners.isEmpty()) {
            Label noInvitationsLabel = new Label("No incoming invitations");
            noInvitationsLabel.getStyleClass().add("no-invitations-label");
            invitationsContainer.getChildren().add(noInvitationsLabel);
            return;
        }

        for (User owner : owners) {
            createInvitationCard(owner);
        }
    }

    private void createInvitationCard(User owner) {
        HBox card = new HBox(10);
        card.setStyle("-fx-background-color: white; -fx-padding: 15; -fx-background-radius: 5;");
        card.setAlignment(Pos.CENTER_LEFT);
        card.setMaxWidth(Double.MAX_VALUE);
        
        VBox infoBox = new VBox(5);
        Label nameLabel = new Label(owner.getPrenom() + " " + owner.getNom());
        nameLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        Label emailLabel = new Label(owner.getEmail());
        emailLabel.setStyle("-fx-text-fill: #666666;");
        infoBox.getChildren().addAll(nameLabel, emailLabel);
        
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        
        Button viewTasksButton = new Button("View Tasks");
        viewTasksButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-padding: 8 15;");
        viewTasksButton.setOnAction(e -> handleViewTasks(owner));
        
        buttonBox.getChildren().add(viewTasksButton);
        HBox.setHgrow(infoBox, Priority.ALWAYS);
        card.getChildren().addAll(infoBox, buttonBox);
        invitationsContainer.getChildren().add(card);
    }

    private void handleViewTasks(User owner) {
        try {
            System.out.println("Loading SharedTasks.fxml for user: " + owner.getEmail());
            
            // Load the SharedTasks view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/SharedTasks.fxml"));
            if (loader.getLocation() == null) {
                throw new IOException("Could not find SharedTasks.fxml");
            }
            
            Parent sharedTasksRoot = loader.load();
            
            // Get the controller and set the task owner
            SharedTasksController controller = loader.getController();
            controller.setTaskOwner(owner);
            
            // Get the current stage
            Stage stage = (Stage) invitationsContainer.getScene().getWindow();
            
            // Create a new scene with the shared tasks view
            Scene scene = new Scene(sharedTasksRoot);
            stage.setScene(scene);
            stage.show();
            
        } catch (IOException ex) {
            System.err.println("Error loading SharedTasks.fxml: " + ex.getMessage());
            ex.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not load shared tasks");
            alert.setContentText("Details: " + ex.getMessage());
            alert.showAndWait();
        }
    }

    @Override
    protected void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
} 
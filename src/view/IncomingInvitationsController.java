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
import javafx.scene.text.Text;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class IncomingInvitationsController extends BaseController implements Initializable {
    @FXML private VBox invitationsContainer;
    @FXML private Text userNameText;
    @FXML private Label dayLabel;
    @FXML private Label dateLabel;
    @FXML private Label coinsAmount;
    private TaskController taskController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        taskController = new TaskController();
        loadIncomingInvitations();
        updateUserInfo();
        updateDateTime();
        updateCoins();
    }

    @Override
    public void updateUserInfo() {
        super.updateUserInfo();
        User currentUser = UserSession.getInstance().getCurrentUser();
        if (currentUser != null && userNameText != null) {
            userNameText.setText(currentUser.getPrenom() + " " + currentUser.getNom());
        }
    }

    private void updateDateTime() {
        LocalDateTime now = LocalDateTime.now();
        dayLabel.setText(now.format(DateTimeFormatter.ofPattern("EEEE")));
        dateLabel.setText(now.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")));
    }

    private void updateCoins() {
        User currentUser = UserSession.getInstance().getCurrentUser();
        if (currentUser != null) {
            coinsAmount.setText(String.valueOf(currentUser.getCoin()));
        }
    }

    private BorderPane getRootBorderPane(Node source) {
        return (BorderPane) source.getScene().getRoot();
    }

    @FXML
    public void showNotifications(javafx.event.ActionEvent event) {
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
    public void showDashboard(javafx.event.ActionEvent event) {
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
    public void showMyTasks(javafx.event.ActionEvent event) {
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
    public void showNotes(javafx.event.ActionEvent event) {
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
    public void openSettings(javafx.event.ActionEvent event) {
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
    public void handleLogout(javafx.event.ActionEvent event) {
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
        
        Button acceptButton = new Button("Accept");
        acceptButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-padding: 8 15;");
        
        Button deleteButton = new Button("Delete");
        deleteButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-padding: 8 15;");
        
        Button viewTasksButton = new Button("View Tasks");
        viewTasksButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-padding: 8 15;");
        viewTasksButton.setVisible(false);
        
        buttonBox.getChildren().addAll(acceptButton, deleteButton, viewTasksButton);
        
        acceptButton.setOnAction(e -> {
            // Hide accept and delete buttons
            acceptButton.setVisible(false);
            deleteButton.setVisible(false);
            // Show view tasks button
            viewTasksButton.setVisible(true);
        });
        
        deleteButton.setOnAction(e -> {
            // Remove the invitation from database
            if (taskController.removeInvitationByEmail(UserSession.getInstance().getCurrentUser().getId(), owner.getEmail())) {
                // Remove the card from the UI
                invitationsContainer.getChildren().remove(card);
                
                // If no more invitations, show the "no invitations" message
                if (invitationsContainer.getChildren().isEmpty()) {
                    Label noInvitationsLabel = new Label("No incoming invitations");
                    noInvitationsLabel.getStyleClass().add("no-invitations-label");
                    invitationsContainer.getChildren().add(noInvitationsLabel);
                }
            } else {
                showError("Failed to delete invitation");
            }
        });
        
        viewTasksButton.setOnAction(e -> {
            try {
                // Load the SharedTasks view
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/SharedTasks.fxml"));
                Parent root = loader.load();
                
                // Get the controller and set the owner info
                SharedTasksController controller = loader.getController();
                controller.setTaskOwner(owner);
                
                // Get the current scene and set the new root
                Scene scene = invitationsContainer.getScene();
                scene.setRoot(root);
            } catch (IOException ex) {
                ex.printStackTrace();
                showError("Failed to load shared tasks: " + ex.getMessage());
            }
        });
        
        card.getChildren().addAll(infoBox, buttonBox);
        HBox.setHgrow(infoBox, Priority.ALWAYS);
        
        invitationsContainer.getChildren().add(card);
    }

    @Override
    public void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
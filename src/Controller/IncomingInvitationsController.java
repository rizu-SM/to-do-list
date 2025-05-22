package Controller;

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
import javafx.event.ActionEvent;

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
    
    private void showMyTasks(javafx.event.ActionEvent  event) {
        try {
            // Get the current BorderPane
            BorderPane borderPane = (BorderPane) ((Node) event.getSource()).getScene().getRoot();
            
            // Load the MyTasks.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MyTasks.fxml"));
            Parent myTasksRoot = loader.load();
            
            // Get the controller and set the current user
            MyTasksController controller = loader.getController();
            UserSession session = UserSession.getInstance();
            if (session.getCurrentUser() != null) {
                controller.setUser(session.getCurrentUser());
            } else {
                showError("No user logged in");
                return;
            }
            
            // Replace only the center content
            borderPane.setCenter(myTasksRoot);
            
        } catch (IOException e) {
            e.printStackTrace();
            showError("Error loading my tasks view");
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
    private void showSettings(ActionEvent event) {
        try {
            System.out.println("=== Loading Settings Page (showSettings) ===");
            System.out.println("Getting BorderPane...");
            BorderPane borderPane = (BorderPane) ((Node) event.getSource()).getScene().getRoot();
            if (borderPane == null) {
                System.out.println("ERROR: BorderPane is null!");
                return;
            }
            
            System.out.println("Creating FXMLLoader for Settings.fxml");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Settings.fxml"));
            
            System.out.println("Loading FXML...");
            Parent settingsRoot = loader.load();
            
            System.out.println("Getting SettingsController...");
            SettingsController settingsController = loader.getController();
            if (settingsController == null) {
                System.out.println("ERROR: SettingsController is null!");
                return;
            }
            System.out.println("SettingsController obtained successfully");
            
            System.out.println("Setting dashboard controller...");
            
            System.out.println("Setting center content...");
            borderPane.setCenter(settingsRoot);
            System.out.println("=== Settings Page Loaded Successfully ===");
            
        } catch (IOException e) {
            System.out.println("ERROR loading settings: " + e.getMessage());
            e.printStackTrace();
            showError("Error loading settings");
        } catch (Exception e) {
            System.out.println("UNEXPECTED ERROR loading settings: " + e.getMessage());
            e.printStackTrace();
            showError("Unexpected error loading settings");
        }
    }

    @FXML
    protected void handleLogout(javafx.event.ActionEvent event) {
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
            e.printStackTrace();
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
        viewTasksButton.setVisible(true);

        buttonBox.getChildren().add(viewTasksButton);

        viewTasksButton.setOnAction(e -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/SharedTasks.fxml"));
                Parent sharedTasksContent = loader.load();

                SharedTasksController controller = loader.getController();
                controller.setTaskOwner(owner);

                BorderPane borderPane = getRootBorderPane((Node) e.getSource());
                if (borderPane != null) {
                    Node centerContent = sharedTasksContent;
                    if (sharedTasksContent instanceof BorderPane) {
                        centerContent = ((BorderPane) sharedTasksContent).getCenter();
                    }
                    borderPane.setCenter(centerContent);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                showError("Failed to load shared tasks: " + ex.getMessage());
            }
        });

        HBox.setHgrow(infoBox, Priority.ALWAYS);
        card.getChildren().addAll(infoBox, buttonBox);
        invitationsContainer.getChildren().add(card);
    }

    // Correct showError method
    @Override
    public void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
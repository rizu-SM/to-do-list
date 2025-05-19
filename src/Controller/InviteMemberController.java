package Controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.List;
import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import java.io.IOException;
import javafx.scene.control.Alert;
import util.UserSession;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;
import Controller.TaskController;
import Model.User;
import javafx.scene.text.Text;

public class InviteMemberController extends BaseController implements Initializable {

    @FXML
    private TextField emailField;

    @FXML
    private Button sendInviteButton;

    @FXML
    private Label dayLabel;

    @FXML
    private Label dateLabel;

    @FXML
    private Label statusLabel;

    @FXML
    private VBox invitedMembersContainer;

    @FXML
    private Label invitedCountLabel;

    @FXML
    private Text userNameText;

    @FXML
    private Label coinsAmount;

    private TaskController taskController;
    private List<String> invitedEmails = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize TaskController
        taskController = new TaskController();
        
        // Set current date
        LocalDate today = LocalDate.now();
        dayLabel.setText(formatDay(today.getDayOfWeek().toString()));
        dateLabel.setText(today.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        // Update user info and coins
        updateUserInfo();
        updateCoins();

        statusLabel.setText("");
        loadInvitedMembers();
    }

    private void updateCoins() {
        User currentUser = UserSession.getInstance().getCurrentUser();
        if (currentUser != null) {
            coinsAmount.setText(String.valueOf(currentUser.getCoin()));
        }
    }

    private String formatDay(String day) {
        return day.substring(0, 1).toUpperCase() + day.substring(1).toLowerCase();
    }

    @Override
    public void updateUserInfo() {
        super.updateUserInfo();
        User currentUser = UserSession.getInstance().getCurrentUser();
        if (currentUser != null && userNameText != null) {
            userNameText.setText(currentUser.getPrenom() + " " + currentUser.getNom());
        }
    }

    private void updateInvitedCount() {
        invitedCountLabel.setText("(" + invitedEmails.size() + ")");
    }

    @FXML
    private void handleSendInvite() {
        String email = emailField.getText().trim();
        if (email.isEmpty()) {
            showStatusMessage("Please enter an email address", false);
            return;
        }
        
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            showStatusMessage("Please enter a valid email address", false);
            return;
        }

        if (invitedEmails.contains(email)) {
            showStatusMessage("This email has already been invited", false);
            return;
        }

        try {
            // Share tasks with the invited user
            if (taskController.shareTasksWithUserByEmail(UserSession.getInstance().getCurrentUser().getId(), email)) {
                // Add to list and update UI
                invitedEmails.add(email);
                addInvitedMemberToUI(email);
                updateInvitedCount();
                
                // Clear the input field and status
                emailField.clear();
                showStatusMessage("Invitation sent successfully!", true);
        } else {
                showStatusMessage("No user found with this email address", false);
            }
        } catch (Exception e) {
            showStatusMessage("Error sending invitation: " + e.getMessage(), false);
        }
    }

    private void loadInvitedMembers() {
        // Clear existing items
        invitedMembersContainer.getChildren().clear();
        
        // Get invited users from backend
        List<User> invitedUsers = taskController.getInvitedUsers(UserSession.getInstance().getCurrentUser().getId());
        invitedEmails.clear();
        
        // Convert User objects to email strings
        for (User user : invitedUsers) {
            invitedEmails.add(user.getEmail());
        }
        
        // Add each invited member to the UI
        for (String email : invitedEmails) {
            addInvitedMemberToUI(email);
        }
        updateInvitedCount();
    }

    private void addInvitedMemberToUI(String email) {
        HBox memberItem = new HBox();
        memberItem.getStyleClass().add("invited-member-item");
        memberItem.setSpacing(10);
        memberItem.setAlignment(Pos.CENTER_LEFT);

        Label emailLabel = new Label(email);
        emailLabel.getStyleClass().add("invited-member-email");
        HBox.setHgrow(emailLabel, javafx.scene.layout.Priority.ALWAYS);

        Button deleteButton = new Button("×");
        deleteButton.getStyleClass().add("delete-invite-button");
        deleteButton.setOnAction(e -> {
            if (taskController.removeInvitationByEmail(UserSession.getInstance().getCurrentUser().getId(), email)) {
                invitedEmails.remove(email);
                invitedMembersContainer.getChildren().remove(memberItem);
                updateInvitedCount();
                statusLabel.setText("Invitation removed successfully!");
            } else {
                statusLabel.setText("Failed to remove invitation. Please try again.");
            }
        });

        memberItem.getChildren().addAll(emailLabel, deleteButton);
        invitedMembersContainer.getChildren().add(memberItem);
    }

    @FXML
    private void showDashboard(ActionEvent event) {
        try {
            // Load only the center content of Dashboard view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Dashboard.fxml"));
            Parent dashboardContent = loader.load();
            
            // Extract only the center content if it's a BorderPane
            if (dashboardContent instanceof BorderPane) {
                dashboardContent = (Parent) ((BorderPane) dashboardContent).getCenter();
            }
            
            // Get the current BorderPane and update its center
            BorderPane borderPane = (BorderPane) ((Node) event.getSource()).getScene().getRoot();
            if (borderPane != null) {
                borderPane.setCenter(dashboardContent);
            }
        } catch (IOException e) {
            showError("Failed to load dashboard: " + e.getMessage());
        }
    }

    @FXML
    private void showMyTasks(ActionEvent event) {
        try {
            // Load only the center content of MyTasks view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MyTasks.fxml"));
            Parent myTasksContent = loader.load();
            
            // Extract only the center content if it's a BorderPane
            if (myTasksContent instanceof BorderPane) {
                myTasksContent = (Parent) ((BorderPane) myTasksContent).getCenter();
            }
            
            // Get the current BorderPane and update its center
            BorderPane borderPane = (BorderPane) ((Node) event.getSource()).getScene().getRoot();
            if (borderPane != null) {
                borderPane.setCenter(myTasksContent);
            }
        } catch (IOException e) {
            showError("Failed to load my tasks: " + e.getMessage());
        }
    }

    @FXML
    private void showNotes(ActionEvent event) {
        try {
            // Load only the center content of Notes view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Notes.fxml"));
            Parent notesContent = loader.load();
            
            // Extract only the center content if it's a BorderPane
            if (notesContent instanceof BorderPane) {
                notesContent = (Parent) ((BorderPane) notesContent).getCenter();
            }
            
            // Get the current BorderPane and update its center
            BorderPane borderPane = (BorderPane) ((Node) event.getSource()).getScene().getRoot();
            if (borderPane != null) {
                borderPane.setCenter(notesContent);
            }
        } catch (IOException e) {
            showError("Failed to load notes: " + e.getMessage());
        }
    }

    @FXML
    private void showNotifications(ActionEvent event) {
        try {
            // Load only the center content of Notifications view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Notifications.fxml"));
            Parent notificationsContent = loader.load();
            
            // Extract only the center content if it's a BorderPane
            if (notificationsContent instanceof BorderPane) {
                notificationsContent = (Parent) ((BorderPane) notificationsContent).getCenter();
            }
            
            // Get the current BorderPane and update its center
            BorderPane borderPane = (BorderPane) ((Node) event.getSource()).getScene().getRoot();
            if (borderPane != null) {
                borderPane.setCenter(notificationsContent);
            }
        } catch (IOException e) {
            showError("Failed to load notifications: " + e.getMessage());
        }
    }

    @FXML
    private void openSettings(ActionEvent event) {
        try {
            // Load only the center content of Settings view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Settings.fxml"));
            Parent settingsContent = loader.load();
            
            // Extract only the center content if it's a BorderPane
            if (settingsContent instanceof BorderPane) {
                settingsContent = (Parent) ((BorderPane) settingsContent).getCenter();
            }
            
            // Get the current BorderPane and update its center
            BorderPane borderPane = (BorderPane) ((Node) event.getSource()).getScene().getRoot();
            if (borderPane != null) {
                borderPane.setCenter(settingsContent);
            }
        } catch (IOException e) {
            showError("Failed to load settings: " + e.getMessage());
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

    private void showStatusMessage(String message, boolean isSuccess) {
        statusLabel.setText(message);
        statusLabel.setTextFill(isSuccess ? Color.GREEN : Color.RED);
    }

    @Override
    public void showError(String message) {
        super.showError(message);
    }
}
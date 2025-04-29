package view;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.net.URL;
import java.util.ResourceBundle;

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
import java.util.ArrayList;
import java.util.List;

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
    private Label userNameLabel;

    @FXML
    private Label userEmailLabel;

    @FXML
    private Label statusLabel;

    @FXML
    private VBox invitedMembersContainer;

    @FXML
    private Label invitedCountLabel;

    private List<String> invitedEmails = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Set current date
        LocalDate today = LocalDate.now();
        dayLabel.setText(formatDay(today.getDayOfWeek().toString()));
        dateLabel.setText(today.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        // Update user info from session
        updateUserInfo();

        statusLabel.setText("");

        loadInvitedMembers();
    }

    private String formatDay(String day) {
        return day.substring(0, 1).toUpperCase() + day.substring(1).toLowerCase();
    }

    @Override
    public void updateUserInfo() {
        UserSession session = UserSession.getInstance();
        userNameLabel.setText(session.getFirstName());
        userEmailLabel.setText(session.getEmail());
    }

    private void updateInvitedCount() {
        invitedCountLabel.setText("(" + invitedEmails.size() + ")");
    }

    @FXML
    private void handleSendInvite() {
        String email = emailField.getText().trim();
        if (email.isEmpty()) {
            statusLabel.setText("Please enter an email address");
            return;
        }
        
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            statusLabel.setText("Please enter a valid email address");
            return;
        }

        if (invitedEmails.contains(email)) {
            statusLabel.setText("This email has already been invited");
            return;
        }

        // Add to list and update UI
        invitedEmails.add(email);
        addInvitedMemberToUI(email);
        updateInvitedCount();
        
        // Clear the input field and status
            emailField.clear();
        statusLabel.setText("Invitation sent successfully!");
    }

    private void loadInvitedMembers() {
        // Clear existing items
        invitedMembersContainer.getChildren().clear();
        
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
            invitedEmails.remove(email);
            invitedMembersContainer.getChildren().remove(memberItem);
            updateInvitedCount();
        });

        memberItem.getChildren().addAll(emailLabel, deleteButton);
        invitedMembersContainer.getChildren().add(memberItem);
    }

    @FXML
    private void handleDashboardButton(ActionEvent event) {
        try {
            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Dashboard.fxml"));
            Parent dashboardRoot = loader.load();
            
            Scene scene = new Scene(dashboardRoot);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showError("Error loading dashboard");
        }
    }

    @FXML
    private void showNewTask(ActionEvent event) {
        try {
            // Load the main layout that contains the navigation
            FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("/view/Dashboard.fxml"));
            Parent mainRoot = mainLoader.load();
            BorderPane mainBorderPane = (BorderPane) mainRoot;

            // Load the NewTask content
            FXMLLoader contentLoader = new FXMLLoader(getClass().getResource("/view/NewTask.fxml"));
            Parent newTaskContent = contentLoader.load();

            // Set the NewTask content in the center of the BorderPane
            mainBorderPane.setCenter(newTaskContent);

            // Update the scene
            Scene scene = new Scene(mainBorderPane);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showError("Error loading new task view");
        }
    }

    @FXML
    private void showNotes(ActionEvent event) {
        try {
            // Load the main layout that contains the navigation
            FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("/view/Dashboard.fxml"));
            Parent mainRoot = mainLoader.load();
            BorderPane mainBorderPane = (BorderPane) mainRoot;

            // Load the Notes content
            FXMLLoader contentLoader = new FXMLLoader(getClass().getResource("/view/Notes.fxml"));
            Parent notesContent = contentLoader.load();

            // Set the Notes content in the center of the BorderPane
            mainBorderPane.setCenter(notesContent);

            // Update the scene
            Scene scene = new Scene(mainBorderPane);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showError("Error loading notes view");
        }
    }

    @FXML
    private void handleTaskCategoriesButton(ActionEvent event) {
        try {
            // Load the main layout that contains the navigation
            FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("/view/Dashboard.fxml"));
            Parent mainRoot = mainLoader.load();
            BorderPane mainBorderPane = (BorderPane) mainRoot;

            // Load the TaskCategories content
            FXMLLoader contentLoader = new FXMLLoader(getClass().getResource("/view/TaskCategories.fxml"));
            Parent categoriesContent = contentLoader.load();

            // Set the TaskCategories content in the center of the BorderPane
            mainBorderPane.setCenter(categoriesContent);

            // Update the scene
            Scene scene = new Scene(mainBorderPane);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showError("Error loading task categories view");
        }
    }

    @FXML
    private void handleSettingsButton(ActionEvent event) {
        try {
            // Load the main layout that contains the navigation
            FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("/view/Dashboard.fxml"));
            Parent mainRoot = mainLoader.load();
            BorderPane mainBorderPane = (BorderPane) mainRoot;
            
            // Load the Settings content
            FXMLLoader contentLoader = new FXMLLoader(getClass().getResource("/view/Settings.fxml"));
            Parent settingsContent = contentLoader.load();
            
            // Set the Settings content in the center of the BorderPane
            mainBorderPane.setCenter(settingsContent);

            // Update the scene
            Scene scene = new Scene(mainBorderPane);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showError("Error loading settings view");
        }
    }

    @FXML
    private void handleLogoutButton(ActionEvent event) {
        try {
            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/SignIn.fxml"));
            Parent signInRoot = loader.load();
            
            Scene scene = new Scene(signInRoot);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showError("Error during logout");
        }
    }

    private void showStatusMessage(String message, boolean isSuccess) {
        statusLabel.setText(message);
        if (isSuccess) {
            statusLabel.setTextFill(Color.GREEN);
        } else {
            statusLabel.setTextFill(Color.RED);
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

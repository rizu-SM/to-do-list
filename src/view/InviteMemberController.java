package view;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.net.URL;
import java.util.ResourceBundle;
import Controller.TaskController;
import Model.User;
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
    private TaskController taskController = new TaskController();

    @FXML
    public void initialize() {
        loadInvitedMembers();
    }

    private void loadInvitedMembers() {
    try {
        // Récupérer l'utilisateur connecté
        User currentUser = UserSession.getInstance().getCurrentUser();
        if (currentUser == null) {
            System.out.println("No user is currently logged in.");
            return;
        }

        // Appeler la méthode getInvitedUsers() avec l'ID de l'utilisateur connecté
        int ownerId = currentUser.getId();
        List<User> invitedUsers = taskController.getInvitedUsers(ownerId);

        // Mettre à jour le compteur des membres invités
        invitedCountLabel.setText("(" + invitedUsers.size() + ")");

        // Ajouter chaque utilisateur invité au conteneur
        invitedMembersContainer.getChildren().clear(); // Nettoyer les anciens éléments
        for (User user : invitedUsers) {
            Label userLabel = new Label(user.getFullName() + " (" + user.getEmail() + ")");
            userLabel.getStyleClass().add("invited-member-label"); // Ajouter une classe CSS si nécessaire
            invitedMembersContainer.getChildren().add(userLabel);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}

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
    private void handleBackToDashboard(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/dashboard.fxml"));
            Parent root = loader.load();

            DashboardController dashboardController = loader.getController();
            User currentUser = UserSession.getInstance().getCurrentUser();
            if (currentUser != null) {
                dashboardController.setLoggedInUser(currentUser);
            } else {
                System.out.println("No user is currently logged in when returning to dashboard.");
            }

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
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

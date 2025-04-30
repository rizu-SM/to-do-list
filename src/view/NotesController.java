package view;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import Model.Note;
import java.io.IOException;
import java.util.function.Consumer;
import util.UserSession;
import javafx.geometry.Pos;

public class NotesController extends BaseController implements Initializable {
    @FXML
    private Button backButton;
    
    @FXML
    private Button saveButton;
    
    @FXML
    private TextField titleField;
    
    @FXML
    private TextArea descriptionArea;
    
    @FXML
    private Text dateText;

    @FXML
    private VBox notesContainer;

    // Static list to store all notes
    private static List<Note> allNotes = new ArrayList<>();

    // Public static method to access notes
    public static List<Note> getAllNotes() {
        return allNotes;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        updateUserInfo();
        System.out.println("NotesController initialized");
        if (notesContainer == null) {
            System.out.println("Error: notesContainer is null");
        } else {
            System.out.println("notesContainer is properly initialized");
            loadNotes();
        }
    }

    @FXML
    private void handleBackButton() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("Dashboard.fxml"));
            Stage stage = (Stage) backButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showError("Error loading dashboard");
        }
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("FirstPgae.fxml"));
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

    @FXML
    private void handleSaveButton() {
        String title = titleField.getText();
        String description = descriptionArea.getText();
        String date = dateText.getText();
        
        // TODO: Save note to database or file system
        
        // Clear fields after saving
        titleField.clear();
        descriptionArea.clear();
        updateDateText();
    }

    private void updateDateText() {
        // Method kept for compatibility but no longer needed
    }

    private void loadNotes() {
        notesContainer.getChildren().clear();
        int currentUserId = UserSession.getInstance().getCurrentUser().getId();
        
        // Add debug label if no notes exist
        if (allNotes.isEmpty()) {
            Label emptyLabel = new Label("No notes found. Create a new note!");
            emptyLabel.getStyleClass().add("empty-notes-label");
            notesContainer.getChildren().add(emptyLabel);
            return;
        }

        System.out.println("Loading notes for user: " + currentUserId);
        System.out.println("Total notes in system: " + allNotes.size());
        
        // Filter notes for current user
        for (Note note : allNotes) {
            if (note.getUserId() == currentUserId) {
                System.out.println("Creating card for note: " + note.getTitre());
                createNoteCard(note);
            }
        }
    }

    private void createNoteCard(Note note) {
        if (note == null) {
            System.out.println("Error: Attempting to create card for null note");
            return;
        }

        VBox noteCard = new VBox();
        noteCard.getStyleClass().add("note-card");
        noteCard.setSpacing(10);
        noteCard.setPadding(new javafx.geometry.Insets(10));
        noteCard.setStyle("-fx-background-color: white; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 0);");

        // Title
        Label titleLabel = new Label(note.getTitre());
        titleLabel.getStyleClass().add("note-title");
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Description
        Label descLabel = new Label(note.getDescription());
        descLabel.getStyleClass().add("note-description");
        descLabel.setWrapText(true);
        descLabel.setStyle("-fx-font-size: 14px;");

        // Actions container
        HBox actionsBox = new HBox();
        actionsBox.getStyleClass().add("note-actions");
        actionsBox.setSpacing(10);
        actionsBox.setAlignment(Pos.CENTER_RIGHT);

        Button deleteButton = new Button("Delete");
        deleteButton.getStyleClass().add("delete-button");
        deleteButton.setStyle("-fx-background-color: #ff4444; -fx-text-fill: white;");
        deleteButton.setOnAction(e -> deleteNote(note));

        actionsBox.getChildren().add(deleteButton);

        noteCard.getChildren().addAll(titleLabel, descLabel, actionsBox);
        notesContainer.getChildren().add(noteCard);
        
        System.out.println("Note card created - Title: " + note.getTitre() + ", Description: " + note.getDescription());
    }

    @FXML
    private void createNewNote(ActionEvent event) {
        try {
            // Get the current BorderPane
            BorderPane borderPane = (BorderPane) ((Node) event.getSource()).getScene().getRoot();
            
            // Load the NewNote content
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/NewNote.fxml"));
            Parent newNoteContent = loader.load();

            // Get controller and set callback
            NewNoteController controller = loader.getController();
            controller.setOnSaveCallback(note -> {
                allNotes.add(note);
                loadNotes();
            });

            // Replace the center content
            borderPane.setCenter(newNoteContent);
            
        } catch (IOException e) {
            e.printStackTrace();
            showError("Error loading new note view");
        }
    }

    private void editNote(Note note) {
        // TODO: Implement edit functionality
    }

    private void deleteNote(Note note) {
        allNotes.remove(note);
        loadNotes();
    }

    @FXML
    private void showDashboard(ActionEvent event) {
        try {
            // Get the current BorderPane
            BorderPane borderPane = (BorderPane) ((Node) event.getSource()).getScene().getRoot();
            
            // Load the Dashboard.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Dashboard.fxml"));
            Parent dashboardRoot = loader.load();
            
            // Replace only the center content
            borderPane.setCenter(dashboardRoot);
            
        } catch (IOException e) {
            e.printStackTrace();
            showError("Error loading dashboard");
        }
    }

    @FXML
    private void showNewTask(ActionEvent event) {
        try {
            // Get the current BorderPane
            BorderPane borderPane = (BorderPane) ((Node) event.getSource()).getScene().getRoot();
            
            // Load the NewTask.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("NewTask.fxml"));
            Parent newTaskRoot = loader.load();
            
            // Replace only the center content
            borderPane.setCenter(newTaskRoot);
            
        } catch (IOException e) {
            e.printStackTrace();
            showError("Error loading new task view");
        }
    }

    @FXML
    private void showSettings(ActionEvent event) {
        try {
            // Get the current BorderPane
            BorderPane borderPane = (BorderPane) ((Node) event.getSource()).getScene().getRoot();
            
            // Load the Settings.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Settings.fxml"));
            Parent settingsRoot = loader.load();
            
            // Replace only the center content
            borderPane.setCenter(settingsRoot);
            
        } catch (IOException e) {
            e.printStackTrace();
            showError("Error loading settings");
        }
    }

    @FXML
    private void showTaskCategories(ActionEvent event) {
        try {
            // Get the current BorderPane
            BorderPane borderPane = (BorderPane) ((Node) event.getSource()).getScene().getRoot();
            
            // Load the TaskCategories.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("TaskCategories.fxml"));
            Parent taskCategoriesRoot = loader.load();
            
            // Replace only the center content
            borderPane.setCenter(taskCategoriesRoot);
            
        } catch (IOException e) {
            e.printStackTrace();
            showError("Error loading task categories");
        }
    }

    @FXML
    private void showNotes(ActionEvent event) {
        try {
            // Get the current BorderPane
            BorderPane borderPane = (BorderPane) ((Node) event.getSource()).getScene().getRoot();
            
            // Load the Notes.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Notes.fxml"));
            Parent notesRoot = loader.load();
            
            // Replace only the center content
            borderPane.setCenter(notesRoot);
            
        } catch (IOException e) {
            e.printStackTrace();
            showError("Error loading notes view");
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
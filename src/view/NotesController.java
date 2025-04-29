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

    // Temporary storage for notes (replace with database later)
    private static List<Model.Note> notes = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        updateUserInfo();
        loadNotes();
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
        
        for (Model.Note note : notes) {
            createNoteCard(note);
        }
    }

    private void createNoteCard(Model.Note note) {
        // Create card container
        VBox card = new VBox();
        card.getStyleClass().add("note-card");
        card.setSpacing(10);
        
        // Title
        Label titleLabel = new Label(note.getTitle());
        titleLabel.getStyleClass().add("note-title");
        
        // Description
        Label descLabel = new Label(note.getDescription());
        descLabel.getStyleClass().add("note-description");
        descLabel.setWrapText(true);
        
        // Date
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm");
        Label dateLabel = new Label(note.getCreationTime().format(formatter));
        dateLabel.getStyleClass().add("note-date");
        
        // Actions container
        HBox actions = new HBox();
        actions.setSpacing(10);
        actions.getStyleClass().add("note-actions");
        
        Button deleteButton = new Button("Delete");
        deleteButton.getStyleClass().add("delete-button");
        
        // Add delete functionality
        deleteButton.setOnAction(e -> {
            notes.remove(note);
            loadNotes();
        });
        
        actions.getChildren().add(deleteButton);
        
        // Add all elements to card
        card.getChildren().addAll(titleLabel, descLabel, dateLabel, actions);
        
        // Add card to container
        notesContainer.getChildren().add(card);
    }

    @FXML
    private void createNewNote(ActionEvent event) {
        try {
            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            
            // Store current window dimensions
            double currentWidth = stage.getWidth();
            double currentHeight = stage.getHeight();
            
            // Load the main layout that contains the navigation
            FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("/view/Dashboard.fxml"));
            Parent mainRoot = mainLoader.load();
            BorderPane mainBorderPane = (BorderPane) mainRoot;

            // Load the NewNote content
            FXMLLoader contentLoader = new FXMLLoader(getClass().getResource("/view/NewNote.fxml"));
            Parent newNoteContent = contentLoader.load();

            // Set the NewNote content in the center of the BorderPane
            mainBorderPane.setCenter(newNoteContent);

            // Get controller and set callback using lambda with fully qualified type
            NewNoteController controller = contentLoader.getController();
            Consumer<Model.Note> callback = (Model.Note note) -> {
                notes.add(note);
                loadNotes();
            };
            controller.setOnSaveCallback(callback);
            
            // Create new scene with the new note view and preserve window size
            Scene scene = new Scene(mainBorderPane);
            stage.setScene(scene);
            
            // Restore window dimensions
            stage.setWidth(currentWidth);
            stage.setHeight(currentHeight);
            
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showError("Error loading new note view");
        }
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
            
            // Load the NotesContent.fxml (contains only the main content)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/NotesContent.fxml"));
            Parent notesContent = loader.load();
            
            // Replace only the center content
            borderPane.setCenter(notesContent);
            
            // Update button styles in the sidebar
            Node sourceButton = (Node) event.getSource();
            if (sourceButton.getParent() instanceof VBox) {
                VBox sidebar = (VBox) sourceButton.getParent();
                
                // Reset all buttons to default style
                sidebar.getChildren().forEach(node -> {
                    if (node instanceof Button) {
                        node.getStyleClass().remove("sidebar-button-selected");
                        node.getStyleClass().add("sidebar-button");
                    }
                });
                
                // Set the Notes button to selected
                sourceButton.getStyleClass().remove("sidebar-button");
                sourceButton.getStyleClass().add("sidebar-button-selected");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            showError("Error loading notes");
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
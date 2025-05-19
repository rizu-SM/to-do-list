package Controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import Model.Note;
import java.io.IOException;
import java.util.function.Consumer;
import util.UserSession;
import javafx.scene.control.ListView;
import Controller.NoteController;
import Controller.NewNoteController;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.geometry.Insets;

public class NotesController extends BaseController implements Initializable {
    @FXML
    private Button backButton;
    
    @FXML
    private TextField titleField;
    
    @FXML
    private TextArea descriptionArea;
    
    @FXML
    private Text dateText;

    @FXML
    private ListView<Note> notesListView;

    private NoteController noteController;

    public NotesController() {
        this.noteController = new NoteController();
    }

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

    private void loadNotes() {
        try {
            int userId = UserSession.getInstance().getCurrentUser().getId();
            List<Note> notes = noteController.getNotesByUserId(userId);
            notesListView.getItems().setAll(notes);
            
            // Set custom cell factory to display notes nicely
            notesListView.setCellFactory(lv -> new javafx.scene.control.ListCell<Note>() {
                private final VBox vbox = new VBox(8);
                private final HBox titleBar = new HBox(10);
                private final javafx.scene.control.Label titleLabel = new javafx.scene.control.Label();
                private final javafx.scene.control.Label descLabel = new javafx.scene.control.Label();
                private final Button deleteButton = new Button("×");
                private final Region spacer = new Region();

                {
                    // Setup the layout
                    vbox.setPadding(new Insets(10));
                    vbox.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 8; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 2);");
                    
                    titleBar.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
                    HBox.setHgrow(spacer, Priority.ALWAYS);
                    
                    titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #333333;");
                    descLabel.setStyle("-fx-text-fill: #666666;");
                    descLabel.setWrapText(true);
                    
                    deleteButton.setStyle("-fx-background-color: transparent; -fx-text-fill: #ff4444; -fx-font-size: 16px; -fx-font-weight: bold;");
                    
                    titleBar.getChildren().addAll(titleLabel, spacer, deleteButton);
                    vbox.getChildren().addAll(titleBar, descLabel);
                    
                    // Prevent the cell from being selected
                    setStyle("-fx-background-color: transparent;");
                }

                @Override
                protected void updateItem(Note note, boolean empty) {
                    super.updateItem(note, empty);
                    if (empty || note == null) {
                        setGraphic(null);
                    } else {
                        titleLabel.setText(note.getTitre());
                        descLabel.setText(note.getDescription());
                        descLabel.setMaxWidth(notesListView.getWidth() - 40);
                        
                        deleteButton.setOnAction(e -> confirmAndDeleteNote(note));
                        
                        setGraphic(vbox);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            showError("Error loading notes: " + e.getMessage());
        }
    }

    private void confirmAndDeleteNote(Note note) {
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Delete Note");
        confirmDialog.setHeaderText("Delete Note");
        confirmDialog.setContentText("Are you sure you want to delete this note?");
        
        confirmDialog.showAndWait().ifPresent(response -> {
            if (response == javafx.scene.control.ButtonType.OK) {
                try {
                    if (noteController.deleteNote(note.getId())) {
                        loadNotes(); // Refresh the list after deletion
                    } else {
                        showError("Failed to delete note");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    showError("Error deleting note: " + e.getMessage());
                }
            }
        });
    }

    @FXML
    private void handleNewNoteButton(ActionEvent event) {
        try {
            // Get the current BorderPane
            BorderPane borderPane = (BorderPane) ((Node) event.getSource()).getScene().getRoot();
            
            // Load the NewNote.fxml using relative path
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/NewNote.fxml"));
            Parent newNoteRoot = loader.load();
            
            // Get the controller and set up the callback
            NewNoteController controller = loader.getController();
            controller.setOnSaveCallback(note -> {
                // Refresh the notes list when a new note is saved
                loadNotes();
            });
            
            // Replace only the center content
            borderPane.setCenter(newNoteRoot);
            
        } catch (IOException e) {
            e.printStackTrace();
            showError("Error opening new note view");
        }
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
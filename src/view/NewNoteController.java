package view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import Model.Note;
import java.io.IOException;
import java.util.function.Consumer;
import java.time.LocalDateTime;

public class NewNoteController {
    @FXML
    private TextField titleField;
    
    @FXML
    private TextArea descriptionArea;
    
    private Consumer<Model.Note> onSaveCallback;
    
    public void setOnSaveCallback(Consumer<Model.Note> callback) {
        this.onSaveCallback = callback;
    }
    
    @FXML
    private void handleBackButton(ActionEvent event) {
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
            showError("Error returning to notes view");
        }
    }
    
    @FXML
    private void handleSaveButton(ActionEvent event) {
        String title = titleField.getText().trim();
        String description = descriptionArea.getText().trim();
        
        if (title.isEmpty()) {
            showError("Please enter a title for your note");
            return;
        }
        
        // Create new note with current time
        Model.Note note = new Model.Note(title, description, LocalDateTime.now());
        if (onSaveCallback != null) {
            onSaveCallback.accept(note);
        }
        
        // Return to notes view
        handleBackButton(event);
    }
    
    private void showError(String message) {
        // Show error alert
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
} 
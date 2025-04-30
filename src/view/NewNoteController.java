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
import util.UserSession;

public class NewNoteController {
    @FXML
    private TextField titleField;
    
    @FXML
    private TextArea descriptionArea;
    
    private Consumer<Note> onSaveCallback;
    
    public void setOnSaveCallback(Consumer<Note> callback) {
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
            
            // Get the controller and ensure it loads notes
            NotesController notesController = loader.getController();
            
            // Replace only the center content
            borderPane.setCenter(notesRoot);
            
            System.out.println("Returning to notes view - Total notes: " + NotesController.getAllNotes().size());
            
        } catch (IOException e) {
            e.printStackTrace();
            showError("Error returning to notes view");
        }
    }
    
    @FXML
    private void handleSaveButton(ActionEvent event) {
        String titre = titleField.getText().trim();
        String description = descriptionArea.getText().trim();
        
        if (titre.isEmpty()) {
            showError("Please enter a title for your note");
            return;
        }
        
        try {
            // Get current user ID
            int userId = UserSession.getInstance().getCurrentUser().getId();
            
            // Create new note with user ID
            Note note = new Note(userId, titre, description);
            
            // Add note to the static list
            NotesController.getAllNotes().add(note);
            
            // Notify callback if present
            if (onSaveCallback != null) {
                onSaveCallback.accept(note);
            }
            
            // Return to notes view
            handleBackButton(event);
            
        } catch (Exception e) {
            e.printStackTrace();
            showError("Error saving note: " + e.getMessage());
        }
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
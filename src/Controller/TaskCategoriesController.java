package Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.geometry.Pos;
import javafx.scene.control.ListCell;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import Controller.CategoryManager;

import java.io.IOException;
import java.util.List;

public class TaskCategoriesController {
    @FXML private TextField categoryNameField;
    @FXML private Button addCategoryButton;
    @FXML private Hyperlink goBackLink;
    @FXML private ListView<String> categoriesListView;
    
    private CategoryManager categoryManager = CategoryManager.getInstance();

    @FXML
    public void initialize() {
        // Use categories from CategoryManager
        categoriesListView.setItems(categoryManager.getCategories());
        
        // Set custom cell factory for the ListView to include delete button
        categoriesListView.setCellFactory(lv -> new ListCell<String>() {
            private final Button deleteButton = new Button("🗑️");
            private final HBox cell = new HBox();
            private final Label label = new Label();
            private final Region spacer = new Region();
            
            {
                // Configure delete button
                deleteButton.getStyleClass().add("delete-button");
                deleteButton.setOnAction(event -> {
                    String item = getItem();
                    if (item != null) {
                        // Show confirmation dialog
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Delete Category");
                        alert.setHeaderText("Delete category \"" + item + "\"?");
                        alert.setContentText("Are you sure you want to delete this category?");
                        
                        alert.showAndWait().ifPresent(response -> {
                            if (response == ButtonType.OK) {
                                categoryManager.removeCategory(item);
                            }
                        });
                    }
                });
                
                // Configure HBox layout
                cell.setAlignment(Pos.CENTER_LEFT);
                cell.setSpacing(10);
                
                // Make the label take up all available space
                HBox.setHgrow(spacer, Priority.ALWAYS);
                
                // Add components to cell
                cell.getChildren().addAll(label, spacer, deleteButton);
            }
            
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    label.setText(item);
                    setGraphic(cell);
                }
            }
        });
    }

    @FXML
    private void handleAddCategory(ActionEvent event) {
        String newCategory = categoryNameField.getText().trim();
        
        if (newCategory.isEmpty()) {
            showAlert("Error", "Category name cannot be empty");
            return;
        }
        
        if (categoryManager.getCategories().contains(newCategory)) {
            showAlert("Error", "This category already exists");
            return;
        }
        
        categoryManager.addCategory(newCategory);
        categoryNameField.clear();
    }

    @FXML
    private void showDashboard(ActionEvent event) {
        try {
            // Load main layout with navigation
            FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("/view/Dashboard.fxml"));
            Parent mainRoot = mainLoader.load();
            BorderPane mainBorderPane = (BorderPane) mainRoot;

            // Load specific content
            FXMLLoader contentLoader = new FXMLLoader(getClass().getResource("/view/SpecificView.fxml"));
            Parent content = contentLoader.load();

            // Set content in the center
            mainBorderPane.setCenter(content);
            
        } catch (IOException ex) {
            ex.printStackTrace();
            showAlert("Error", "Could not load dashboard: " + ex.getMessage());
        }
    }

    @FXML
    private void openSettings(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Settings.fxml"));
            Parent settingsRoot = loader.load();
            
            // Get the current scene
            Scene currentScene = ((Node) event.getSource()).getScene();
            
            // Replace the entire scene
            currentScene.setRoot(settingsRoot);
            
        } catch (IOException ex) {
            ex.printStackTrace();
            showAlert("Error", "Could not load settings: " + ex.getMessage());
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
} 
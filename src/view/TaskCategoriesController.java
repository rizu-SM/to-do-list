package view;

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

import java.io.IOException;
import java.util.List;

public class TaskCategoriesController {
    @FXML private TextField categoryNameField;
    @FXML private Button addCategoryButton;
    @FXML private Hyperlink goBackLink;
    @FXML private ListView<String> categoriesListView;
    
    private ObservableList<String> categories;
    private List<String> defaultCategories = List.of("Personal", "Work", "Chores", "Religious", "Others");

    @FXML
    public void initialize() {
        // Initialize the categories list with default categories
        categories = FXCollections.observableArrayList(defaultCategories);
        categoriesListView.setItems(categories);
    }

    @FXML
    private void handleAddCategory(ActionEvent event) {
        String newCategory = categoryNameField.getText().trim();
        
        if (newCategory.isEmpty()) {
            showAlert("Error", "Category name cannot be empty");
            return;
        }
        
        if (categories.contains(newCategory)) {
            showAlert("Error", "This category already exists");
            return;
        }
        
        categories.add(newCategory);
        categoryNameField.clear();
    }

    @FXML
    private void showDashboard(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/dashboard.fxml"));
            Parent dashboardRoot = loader.load();
            
            // Get the current scene
            Scene currentScene = ((Node) event.getSource()).getScene();
            
            // Replace the entire scene
            currentScene.setRoot(dashboardRoot);
            
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
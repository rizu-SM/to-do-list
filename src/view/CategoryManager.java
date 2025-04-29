package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.List;
import java.util.ArrayList;

public class CategoryManager {
    private static CategoryManager instance;
    private ObservableList<String> categories;
    private List<String> defaultCategories = List.of("Personal", "Work", "Chores", "Religious", "Others");

    private CategoryManager() {
        // Convert defaultCategories to ArrayList to allow modifications
        categories = FXCollections.observableArrayList(new ArrayList<>(defaultCategories));
    }

    public static CategoryManager getInstance() {
        if (instance == null) {
            instance = new CategoryManager();
        }
        return instance;
    }

    public ObservableList<String> getCategories() {
        return categories;
    }

    public void addCategory(String category) {
        if (!categories.contains(category)) {
            categories.add(category);
        }
    }

    public boolean hasCategory(String category) {
        return categories.contains(category);
    }

    public void deleteCategory(String category) {
        categories.remove(category);
    }

    public boolean isDefaultCategory(String category) {
        return defaultCategories.contains(category);
    }
} 
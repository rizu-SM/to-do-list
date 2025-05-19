package Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CategoryManager {
    private static CategoryManager instance;
    private ObservableList<String> categories;

    private CategoryManager() {
        categories = FXCollections.observableArrayList(
            "Routin",
            "Work",
            "Study",
            "Sport",
            "Other"
        );
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

    public void removeCategory(String category) {
        categories.remove(category);
    }
} 
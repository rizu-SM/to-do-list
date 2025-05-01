package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import Controller.NotificationController;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Start the notification scheduler
            NotificationController.startScheduler();

            Parent root = FXMLLoader.load(getClass().getResource("/view/SignIn.fxml"));

            Scene scene = new Scene(root);

            primaryStage.setScene(scene);
            primaryStage.setTitle("To Do List");
            primaryStage.setMaximized(false); 
            primaryStage.setMaximized(true);      
            primaryStage.setResizable(false);  
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args); 
    }
}

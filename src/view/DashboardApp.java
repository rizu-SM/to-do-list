package view;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.net.URL;

public class DashboardApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            // CHANGER LES CHEMINS SELON TA NOUVELLE ORGANISATION
            URL fxmlLocation = getClass().getResource("dashboard.fxml");
            if (fxmlLocation == null) {
                throw new IllegalStateException("FXML file not found! Check the path.");
            }
            
            Parent root = FXMLLoader.load(fxmlLocation);
            Scene scene = new Scene(root);

            // Charger le CSS
            URL cssLocation = getClass().getResource("application.css");
            

            if (cssLocation != null) {
               
            	scene.getStylesheets().add(getClass().getResource("dashboard.css").toExternalForm());

                

                System.out.println("CSS chargé depuis : " + cssLocation.toExternalForm());

            }
            System.out.println("FXML chargé correctement !");

            primaryStage.setTitle("Task Manager Dashboard");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

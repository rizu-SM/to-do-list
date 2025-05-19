package Controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;
import java.net.URL;


public class InviteMemberView extends Application {
	

	



	    @Override
	    public void start(Stage primaryStage) {
	        try {
	            // Charger l'interface depuis le fichier FXML
	            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/InviteMember.fxml"));
	            
	            Parent root = loader.load();

	            // Créer la scène et appliquer la feuille de style CSS
	            Scene scene = new Scene(root);
	            URL cssLocation = getClass().getResource("/view/style.css");
	            if (cssLocation != null) {
	                System.out.println(" style.css trouvé : " + cssLocation.toExternalForm());
	                scene.getStylesheets().add(cssLocation.toExternalForm());
	            } else {
	                System.out.println(" style.css NOT found!");
	            }


	            URL inviteCssLocation = getClass().getResource("InviteMember.css");
	            if (inviteCssLocation != null) {
	                scene.getStylesheets().add(inviteCssLocation.toExternalForm());
	            } else {
	                System.out.println("⚠️ InviteMember.css not found!");
	            }



	            // Configurer et afficher la fenêtre principale
	            primaryStage.setTitle("Account Information");
	            primaryStage.setScene(scene);
	            primaryStage.show();
	        } catch (Exception e) {
	            e.printStackTrace(); // Affiche l'erreur en cas de problème
	        }
	    }

	    public static void main(String[] args) {
	        launch(args);
	    
	}


}
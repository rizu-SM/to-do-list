package view;
    import javafx.application.Application;
	import javafx.fxml.FXMLLoader;
	import javafx.scene.Parent;
	import javafx.scene.Scene;
	import javafx.stage.Stage;
	import javafx.scene.layout.BorderPane;

	public class AccountInformationView extends Application {

	    @Override
	    public void start(Stage primaryStage) {
	        try {
	            // Charger l'interface depuis le fichier FXML
	            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/AccountInfo.fxml"));
	            Parent root = loader.load();

	            // Créer la scène et appliquer la feuille de style CSS
	            Scene scene = new Scene(root);
	            scene.getStylesheets().add(getClass().getResource("/application/ressources/style.css").toExternalForm());

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





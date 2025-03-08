package application;


import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class NewTaskController {

	
	
	   @FXML
	    private Label label; // Correspond à l'élément Label dans ton FXML

	    @FXML
	    private Button button; // Correspond à l'élément Button dans ton FXML


	  @FXML
	  private ChoiceBox<String> category;
	  @FXML
	  public void initialize() {
		  category.getItems().addAll("Personal", "Work", "Chores", "Religious", "Others");
		  category.setValue("Personal");
	  }
}

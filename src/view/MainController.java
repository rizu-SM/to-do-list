package view;



import java.awt.event.ActionListener;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class MainController {

    @FXML
    private Label label; // Correspond à l'élément Label dans ton FXML

    @FXML
    private Button button; // Correspond à l'élément Button dans ton FXML

  private Stage stage;
  private Scene scene; 
  private Parent root;

  @FXML
  public void afficherSignInForm (ActionEvent e) throws IOException { 
	  Parent root = FXMLLoader.load(getClass().getResource("/view/SignIn.fxml"));
	  stage = (Stage)((Node)e.getSource()).getScene().getWindow();
	  
	  double width = stage.getScene().getWidth();
	    double height = stage.getScene().getHeight();
	    
	    
   scene = new Scene(root, width, height);
	    
	  
	    stage.setResizable(false);
	    
	  
	  stage.setScene(scene);
	  stage.show();
	  
	  
  }
  @FXML
  public void afficherLogInForm(ActionEvent e) throws IOException { 
	    Parent root = FXMLLoader.load(getClass().getResource("/view/SignUp.fxml"));
	    stage = (Stage)((Node)e.getSource()).getScene().getWindow();
	    
	   
	    double width = stage.getScene().getWidth();
	    double height = stage.getScene().getHeight();
	    
	    
	    scene = new Scene(root, width, height);
	    
	    stage.setScene(scene);
	    stage.setResizable(false);  
	    stage.show();
	}
  
  @FXML
  public void afficherDashboard(ActionEvent e) throws IOException { 
	    Parent root = FXMLLoader.load(getClass().getResource("/view/dashboard.fxml"));
	    stage = (Stage)((Node)e.getSource()).getScene().getWindow();
	    
	   
	    double width = stage.getScene().getWidth();
	    double height = stage.getScene().getHeight();
	    
	    
	    scene = new Scene(root, width, height);
	    
	    stage.setScene(scene);
	    stage.setResizable(false);  
	    stage.show();
	}
  


}
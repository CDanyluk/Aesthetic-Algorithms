import javafx.fxml.FXMLLoader;

import java.io.IOException;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.application.Application;
import javafx.scene.layout.VBox;

public class AestheticAPP {	
	
	@Override
	public void start(Stage primaryStage) throws Exception{
		
		FXMLLoader loader = new FXMLLoader();
		
		Parent root = FXMLLoader.load(getClass().getResource("ViewGUI.fxml"));
	    Scene scene = new Scene(root);


		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		Launch(args);
	}
}


import javafx.fxml.FXMLLoader;

import java.io.IOException;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.application.Application;
import javafx.scene.layout.VBox;

public class AestheticAPP {
	
	@Override
	public void start(Stage primaryStage) {
		Pane root;
		try {
			root = (Pane) FXMLLoader.load(getClass().getResource("ViewGUI.fxml"));
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (IOException e) {
			System.out.println("Could not open window!");
			e.printStackTrace();
			System.exit(1);
		}
	}

	public static void main(String[] args) {Launch(args);}

}



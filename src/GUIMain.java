
import java.awt.ScrollPane;
import java.io.IOException;
import java.sql.SQLException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import Database.Build;
import Database.Send;


public class GUIMain extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception{
		FXMLLoader loader = new FXMLLoader();
		
		Parent root = FXMLLoader.load(getClass().getResource("ViewGUI.fxml"));
	    Scene scene = new Scene(root);

		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/*public static void main(String[] args) {
		launch(args);
		launch(args);
		Build data = new Build();
		Send send = new Send();
		try {
			data.main();
			send.send("CREATE TABLE results (values INTEGER)");
			send.send("INSERT INTO rules VALUES (1)");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("Class not found.");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("SQL Exception.");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("IO Exception.");
		}
	}*/
}

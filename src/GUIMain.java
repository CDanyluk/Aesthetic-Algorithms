
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
import Database.Read;
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

	public static void main(String[] args) {
		launch(args);
		try {
			Build data = new Build();
			data.main();
		} catch (Exception e){
			System.out.println("pre-existing database");
		}
		/*try {
			//data.main();
			//send.send("INSERT INTO Cells VALUES ('automata1', 'seeds0', 'dead8', 'live2', 'colorshit', 'iterate7', 42)");
			//System.out.println(send.readCells("SELECT * FROM Cells WHERE ImageName = 'automata1'")[1]);
			//System.out.println(send.readCells("SELECT ImageName, Score FROM Cells"));
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("Class not found.");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("SQL Exception.");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("IO Exception.");
		}*/
		//launch(args);
	}
}

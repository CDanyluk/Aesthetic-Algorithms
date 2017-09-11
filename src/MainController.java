

import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.paint.Color;

public class MainController {
	
	@FXML
	RadioButton lSystem;
	
	@FXML
	RadioButton cellularAutomata;
	
	@FXML
	RadioButton cartesianGenetic;
	
	final ToggleGroup algorithm = new ToggleGroup();
	
	@FXML
	TextField backgroundColor;
	
	@FXML
	TextField penColor;
	
	@FXML
	TextField iterations;
	
	@FXML
	Button start;
	
	@FXML
	Canvas picture;
	
	
	@FXML 
	public void initialize() {
		setButtonGroup();
		startHandler();
		
		
		
	}
	
	private void startHandler(){
		start.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	//checkColor();
            	draw();
            	
            }
        });
	}	

	
	public void setButtonGroup(){
		lSystem.setToggleGroup(algorithm);
		cellularAutomata.setToggleGroup(algorithm);
		cartesianGenetic.setToggleGroup(algorithm);
		lSystem.setSelected(true);
	}
	
	
	//This won't work, hex colors are not exclusively ints, unfortunate because this was going to make our lives easier
	/*public void checkColor(){
		if(backgroundColor.getText().length() != 7 || !backgroundColor.getText().substring(0).equals("#")){
			try{
				Integer.parseInt(backgroundColor.getText().substring(1));
			} catch(NumberFormatException nfe){
				throwErrorAlert("Please enter a valid background color.");
			}	
		}
		
		if(penColor.getText().length() != 7 || !penColor.getText().substring(0).equals("#")){
			try{
				Integer.parseInt(penColor.getText().substring(1));
			} catch(NumberFormatException nfe){
				throwErrorAlert("Please enter a valid pen color.");
			}	
		}
			
	}*/
	
	private void throwErrorAlert(String msg){
		Alert alert = new Alert(AlertType.ERROR, msg, ButtonType.OK);
		alert.showAndWait();
	}
	
	// TODO: Write this method! 
	// Will draw on the canvas
	public void draw(){
		GraphicsContext gc = picture.getGraphicsContext2D();
		// fuuuuuuuuuuck
		gc.setFill(Color.rgb(red, green, blue, .99));
		
		
		
	}
	
	


}

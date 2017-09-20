

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
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ColorPicker;
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
	ColorPicker backcolor;
	
	@FXML
	ColorPicker drawcolor;
	
	@FXML
	TextField iterations;
	
	@FXML
	Button start;
	
	//@FXML
	//Button lsystemgo;
	
	@FXML
	Canvas picture;
	
	private double[] seed;
	private LSystem lway;
	
	@FXML 
	public void initialize() {
		seed = new double[2];
		seed[0] = 305;
		seed[1] = 230;
		lway = new LSystem(300);
		setButtonGroup();
		startHandler();
		picture.setOnMouseClicked(event -> draw(event));
		
	}
	
	private void startHandler(){
		start.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	//checkColor();
            	setColor();
            	
            }
        });
	}	

	
	public void setButtonGroup(){
		lSystem.setToggleGroup(algorithm);
		cellularAutomata.setToggleGroup(algorithm);
		cartesianGenetic.setToggleGroup(algorithm);
		lSystem.setSelected(true);
	}
	
	public void setColor(){
		GraphicsContext gc = picture.getGraphicsContext2D();
		gc.setFill(backcolor.getValue());
		gc.fillRect(0,0,613,460);
		
	}
	

	public void drawTree(double[] start) {
		
		// 20 < start[0] && start[0] < 590 && 20 < start[1] && start[1] < 440
		double[] left = lway.calculateLeft(start);
		double[] right = lway.calculateRight(start);
		
		if (lway.getlength() > 10) {
			drawLine(start, left);
			drawLine(start, right);
			drawTree(left);
			drawTree(right);
		}else {
			return;
		}
		
	}
		
	
	private void throwErrorAlert(String msg){
		Alert alert = new Alert(AlertType.ERROR, msg, ButtonType.OK);
		alert.showAndWait();
	}
	
	
	// Will draw on the canvas
	public void draw(MouseEvent event){
		GraphicsContext gc = picture.getGraphicsContext2D();
		double x = event.getX();
		double y = event.getY();
		if (cellularAutomata.isSelected()) {
			if (x < 5) {
				x = (x - (x%10));
			}else if (x > 5) {
				x = (x + (10 - (x%10)));
			}
			if (y < 5) {
				y = (y - (y%10));
			}else if (y > 5) {
				y = (y + (10 - (y%10)));
			}
		}else if (lSystem.isSelected()) {
			gc.setFill(backcolor.getValue());
			gc.fillRect(0,0,613,460);
			seed[0] = x;
			seed[1] = y;
			//wut
			drawTree(seed);
		}
		gc.setFill(drawcolor.getValue());
		gc.fillRect(x,y,10,10);
	}
	
	public void drawLine(int x, int y, int x2, int y2) {
		GraphicsContext gc = picture.getGraphicsContext2D();
		gc.setStroke(drawcolor.getValue());
		gc.setLineWidth(10);
		gc.strokeLine(x,  y, x2, y2);
		
	}
	
	public void drawLine(double[] start, double[] end) {
		GraphicsContext gc = picture.getGraphicsContext2D();
		gc.setStroke(drawcolor.getValue());
		gc.setLineWidth(5);
		//nothing
		gc.beginPath();
		gc.moveTo(start[0], start[1]);
		gc.lineTo(end[0], end[1]);
		gc.stroke();
		gc.closePath();
		//end nothing
		System.out.println("Start: " + start[0] + " , " + start[1]);
		System.out.println("End: " + end[0] + " , " + end[1]);
		gc.strokeLine(start[0],  start[1], end[0], end[1]);
		
	}
	
	


}

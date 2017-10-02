

import java.util.Arrays;
import java.util.Set;

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
	TextField angle;
	
	@FXML
	TextField length;
	
	@FXML
	TextField trimmer;
	
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
	
	@FXML
	Button lsystemgo;
	
	@FXML
	Button fetch;
	
	@FXML
	Button judgeus;
	
	@FXML
	Canvas picture;
	
	private double[] seed;
	private LSystem lway;
	private Cells cells;
	
	@FXML 
	public void initialize() {
		seed = new double[2];
		seed[0] = 305;
		seed[1] = 230;
		cells = new Cells();
		setButtonGroup();
		startHandler();
		picture.setOnMouseClicked(event -> draw(event));
		setColor();
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
	
	@FXML
	public void goButton() {
		//A tree will be drawn from that seed
		drawTree(seed);
	}
	
	@FXML
	public void fetchButton() {
		//Gets the graph stored in cells
		double[][] graph = cells.get();
		//Goes through for loop and fetches the values from cells
		for (int x = 0; x < 61; x++) {
			for (int y = 0; y < 46; y ++) {
				//if the point is "alive" or 1
				if (graph[x][y] == 1) {
					//Then draw it on the canvas
					//At ten times the original value of course ;)
					drawPoint(x*10,y*10);
				}
			}
		}
	}
	

	public void drawTree(double[] start) {
		//Creates a new LSystem
		//length temporarily a "magic number" for testing
		double lenVal = Double.parseDouble(length.getText());
		double trimVal = Double.parseDouble(trimmer.getText());
		double angleVal = Double.parseDouble(angle.getText());
		lway = new LSystem(start, lenVal, trimVal, angleVal );
		//get the tree out of lway, since it auto-makes it
		Set<Line> tree = lway.getTree();
		//for every line in that tree, draw it
		for (Line currentBranch: tree) {
			drawLine(currentBranch);
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
			//So if any x or y value is less than 5
			//We round it down so it is an even 10*n
			if (x < 5) {
				x = (x - (x%10));
			//for greater than 5 we round up
			}else if (x > 5) {
				x = (x + (10 - (x%10)));
			}
			//round down
			if (y < 5) {
				y = (y - (y%10));
			//round up
			}else if (y > 5) {
				y = (y + (10 - (y%10)));
			}
			//Now we want to store these values in cells
			//But we can't just plop them in since cells is actually 61x46
			//No, first we divide by 10 ;)
			double cellx = x/10;
			double celly = y/10;
			//Now we plop them in, or they come "alive"
			cells.live(cellx, celly);
			
		//When L-System is selected
		}else if (lSystem.isSelected()) {
			gc.setFill(backcolor.getValue());
			gc.fillRect(0,0,613,460);
			//Wherever is clicked will become the new seed
			seed[0] = x;
			seed[1] = y;
		}
		gc.setFill(drawcolor.getValue());
		gc.fillRect(x,y,10,10);
	}
	
	//Draws a single point
	public void drawPoint(int x, int y) {
		GraphicsContext gc = picture.getGraphicsContext2D();
		gc.setFill(drawcolor.getValue());
		gc.fillRect(x,y,10,10);
		
	}
	
	//THE THREE WAYS TO DRAW A LINE ----------------------------
	
	//By individual points
	public void drawLine(int x, int y, int x2, int y2) {
		GraphicsContext gc = picture.getGraphicsContext2D();
		gc.setStroke(drawcolor.getValue());
		gc.setLineWidth(10);
		gc.strokeLine(x,  y, x2, y2);
	}
	
	//By two double[] arrays
	public void drawLine(double[] start, double[] end) {
		GraphicsContext gc = picture.getGraphicsContext2D();
		gc.setStroke(drawcolor.getValue());
		gc.setLineWidth(5);
		gc.strokeLine(start[0],  start[1], end[0], end[1]);
		
	}
	
	//By giving it a line, which consists of two double arrays
	public void drawLine(Line line) {
		double[] start = line.getFirst();
		double[] end = line.getLast();
		drawLine(start, end);
	}
	
	


}



import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
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
import javafx.scene.control.ToggleButton;
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
	RadioButton cellularAutomata;
	@FXML ToggleButton alive0;
	@FXML ToggleButton alive1;
	@FXML ToggleButton alive2;
	@FXML ToggleButton alive3;
	@FXML ToggleButton alive4;
	@FXML ToggleButton alive5;
	@FXML ToggleButton alive6;
	@FXML ToggleButton alive7;
	@FXML ToggleButton alive8;
	
	@FXML ToggleButton dead0;
	@FXML ToggleButton dead1;
	@FXML ToggleButton dead2;
	@FXML ToggleButton dead3;
	@FXML ToggleButton dead4;
	@FXML ToggleButton dead5;
	@FXML ToggleButton dead6;
	@FXML ToggleButton dead7;
	@FXML ToggleButton dead8;
	
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
		picture.setOnMouseDragged(event -> draw(event));
		
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
		cells.clear();
	}
	
	@FXML
	public void goButton() {
		//A tree will be drawn from that seed
		drawTree(seed);
	}
	
	@FXML
	public void fetchButton() {
		
		//checks if iterations is valid and gets the number
		int iterateNum = 0;
		try {
			iterateNum = Integer.parseInt(iterations.getText());
		}catch (Exception e) {
			iterateNum = 1;
		}
		
		//Create two hashmap of alive and dead buttons pressed
		//These hashmaps represent the rules where the cells come alive and die
		//For alive: black cells will live where true; die otherwise
		//For dead: white cells will live where true; die otherwise
		Map<Integer, Boolean> alive = new HashMap<Integer, Boolean>();
		Map<Integer, Boolean> dead = new HashMap<Integer, Boolean>();
		for (int i = 0; i < 9; i++) {
			alive.put(i, false);
			dead.put(i, false);
		}
		//This is a chunk checking alive's buttons toggle and putting them in the hashmap
		if (alive0.isSelected()) { alive.put(0, true);}
		if (alive1.isSelected()) { alive.put(1, true);}
		if (alive2.isSelected()) { alive.put(2, true);}
		if (alive3.isSelected()) { alive.put(3, true);}
		if (alive4.isSelected()) { alive.put(4, true);}
		if (alive5.isSelected()) { alive.put(5, true);}
		if (alive6.isSelected()) { alive.put(6, true);}
		if (alive7.isSelected()) { alive.put(7, true);}
		if (alive8.isSelected()) { alive.put(8, true);}
		//This is a chunk checking deads button toggles
		if (dead0.isSelected()) { dead.put(0, true);}
		if (dead1.isSelected()) { dead.put(1, true);}
		if (dead2.isSelected()) { dead.put(2, true);}
		if (dead3.isSelected()) { dead.put(3, true);}
		if (dead4.isSelected()) { dead.put(4, true);}
		if (dead5.isSelected()) { dead.put(5, true);}
		if (dead6.isSelected()) { dead.put(6, true);}
		if (dead7.isSelected()) { dead.put(7, true);}
		if (dead8.isSelected()) { dead.put(8, true);}
		System.out.println(dead);
		
		
		//Calls conway on the iteration number
		//cells.iterate(iterateNum);
		cells.generalLifeandDeath(alive, dead);
		
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
				}if (graph[x][y] == 0) {
					erasePoint(x*10,y*10);
				}if (graph[x][y] == 2) {
					errorPoint(x*10,y*10);
				}
			}
		}
	}
	

	public void drawTree(double[] start) {
		//Creates a new LSystem
		//length temporarily a "magic number" for testing
		double length = 150;
		lway = new LSystem(length, start);
		
		//Shit, was this working before?   <--------------------------------------------
		
		
		//lway = new LSystem(Double.parseDouble(length.getText()), start);
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
	
	//Erases single point
	public void erasePoint(int x, int y) {
		GraphicsContext gc = picture.getGraphicsContext2D();
		gc.setFill(backcolor.getValue());
		gc.fillRect(x,y,10,10);
	}
	
	//Error point, red shows bad
	public void errorPoint(int x, int y) {
		GraphicsContext gc = picture.getGraphicsContext2D();
		gc.setFill(Color.RED);
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

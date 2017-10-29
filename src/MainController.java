import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
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
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Window;

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
	Button export;
	
	@FXML
	Button clear;
	
	@FXML
	Canvas picture;
	
	private double[] seed;
	private Cells cells;
	private int width;
	private int height;
	private LSystems lway;
	
	@FXML 
	public void initialize() {
		seed = new double[2];
		cells = new Cells(width, height);
		setButtonGroup();
		startHandler();
		height = (int)picture.getHeight();
		width = (int)picture.getWidth();
		cells = new Cells(width, height);
		setButtonGroup();
		startHandler();
		clearHandler();
		picture.setOnMouseClicked(event -> draw(event));
		picture.setOnMouseDragged(event -> draw(event));
		

		setColor();
		GraphicsContext gc = picture.getGraphicsContext2D();
		gc.clearRect(0, 0, width, height);

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
	
	private void clearHandler(){
		clear.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	GraphicsContext gc = picture.getGraphicsContext2D();
        		gc.clearRect(0, 0, width, height);
            	initialize();
            	
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
		gc.fillRect(0,0,width,height);
		cells = new Cells(width, height);
	}
	
	@FXML
	public void goButton() {
		//A tree will be drawn from that seed
		drawTree(seed);
	}
	
	@FXML
	public void exportAs() {
		FileChooser fileChooser = new FileChooser();
        
        //Set extension filter
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("png files (*.png)", "*.png"));
       
		//Show save file dialog
        File file = fileChooser.showSaveDialog(null);
        
         
        if(file != null){
            try {
                WritableImage writableImage = new WritableImage(width, height);
                picture.snapshot(null, writableImage);
                RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
                //Write the snapshot to the chosen file
                ImageIO.write(renderedImage, "png", file);
            } catch (IOException ex) {
                //Logger.getLogger(JavaFX_DrawOnCanvas.class.getName()).log(Level.SEVERE, null, ex);
            	System.out.println("error cannot save file you wrote this wrong");
            }
        }
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
		
		//FOR colored cellular automata
		//cells.colorAutomata(alive, dead, rainbow.size()-1);
		cells.change(alive, dead);
		drawColorAutomata();
	}
	
	public void drawAutomata() {
		//Gets the graph stored in cells
		double[][] graph = cells.getGraph();
		//Goes through for loop and fetches the values from cells
		for (int x = 0; x < cells.getWidth(); x++) {
			for (int y = 0; y < cells.getHeight(); y ++) {
				//if the point is "alive" or 1
				if (graph[x][y] == 1) {
					//Then draw it on the canvas
					//At ten times the original value of course ;)
					drawPoint((x-1)*10,(y-1)*10);
				}if (graph[x][y] == 0) {
					erasePoint((x-1)*10,(y-1)*10);
				}if (graph[x][y] == 2) {
					errorPoint((x-1)*10,(y-1)*10);
				}
			}
		}
	}
	
	public void drawColorAutomata() {
		//Gets the graph stored in cells
		double[][] graph = cells.getGraph();
		//Goes through for loop and fetches the values from cells
		
		for (int x = 0; x < cells.getWidth(); x++) {
			for (int y = 0; y < cells.getHeight(); y ++) {
				int colorKey = (int)cells.graph[x][y];
				int cellx = (x)*10;
				int celly = (y)*10;
				System.out.println(".");
				System.out.println("Retrieved cells : " + x + " , " + y);
				System.out.println("Drawn cells : " + cellx + " , " + celly);
				drawColorPoint(cellx, celly, colorKey);
			}
		}
	}
	
	public void printGraph(double[][] graph) {
		for (int x = 0; x < cells.getWidth(); x++) {
			for (int y = 0; y < cells.getHeight(); y ++) {
			
				System.out.print(graph[x][y]+ " ");
			}
			System.out.print("\n");
		}
	}
	

	public void drawTree(double[] start) {
		//Creates a new LSystem
		HashMap<String, String> rules = new HashMap<String, String>();
		//lway = new LSystem(Double.parseDouble(length.getText()), start);
		//Binary Tree
		//rules.put("0", "1[0]0");
		//rules.put("1", "11");
		
		//Sierpinski Triangle
		//rules.put("A", "B-A-B");
		//rules.put("B", "A+B+A");
		
		//Dragon Curve
		//rules.put("X", "X+YF+");
		//rules.put("Y", "−FX−Y");
		
		//Koch Curve
		rules.put("F", "F+F-F-F");
		
		//Koch Adaptation
		//rules.put("F", "F+F-F+F");
		
		lway = new LSystems(start, "F-F-F+F", rules, Integer.parseInt(trimmer.getText()), Double.parseDouble(length.getText()), Integer.parseInt(angle.getText()));
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
			int cellx = (int)(x)/10;
			int celly = (int)(y)/10;
			//System.out.println("Original raw cell : " + x + " , " + y);
			//System.out.println("Stored cell: " + cellx + " , " + celly);
			//Now we plop them in, or they come "alive"
			//Will be -1 without error number
			cells.liveColor(cellx, celly);
			
		//When L-System is selected
		}else if (lSystem.isSelected()) {
			gc.setFill(backcolor.getValue());
			gc.fillRect(0,0,width,height);
			//Wherever is clicked will become the new seed
			gc.fillRect(0,0,width,height);
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
	
	public void drawColorPoint(int x, int y, int color) {
		GraphicsContext gc = picture.getGraphicsContext2D();
		gc.setFill(cells.getColor(color));
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

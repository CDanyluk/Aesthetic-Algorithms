import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import edu.hendrix.blob.Blob;
import edu.hendrix.cluster.KMeansPlusPlus;
import edu.hendrix.cluster.color.ColorCluster;
import edu.hendrix.cluster.color.gui.ClusterPointsController;
import edu.hendrix.cluster.color.gui.FindShapes;
import edu.hendrix.cluster.color.gui.FindSizes;
import edu.hendrix.cluster.color.gui.WrappedBlobList;
import edu.hendrix.util.Alerter;
import edu.hendrix.util.Util;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

public class MainController {
	
	@FXML
	RadioButton lSystem;
	
	@FXML
	TextField angle;
	
	@FXML
	TextField length;
	
	@FXML
	TextField recursions;
	
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

	
	final ToggleGroup algorithm = new ToggleGroup();
	
	@FXML
	ColorPicker backcolor;
	
	@FXML
	ColorPicker drawcolor;
	
	@FXML
	TextField iterations;
	
	@FXML
	TextField exporthowmany;
	
	@FXML
	TextField exportLSystem;
	
	@FXML
	Button start;
	
	@FXML
	Button lsystemgo;
	
	@FXML
	Button randomizegrid;
	
	@FXML
	Button openedge;
	
	@FXML
	Button fetch;
	
	@FXML
	Button score;
	
	@FXML
	Button delete;
	
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
	private String folder;
	private int picNum;
	
	Optional<BufferedImage> image = Optional.empty();
	Optional<KMeansPlusPlus<ColorCluster>> clusters = Optional.empty();
	Optional<WrappedBlobList> blobs = Optional.empty();
	
	//God bad programming but shit take it
	private HashMap<Blob, HashMap<String, Double>> blobTypes = new HashMap<Blob, HashMap<String, Double>>();
	private HashMap<String, Integer> numOfShapes = new HashMap<String, Integer>();
	ArrayList<Color> colors = new ArrayList<>();
	
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
	
	@FXML 
	private void edgeScreen() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/edu/hendrix/cluster/color/gui/ClusterPoints.fxml"));
			BorderPane root = (BorderPane)loader.load();

			ClusterPointsController second = (ClusterPointsController)loader.getController();

			Stage secondStage = new Stage();
			Scene scene = new Scene(root);
			secondStage.setScene(scene);
			secondStage.show();
			
			
			//close the current window so just edge detector is there
			Stage stage = (Stage) openedge.getScene().getWindow();
			stage.close();

		} catch (Exception exc) {
			exc.printStackTrace();
			System.out.println("Could not open Ferrers program!");
		}
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
		GraphicsContext gc = picture.getGraphicsContext2D();
		gc.clearRect(0, 0, width, height);
		drawLSystem();
	}
	
	//Ferrer Code mixed in --------------------------------------------------------------------------------------
	
	void kMeans(int num) {
		image.ifPresent(img -> {
			KMeansPlusPlus<ColorCluster> kmeans = new KMeansPlusPlus<>(num);
			for (int x = 0; x < img.getWidth(); x++) {
				for (int y = 0; y < img.getHeight(); y++) {
					kmeans.train(new ColorCluster(img.getRGB(x, y)));
				}
			}
			clusters = Optional.of(kmeans);
			showImage();
		});
	}
	
	void showImage() {
		colors = new ArrayList<>();
		image.ifPresent(img -> {
			picture.getGraphicsContext2D().setFill(Color.WHITE);
			picture.getGraphicsContext2D().fillRect(0, 0, picture.getWidth(), picture.getHeight());
			double pixelWidth = picture.getWidth() / img.getWidth();
			double pixelHeight = picture.getHeight() / img.getHeight();
			for (int x = 0; x < img.getWidth(); x++) {
				for (int y = 0; y < img.getHeight(); y++) {
					Color chosen;
					KMeansPlusPlus<ColorCluster> kmeans = clusters.get();
					chosen = kmeans.getMatchingIdealInputFor(new ColorCluster(img.getRGB(x, y))).toColor();
					if (!colors.contains(chosen)) {
						colors.add(chosen);
					}
					picture.getGraphicsContext2D().setFill(chosen);
					picture.getGraphicsContext2D().fillRect(x * pixelWidth, y * pixelHeight, pixelWidth, pixelHeight);						
				}
			}
		});
	}
	
	void findBlobs() {
		image.ifPresent(img -> {
			clusters.ifPresent(cls -> {
				blobs = Optional.of(new WrappedBlobList(img, cls));
				//Find number of blobs
				//blobs.get().size();
				findSizeBlobs(new WrappedBlobList(img, cls));
				showImage();
				
				
			});
		});
	}
	
	//find blobsize and findshapes can probably be put outside of the controller too
	void findSizeBlobs(WrappedBlobList blobs) {
		FindSizes fit = new FindSizes(blobs);
		fit.findSizeBlobs();
		//Get number of large med and small blobs
		//fit.getBigBlobs();
		//fit.getMediumBlobs();
		//fit.getSmallBlobs();
		//fit.getTinyBlobs();
		findShapes(blobs);

	}
	
	void findShapes(WrappedBlobList blobs) {
		BufferedImage pic = image.get();
		FindShapes blobShapes = new FindShapes(pic.getWidth(), pic.getHeight(), blobs);
		//call function for returning lines found
		HashMap<String, Integer> numShapes = blobShapes.getNumOfShapes();
		//Get amount of shapes in the image
		//numShapes.get("line");
		//numShapes.get("square");
		//numShapes.get("triangle");
		//numShapes.get("circle");
		blobTypes = blobShapes.getTypes();
		numOfShapes = blobShapes.getNumOfShapes();
	}
	
	//THIS IS WHERE IT READS IN A FILE
	@FXML
	public void score() {
		//read file in
		//the global variable folder has the folder name that has been randomly generated.
		
		if (picNum >= 0) {
			//Go through the images up to the number exported, picNum is stored as a global variable :(
			for (int i = 0; i < picNum; i ++) {
				File path = new File(folder + "/automata" + i + ".png");
				System.out.println("Path for reading : " + path);
				try {
					image = Optional.of(ImageIO.read(path));
				} catch (IOException e) {
					System.out.println("The program couldn't auto read a damn file, problem in score()");
					e.printStackTrace();
				}
				kMeans(3);
				findBlobs();
				//execute some kind of code to give it a score
				HashMap<String, Integer> ideal = new HashMap<String, Integer>();
				ideal.put("square", 12);
				ideal.put("line", 19);
				ideal.put("circle", 1);
				ideal.put("triangle", 2);
				//print score
				//System.out.println("numOfShapes : " + numOfShapes);
				//System.out.println("Chi score = " + chisquared(ideal, numOfShapes));
				//System.out.println("colors : " + colors);
				double colorscore = colorchi(colors);
				if (colorscore > 0.2) {
					for (int c = 0; c < colors.size(); c++) {
						int r = (int)Math.round(255 * colors.get(c).getRed());
						int g = (int)Math.round(255 * colors.get(c).getGreen());
						int b = (int)Math.round(255 * colors.get(c).getBlue());
						String hex = String.format("#%02x%02x%02x", r, g, b);  
						System.out.println("color " + c + " hex #" + hex);
					}
				}
					
				System.out.println("color score = " + colorchi(colors));
			}
		}
		
	}
	
	//These chi things can probably be put outside of the actual controller
	private double colorchi(ArrayList<Color> colorList) {
		double scoretotal = 0;
		double expected = 120;
		for (int i = 0; i < colorList.size(); i++) {
			double dif = 0;
			if (i != colorList.size()-1) {
				double color1 = colorList.get(i).getHue();
				double color2 = colorList.get(i+1).getHue();
				dif = Math.abs(color1 - color2);
			}else {
				double color1 = colorList.get(i).getHue();
				double color2 = colorList.get(0).getHue();
				dif = Math.abs(color1 - color2);
			}
			scoretotal += 1/((Math.abs(expected - dif))+1);
		}
		scoretotal = scoretotal/colorList.size();
		return scoretotal;
	}
	
	private double chisquared(HashMap<String, Integer> expected, HashMap<String, Integer> given) {
		double difcount = 0;
		//blobcount exists because if there is only one blob it should automatically get a score of 0
		int blobcount = 0;
		for (String k : expected.keySet()) {
			double dif = 0;
			dif = Math.abs(expected.get(k) - given.get(k));
			//add the blobs of given to blobcount
			blobcount += given.get(k);
			dif++;
			difcount = difcount + (1/dif);
		}
		difcount = difcount / expected.size();
		//if the blobcount is 1 that is v bad and means the picture is empty
		if (blobcount == 1) {
			return 0.0;
		}
		return difcount;
	}
	
	//END Ferrers code combine ------------------------------------------------------------------------------------------------------------------------
	
	//decides whether you should choose where to export,
	//or whether it should auto-export
	@FXML
	public void export() {
		if (algorithm.getSelectedToggle() == cellularAutomata) {
			try {
				picNum = Integer.parseInt(exporthowmany.getText());
			}catch (Exception e) {
				picNum = 1;
			}
			if (picNum == 1) {
				exportAs();
			}else {
				autoExport("/AutomataImages/Images");
			}
		}else {
			try {
				picNum = Integer.parseInt(exportLSystem.getText());
			}catch (Exception e) {
				picNum = 1;
			}
			if (picNum == 1) {
				exportAs();
			}else {
				autoExport("/LSystemImages/Images");
			}
		}
		
	}
	
	//manual export
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
	
	//auto-export
	public void autoExport(String p) {
		String path = p+randomnumber();
		folder = path;
		System.out.println(folder);
		File directory = new File(path);
		FileChooser saveLocation = new FileChooser();
		directory.mkdirs();
		for (int i = 0; i < picNum; i++) {
			if ( p == "/AutomataImages/Images" ) {
				exportImage(i, directory);
			}
			else {
				exportLSystem(directory);
			}
		}
		
	}
	
	public void exportLSystem(File directory) {
		String number = randomnumber();
		initialize();
		//here is where you put functions to randomize your L system
		drawLSystem();
		goButton();
		//Write the text file
		
		//Write the image file
		File file = new File(directory, "lsystem" + number + ".png");
		if (file != null){
			try {
				WritableImage writableImage = new WritableImage(width, height);
				picture.snapshot(null, writableImage);
				RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
				//Write the snapshot to the chosen file
				ImageIO.write(renderedImage, "png", file);
		    } catch (IOException ex) {
		    	//Logger.getLogger(JavaFX_DrawOnCanvas.class.getName()).log(Level.SEVERE, null, ex);
		        System.out.println("exportImage() fuuuuucked");
		        }
		    }
	}
	
	//ideally we would save by the date not a random number but oh well
	public String randomnumber () {
		String one = (int)Math.floor( Math.random() * 1000 ) + "";
		String two = (int)Math.floor( Math.random() * 1000 ) + "";
		String three = (int)Math.floor( Math.random() * 1000 ) + "";
		return one+two+three;
	}
	
	public void exportImage(int num, File directory) {
		//String number = randomnumber();
		String number = num + "";
		initialize();
		randomGrid();
		//centerGrid();
		//This dumb chunk is literally to record the seeds
		String seed = "Seeds: " + cells.getSeed() + "\n";
		fetchButton();
		String name = seed + cells.getRuleset();
		
		//Write the text file
		try(  PrintWriter out = new PrintWriter( directory + "/automata" + number + ".txt" )  ){
		    out.println(seed);
		    out.println(cells.getDead());
		    out.println(cells.getAlive());
		    out.println(cells.getColors());
		    out.println(cells.getIterations());
		    out.println(cells.getFukit());
		    out.println("WHY THE FUCK IS IT NOT ADDING THIS");
		} catch (FileNotFoundException e) {
			System.out.println("Your text writer is fucked");
		}
		
		//Write the image file
		File file = new File(directory, "automata" + number + ".png");
		if (file != null){
			try {
				WritableImage writableImage = new WritableImage(width, height);
				picture.snapshot(null, writableImage);
				RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
				//Write the snapshot to the chosen file
				ImageIO.write(renderedImage, "png", file);
		    } catch (IOException ex) {
		    	//Logger.getLogger(JavaFX_DrawOnCanvas.class.getName()).log(Level.SEVERE, null, ex);
		        System.out.println("exportImage() fuuuuucked");
		        }
		    }
		return;
	}
	
	@FXML
	public void randomGrid() {
		cells.randomGraph(100);
		drawColorAutomata();
	}
	
	public void centerGrid() {
		cells.centerOf();
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
		cells.iterate(iterateNum, alive, dead);
		drawColorAutomata();
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
				drawColorPoint(cellx, celly, colorKey, 10);
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
	

	public void drawLSystem() {
		HashMap<String, String> rules = new HashMap<String, String>();
		
		int numRules = ThreadLocalRandom.current().nextInt(1, 3);
		//System.out.println(numRules);
		for(int i = 0; i < numRules; i++){
			int numChars = ThreadLocalRandom.current().nextInt(1, 50);
			//System.out.println(numChars);
			String toUse = "";
			for(int j = 0; j < numChars; j++){
				String alphabet = "01-+";
				int k = alphabet.length();
				Random r = new Random();
				toUse += alphabet.charAt(r.nextInt(k));
			}
			//System.out.println(toUse);
			rules.put(i + "",toUse );
		}
		
		int startStringLength = ThreadLocalRandom.current().nextInt(1, 4);
		String startingString = "";
		for(int m = 0; m < startStringLength; m++){
			String alphabet = "01";
			int k = alphabet.length();
			Random r = new Random();
			startingString += alphabet.charAt(r.nextInt(k));
		}
		
		Integer randomAngle = ThreadLocalRandom.current().nextInt(1, 360);
		angle.setText(randomAngle.toString());
		
		Integer randomRecursions = ThreadLocalRandom.current().nextInt(1, 4);
		recursions.setText(randomRecursions.toString());
		
		Integer randomLength = ThreadLocalRandom.current().nextInt(1, 15);
		length.setText(randomLength.toString());
		
		
		int randomStartX = ThreadLocalRandom.current().nextInt(15, 685);
		int randomStartY = ThreadLocalRandom.current().nextInt(15, 685);
		double[] randomStart = new double[2];
		randomStart[0] = randomStartX;
		randomStart[1] = randomStartY;
		
		
		int randomMakeRandom = ThreadLocalRandom.current().nextInt(3, 697);
		boolean makeRandom = false;
		if(randomMakeRandom == 1){makeRandom = true;}else{makeRandom = false;}
		
		
		lway = new LSystems(randomStart, 
							startingString, 
							rules, 
							makeRandom,
							randomRecursions,
							randomLength, 
							randomAngle);
		
		ArrayList<Line> tree = lway.getDrawing();
		drawLSystemLines(tree);
		
	}
	
	private void drawLSystemLines(ArrayList
			<Line> tree){
		double red = ThreadLocalRandom.current().nextInt(0, 255);
		double green = ThreadLocalRandom.current().nextInt(0, 255);
		double blue =  ThreadLocalRandom.current().nextInt(0, 255);
		
		int randomWidth = ThreadLocalRandom.current().nextInt(2, 10);
		double colorChange = ThreadLocalRandom.current().nextInt(1, 60);
		int randomColor = ThreadLocalRandom.current().nextInt(1, 3);
		
		GraphicsContext gc = picture.getGraphicsContext2D();
		int canvasRed = ThreadLocalRandom.current().nextInt(0, 255);
		int canvasGreen = ThreadLocalRandom.current().nextInt(0, 255);
		int canvasBlue =  ThreadLocalRandom.current().nextInt(0, 255);
		gc.setFill(Color.rgb(canvasRed, canvasGreen, canvasBlue));
		gc.fillRect(0,0,width,height);
		
		
		//for every line in that tree, draw it
		for (Line currentBranch: tree) {
			double[] first = currentBranch.getFirst();
			double[] last = currentBranch.getLast();
			if(randomColor == 1){
				if(red + colorChange > 255){
					red = colorChange;
				}else{
					red += colorChange;
				}
			}if(randomColor == 2){
				if(green + colorChange > 255){
					green = colorChange;
				} else {
					green += colorChange;
				}
			}if(randomColor == 3){
				if(blue + colorChange > 255){
					blue = colorChange;
				} else {
					blue += colorChange;
				}
			}
			//System.out.println(red);
			
			drawLineSpecificColor(first, last, randomWidth, red, green, blue );
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
	
	public void drawColorPoint(int x, int y, int color, int width) {
		GraphicsContext gc = picture.getGraphicsContext2D();
		gc.setFill(cells.getColor(color));
		gc.fillRect(x,y,width,width);
	}
	
	
	//THE WAYS TO DRAW A LINE ----------------------------
	
	//By two double[] arrays
	public void drawLine(double[] start, double[] end, int linewidth) {
		GraphicsContext gc = picture.getGraphicsContext2D();
		int red =  ThreadLocalRandom.current().nextInt(0, 255);
		int green = ThreadLocalRandom.current().nextInt(0, 255);
		int blue =  ThreadLocalRandom.current().nextInt(0, 255);
		gc.setStroke(Color.rgb(red, green, blue));
		gc.setLineWidth(linewidth);
		gc.strokeLine(start[0],  start[1], end[0], end[1]);
		
	}
	
	public void drawLineSpecificColor(double[] first, double[] last, int linewidth, double red, double green, double blue) {
		GraphicsContext gc = picture.getGraphicsContext2D();
		gc.setStroke(Color.rgb((int)red, (int)green,(int) blue));
		gc.setLineWidth(linewidth);
		gc.strokeLine(first[0],  first[1], last[0], last[1]);
		
	}
	
	//By giving it a line, which consists of two double arrays
	public void drawLine(Line line) {
		double[] start = line.getFirst();
		double[] end = line.getLast();
		drawLine(start, end, 10);
	}
	
	//Draws a line of your requested width
	public void drawLine(Line line, int linewidth) {
		double[] start = line.getFirst();
		double[] end = line.getLast();
		drawLine(start, end, linewidth);
		
	}

}

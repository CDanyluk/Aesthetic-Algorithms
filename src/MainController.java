import java.awt.color.ColorSpace;

import java.awt.image.BufferedImage;

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

import Automata.Cells;
import Automata.Cross;
import Database.DatabaseInput;
import Database.ParseCells;
import Database.Read;
import Database.ReadCells;
import Database.ReadLSystems;
import Database.Send;
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
import javafx.scene.control.CheckBox;
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
	Button deletebad;
	
	@FXML
	Button deleteall;
	
	@FXML
	Button viewcells;
	
	@FXML
	Button viewbest;
	
	@FXML
	Button crossmutate;
	
	@FXML
	TextField generation;
	
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
	private Map<String, Integer> blobSizes = new HashMap<String, Integer>();
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
		if(Integer.parseInt(exportLSystem.getText()) != 1){
			drawMultipleLSystems();
		}else {
			drawLSystem();
		}
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
		blobSizes = fit.getBlobSizes();

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
	
	
	public double[] scoreThis(String imageName) {
		double[] scoreContainer = new double[3];
		double score = 0;
		File path = new File(folder + "/" + imageName + ".png");
		System.out.println("path : " + path);
		//System.out.println("Path for reading : " + path);
		try {
			image = Optional.of(ImageIO.read(path));
		} catch (IOException e) {
			System.out.println("The program couldn't auto read a damn file, problem in scoreThis()");
			e.printStackTrace();
		}
		kMeans(4);
		findBlobs();
		//the golden ratio ideal
		Map<String, Integer> golden = new HashMap<String, Integer>();
		golden.put("big", 7);
		golden.put("medium", 7);
		golden.put("small", 3);
		
		//find the two scores
		double colorscore = colorchi(colors);
		double sizescore = sizechi(golden, blobSizes);
		//combine the two scores and divide by two
		score = (colorscore + sizescore)/2;
		scoreContainer[0] = colorscore;
		scoreContainer[1] = sizescore;
		scoreContainer[2] = score;
		
		return scoreContainer;
		
	}
	
	
	public double[] scoreThisL(String imageName) {
		double[] scoreContainer = new double[3];
		double score = 0;
		File path = new File(folder + "/" + imageName + ".png");
		System.out.println("path : " + path);
		//System.out.println("Path for reading : " + path);
		try {
			image = Optional.of(ImageIO.read(path));
		} catch (IOException e) {
			System.out.println("The program couldn't auto read a damn file, problem in scoreThis()");
			e.printStackTrace();
		}
		kMeans(4);
		findBlobs();
		//the golden ratio ideal
		Map<String, Integer> golden = new HashMap<String, Integer>();
		golden.put("huge", 1);
		golden.put("big", 2);
		golden.put("medium", 2);
		golden.put("small", 50);
		
		//find the two scores
		double sizescore = sizechiL(golden, blobSizes);
		//combine the two scores and divide by two
		score = sizescore;
		scoreContainer[0] = 1;
		scoreContainer[1] = sizescore;
		scoreContainer[2] = score;
		
		return scoreContainer;
		
	}
	
	//These chi things can probably be put outside of the actual controller
	private double colorchi(ArrayList<Color> colorList) {
		double scoretotal = 0;
		double expected = 30;
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
			//if the difference is less than 30 do not add it
			if (dif > 30) {
				scoretotal += 1/((Math.abs(expected - dif))+1);
			}
		}
		//scoretotal = scoretotal/colorList.size();
		double brightscore = brightnesschi(colorList);
		return (scoretotal + brightscore)/2;
	}
	
	//chi values for brightness
	private double brightnesschi(ArrayList<Color> colorList) {
		double scoretotal = 0;
		for (int i = 0; i < colorList.size(); i++) {
			double dif = 0;
			//The greater the difference the greater the number
			if (i != colorList.size()-1) {
				double color1 = colorList.get(i).getBrightness();
				double color2 = colorList.get(i+1).getBrightness();
				dif = Math.abs(color1 - color2);
			}else {
				double color1 = colorList.get(i).getBrightness();
				double color2 = colorList.get(0).getBrightness();
				dif = Math.abs(color1 - color2);
			}
			//if the difference is less than 30 do not add it
			scoretotal += dif;
		}
		//because of the nature of getBrightness this value should never exceed 1
		return scoretotal/3;
	}
	
	private double sizechi(Map<String, Integer> expected, Map<String, Integer> given) {
		double bigscore = 0;
		double medscore = 0;
		double smallscore = 0;
		double total = 0;
		
		for(String key : given.keySet()){
			System.out.println(key + " " + given.get(key));
		}
		
		//Compare big blobs
		//if big blobs are between 2 - 5 give it a perfect score
/*		if (given.get("big") >= 7 && given.get("big") <= 7) {
			bigscore = 1;
		//otherwise chisquare it
		}else if (given.get("big") > 7) {
			bigscore = 1/(Math.abs(given.get("big") - 7)+1);
		//if it is less than 2 it is just bad
		}else if (given.get("big") < 2) {
			bigscore = 0;
		}*/
		
		//chisquare the big score
		double expBig = expected.get("big");
		double givBig = given.get("big");
		bigscore = 1/(Math.abs(expBig - givBig)+1);
		
		if (given.get("huge") > 0) {
			return 0;
		}
		
		//chisquare the medium score
		double expMed = expected.get("medium");
		double givMed = given.get("medium");
		medscore = 1/(Math.abs(expMed - givMed)+1);

		//chisquare the small score
		double expSmall = expected.get("small");
		double givSmall = given.get("small");
		smallscore = 1/(Math.abs(expSmall - givSmall)+1);
		if (givSmall >= 400) {
			smallscore = 0;
		}
		
		//add al the scores, divide by three, and return the value
		total = bigscore + medscore + smallscore;
		total = total/3;
		double shapescore = shapechi(numOfShapes);
		return (total + shapescore)/2;
	}
	
	private double sizechiL(Map<String, Integer> expected, Map<String, Integer> given) {
		
		double hugescore = 0;
		double bigscore = 0;
		double medscore = 0;
		double smallscore = 0;
		double total = 0;
		
		for(String key : given.keySet()){
			System.out.println(key + " " + given.get(key));
		}
		
		//chisquare the big score
		double expBig = expected.get("big");
		double givBig = given.get("big");
		bigscore = (Math.pow((givBig - expBig), 2) / expBig);
	
		double expHuge = expected.get("huge");
		double givHuge = given.get("huge");
		hugescore = (Math.pow((givHuge - expHuge), 2) / expHuge);
		
		//chisquare the medium score
		double expMed = expected.get("medium");
		double givMed = given.get("medium");
		medscore = (Math.pow((givMed - expMed), 2) / expMed);

		//chisquare the small score
		double expSmall = expected.get("small");
		double givSmall = given.get("small");
		smallscore = (Math.pow((givSmall - expSmall), 2) / expSmall);
		if (givSmall >= 400) {
			smallscore = 0;
		}
		
		//add al the scores, divide by three, and return the value
		total = hugescore + bigscore + medscore + smallscore;
		total = 1/ (total + 1);
		double shapescore = shapechi(numOfShapes);
		return (total + shapescore)/2;
	}
	
	private double shapechi(HashMap<String, Integer> numOfShapes) {
		if (numOfShapes.get("square") > 400) {
			return 0.0;
		}else {
			return 1.0;
		}
	}
	
	//END Ferrers code combine ------------------------------------------------------------------------------------------------------------------------
	
	
	@FXML
	public void viewCells() {
		ReadCells read = new ReadCells();
		try {
			read.readRow();
		}catch (Exception e) {
			System.out.println("viewCells() is broken");
			e.printStackTrace();
		}
	}
	
	@FXML
	public void deleteCells() {
		ReadCells read = new ReadCells();
		try {
			read.deleteAll();
		}catch (Exception e) {
			System.out.println("deleteCells() is broken");
			e.printStackTrace();
		}
	}
	
	@FXML
	public void deleteLs() {
		ReadLSystems read = new ReadLSystems();
		try {
			read.deleteAll();
		}catch (Exception e) {
			System.out.println("deleteCells() is broken");
			e.printStackTrace();
		}
	}
	
	@FXML
	public void deleteBad() {
		ReadCells read = new ReadCells();
		try {
			read.deleteBad();
		}catch (Exception e) {
			System.out.println("deleteBad() is broken");
			e.printStackTrace();
		}
	}
	
	public void killCells() {
		ReadCells read = new ReadCells();
		try {
			read.killWorst();
		}catch (Exception e) {
			System.out.println("killCells() is broken");
			e.printStackTrace();
		}
	}
	
	@FXML
	public void viewBest() {
		ReadCells read = new ReadCells();
		initialize();
		int iter = 0;
		cells = new Cells(width, height);
		try {
			cells.setDead(read.readBestDead());
			cells.setAlive(read.readBestAlive());
			cells.setColor(read.readBestColors());
			cells.setSeeds(read.readBestSeeds());
			iter = read.readBestIter();
			cells.update(iter);
			drawColorAutomata();
			read.readBest();
		}catch (Exception e) {
			System.out.println("The database is empty.");
			//e.printStackTrace();
		}
	}
	
	public void crossovers(int gen) {
		ReadCells read = new ReadCells();
		Cross cross = new Cross();
		cells = new Cells(width, height);
		try {
			List<Cells> cellList = read.readTopTen();
			for (int i = 0; i < cellList.size(); i++) {
				if (i == 9) {
					cells = cross.cross(cellList.get(i), cellList.get(0));
				}else {
					cells = cross.cross(cellList.get(i), cellList.get(i+1));
				}
				drawColorAutomata();
				exportMutant("crossover", gen);
			}
			
			
		} catch (Exception e) {
			System.out.println("crossovers is broken");
			e.printStackTrace();
		}
	}
	
	public void mutateMult(int gen) {
		ReadCells read = new ReadCells();
		cells = new Cells(width, height);
		try {
			List<Cells> cellList = read.readTopTen();
			for (int i = 0; i < cellList.size(); i++) {
				cells = cellList.get(i);
				cells.update(cells.getIter());
				drawColorAutomata();
				exportMutant("mutant", gen);
			}
		} catch (Exception e) {
			System.out.println("mutateMult is broken");
			e.printStackTrace();
		}
	}
	
	@FXML
	public void CrossMutate() {
		int gen = 1;
		folder = folder + "/Gen" + gen + "/";
		int genNum = Integer.parseInt(generation.getText());
		for (int i = 0; i < genNum; i++) {
			mutateMult(gen);
			crossovers(gen);
			killCells();
			gen++;
			folder = folder + "/Gen" + gen + "/";
		}
	}
	
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
	
	
	//ideally we would save by the date not a random number but oh well
	public String randomnumber() {
		String one = (int)Math.floor( Math.random() * 1000 ) + "";
		String two = (int)Math.floor( Math.random() * 1000 ) + "";
		String three = (int)Math.floor( Math.random() * 1000 ) + "";
		return one+two+three;
	}
	
	public int oneOrTwo() {
		if (Math.random() < 0.5) {
		    return 0;
		}else {
			return 1;
		}
	}
	
	public void exportImage(int num, File directory) {
		//String number = randomnumber();
		String number = "0" + randomnumber();
		initialize();
		randomGrid();
		//centerGrid();
		//This dumb chunk is literally to record the seeds
		int iterateNum = 0;
		try {
			iterateNum = Integer.parseInt(iterations.getText());
		}catch (Exception e) {
			iterateNum = 1;
		}
		cells.setAlive(cells.randomRules());
		cells.setDead(cells.randomRules());
		cells.update(iterateNum);
		drawColorAutomata();
		String name = "automata" + number;
		
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
		
		//Write the database
				try {
				    DatabaseInput input = new DatabaseInput();
				    double[] score = scoreThis(name);
				    input.cellsToDatabase(name, cells, score);
				    
				} catch (Exception e) {
					System.out.println("Could not put cells in database");
					e.printStackTrace();
				}
		return;
	}
	
	public void exportMutant(String fold, int gen) {
		//String p = "/AutomataImages/" + fold;
		//String path = p;
		//folder = path;
		File directory = new File(folder);
		FileChooser saveLocation = new FileChooser();
		directory.mkdirs();
		String num = randomnumber();
		String name = fold + gen + num;
		
		//Write the image file
		File file = new File(directory, name + ".png");
		if (file != null){
			try {
				WritableImage writableImage = new WritableImage(width, height);
				picture.snapshot(null, writableImage);
				RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
				//Write the snapshot to the chosen file
				ImageIO.write(renderedImage, "png", file);
		    } catch (IOException ex) {
		    	//Logger.getLogger(JavaFX_DrawOnCanvas.class.getName()).log(Level.SEVERE, null, ex);
		        System.out.println("exportMutant() fuuuuucked");
		        }
		    }
		
		//Write the database
				try {
				    DatabaseInput input = new DatabaseInput();
				    double score[] = scoreThis(name);
				    input.cellsToDatabase(name, cells, score);
				    
				} catch (Exception e) {
					System.out.println("Could not put cells in database");
					e.printStackTrace();
				}
		return;
	}
	
	@FXML
	public void randomGrid() {
		cells.randomGraph(88);
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
		
		//FOR colored cellular automata
		//cells.colorAutomata(alive, dead, rainbow.size()-1);
		cells.setAlive(cells.randomRules());
		cells.setDead(cells.randomRules());
		cells.update(iterateNum);
			drawColorAutomata();

		/*cells.iterate(iterateNum, alive, dead);
		drawColorAutomata();*/
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
	
//	LSystems Code -----------------------------------------------------------------------------
	
	private void drawLSystemLines(ArrayList
			<Line> tree){
		double red = ThreadLocalRandom.current().nextInt(0, 255);
		double green = ThreadLocalRandom.current().nextInt(0, 255);
		double blue = ThreadLocalRandom.current().nextInt(0, 255);
		
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
	
	public void drawLSystem() {
		HashMap<String, String> rules = new HashMap<String, String>();
		
		int numRules = 2;
		//System.out.println(numRules);
		for(int i = 0; i < numRules; i++){
			int numUsed = 0;
			int numChars = ThreadLocalRandom.current().nextInt(1, 50);
			String toUse = "";
			for(int j = 0; j < numChars; j++){
				String alphabet = "01-+[";
				int k = alphabet.length();
				Random r = new Random();
				char charAdd =  alphabet.charAt(r.nextInt(k));
				toUse += charAdd;
				if(charAdd == '[' ){
					numUsed += 1;
				}
			}
			
			int next = -1;
			for(int m = 1; m < numUsed; m++){
				int len = toUse.length();
				next = toUse.indexOf("[",next+1);
				int locationAdd = toUse.length();
				if(next+1 != toUse.length()){
					locationAdd = ThreadLocalRandom.current().nextInt(next + 1, toUse.length());
				}
				String first = toUse.substring(0, locationAdd);
				String last = toUse.substring(locationAdd, len);
				toUse = first + "]" + last;
			}
			
			int n = 0;
			for(int x=0; x<toUse.length(); x++){
				if(toUse.substring(x, x+1).equals("]")){
					n += 1;
				}
			}
			
			if(n != numUsed){
				for(int p = 0; p < (numUsed - n); p++){
					toUse += "]";
				}
			}
			
			System.out.println(toUse);
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
		
		
		int randomStartX = ThreadLocalRandom.current().nextInt(300, 301);
		int randomStartY = ThreadLocalRandom.current().nextInt(300, 301);
		double[] randomStart = new double[2];
		randomStart[0] = randomStartX;
		randomStart[1] = randomStartY;
		
		
		int randomMakeRandom = ThreadLocalRandom.current().nextInt(1, 2);
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
		
		DatabaseInput input = new DatabaseInput();
		int num = ThreadLocalRandom.current().nextInt(1, 500);
		exportSingleLSystem( new File("/Users/taylorbaer/Desktop/LSystems"), num);
		double[] score = scoreThisL("lsystem" + num);
		System.out.println("\n\n\n\nScore: " + score[2]);
		try {
			input.lsystemsToDatabase("lsystem" + num, randomStartX + "." + randomStartY, startingString, rules.toString(), randomRecursions, randomLength, randomAngle, score[2]);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
	public void drawLSystemSpecific(double [] start, String startingString, Boolean makeRandom, HashMap<String, String> rules, Integer randomRecursions, Integer randomLength, Integer randomAngle) {
		System.out.println("in drawing!");
		int whichMutate =  ThreadLocalRandom.current().nextInt(1, 5);
		if(whichMutate == 1){
			int startStringLength = ThreadLocalRandom.current().nextInt(1, 50);
			startingString = "";
			for(int m = 0; m < startStringLength; m++){
				String alphabet = "01";
				int k = alphabet.length();
				Random r = new Random();
				startingString += alphabet.charAt(r.nextInt(k));
			}
		}if(whichMutate == 2){
			randomAngle = ThreadLocalRandom.current().nextInt(1, 360);
			angle.setText(randomAngle.toString());
		} if(whichMutate == 3){
			randomRecursions = ThreadLocalRandom.current().nextInt(1, 4);
			recursions.setText(randomRecursions.toString());
		} if(whichMutate == 4){
			randomLength = ThreadLocalRandom.current().nextInt(1, 15);
			length.setText(randomLength.toString());
		} if(whichMutate == 5){
			int randomRule = ThreadLocalRandom.current().nextInt(1, rules.size()) - 1;
			String mutateRule = rules.get(randomRule);
			for(int k = 0; k < mutateRule.length();k++){
				if(mutateRule.substring(k, k+1).equals("0")){
					mutateRule = mutateRule.substring(0, k - 1) + "1" + mutateRule.substring(k+1);
				} else if(mutateRule.substring(k, k+1).equals("1")){
					mutateRule = mutateRule.substring(0, k - 1) + "0" + mutateRule.substring(k+1);
				} else if(mutateRule.substring(k, k+1).equals("-")){
					mutateRule = mutateRule.substring(0, k - 1) + "+" + mutateRule.substring(k+1);
				} else if(mutateRule.substring(k, k+1).equals("+")){
					mutateRule = mutateRule.substring(0, k - 1) + "-" + mutateRule.substring(k+1);
				} 
			}
			
			rules.put(randomRule + "", mutateRule);
		}
		
		System.out.println(whichMutate);
		
//		int randomMakeRandom = ThreadLocalRandom.current().nextInt(1, 2);
//		boolean makeRandom = false;
//		
//		if(randomMakeRandom == 1){makeRandom = true;}else{makeRandom = false;}
		lway = new LSystems(start, 
							startingString, 
							rules, 
							makeRandom,
							randomRecursions,
							randomLength, 
							randomAngle);
		System.out.println("made lsys");
		ArrayList<Line> tree = lway.getDrawing();
		System.out.println("about to draw");
		drawLSystemLines(tree);
		System.out.println("drawn");
		String stringRules = "";
		DatabaseInput input = new DatabaseInput();
		int num = ThreadLocalRandom.current().nextInt(1, 3000);
		exportSingleLSystem( new File("/Users/taylorbaer/Desktop/LSystems"), num);
		double[] score = scoreThisL("lsystem" + num);
		System.out.println("\n\n\n\nScore: " + score[2]);
		try {
			input.lsystemsToDatabase("lsystem" + num, start.toString(), startingString, rules.toString(), randomRecursions, randomLength, randomAngle, score[2]);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		System.out.println("\n\n\nStart: " + start[0] + "," + start[1] + "\nStart String: " + startingString.toString() + "\nRules: " + rules.toString()
				+ "\nMake random: " + makeRandom + "\nRecursions: " + randomRecursions + "\nLength: " + randomLength + "\nAngle: " + randomAngle);
		//mutateMultipleLSystems();
	}
	
	
	public void drawMultipleLSystems(){
		for(int i = 0; i < Integer.parseInt(exportLSystem.getText()); i++){
			
			folder = "/Users/taylorbaer/Desktop/LSystems";
			drawLSystem();
			//exportLSystem( new File("/Users/taylorbaer/Desktop/LSystems"));
		}
	}
	
	public void mutateMultipleLSystems(){
		for(int i = 0; i < Integer.parseInt(exportLSystem.getText()); i++){
			//drawLSystem();
			exportLSystem( new File("/Users/taylorbaer/Desktop/LSystems"));
		}
	}
	
	public void exportMutants(File directory) {
		String number = randomnumber();
		initialize();
		//here is where you put functions to randomize your L system
		drawLSystem();
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
		        System.out.println("exportLSystem() fuuuuucked");
		        }
		    }
	}
	
	public void exportSingleLSystem(File directory, int num) {
		String number = randomnumber();
		File file = new File(directory, "lsystem" + num + ".png");
		if (file != null){
			try {
				WritableImage writableImage = new WritableImage(width, height);
				picture.snapshot(null, writableImage);
				RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
				//Write the snapshot to the chosen file
				ImageIO.write(renderedImage, "png", file);
		    } catch (IOException ex) {
		    	//Logger.getLogger(JavaFX_DrawOnCanvas.class.getName()).log(Level.SEVERE, null, ex);
		        System.out.println("exportLSystem() fuuuuucked");
		        }
		    }
	}
	
	public void exportLSystem(File directory) {
		String number = randomnumber();
		initialize();
		//here is where you put functions to randomize your L system
		mutateLSystem();
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
		        System.out.println("exportLSystem() fuuuuucked");
		        }
		    }
	}
	
	@FXML
	public void viewLSystem() {
		ReadLSystems read = new ReadLSystems();
		try {
			read.readRow();
		}catch (Exception e) {
			System.out.println("Cannot view LSystems (sad day...)");
			e.printStackTrace();
		}
	}
	
	@FXML
	public void viewBestLSystem() {
// Come back to this please!
		ReadLSystems read = new ReadLSystems();
		GraphicsContext gc = picture.getGraphicsContext2D();
		gc.clearRect(0, 0, width, height);
		try {
			read.readBest();
			
		}catch (Exception e) {
			System.out.println("The database is empty.\n" + e);
			//e.printStackTrace();
		}
		drawColorAutomata();
	}
	
	@FXML
	public void mutateLSystem() {
		System.out.println("calling mutate");
		realMutateLSystem();
//		double[] start = new double[2];
//		start[0] = 300;
//		start[1] = 300;
//		HashMap<String, String> rules = new HashMap<String,String>();
//		rules.put("0", "001[11++000[+---+[[+]]]00]+[]");
//		rules.put("1", "0+0-[+[-+[00-0+]10]]-+[1][]");
//		drawLSystemSpecific(start, "100", true, rules, 2, 13, 47);
	}
	
	public void realMutateLSystem() {
		ReadLSystems read = new ReadLSystems();
		System.out.println("in function");
		try {
			 List<HashMap<String, String>> results = read.getTopTen();
			 for(HashMap<String, String> r : results){
				 System.out.println("in for");
				 System.out.println(r);
				double[] start = new double[2];
				start[0] = 300;
				start[1] = 300;
				HashMap<String, String> rules = new HashMap<String,String>();
				String completeRules = r.get("Rules");
				System.out.println(completeRules);
				String split1 = completeRules.substring(0,completeRules.indexOf(","));
				String split2 = completeRules.substring(completeRules.indexOf(",") + 1);
				System.out.println(split1.substring(completeRules.indexOf("=") + 1));
				System.out.println(split2.substring(completeRules.indexOf("=") + 1, split2.length() - 1));
				System.out.println(r.get("StartString"));
				rules.put("0",split1.substring(completeRules.indexOf("=") + 1));
				rules.put("1", split2.substring(completeRules.indexOf("=") + 1, split2.length() - 1));
				drawLSystemSpecific(start, r.get("StartString"), false, rules, Integer.parseInt(r.get("Recursions")), Integer.parseInt(r.get("Length")), Integer.parseInt(r.get("Angle")));
			}
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}


import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;

public class EdgeController {
	
	@FXML
	Button in;
	
	@FXML
	Button detect;
	
	@FXML
	Label colors;
	
	@FXML
	Label distinctcolors;
	
	@FXML
	Label verticaledges;
	
	@FXML
	Label horizontaledges;
	
	@FXML
	Canvas canvas;
	
	private double width;
	private double height;
	BufferedImage pic;
	Graphics graphic;
	private Set<Line> vert;
	private Set<Line> hor;
	List<Color> colorList;

	
	@FXML 
	public void initialize() {
		height = canvas.getHeight();
		width = canvas.getWidth();
		this.vert = new HashSet<Line>();
		this.hor = new HashSet<Line>();
		colorList = new ArrayList<Color>();
	}
	
	@FXML
	public void importButt() throws IOException {
		GraphicsContext gc = canvas.getGraphicsContext2D();
		FileChooser fileChooser = new FileChooser();
		//Set extension filter
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("png files (*.png)", "*.png"));
		//Show open file dialog
		File file = fileChooser.showOpenDialog(null);
		//Opens image file in canvas, wrong image file for analysis
		if (file != null) {
			InputStream inputStream = new FileInputStream(file.getAbsolutePath());
            Image image = new Image(inputStream);
			gc.drawImage(image, 0.0, 0.0, width, height);
			//This gets the image file for analysis
			setGraphics(file);
		} else {
			System.out.println("file was null");
		}
		initialize();
	}
	
	public void setGraphics(File file) {
		//Opens the image again in a different format better for accessing data
		BufferedImage img = null;
		try {
		    img = ImageIO.read(file);
			pic = img;
		} catch (IOException e) {
			System.out.println("Could not set graphics");
		}
	
	}
	
	@FXML
	public void Detect() {
		AnaColor();
		DistColor();
		Vertical();
		Horizontal();
		drawAll();
	}
	
	public void AnaColor() {
		for (int x = 0; x < pic.getWidth(); x++) {
			for (int y = 0; y < pic.getHeight(); y++) {
				//Get RGB value on each pixel
				Color c = new Color(pic.getRGB(x, y));
				if (colorList.contains(c) == false) {
					colorList.add(c);
				}
			}
		}
		colors.setText(colorList.size()+"");
	}
	
	public void DistColor() {
		List<Color> distColor = new ArrayList<Color>(colorList);
		for (int i = 0; i < distColor.size(); i++) {
			for (int j = 0; j < colorList.size(); j++) {
				if (isDistinct(distColor.get(i), colorList.get(j)) == false) {
					distColor.remove(i);
				}
			}
		}
		distinctcolors.setText(distColor.size()+"");
	}
	
	//returns true if there is a difference greater than 20
	public boolean isDistinct(Color first, Color second) {
		//RGB values of first
		int red1 = first.getRed();
		int green1 = first.getGreen();
		int blue1 = first.getBlue();
		//RGB values of second
		int red2 = second.getRed();
		int green2 = second.getGreen();
		int blue2 = second.getBlue();
		
		//if any of the values are 20 off
		if (Math.abs(red1-red2) > 20) {
			return true;
		}
		if (Math.abs(green1-green2) > 20) {
			return true;
		}
		if (Math.abs(blue1-blue2) > 20) {
			return true;
		}if (first.equals(second)) {
			return true;
		}else {
			System.out.println("first: "+red1+","+green1+","+blue1);
			System.out.println("second: "+red2+","+green2+","+blue2);
			return false;
		}
	}
	
	public void Vertical() {
		//Go through the picture
		for (int x = 0; x < pic.getWidth()-10; x=x+10) {
			for (int y = 0; y < pic.getHeight(); y=y+10) {
				//if the RGB on pixel x is different from the next pixel over
				Color before = new Color(pic.getRGB(x, y));
				Color after = new Color(pic.getRGB(x+10, y));
				if (before.equals(after) == false) {
					//create a line between them
					double[] start = new double[2];
					double[] end = new double[2];
					start[0] = x+10;
					start[1] = y;
					end[0] = x+10;
					end[1] = y + 10;
					//add the line to vert
					vert.add(new Line(start, end));
				}
			}
		}
		verticaledges.setText(vert.size()+"");
	}
	
	public void Horizontal() {
		//Go through the picture
		for (int x = 0; x < pic.getWidth(); x=x+10) {
			for (int y = 0; y < pic.getHeight()-10; y=y+10) {
				//if the RGB on pixel x is different from the next pixel over
				Color before = new Color(pic.getRGB(x, y));
				Color after = new Color(pic.getRGB(x, y+10));
				if (before.equals(after) == false) {
					//create a line between them
					double[] start = new double[2];
					double[] end = new double[2];
					start[0] = x;
					start[1] = y+10;
					end[0] = x+10;
					end[1] = y + 10;
					//add the line to vert
					hor.add(new Line(start, end));
					}
				}
			}
		horizontaledges.setText(vert.size()+"");
	}
	
	public void drawAll() {
		for (Line v: vert) {
			drawLine(v);
		}
		for (Line h: hor) {
			drawLine(h);
		}
	}
	
	public void drawLine(Line line) {
		double[] start = line.getFirst();
		double[] end = line.getLast();
		drawLine(start, end, 1);
	}
	
	public void drawLine(double[] start, double[] end, int linewidth) {
		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.setStroke(gc.getFill());
		gc.setLineWidth(linewidth);
		gc.strokeLine(start[0],  start[1], end[0], end[1]);
		
	}

}

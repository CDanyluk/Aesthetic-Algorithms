
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
import java.util.List;

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

	
	@FXML 
	public void initialize() {
		height = canvas.getHeight();
		width = canvas.getWidth();
		//Graphics2D gc = new Graphics2D();
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
	}
	
	@FXML 
	public void AnaColor() {
		List<Color> colorList = new ArrayList<Color>();
		for (int x = 0; x < pic.getWidth(); x++) {
			for (int y = 0; y < pic.getHeight(); y++) {
				//Get RGB value on each pixel
				Color c = new Color(pic.getRGB(x, y));
				if (colorList.contains(c) == false) {
					colorList.add(c);
				}
			}
		}
		System.out.println(colorList);
		colors.setText(colorList.size()+"");
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


}

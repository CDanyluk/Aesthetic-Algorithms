package edu.hendrix.cluster.color.gui;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Optional;
import java.util.function.Consumer;

import javax.imageio.ImageIO;

import edu.hendrix.blob.Blob;
import edu.hendrix.cluster.KMeansPlusPlus;
import edu.hendrix.cluster.color.ColorCluster;
import edu.hendrix.util.Alerter;
import edu.hendrix.util.Util;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

public class ClusterPointsController {
	@FXML
	TextField kField;
	@FXML
	CheckBox showK;
	@FXML
	Canvas canvas;
	
	@FXML
	TextField numBlobs;
	@FXML
	TextField blobNum;
	@FXML
	CheckBox showBlob;
	
	@FXML
	TextField largeBlobs;
	@FXML 
	TextField mediumBlobs;
	@FXML
	TextField smallBlobs;
	@FXML
	TextField tinyBlobs;
	@FXML
	Button export;
	@FXML
	TextField numLine;
	@FXML
	TextField numSquare;
	@FXML
	TextField numTriangle;
	@FXML
	TextField numCircle;
	
	FileChooser chooser = new FileChooser();
	
	Optional<BufferedImage> image = Optional.empty();
	Optional<KMeansPlusPlus<ColorCluster>> clusters = Optional.empty();
	Optional<WrappedBlobList> blobs = Optional.empty();
	
	//God bad programming but shit take it
	private HashMap<Blob, HashMap<String, Double>> blobTypes = new HashMap<Blob, HashMap<String, Double>>();
	private HashMap<String, Integer> numOfShapes = new HashMap<String, Integer>();
	
	@FXML
	void initialize() {
		showK.setSelected(false);
	}
	
	//Make a proper array
	//chi square it
	//your notes are in your phone
	@FXML
	void export() {
		kMeans(3);
		findBlobs();
		HashMap<String, Integer> ideal = new HashMap<String, Integer>();
		ideal.put("square", 12);
		ideal.put("line", 19);
		ideal.put("circle", 1);
		ideal.put("triangle", 2);
		System.out.println(numOfShapes);
		System.out.println("Chi score = " + chisquared(ideal, numOfShapes));
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
	
	@FXML
	void kMeans() {
		image.ifPresent(img -> {
			KMeansPlusPlus<ColorCluster> kmeans = new KMeansPlusPlus<>(Integer.parseInt(kField.getText()));
			for (int x = 0; x < img.getWidth(); x++) {
				for (int y = 0; y < img.getHeight(); y++) {
					kmeans.train(new ColorCluster(img.getRGB(x, y)));
				}
			}
			clusters = Optional.of(kmeans);
			showK.setSelected(true);
			showImage();
		});
	}
	
	void kMeans(int num) {
		image.ifPresent(img -> {
			KMeansPlusPlus<ColorCluster> kmeans = new KMeansPlusPlus<>(num);
			for (int x = 0; x < img.getWidth(); x++) {
				for (int y = 0; y < img.getHeight(); y++) {
					kmeans.train(new ColorCluster(img.getRGB(x, y)));
				}
			}
			clusters = Optional.of(kmeans);
			showK.setSelected(true);
			showImage();
		});
	}
	
	@FXML
	void showImage() {
		image.ifPresent(img -> {
			canvas.getGraphicsContext2D().setFill(Color.WHITE);
			canvas.getGraphicsContext2D().fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
			double pixelWidth = canvas.getWidth() / img.getWidth();
			double pixelHeight = canvas.getHeight() / img.getHeight();
			for (int x = 0; x < img.getWidth(); x++) {
				for (int y = 0; y < img.getHeight(); y++) {
					Color chosen;
					if (showK.isSelected() && clusters.isPresent()) {
						KMeansPlusPlus<ColorCluster> kmeans = clusters.get();
						chosen = kmeans.getMatchingIdealInputFor(new ColorCluster(img.getRGB(x, y))).toColor();
					} else {
						int rgb = img.getRGB(x, y);
						chosen = Color.rgb(Util.getRed(rgb), Util.getGreen(rgb), Util.getBlue(rgb));
					}
					
					if (showBlob.isSelected() && blobs.isPresent()) {
						WrappedBlobList wbl = blobs.get();
						if (wbl.inCurrentBlob(x, y)) {
							//Color the blobs differently depending on size
							//If large blob = red
							if (wbl.getBlob(x, y).getSize() >= 25000) {
								chosen = Color.RED;
							} else if (wbl.getBlob(x, y).getSize() < 25000 && wbl.getBlob(x, y).getSize() >= 1000) {
								chosen = Color.YELLOW;
							} else if (wbl.getBlob(x, y).getSize() < 1000 && wbl.getBlob(x, y).getSize()>= 100) {
							
							}else {
								chosen = Color.CYAN;
							}
						}
					}
					canvas.getGraphicsContext2D().setFill(chosen);
					canvas.getGraphicsContext2D().fillRect(x * pixelWidth, y * pixelHeight, pixelWidth, pixelHeight);						
				}
			}
		});
	}
	
	@FXML
	void open() {
		Alerter.fileIO(chooser.showOpenDialog(null), f -> {
			image = Optional.of(ImageIO.read(f));
			showImage();
		});
	}
	
	@FXML
	void nextK() {
		changeK(1);
	}
	
	@FXML
	void prevK() {
		changeK(-1);
	}
	
	void changeK(int incr) {
		int k = Integer.parseInt(kField.getText());
		int updated = k + incr;
		if (updated >= 2) {
			kField.setText(Integer.toString(updated));
		}
	}
	
	@FXML
	void findBlobs() {
		image.ifPresent(img -> {
			clusters.ifPresent(cls -> {
				blobs = Optional.of(new WrappedBlobList(img, cls));
				numBlobs.setText(Integer.toString(blobs.get().size()));
				findSizeBlobs(new WrappedBlobList(img, cls));
				blobNum.setText("0");
				showBlob.setSelected(true);
				showImage();
				
				
			});
		});
	}
	
	@FXML
	void findShapes(WrappedBlobList blobs) {
		BufferedImage pic = image.get();
		FindShapes blobShapes = new FindShapes(pic.getWidth(), pic.getHeight(), blobs);
		//call function for returning lines found
		HashMap<String, Integer> numShapes = blobShapes.getNumOfShapes();
		numLine.setText(Integer.toString(numShapes.get("line")));
		numSquare.setText(Integer.toString(numShapes.get("square")));
		numTriangle.setText(Integer.toString(numShapes.get("triangle")));
		numCircle.setText(Integer.toString(numShapes.get("circle")));
		blobTypes = blobShapes.getTypes();
		numOfShapes = blobShapes.getNumOfShapes();
	}
	
	@FXML
	void findSizeBlobs(WrappedBlobList blobs) {
		FindSizes fit = new FindSizes(blobs);
		fit.findSizeBlobs();
		largeBlobs.setText(Integer.toString(fit.getBigBlobs()));
		mediumBlobs.setText(Integer.toString(fit.getMediumBlobs()));
		smallBlobs.setText(Integer.toString(fit.getSmallBlobs()));
		tinyBlobs.setText(Integer.toString(fit.getTinyBlobs()));
		findShapes(blobs);

	}
	
	void percentageChange(FindSizes fit) {
		System.out.println(fit.percentageChange());
	}
	
	@FXML
	void nextBlob() {
		blobs.ifPresent(b -> moveBlob(b, b2 -> b2.next()));
	}
	
	@FXML
	void prevBlob() {
		blobs.ifPresent(b -> moveBlob(b, b2 -> b2.prev()));
	}
	
	void moveBlob(WrappedBlobList b, Consumer<WrappedBlobList> mover) {
		mover.accept(b);
		blobNum.setText(Integer.toString(b.current()));
		showImage();
	}
}

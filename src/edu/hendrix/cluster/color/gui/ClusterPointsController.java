package edu.hendrix.cluster.color.gui;

import java.awt.image.BufferedImage;
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
	
	FileChooser chooser = new FileChooser();
	
	Optional<BufferedImage> image = Optional.empty();
	Optional<KMeansPlusPlus<ColorCluster>> clusters = Optional.empty();
	Optional<WrappedBlobList> blobs = Optional.empty();
	
	
	@FXML
	void initialize() {
		showK.setSelected(false);
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
	void findLine(WrappedBlobList blobs) {
		findLines lineFinder = new findLines(blobs);
		//call function for returning lines found
		lineFinder.lineSize();
		//change text box
		numLine.setText("0");
	}
	
	@FXML
	void findSizeBlobs(WrappedBlobList blobs) {
		Fitness fit = new Fitness(blobs);
		fit.findSizeBlobs();
		percentageChange(fit);
		largeBlobs.setText(Integer.toString(fit.getBigBlobs()));
		mediumBlobs.setText(Integer.toString(fit.getMediumBlobs()));
		smallBlobs.setText(Integer.toString(fit.getSmallBlobs()));
		tinyBlobs.setText(Integer.toString(fit.getTinyBlobs()));
		findLine(blobs);
	}
	
	void percentageChange(Fitness fit) {
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

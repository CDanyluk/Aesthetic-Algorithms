package edu.hendrix.util;

import java.io.File;
import java.io.IOException;

import javafx.scene.control.Alert;

public class Alerter {
	public static void errorBox(String errorMsg) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setContentText(errorMsg);
		alert.show();
	}
	
	public static void infoBox(String infoMsg) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setContentText(infoMsg);
		alert.show();
	}
	
	public static void fileIO(File f, FileIO func) {
		try {
			if (f == null) {
				errorBox("No file selected");
			} else {
				func.accessFile(f);
			}
		} catch (IOException ioe) {
			errorBox(ioe.getMessage());
		}
	}
}

package edu.hendrix.cluster.color.gui;

public class findLines {
	
	private WrappedBlobList blobs;
	
	public findLines(WrappedBlobList b) {
		this.blobs = b;
	}
	
	public void lineSize() {
		for (int i = 0; i < blobs.size(); i++) {
			blobs.get(i).setHeight();
		}
	}

}

package edu.hendrix.cluster.color.gui;

import java.util.ArrayList;
import java.util.List;

import edu.hendrix.blob.Blob;

public class findLines {
	
	private WrappedBlobList blobs;
	private  List<Blob> lines;
	private int imagew;
	private int imageh;
	
	public findLines(int imagew, int imageh, WrappedBlobList b) {
		this.blobs = b;
		this.imagew = imagew;
		this.imageh = imageh;
		this.lines = new ArrayList<Blob>();
		initializeLines();
	}
	
	public void initializeLines() {
		for (int i = 0; i < blobs.size(); i++) {
			Blob b = blobs.get(i);
			b.setHW();
			if (((double)(b.getWidth()/imagew) < 0.06) || (double)(b.getHeight()/imageh) < 0.06) {
				System.out.println("Width : " + b.getWidth()+ "   Height : " + b.getHeight());
				lines.add(b);
			}
		}
	}
	
	public int getLineNum() {
		return lines.size();
	}
	

}

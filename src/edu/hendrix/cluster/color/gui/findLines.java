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
			if (straightCheck(b)) {
				System.out.println("Width : " + b.getWidth()+ "   Height : " + b.getHeight());
				lines.add(b);
			}else if (concaveCheck(b)) {
				lines.add(b);
			}
		}
	}
	
	public int getLineNum() {
		return lines.size();
	}
	
	public boolean straightCheck(Blob b) {
		return (b.getWidth()*1.0/imagew < 0.05) || (b.getHeight()*1.0/imageh < 0.05);
	}
	
	public boolean concaveCheck(Blob b) {
		int x1 = b.getMinX();
		int y1 = b.getMinY();
		int x2 = b.getMaxX();
		int y2 = b.getMaxY();
		double H = b.getHeight();
		double W = b.getWidth();
		int n = 4;
		Boolean[][] grid = new Boolean[n+1][n+1];
		int falseNum = 0;
		for (double i = 0; i < n; i++) {
			for (double j = 0; j < n; j++) {
				double curX = x1+(W*(i/n));
				double nextX = (x1)+(W*(((i+1)/n)));
				double curY = y1+(H*(j/n));
				double nextY = y1+(H*((j+1)/n));
				grid[(int)j][(int)i] = isEmpty(curX, nextX, curY, nextY, b);
			}
		}
		printGraph(grid, n);
		/*for (int x = 0; x < n; x++) {
			for (int y = 0; y < n; y++) {
				if (grid[x][y] == false) {
					falseNum++;
				}
			}
		}*/
		return falseNum >= 6;
		
		
	}
	
	public void printGraph(Boolean[][] graph, int num) {
		for (int x = 0; x < num; x++) {
			for (int y = 0; y < num; y ++) {
			
				System.out.print(graph[x][y]+ " ");
			}
			System.out.print("\n");
		}
	}
	
	//Instead of checking if every pixel in the box is part of the blob, 
	//this will only check to see if when cut in the middle both ways it runs across a blob
	public boolean isEmpty(double x1, double x2, double y1, double y2, Blob b) {
		double halfX = (x1 + ((x2 - x1)/2));
		double halfY = (y1 + ((y2 - y1)/2));
		//cut up and down, false if crosses blob
		for (double i = y1; i <= y2; i ++) {
			if (blobs.getBlob((int)halfX, (int)i) == b) {
				return true;
			}
		}
		//cut left and right and sees if it crosses blob
		for (double j = x1; j <= x2; j ++) {
			if (blobs.getBlob((int)j, (int)halfY) == b) {
				return true;
			}
		}
		return false;
	}
	

}

package edu.hendrix.cluster.color.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.hendrix.blob.Blob;

public class FindShapes {
	
	private WrappedBlobList blobs;

	private HashMap<Blob, HashMap<String, Double>> blobTypes;
	private int imagew;
	private int imageh;
	private int n;
	
	public FindShapes(int imagew, int imageh, WrappedBlobList b) {
		this.blobs = b;
		this.imagew = imagew;
		this.imageh = imageh;
		//n is the matrix dimensions : nxn
		this.n = 6;
		this.blobTypes = new HashMap<Blob, HashMap<String, Double>>();
		initializeShapes();
	}
	
	public HashMap<Blob, HashMap<String, Double>> getTypes() {
		return blobTypes;
	}
	
	public void initializeShapes() {
		HashMap<String, Double> percentages = new HashMap<String, Double>();
		for (int i = 0; i < blobs.size(); i++) {
			Blob b = blobs.get(i);
			b.setHW();
			Boolean[][] grid = BlobToMatrix(b);
			//Compare whether the blob is a line and store the value
			double line = gridComparison(Shapes.LINE.getLine(), grid);
			percentages.put("line", line);
			//compare whether the blob is a square and store the value
			double square = gridComparison(Shapes.SQUARE.getSquare(), grid);
			percentages.put("square", square);
			//compare whether the blob is a triangle and store the value
			double triangle = gridComparison(Shapes.TRIANGLE.getTriangle(), grid);
			percentages.put("triangle", triangle);
			//compare whether the blob is a circle and store the value
			double circle = gridComparison(Shapes.CIRClE.getCircle(), grid);
			percentages.put("circle", circle);
			//Check to see if it is a line without matrix
			if (straightCheck(b)) {
				//this means it is absolutely a line, or 1/1
				//override previous value
				percentages.put("line", 1.0);
			}
			blobTypes.put(b, percentages);
		}
	}
	
	public double gridComparison(Boolean[][] expected, Boolean[][] given) {
		//initialize the number of squares different
		int dif = 0;
		//Go through and find the number of squares different
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (expected[i][j] != given[i][j]) {
					dif++;
				}
			}
		}
		//What is the chi-square statistic?
		double chi = 1/((dif)+1);
		return chi;
		
	}
	
	public boolean straightCheck(Blob b) {
		return (b.getWidth()*1.0/imagew < 0.05) || (b.getHeight()*1.0/imageh < 0.05);
	}
	
	//Takes a blob and return a 6x6 matrix version of it
	public Boolean[][] BlobToMatrix(Blob b) {
		int x1 = b.getMinX();
		int y1 = b.getMinY();
		int x2 = b.getMaxX();
		int y2 = b.getMaxY();
		double H = b.getHeight();
		double W = b.getWidth();
		Boolean[][] grid = new Boolean[n+1][n+1];
		int falseNum = 0;
		int trueNum = 0;
		for (double i = 0; i < n; i++) {
			for (double j = 0; j < n; j++) {
				double curX = x1+(W*(i/n));
				double nextX = (x1)+(W*(((i+1)/n)));
				double curY = y1+(H*(j/n));
				double nextY = y1+(H*((j+1)/n));
				grid[(int)j][(int)i] = isEmpty(curX, nextX, curY, nextY, b);
			}
		}
		return grid;
		
	}
	
	public void printGraph(Boolean[][] graph, int num) {
		for (int x = 0; x < num; x++) {
			for (int y = 0; y < num; y ++) {
			
				System.out.print(graph[x][y]+ " ");
			}
			System.out.print("\n");
		}
		System.out.println("---------------------");
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

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
	private HashMap<String, Integer> numOfShapes;
	
	public FindShapes(int imagew, int imageh, WrappedBlobList b) {
		this.blobs = b;
		this.imagew = imagew;
		this.imageh = imageh;
		HashMap<String, Integer> shapes = new HashMap<String, Integer>();
		shapes.put("line", 0);
		shapes.put("square", 0);
		shapes.put("triangle", 0);
		shapes.put("circle", 0);
		this.numOfShapes = shapes;
		//n is the matrix dimensions : n*n
		this.n = 6;
		this.blobTypes = new HashMap<Blob, HashMap<String, Double>>();
		initializeShapes();
	}
	
	public HashMap<Blob, HashMap<String, Double>> getTypes() {
		return blobTypes;
	}
	
	public HashMap<String, Integer> getNumOfShapes() {
		return numOfShapes;
	}
	
	//This will compare shapes to their grids and then call countShapes
	public void initializeShapes() {
		for (int i = 0; i < blobs.size(); i++) {
			HashMap<String, Double> percentages = new HashMap<String, Double>();
			Blob b = blobs.get(i);
			b.setHW();
			Boolean[][] grid = BlobToMatrix(b, i);
			//Go through the shapes enum and compare + store value
			//DID YOU REALLY WRITE THIS BY HAND INSTEAD OF USING A GODDAMN LOOP ~~~~~~~~~~~~~~~~~~~~~~~~~~
			for (Shapes shape : Shapes.values()) {
				double val = gridComparison(shape.getGrid(), grid);
				percentages.put(shape.getName(), val);
			}
			//Check to see if it is a line without matrix
			if (straightCheck(b)) {
				//this means it is absolutely a line, or 1/1
				//override previous value
				percentages.put("line", 1.0);
			}
			blobTypes.put(b, percentages);
		}
		System.out.println(blobTypes);
		countShapes();
	}
	
	//Parse through the established map and counts how many of each type of blob
	//The "counting" counts a shape as something that it is closest to by its chi value
	public void countShapes() {
		//fetch the hashmap inside the hashmap corresponding to that blob
		for (Blob b : blobTypes.keySet()) {
			//hashmap of chi values for that blob
			HashMap<String, Double> percent = blobTypes.get(b);
			//when we find the shape this blob is most like, 
			//we want to declare it that shape
			String gShape = "";
			double greatest = 0;
			//iterate through the hashmap of chi values
			for (String k : percent.keySet()) {
				//DID YOU REALLY WRITE THIS BY HAND INSTEAD OF USING A GODDAMN LOOP ~~~~~~~~~~~~~~~~~~~~~~~~~~
				for (Shapes shape : Shapes.values()) {
					if (k == shape.getName() && Double.compare(percent.get(k), greatest) == 1) {
						gShape = shape.getName();
						greatest = percent.get(k);
					}
				}
			}
			//outside of the for loop, determine which shape we should increment
			int old = numOfShapes.get(gShape);
			int newVal = old+1;
			numOfShapes.put(gShape, newVal);
		}
		System.out.println(numOfShapes);
	}
	
	//Compares two grids and returns a chi value
	public double gridComparison(Boolean[][] expected, Boolean[][] given) {
		//initialize the number of squares different
		double dif = 0;
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
	
	//This just compares the height and width to see if it is a line
	public boolean straightCheck(Blob b) {
		return (b.getWidth()*1.0/imagew < 0.05) || (b.getHeight()*1.0/imageh < 0.05);
	}
	
	//Takes a blob and return a 6x6 matrix version of it
	public Boolean[][] BlobToMatrix(Blob b, int bi) {
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
				grid[(int)j][(int)i] = isEmpty(curX, nextX, curY, nextY, b, bi);
			}
		}
		return grid;
		
	}
	
	public void printGraph(Boolean[][] graph) {
		for (int x = 0; x < n; x++) {
			for (int y = 0; y < n; y ++) {
			
				System.out.print(graph[x][y]+ " ");
			}
			System.out.print("\n");
		}
		System.out.println("---------------------");
	}
	
	//Instead of checking if every pixel in the box is part of the blob, 
	//this will only check to see if when cut in the middle both ways it runs across a blob
	public boolean isEmpty(double x1, double x2, double y1, double y2, Blob b, int bi) {
		double halfX = (x1 + ((x2 - x1)/2));
		double halfY = (y1 + ((y2 - y1)/2));
		//cut up and down, false if crosses blob
		for (double i = y1; i <= y2; i ++) {
			if (blobs.inBlob((int)halfX, (int)i, bi)) {
				return true;
			}
		}
		//cut left and right and sees if it crosses blob
		for (double j = x1; j <= x2; j ++) {
			if (blobs.inBlob((int)j, (int)halfY, bi)) {
				return true;
			}
		}
		return false;
	}
	
}

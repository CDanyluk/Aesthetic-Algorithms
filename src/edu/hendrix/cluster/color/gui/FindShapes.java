package edu.hendrix.cluster.color.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import edu.hendrix.blob.Blob;

public class FindShapes {
	
	private WrappedBlobList blobs;

	//TAYLOR: blobTypes hashmap of a hashmap contains what you are looking for
	//It it stored {Blob b, {line = 0.1, circle, = 0.2, square = 0.3, triangle = 0.4}}
	//You can iterate over shapes to go through the hashmaps inside it, and use the getter function to obtain it
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
		//Enum loop again ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		for (Shapes shape : Shapes.values()) {
			shapes.put(shape.getName(), 0);
		}
		this.numOfShapes = shapes;
		//n is the matrix dimensions : n*n
		this.n = 6;
		this.blobTypes = new HashMap<Blob, HashMap<String, Double>>();
		initializeShapes();
	}
	
	//Blobs with their chi values??
	public HashMap<Blob, HashMap<String, Double>> getTypes() {
		return blobTypes;
	}
	
	//How many shapes there are based on the chi values
	public HashMap<String, Integer> getNumOfShapes() {
		return numOfShapes;
	}
	
	//This will compare shapes to their grids and then call countShapes
	public void initializeShapes() {
		for (int i = 0; i < blobs.size(); i++) {
			HashMap<String, Double> percentages = new HashMap<String, Double>();
			Blob b = blobs.get(i);
			b.setHW();
			//DO NOT CHECK TINY BLOBS
			if (b.getSize() > 100) {
				Boolean[][] grid = BlobToMatrix(b, i);
				//Go through the shapes enum and compare + store value
				for (Shapes shape : Shapes.values()) {
					if (shape.getName() == "line") {
						double val = lineCheck(grid);
						percentages.put(shape.getName(), val);
					}else {
						double val = twistCheck(shape, grid);
						percentages.put(shape.getName(), val);
					}
				}
				//Check to see if it is a line without matrix or fancy county things
				if (straightCheck(b)) {
					//this means it is absolutely a line, or 1/1
					//override previous value
					percentages.put("line", 1.0);
				}
				blobTypes.put(b, percentages);
			}
		}
		System.out.println(blobTypes);
		countShapes();
	}
	
	public double lineCheck(Boolean[][] g) {
		int falseNum = 0;
		int trueNum = 0;
		for (int x = 0; x < n; x++) {
			for (int y = 0; y < n; y++) {
				if (g[x][y] == false) {
					falseNum++;
				}else {
					trueNum++;
				}
			}
		}
		
		if (falseNum >= 20 && falseNum <= 30) {
			return 1.0;
		}else if (falseNum < 20) {
			double dif = 20.0 - falseNum;
			double chi = 1/((dif)+1);
			return chi;
		}else {
			double dif = falseNum - 30.0;
			double chi = 1/((dif)+1);
			return chi;
		}
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
				//Go through all enums and compare which one is greatest
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
		//System.out.println(numOfShapes);
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
	
	//Brand new twisting function
	public double twistCheck(Shapes s, Boolean[][] given) {
		//max stores the greatest value of comparison
		double max = 0.0;
		for (int i = 1; i <=8; i++) {
			//get the twisted shape
			Boolean[][] twist = s.getGrid(i);
			//Compare the twisted shape to the given grid
			max = Math.max(max, gridComparison(twist, given));
		}
		return max;
	}
	
	public Boolean[][] subMatrix(Boolean[][] grid) {
		int left = Integer.MAX_VALUE;
		int right = -1;
		int top = Integer.MAX_VALUE;
		int bottom = -1;
		//Find the max and min values, ot the square surrounding the twisted shape
		for (int y = 0; y < n+3; y++) {
			for (int x = 0; x < n+3; x++) {
				if (grid[x][y]) {
					if (left > x) {
						left = x;
					}if (right < x) {
						right = x;
					}if (top > y) {
						top = y;
					}if (bottom < y) {
						bottom = y;
					}
							
				}
			}
		}
		// Cut it so that there is no extra space on the side of the twisted shape
		if ((top - bottom) > 6) {
			top--;
		}
		if ((right - left) > 6) {
			right--;
		}
		Boolean[][] sub = new Boolean[n][n];
		//Go through the large list
		for (int y = bottom; y <= top; y++) {
			int Y = 0;
			for (int x = left; x <= right; x++) {
			int X = 0;	
			if (grid[x][y]) {
				sub[X][Y] = true;
			}
			X++;
			}
			Y++;
		}
		return sub;
		
	}
	
	//This just compares the height and width to see if it is a line
	public boolean straightCheck(Blob b) {
		if ((b.getWidth()*1.0/imagew < 0.05) && (b.getHeight() >= b.getWidth()*3)) {
			return true;
		}else if ((b.getHeight()*1.0/imageh < 0.05) && (b.getWidth() >= b.getHeight()*3)) {
			return true;
		}else {
			return false;
		}
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
				if (graph[x][y]) {
					System.out.print("@");
				} else {
					System.out.print(".");
				}
				//System.out.print(graph[x][y]+ " ");
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

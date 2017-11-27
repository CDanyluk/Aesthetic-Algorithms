package edu.hendrix.blob;

import java.util.*;

public class BlobList {
	private TreeMap<Integer,Blob> blobs;
	private int[][] blobNums;
	
	public BlobList(int width, int height) {
		blobs = new TreeMap<Integer,Blob>();
		blobNums = new int[width][height];
	}
	
	public void addPoint(int x, int y, int label) {
		if (!blobs.containsKey(label)) {
			blobs.put(label, new Blob());
		}
		
		blobs.get(label).addPoint(x, y);
		blobNums[x][y] = label;
	}
	
	public int getLabelOf(int x, int y) {
		return blobNums[x][y];
	}
	
	public Blob getBlobFor(int x, int y) {
		return blobs.get(getLabelOf(x, y));
	}
	
	public TreeSet<Integer> getLabelNums() {
		return new TreeSet<Integer>(blobs.keySet());
	}
	
	public Blob getBlobFor(int label) {
		return blobs.get(label);
	}
}

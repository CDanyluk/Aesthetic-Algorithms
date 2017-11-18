package edu.hendrix.cluster.color.gui;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import edu.hendrix.blob.Blob;
import edu.hendrix.blob.BlobLabeler;
import edu.hendrix.blob.BlobList;
import edu.hendrix.cluster.Clusterer;
import edu.hendrix.cluster.color.ColorCluster;

public class WrappedBlobList {
	private BlobList blobs;
	private int current;
	private ArrayList<Integer> labels;
	//private TreeMap<Integer,Integer> labelToIndex;
	
	public WrappedBlobList(BufferedImage img, Clusterer<ColorCluster> clusterer) {
		blobs = BlobLabeler.blobLabel(img, clusterer);
		current = 0;
		labels = new ArrayList<>(blobs.getLabelNums());
		/*labelToIndex = new TreeMap<>();
		for (int i = 0; i < labels.size(); i++) {
			labelToIndex.put(labels.get(i), i);
		}*/
	}
	
	public void next() {
		current = (current + 1) % labels.size();
	}
	
	public void prev() {
		current = (current - 1 + labels.size()) % labels.size();
	}
	
	public int current() {
		return current;
	}
	
	public int size() {
		return labels.size();
	}
	
	public Blob get(int i) {
		return blobs.getBlobFor(labels.get(i));
	}
	
	public boolean inCurrentBlob(int x, int y) {
		return blobs.getLabelOf(x, y) == labels.get(current);
	}
}

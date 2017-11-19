package edu.hendrix.cluster.color.gui;

import java.util.HashMap;
import java.util.Map;

import edu.hendrix.blob.Blob;

public class Fitness {
	
	private WrappedBlobList blobs;

	Map<String, Integer> blobTypes = new HashMap<String, Integer>();
	Map<String, Integer> goodValues = new HashMap<String, Integer>();
	
	public Fitness(WrappedBlobList b) {
		this.blobs = b;
		goodValues.put("big", 2);
		goodValues.put("medium", 11);
		goodValues.put("small", 21);
		goodValues.put("tiny", 500);
		
	}
	
	
	public int getBigBlobs() { return blobTypes.get("big"); }
	public int getMediumBlobs() { return blobTypes.get("medium"); }
	public int getSmallBlobs() { return blobTypes.get("small"); }
	public int getTinyBlobs() { return blobTypes.get("tiny"); }
	
	public void findSizeBlobs() {
		int big = 0;
		int medium = 0;
		int small = 0;
		int tiny = 0;
		for (int i = 0; i < blobs.size(); i++) {
			Blob blob = blobs.get(i);
			int howBig = blob.getSize();
			if (howBig >= 25000) {
				big++;
			}else if (howBig < 25000 && howBig >= 1000) {
				medium++;
			}else if (howBig < 1000 && howBig>= 100) {
				small++;
			}else {
				tiny++;
			}
		}
		blobTypes.put("big", big);
		blobTypes.put("medium", medium);
		blobTypes.put("small", small);
		blobTypes.put("tiny", tiny);
	}
	
	public Map<String, Integer> percentageChange() {
		Map<String, Integer> percentages = new HashMap<String, Integer>();
		for (String key : blobTypes.keySet()) {
			int type = blobTypes.get(key);
			int good = goodValues.get(key);
			if (type > good) {
				int dif = type - good;
				int per = (dif / good) * 100;
				percentages.put(key, per);
			}else {
				int dif = good - type;
				int per = (dif / good) * 100;
				percentages.put(key, per);
			}
		}
		return percentages;
	}


}

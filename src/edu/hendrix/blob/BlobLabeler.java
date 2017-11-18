package edu.hendrix.blob;

import java.awt.image.BufferedImage;

import edu.hendrix.cluster.Clusterer;
import edu.hendrix.cluster.color.ColorCluster;

public class BlobLabeler {
	
	private static class FirstPassStats {
		private int width, height;
		private DisjointSets equivs;
		private int[][] clusters;
		private int[][] labels;

		public FirstPassStats(int width, int height) {
			this.width = width;
			this.height = height;
			equivs = new DisjointSets();
			labels = new int[width][height];
			clusters = new int[width][height];
			equivs.makeSet();
		}
		
		public void procPixel(BufferedImage pixels, Clusterer<ColorCluster> clusterer, int x, int y) {
			ColorCluster pixel = new ColorCluster(pixels.getRGB(x, y));
			clusters[x][y] = clusterer.getClosestMatchFor(pixel);
			boolean up = y > 0 && clusters[x][y] == clusters[x][y-1];
			boolean left = x > 0 && clusters[x][y] == clusters[x-1][y];

			if (up) {
				labels[x][y] = labels[x][y-1];
			} else if (left) {
				labels[x][y] = labels[x-1][y];
			} else {
				labels[x][y] = equivs.makeSet();
			}

			if (up && left && labels[x][y-1] != labels[x-1][y]) {
				equivs.union(labels[x][y-1], labels[x-1][y]);
			}
		}
		
		public BlobList makeBlobs() {
			BlobList result = new BlobList(width, height);
			for (int x = 0; x < width; ++x) {
				for (int y = 0; y < height; ++y) {
					result.addPoint(x, y, equivs.find(labels[x][y]));
				}
			}
			return result;
		}
	}
	
	public static BlobList blobLabel(BufferedImage pixels, Clusterer<ColorCluster> clusters) {
		FirstPassStats fps = new FirstPassStats(pixels.getWidth(), pixels.getHeight());
		for (int x = 0; x < pixels.getWidth(); ++x) {
			for (int y = 0; y < pixels.getHeight(); ++y) {
				fps.procPixel(pixels, clusters, x, y);
			}
		}
		return fps.makeBlobs();
	}
}

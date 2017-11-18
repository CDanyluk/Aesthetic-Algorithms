 package edu.hendrix.cluster;

public interface Clusterable<T> {
	public T weightedCentroidWith(T other, long thisCount, long otherCount);
	
	public double distance(T other);
	
	public static double weightedMean(double a, double b, long aCount, long bCount) {
		return (a * aCount + b * bCount) / (aCount + bCount);
	}
	
	public static <T extends Clusterable<T>> T clusterOf(Iterable<T> candidates) {
		int count = 0;
		T result = null;
		for (T candidate: candidates) {
			if (result == null) {
				result = candidate;
				count = 1;
			} else {
				result = result.weightedCentroidWith(candidate, count, 1);
				count += 1;
			}
		}
		return result;
	}
}

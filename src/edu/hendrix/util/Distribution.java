package edu.hendrix.util;

public class Distribution {
	private double[] dist;
	
	public Distribution(double... weights) {
		double probSum = 0.0;
		for (double p: weights) {probSum += p;}
		
		dist = new double[weights.length - 1];
		double totalSoFar = 0.0;
		for (int i = 0; i < dist.length; i++) {
			totalSoFar += weights[i] / probSum;
			dist[i] = totalSoFar;
		}
	}
	
	public Distribution(String src) {
		String[] values = src.split(";");
		dist = new double[values.length - 1];
		for (int i = 0; i < dist.length; i++) {
			dist[i] = Double.parseDouble(values[i]);
		}
	}
	
	public static double normal(double x, double mu, double sigma) {
		return Math.exp(-0.5 * Math.pow(x - mu, 2) / Math.pow(sigma, 2)) / sigma * 2.0 * Math.PI;
	}
	
	public static double normalMidpointIndex(int numElements) {
		return (numElements - 1) / 2.0;
	}
	
	public static Distribution makeNormal(int numElements, double mu, double sigma) {
		double[] probs = new double[numElements];
		for (int i = 0; i < probs.length; i++) {
			probs[i] = normal(i - normalMidpointIndex(probs.length), mu, sigma);
		}
		return new Distribution(probs);
	}
	
	public int numOutcomes() {return dist.length + 1;}
	
	public int pick() {
		double value = Math.random();
		for (int i = 0; i < dist.length; i++) {
			if (value < dist[i]) {
				return i;
			}
		}
		return dist.length;
	}
	
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < dist.length; i++) {
			result.append(Double.toString(dist[i]));
			result.append(';');
		}
		result.append(Double.toString(1.0));
		return result.toString();
	}
	
	@Override
	public int hashCode() {return toString().hashCode();}
	
	@Override
	public boolean equals(Object other) {
		return toString().equals(other.toString());
	}
}

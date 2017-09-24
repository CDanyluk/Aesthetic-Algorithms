import java.util.HashSet;
import java.util.Set;

public class LSystem {
	
	private double length;
	private Set<double[]> tree;
	
	public LSystem(double seed, double[] start) {
		this.length = seed;
		this.tree = new HashSet<double[]>();
		tree.add(e)
		double[] left = calculateLeft(start);
		double[] right = calculateRight(start);
		
		
	}
	
	public void reset(double seed) {
		this.length = seed;
	}
	
	public void maketree() {
		if (distance(start, left) > 10 ) {
			drawLine(start, left);
			drawTree(left);
		}else if (distance(start, right) > 10) {
			drawLine(start, right);
			drawTree(right);
		}
		else {
			return;
		}
	}
	
	public double distance(double[] first, double[] second) {
		double dist = Math.sqrt(Math.pow((first[0] - second[0]), 2) + Math.pow((first[1]) - second[1], 2) );
		return dist;
	}
	
	public double[] root(double[] start) {
		double[] stem = new double[2];
		stem[0] = start[0];
		stem[1] = start[1] + length;
		return stem;
	}
	
	public double[] calculateRight(double[] start) {
		double[] coordinates = new double[2];
		//new array here and then return new one
		coordinates[0] = start[0] + 0.5*length;
		coordinates[1] = start[1] - 0.5*length;
		length = 0.6*length;
		return coordinates;
	}
	
	public double getlength() {
		return length;
	}
	
	public double[] calculateLeft(double[] start) {
		double[] coordinates = new double[2];
		//new array here and then return new one
		coordinates[0] = start[0] - 0.5*length;
		coordinates[1] = start[1] - 0.5*length;
		length = 0.6*length;
		return coordinates;
	}
	

}

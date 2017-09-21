
public class LSystem {
	
	private double length;
	
	public LSystem(double seed) {
		this.length = seed;
	}
	
	public void reset(double seed) {
		this.length = seed;
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

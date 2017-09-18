
public class LSystem {
	
	private double x;
	private double y;
	
	public LSystem(double[] seed) {
		this.x = seed[0];
		this.y = seed[1];
	}
	
	public void reset(double[] seed) {
		this.x = seed[0];
		this.y = seed[1];
	}
	
	public double[] start() {
		double[] coordinates = new double[2];
		this.x = x + 100;
		coordinates[0] = x;
		coordinates[1] = y;
		return coordinates;
	}
	
	public double[] calculateRight(double[] start) {
		double[] coordinates = new double[2];
		//new array here and then return new one
		coordinates[0] = start[0] + start[0]/2;
		coordinates[1] = start[1] - start[1]/2;
		return coordinates;
	}
	
	public double[] calculateLeft(double[] start) {
		double[] coordinates = new double[2];
		//new array here and then return new one
		coordinates[0] = start[0] - start[0]/2;
		coordinates[1] = start[1] - start[1]/2;
		return coordinates;
	}
	

}

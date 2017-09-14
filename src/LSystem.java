
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
		start[0] = x + x/2;
		start[1] = y + y/2;
		return start;
	}
	
	public double[] calculateLeft(double[] start) {
		start[0] = x - x/2;
		start[1] = y - y/2;
		return start;
	}
	

}

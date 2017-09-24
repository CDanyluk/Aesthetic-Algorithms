import javafx.animation.AnimationTimer;

public class Line {
	//stores two double[] points
	//fuckyeah abstraction
	
	private double[] first;
	private double[] last;

	
	public Line(double[] first, double[] last) {
		this.first = first;
		this.last = last;
	}
	
	public double[] getFirst() {
		return first;
	}
	
	public double[] getLast() {
		return last;
	}
	
	public double getLength() {
		double dist = Math.sqrt(Math.pow((first[0] - last[0]), 2) + Math.pow((first[1]) - last[1], 2) );
		return dist;
	}
	
	
}

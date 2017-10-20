import java.util.HashSet;
import java.util.Set;

public class LSystemsRedo {
	
	private double length;
	private Set<Line> tree;
	private double trimmer;
	private double angle;
	private Turtle turtle;
	
	public LSystemsRedo(double[] startPoint, double startLength, double trimmer, double angle) {
		this.length = startLength;
		this.tree = new HashSet<Line>();
		this.trimmer = trimmer;
		this.angle = angle;
		this.turtle = new Turtle(startPoint[0], startPoint[1], -angle);
		
		turtle.goForward(startPoint[1] - length);
		double[] newPoint = new double[2];
		newPoint[0] = turtle.x;
		newPoint[1] = turtle.y;
		Line stem = new Line(startPoint, newPoint);
		tree.add(stem);
		length = length*trimmer;
		maketree(newPoint, length);
		
	}
	
	public void reset(double seed) {
		this.length = seed;
	}
	
	public double getlength() {
		return length;
	}
	
	public Set<Line> getTree() {
		return tree;
	}
	
	public void maketree(double[] newSprout, double branchLength) {
		Line rightBranch = calculateBranch(newSprout, branchLength);
		tree.add(rightBranch);
		if (rightBranch.getLength() > 5 ) {
			double newBranch = branchLength*trimmer;
			maketree(rightBranch.getLast(), newBranch);
		} else{
			return;
		}
		
	}
	
	public Line calculateBranch(double[] start, double branchLength) {
		turtle.turnLeft(-angle);
		//System.out.println(turtle.angle);
		//System.out.println(start[0] + "," + start[1]);
		length = length/2;
		turtle.goForward(length/2);
		double[] coordinates = new double[2];
		
		// Don't mess with plus and minus! They are actually correct! :-)
		coordinates[0] = turtle.x;
		coordinates[1] = turtle.y;
		Line branch = new Line(start, coordinates);
		return branch;
	}
	
	
}
import java.util.HashSet;
import java.util.Set;

public class LSystem {
	
	private double length;
	private Set<Line> tree;
	private double trimmer;
	private double angle;
	private Turtle turtle;
	
	public LSystem(double[] startPoint, double startLength, double trimmer, double angle) {
		//initialize length and tree, plus trimmer to be how much to cut a branch by each time
		this.length = startLength;
		this.tree = new HashSet<Line>();
		this.trimmer = trimmer;
		this.angle = angle;
		this.turtle = new Turtle(startPoint[0], startPoint[1], angle);
		
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
		//first set length to be 1/2
		//Now calculate the two branches to be added to the tree
		Line rightBranch = calculateRight(newSprout, branchLength);
		Line leftBranch = calculateLeft(newSprout, branchLength);
		tree.add(rightBranch);
		tree.add(leftBranch);
		//base case for recursive argument
		if (rightBranch.getLength() > 5 ) {
			//trim the branch
			double newBranch = branchLength*trimmer;
			maketree(rightBranch.getLast(), newBranch);
		}
		if (leftBranch.getLength() > 5) {
			//trim the branch
			double newBranch = branchLength*trimmer;
			maketree(leftBranch.getLast(), newBranch);
		}
		//Too short then return
		return;
		
	}
	
	public Line calculateRight(double[] start, double branchLength) {
		double[] coordinates = new double[2];
		//Make a new point that is different than the old point
		double over = (branchLength * Math.cos((180 - angle))) /2;
		//System.out.println("Right: " + over);
		double up = (branchLength/2) + over;
		//System.out.println("Right: " + up);
		
		// Don't mess with plus and minus! They are actually correct! :-)
		coordinates[0] = start[0] + over;
		coordinates[1] = start[1] - up;
		Line branch = new Line(start, coordinates);
		return branch;
	}
	
	public Line calculateLeft(double[] start, double branchLength) {
		double[] coordinates = new double[2];
		//Make a new point that is different than the old point
		double over = (branchLength * Math.cos((180 - (2 * (180 - angle)))/ 2)) /2;
		//System.out.println("Left: " + over);
		double up = (branchLength/2) - over;
		//System.out.println("Left: " + up);
		// Don't mess with plus and minus! They are actually correct! :-)
		coordinates[0] = start[0] - over;
		coordinates[1] = start[1] - up;
		Line branch = new Line(start, coordinates);
		return branch;
	}
	

}

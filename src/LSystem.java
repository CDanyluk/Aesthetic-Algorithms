import java.util.HashSet;
import java.util.Set;

public class LSystem {
	
	private double length;
	private Set<Line> tree;
	
	public LSystem(double startLength, double[] startPoint) {
		//initialize length and tree
		this.length = startLength;
		this.tree = new HashSet<Line>();
		//Create first line or stem, which is just startPoint[y] - length
		double[] endStem = new double[2];
		endStem[0] = startPoint[0];
		endStem[1] = startPoint[1] - length;
		Line stem = new Line(startPoint, endStem);
		//Add this line to the tree
		tree.add(stem);
		//Call make tree to begin recursive process
		maketree(endStem);
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
	
	public void maketree(double[] newSprout) {
		//first set length to be 1/2
		length = 0.5*length;
		//Now calculate the two branches to be added to the tree
		Line rightBranch = calculateRight(newSprout);
		Line leftBranch = calculateLeft(newSprout);
		//base case for recursive argument
		if (rightBranch.getLength() > 5 ) {
			maketree(rightBranch.getLast());
		}
		if (leftBranch.getLength() > 5) {
			maketree(leftBranch.getLast());
		}
		//Too short then return
		return;
		
	}
	
	public Line calculateRight(double[] start) {
		double[] coordinates = new double[2];
		//Make a new point that is different than the old point
		//Here be the math Taylor is working on
		coordinates[0] = start[0] + length;
		coordinates[1] = start[1] - length;
		Line branch = new Line(start, coordinates);
		return branch;
	}
	
	public Line calculateLeft(double[] start) {
		double[] coordinates = new double[2];
		//Make a new point that is different than the old point
		//here be the math Taylor is working on
		coordinates[0] = start[0] - length;
		coordinates[1] = start[1] - length;
		Line branch = new Line(start, coordinates);
		return branch;
	}
	

}

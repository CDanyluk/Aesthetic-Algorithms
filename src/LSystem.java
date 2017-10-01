import java.util.HashSet;
import java.util.Set;

public class LSystem {
	
	private double length;
	private Set<Line> tree;
	private double trimmer;
	private double angle;
	
	public LSystem(double[] startPoint, double startLength, double trimmer, double angle) {
		//initialize length and tree, plus trimmer to be how much to cut a branch by each time
		this.length = startLength;
		this.tree = new HashSet<Line>();
		this.trimmer = trimmer;
		this.angle = angle;
		
		//Create first line or stem, which is just startPoint[y] - length
		double[] endStem = new double[2];
		endStem[0] = startPoint[0];
		endStem[1] = startPoint[1] - length;
		Line stem = new Line(startPoint, endStem);
		//Add this line to the tree
		tree.add(stem);
		//Trim length and call make tree to begin recursive process
		length = length*trimmer;
		maketree(endStem, length);
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
		//Now add them to the goddamn tree how did you miss this part
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
		//Here be the math Taylor is working on
		double over = (branchLength * Math.cos((180 - (2 * angle))/ 2)) /2;
		double up = (branchLength/2) + over;
		
		
		coordinates[0] = start[0] + up;
		coordinates[1] = start[1] - over;
		Line branch = new Line(start, coordinates);
		return branch;
	}
	
	public Line calculateLeft(double[] start, double branchLength) {
		double[] coordinates = new double[2];
		//Make a new point that is different than the old point
		//here be the math Taylor is working on
		double over = (branchLength * Math.cos((180 - (2 * angle))/ 2)) /2;
		double up = (branchLength/2) - over;
		
		coordinates[0] = start[0] - up;
		coordinates[1] = start[1] - over;
		Line branch = new Line(start, coordinates);
		return branch;
	}
	

}

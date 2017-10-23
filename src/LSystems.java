import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class LSystems {
	
	//TODO: implement this in future iterations
	//private String[] constants;
	private String axiom; //start
	private HashMap<String, String> rules;
	private Turtle turtle;
	private Stack<Turtle> toDo;
	private double[] startPoint;
	private Set<Line> treeDrawing;
	private int length;
	private int angle;
	
	public LSystems(double[] startPoint, 
			String axiom,
			HashMap<String, String> rules,
			int recursions,
			int length,
			int angle){
		this.startPoint = startPoint;
		this.rules = rules;
		this.turtle = new Turtle(startPoint[0], startPoint[1]);
		this.toDo = new Stack<Turtle>();
		this.treeDrawing = new HashSet<Line>();
		this.length = length;
		this.angle = angle;
		//drawTree("1111[11[1[0]0]1[0]0]11[1[0]0]1[0]0");
		drawTree(recurseTree(axiom, recursions));
	}

	public Set<Line> getTree() {
		return treeDrawing;
	}
	
	private String recurseTree(String tree, int recursions){
		if(recursions == 0){
			System.out.println("Final Tree: " + tree);
			return tree;
		} else{
			String treeNew = "";
			for(int i = 0; i < tree.length(); i++){
				if(tree.substring(i, i+1).equals("0")){
					System.out.println("replaced a zero: " + treeNew);
					treeNew += rules.get("0");
					//tree = tree.substring(0,i) + rules.get("0") + tree.substring(i+1);
					//tree += rules.get("0");
				} else if(tree.substring(i, i+1).equals("1")){
					//tree = tree.substring(0,i) + rules.get("1") + tree.substring(i+1);
					
					System.out.println("replaced a zero: " + treeNew);
					treeNew += rules.get("1");
					//tree += rules.get("1");
				} else {
					treeNew += tree.substring(i, i+1);
				}
			}
			return recurseTree(treeNew, recursions - 1);
		}
	}
	
	private void drawTree(String tree){
		/* if 0 ---> draw line segment ending in leaf
		 * if 1 ---> draw line segment 
		 * if [ ---> push angle left
		 * if ] ---> pop angle right
		 * 
		 */
		
		// Stack of turtles
		
		for(int i = 0; i < tree.length(); i++){
			if(tree.substring(i, i +1).equals("0")){
				double[] start = new double[2];
				start[0] = turtle.x;
				start[1] = turtle.y;
				
				turtle.goForward(length);
				double[] end = new double[2];
				end[0] = turtle.x;
				end[1] = turtle.y;
				treeDrawing.add(new Line(start, end));
			} if(tree.substring(i, i +1).equals("1")){
				double[] start = new double[2];
				start[0] = turtle.x;
				start[1] = turtle.y;
				
				turtle.goForward(length);
				double[] end = new double[2];
				end[0] = turtle.x;
				end[1] = turtle.y;
				treeDrawing.add(new Line(start, end));
			} if(tree.substring(i, i +1).equals("[")){
				double[] start = new double[2];
				start[0] = turtle.x;
				start[1] = turtle.y;
				
				toDo.push(new Turtle(turtle.x, turtle.y));				
				toDo.peek().turnLeft(angle);
				toDo.peek().goForward(length);
				double[] end = new double[2];
				end[0] = toDo.peek().x;
				end[1] = toDo.peek().y;
				treeDrawing.add(new Line(start, end));
			} if(tree.substring(i, i +1).equals("]")){
				double[] start = new double[2];
				start[0] = turtle.x;
				start[1] = turtle.y;
				
				toDo.pop();
				turtle.turnLeft(-angle);
				turtle.goForward(length);
				double[] end = new double[2];
				end[0] = turtle.x;
				end[1] = turtle.y;
				treeDrawing.add(new Line(start, end));
			} 
		}
	}
	
	
	
	
}
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class LSystems {
	
	//TODO: implement this in future iterations
	//private String[] constants;
	private String axiom; //start
	private HashMap<String, String> rules;
	private Stack<Turtle> toDo;
	private double[] startPoint;
	private Set<Line> drawing;
	private double length;
	private int angle;
	
	public LSystems(double[] startPoint, 
			String axiom,
			HashMap<String, String> rules,
			int recursions,
			double length,
			int angle){
		this.startPoint = startPoint;
		this.rules = rules;
		this.toDo = new Stack<Turtle>();
		this.drawing = new HashSet<Line>();
		this.length = length;
		this.angle = angle;
		//drawTree("1111[11[1[0]0]1[0]0]11[1[0]0]1[0]0");
		//drawTree(new Turtle(startPoint[0], startPoint[1], 270), recurseTree(axiom, recursions));
		//drawTriangle(new Turtle(startPoint[0], startPoint[1], 0), recurseTriangle(axiom, recursions));
		//drawDragon(new Turtle(startPoint[0], startPoint[1], 0), recurseDragon(axiom, recursions));
		drawKoch(new Turtle(startPoint[0], startPoint[1], 0), recurseKoch(axiom, recursions));
	}


	public Set<Line> getTree() {
		return drawing;
	}
	
	private String recurseTree(String tree, int recursions){
		if(recursions == 0){
			System.out.println("Final Tree: " + tree);
			return tree;
		} else{
			String treeNew = "";
			for(int i = 0; i < tree.length(); i++){
				if(tree.substring(i, i+1).equals("0")){
					treeNew += rules.get("0");
				} else if(tree.substring(i, i+1).equals("1")){
					treeNew += rules.get("1");
				} else {
					treeNew += tree.substring(i, i+1);
				}
			}
			return recurseTree(treeNew, recursions - 1);
		}
	}
	
	private void drawTree(Turtle first, String tree){
		toDo.push(first);
		for(int i = 0; i < tree.length(); i++){
			if(tree.substring(i, i +1).equals("0")){
				double[] start = new double[2];
				start[0] = toDo.peek().x;
				start[1] = toDo.peek().y;
				
				toDo.peek().goForward(length);
				double[] end = new double[2];
				end[0] = toDo.peek().x;
				end[1] = toDo.peek().y;
				drawing.add(new Line(start, end));
			} if(tree.substring(i, i +1).equals("1")){
				double[] start = new double[2];
				start[0] = toDo.peek().x;
				start[1] = toDo.peek().y;
				
				toDo.peek().goForward(length);
				double[] end = new double[2];
				end[0] = toDo.peek().x;
				end[1] = toDo.peek().y;
				drawing.add(new Line(start, end));
			} if(tree.substring(i, i +1).equals("[")){
				double[] start = new double[2];
				start[0] = toDo.peek().x;
				start[1] = toDo.peek().y;
				
				toDo.push(new Turtle(toDo.peek().x, toDo.peek().y, toDo.peek().angle));				
				toDo.peek().turnLeft(angle);
				toDo.peek().goForward(length);
				double[] end = new double[2];
				end[0] = toDo.peek().x;
				end[1] = toDo.peek().y;
				drawing.add(new Line(start, end));
			} if(tree.substring(i, i +1).equals("]")){
				double[] start = new double[2];
				start[0] = toDo.peek().x;
				start[1] = toDo.peek().y;
				
				toDo.pop();
				toDo.peek().turnLeft(-angle);
				toDo.peek().goForward(length);
				double[] end = new double[2];
				end[0] = toDo.peek().x;
				end[1] = toDo.peek().y;
			} 
		}
		
	}
	
	private String recurseTriangle(String triangle, int recursions) {
		if(recursions == 0){
			System.out.println("Final product: " + triangle);
			return triangle;
		} else{
			String triangleNew = "";
			for(int i = 0; i < triangle.length(); i++){
				if(triangle.substring(i, i+1).equals("A")){
					//System.out.println("Replaced an F: " + triangle);
					triangleNew += rules.get("A");
				} else if(triangle.substring(i, i+1).equals("B")){
					triangleNew += rules.get("B");
				} else {
					triangleNew += triangle.substring(i, i+1);
				}
			}
			return recurseTriangle(triangleNew, recursions - 1);
		}
	}
	
	private void drawTriangle(Turtle first, String triangle) {
		toDo.push(first);
		//Here, F and G both mean "draw forward", + means "turn left by angle", and − means "turn right by angle".
		for(int i = 0; i < triangle.length(); i++) {
			if(triangle.substring(i, i+1).equals("A") || triangle.substring(i, i+1).equals("B")) {
				
				double[] start = new double[2];
				start[0] = toDo.peek().x;
				start[1] = toDo.peek().y;
				
				toDo.peek().goForward(length);
				double[] end = new double[2];
				end[0] = toDo.peek().x;
				end[1] = toDo.peek().y;
				drawing.add(new Line(start, end));
				//System.out.println(start[0] + ", " + start[1] + " : " + end[0] + ", " + end[1] );
			} else if(triangle.substring(i, i+1).equals("+")) {
				toDo.peek().turnLeft(angle);
			} else if(triangle.substring(i, i+1).equals("-")) {
				toDo.peek().turnLeft(-angle);
			}
		}
		
	}
	
	private String recurseDragon(String dragon, int recursions){
		if(recursions == 0){
			System.out.println("Final product: " + dragon);
			return dragon;
		} else{
			String dragonNew = "";
			for(int i = 0; i < dragon.length(); i++){
				if(dragon.substring(i, i+1).equals("X")){
					dragonNew += rules.get("X");
				} else if(dragon.substring(i, i+1).equals("Y")){
					dragonNew += rules.get("Y");
				} else {
					dragonNew += dragon.substring(i, i+1);
				}
			}
			return recurseDragon(dragonNew, recursions - 1);
		}
	}
	
	private void drawDragon(Turtle first, String dragon) {
		toDo.push(first);
		//Here, F and G both mean "draw forward", + means "turn left by angle", and − means "turn right by angle".
		for(int i = 0; i < dragon.length(); i++) {
			if(dragon.substring(i, i+1).equals("F")) {
				
				double[] start = new double[2];
				start[0] = toDo.peek().x;
				start[1] = toDo.peek().y;
				
				toDo.peek().goForward(length);
				double[] end = new double[2];
				end[0] = toDo.peek().x;
				end[1] = toDo.peek().y;
				drawing.add(new Line(start, end));
				//System.out.println(start[0] + ", " + start[1] + " : " + end[0] + ", " + end[1] );
			} else if(dragon.substring(i, i+1).equals("-")) {
				toDo.peek().turnLeft(angle);
			} else if(dragon.substring(i, i+1).equals("+")) {
				toDo.peek().turnLeft(-angle);
			}
		}
		
	}
	
	private String recurseKoch(String koch, int recursions){
		if(recursions == 0){
			System.out.println("Final product: " + koch);
			return koch;
		} else{
			String kochNew = "";
			for(int i = 0; i < koch.length(); i++){
				if(koch.substring(i, i+1).equals("F")){
					kochNew += rules.get("F");
				} else {
					kochNew += koch.substring(i, i+1);
				}
			}
			return recurseKoch(kochNew, recursions - 1);
		}
	}
	
	private void drawKoch(Turtle first, String koch) {
		toDo.push(first);
		//Here, F and G both mean "draw forward", + means "turn left by angle", and − means "turn right by angle".
		for(int i = 0; i < koch.length(); i++) {
			if(koch.substring(i, i+1).equals("F")) {
				
				double[] start = new double[2];
				start[0] = toDo.peek().x;
				start[1] = toDo.peek().y;
				
				toDo.peek().goForward(length);
				double[] end = new double[2];
				end[0] = toDo.peek().x;
				end[1] = toDo.peek().y;
				drawing.add(new Line(start, end));
				//System.out.println(start[0] + ", " + start[1] + " : " + end[0] + ", " + end[1] );
			} else if(koch.substring(i, i+1).equals("+")) {
				toDo.peek().turnLeft(angle);
			} else if(koch.substring(i, i+1).equals("-")) {
				toDo.peek().turnLeft(-angle);
			}
		}
		
	}
	
	
}
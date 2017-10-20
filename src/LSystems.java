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
	private Stack<String> toDo;
	private double[] startPoint;
	private int recursions;
	
	public LSystems(double[] startPoint, 
			String axiom,
			HashMap<String, String> rules,
			int recursions){
		this.startPoint = startPoint;
		this.rules = rules;
		this.turtle = new Turtle(startPoint[0], startPoint[1]);
		this.toDo = new Stack();
		this.recursions = recursions;
		String tree = axiom;
		
		recurseTree(tree);
		drawTree(recurseTree(tree));
	}

	
	private String recurseTree(String tree){
		if(recursions == 0){
			return tree;
		} else{
			String newTree = "";
			for(int i = 0; i < tree.length(); i++){
				if(tree.substring(i, i+1) == "0"){
					newTree += rules.get("0");
				} else if(tree.substring(i, i+1) == "1"){
					newTree += rules.get("1");
				}
			}
			recursions -= 1;
			return recurseTree(newTree);
		}
	}
	
	private void drawTree(String tree){
		
	}
	
	
	
	
}
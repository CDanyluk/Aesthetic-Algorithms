import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class LSystems {
	
	private String axiom; //start
	private HashMap<String, String> rules;
	private Stack<Turtle> toDo;
	private double[] startPoint;
	private ArrayList<Line> drawing;
	private double length;
	private int angle;
	private boolean randomize;
	
	public LSystems(double[] startPoint, 
			String axiom,
			HashMap<String, String> rules,
			boolean randomize,
			int recursions,
			double length,
			int angle){
		this.startPoint = startPoint;
		this.rules = rules;
		this.toDo = new Stack<Turtle>();
		this.drawing = new ArrayList<Line>();
		this.length = length;
		this.angle = angle;
		this.randomize = randomize;
		draw(new Turtle(startPoint[0], startPoint[1], angle), recurse(axiom, recursions, rules), randomize);

	}


	public ArrayList<Line> getDrawing() {
		return drawing;
	}
	
	private String recurse(String product, int recursions, HashMap<String, String> rules ){
		if(recursions == 0){
			return product;
		} else{
			String productNew = "";
			for(int i = 0; i < product.length(); i++){
				String index = product.substring(i, i+1);
				if(rules.containsKey(index)){
					productNew += rules.get(index);
				} else {
					productNew += product.substring(i, i+1);
				}
			}
			return recurse(productNew, recursions - 1, rules);
		}
	}
	
	private void draw(Turtle first, String toDraw, boolean randomize){
		toDo.push(first);
		for(int i = 0; i < toDraw.length(); i++){
			String letter = toDraw.substring(i, i +1);
			
			double[] start = new double[2];
			start[0] = toDo.peek().x;
			start[1] = toDo.peek().y;
			
			if(letter.equals("0")){
				toDo.peek().goForward(length);
				double[] end = new double[2];
				end[0] = toDo.peek().x;
				end[1] = toDo.peek().y;
				drawing.add(new Line(start, end));
			} if(letter.equals("1")){
				toDo.peek().goForward(length);
				double[] end = new double[2];
				end[0] = toDo.peek().x;
				end[1] = toDo.peek().y;
				if(toDraw.substring(i+1, i+2).equals("1")){
					toDo.peek().goForward(length);
					end[0] = toDo.peek().x;
					end[1] = toDo.peek().y;
					i+=1;
				}
				drawing.add(new Line(start, end));
			} if(letter.equals("[")){toDo.push(new Turtle(toDo.peek().x, toDo.peek().y, toDo.peek().angle));	
				if(randomize){
					int randomNum = ThreadLocalRandom.current().nextInt(5, 100);
					toDo.peek().turnLeft(randomNum);
				} else {
					toDo.peek().turnLeft(angle);
				}
				toDo.peek().goForward(length);
				double[] end = new double[2];
				end[0] = toDo.peek().x;
				end[1] = toDo.peek().y;
				drawing.add(new Line(start, end));
			} if(letter.equals("]")){
				toDo.pop();
				toDo.peek().turnLeft(-angle);
				toDo.peek().goForward(length);
				double[] end = new double[2];
				end[0] = toDo.peek().x;
				end[1] = toDo.peek().y;
			} else if(letter.equals("+")) {
				toDo.peek().turnLeft(angle);
			} else if(letter.equals("-")) {
				toDo.peek().turnLeft(-angle);
			}
		}
		
	}
	
	
	
}
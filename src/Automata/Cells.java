package Automata;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javafx.scene.paint.Color;

public class Cells {
	
	//Since our grid is 610x460 and we have a pixels of 10x10
	public double[][] graph;
	public boolean[][] seedgraph;
	private int w;
	private int h;
	private int iteration;
	private CellPattern pattern;
	
	public Cells(int width, int height) {
		this.w = (width/10)+1;
		this.h = (height/10)+1;
		this.iteration = -1;
		graph = new double[w][h];
		seedgraph = new boolean[w][h];
		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y ++) {
				graph[x][y] = 0;
			}
		}
	pattern = new CellPattern(w,h);
	}
	
	public void setDead(Map<Integer, Boolean> deadmap) { pattern.setDead(deadmap); }
	
	public void setAlive(Map<Integer, Boolean> alivemap) { pattern.setAlive(alivemap); }
	
	public void setColor(HashMap<Integer, Color> colors) { pattern.setColors(colors); }
	
	public void setIterations(int iter) { this.iteration = iter; }
	
	public void setSeeds(boolean[][] graph) {
		for (int x = 0; x < graph.length; x++) {
			for (int y = 0; y < graph.length; y ++) {
				if (graph[x][y] == true) {
					liveColor(x, y);
					seedgraph[x][y] = true;
				}
			}
		}
	}
	
	public double[][] getGraph() { return graph; }
	
	public int getWidth() { return w; }
	
	public int getHeight() { return h; }
	
	public boolean[][] getSeeds() { return seedgraph; }
	
	public void clear() {
		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y ++) {
				graph[x][y] = 0;
			}
		}
	}
	
	public void centerOf() {
		liveColor(w/2, h/2);
	}
	
	public String getSeedData() {
		boolean[][] g = seedgraph;
		String seeds = new String();
		//go through x
		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y ++) {
				//if the value at that point is true
				if (g[x][y] == true) {
					//append them to a string
					String num = new String();
					//Add 0's to make them three digits long (009008)
					//Check if x value needs 0's
					if (x < 10) {
						num += "00" + x;
					}else if (x < 100) {
						num += "0" + x;
					}else {
						num += "" + x;
					}
					//check if y value needs 0's
					if (y < 10) {
						num += "00" + y + "%";
					}else if (y < 100) {
						num += "0" + y + "%";
					}else {
						num += y + "%";
					}
					seeds += num;
					
				}
			}
		}
		return seeds;
	}
	
	//THIS HERE BITCH SHOULD RETURN THE RULESET USED FOR ALL THIS JAZZZ
	public String getRuleset() {
		String rules = new String();
		rules = rules + "Dead: " + pattern.getDead() + "\n";
		rules = rules + "Alive: " + pattern.getAlive() + "\n";
		HashMap<Integer, Color> colorsRaw = pattern.getColorMap();
		//System.out.println("MainController/Cells/getRuleset()/colorsRaw: " + colorsRaw);
		HashMap<Integer, String> colors = new HashMap<Integer, String>();
		//System.out.println("MainController/Cells/getRuleset()/colorsRaw: " + colors);
		for (int i = 0; i < colorsRaw.size(); i++) {
			Color c = colorsRaw.get(i);
			int red = (int)c.getRed();
			int green = (int)c.getGreen();
			int blue = (int)c.getBlue();
			String color = red + green + blue + "";
			colors.put(i, color);
		}
		rules = rules + "Color: " + colors + "\n";
		rules = rules + "Iterations: " + iteration + "\n";
		return rules;
	}
	
	public Map<Integer, Boolean> getDead() {
		return pattern.getDead();
	}
	
	public String getDeadData() {
		String rules = new String();
		Map<Integer, Boolean> dead = pattern.getDead();
		for (Integer key : dead.keySet()) {
			if (dead.get(key) == true) {
				rules += 'T';
			}else {
				rules += 'F';
			}
		}
		return rules;
	}
	
	public Map<Integer, Boolean> getAlive() {
		return pattern.getAlive();
	}
	
	public String getAliveData() {
		String rules = new String();
		Map<Integer, Boolean> alive = pattern.getAlive();
		for (Integer key : alive.keySet()) {
			if (alive.get(key) == true) {
				rules += 'T';
			}else {
				rules += 'F';
			}
		}
		return rules;
	}
	
	public String getColors() {
		String rules = new String();
		HashMap<Integer, Color> colorsRaw = pattern.getColorMap();
		HashMap<Integer, String> colors = new HashMap<Integer, String>();
		for (int i = 0; i < colorsRaw.size(); i++) {
			Color c = colorsRaw.get(i);
			int r = (int)Math.round(255 * c.getRed());
			int g = (int)Math.round(255 * c.getGreen());
			int b = (int)Math.round(255 * c.getBlue());
			String hex = String.format("#%02x%02x%02x", r, g, b); 
			colors.put(i, hex);
		}
		rules = rules + "Color: " + colors + "\n";
		return rules;
	}
	
	public HashMap<Integer, Color> getColorMap() {
		return pattern.getColorMap();
	}
	
	public String getColorsData() {
		String rules = new String();
		HashMap<Integer, Color> colorsRaw = pattern.getColorMap();
		//System.out.println("colorsRaw = " + colorsRaw);
		for (int i = 0; i < colorsRaw.size(); i++) {
			Color c = colorsRaw.get(i);
			//System.out.println("Colors c = " + c);
			//System.out.println("int i = " + i);
			int r = (int)Math.round(255 * c.getRed());
			int g = (int)Math.round(255 * c.getGreen());
			int b = (int)Math.round(255 * c.getBlue());
			String hex = String.format("#%02x%02x%02x", r, g, b); 
			rules += hex;
		}
		return rules;
	}
	
	public String getIterations() {
		String rules = new String();
		rules = rules + "Iterations: " + iteration + "\n";
		return rules;
	}
	
	public int getIter() {
		return iteration;
	}
	
	public String getIterData() {
		return iteration+"";
	}
	
	//luck is randomized and it will have 1/luck chance of coming alive
	public void randomGraph() {
		int luck = (int)Math.floor( Math.random() * 100 );
		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y ++) {
				int change = (int)Math.floor( Math.random() * luck );
				if (change == 1) {
					liveColor(x, y);
				}
			}
		}
	}
	
	
	
	//chance is set by the user, and it will have 1/chance of coming alive
	public void randomGraph(int chance) {
		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y ++) {
				int change = (int)Math.floor( Math.random() * chance );
				if (change == 1) {
					liveColor(x, y);
				}
			}
		}
	}
	
	
	public void iterate(int num, Map<Integer, Boolean> alive, Map<Integer, Boolean> dead) {
		if (num == 0) {
			ruleset(alive, dead);
		}else {
			this.iteration = num;
			pattern.randomDead();
			pattern.randomAlive();
			for (int i = 0; i < num; i++) {
				predictable(alive, dead);
			}
		}
	}
	
	public void update(int num){
		this.iteration = num;
		graph = pattern.multipleAutomata(num, graph);
	}
	
	
	
	public Map<Integer, Boolean> randomRules() {
		Map<Integer, Boolean> rules = new HashMap<Integer, Boolean>();
		for (int i = 0; i < 9; i++) {
			boolean on = (Math.random() < 0.5);
			rules.put(i, on);
		}
		return rules;
	}
	
	//Randomizes 3 values in dead and alive
	//Randomizes 1 color each iteration
	public void change(Map<Integer, Boolean> alive, Map<Integer, Boolean> dead) {
		pattern.randomValDead(2);
		pattern.randomValAlive(2);
		graph = pattern.colorAutomata(graph);
		pattern.randomAColor(2);
	}
	
	public void predictable(Map<Integer, Boolean> alive, Map<Integer, Boolean> dead) {
		graph = pattern.colorAutomata(graph);
	}
	
	
	//Plays by the rules set by the user
	public void ruleset(Map<Integer, Boolean> alive, Map<Integer, Boolean> dead) {
		pattern.setAlive(alive);
		pattern.setDead(dead);
		graph = pattern.colorAutomata(graph);
	}
	
	
	
	public Color getColor(int i) {
		return pattern.getColorMap().get(i);
	}
	
	public void printGraph(double[][] graph) {
		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y ++) {
			
				System.out.print(graph[x][y]+ " ");
			}
			System.out.print("\n");
		}
	}
	
	
	public void liveColor(int x, int y) {
		int max = pattern.getColorSize();
		int newx = x;
		int newy = y;
		graph[newx][newy] = max;
		seedgraph[newx][newy] = true;
	}
	

}

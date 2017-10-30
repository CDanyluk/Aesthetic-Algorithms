import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javafx.scene.paint.Color;

public class Cells {
	
	//Since our grid is 610x460 and we have a pixels of 10x10
	public double[][] graph;
	private int w;
	private int h;
	private CellPattern pattern;
	
	public Cells(int width, int height) {
		this.w = (width/10)+1;
		this.h = (height/10)+1;
		graph = new double[w][h];
		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y ++) {
				graph[x][y] = 0;
			}
		}
	pattern = new CellPattern(w,h);
	}
	
	public double[][] getGraph() {
		return graph;
	}
	
	public int getWidth() {
		return w;
	}
	
	public int getHeight() {
		return h;
	}
	
	public void clear() {
		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y ++) {
				graph[x][y] = 0;
			}
		}
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
		for (int i = 0; i < num; i++) {
			change(alive, dead);
		}
	}
	
	//Randomizes 3 values in dead and alive
	//Randomizes 1 color each iteration
	public void change(Map<Integer, Boolean> alive, Map<Integer, Boolean> dead) {
		pattern.randomValDead(3);
		pattern.randomValAlive(3);
		graph = pattern.colorAutomata(graph);
		pattern.randomAColor(1);
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
		int newx = (int) x;
		int newy = (int) y;
		graph[newx][newy] = max;
	}
	

}

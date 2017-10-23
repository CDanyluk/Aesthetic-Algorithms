import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javafx.scene.paint.Color;

public class Cells {
	
	//Since our grid is 610x460 and we have a pixels of 10x10
	double[][] graph = new double[64][49];
	private CellPattern pattern;
	
	public Cells() {
		for (int x = 0; x < 64; x++) {
			for (int y = 0; y < 49; y ++) {
				graph[x][y] = 0;
			}
		}
	pattern = new CellPattern();
	}
	
	public double[][] getGraph() {
		return graph;
	}
	
	public void clear() {
		for (int x = 0; x < 63; x++) {
			for (int y = 0; y < 48; y ++) {
				graph[x][y] = 0;
			}
		}
	}
	
	
	public void iterate(int num, Map<Integer, Boolean> alive, Map<Integer, Boolean> dead) {
		for (int i = 0; i < num; i++) {
		//	generalLifeandDeath(alive, dead);
		}
	}
	
	public void change(Map<Integer, Boolean> alive, Map<Integer, Boolean> dead) {
		//pattern.setAlive(alive);
		//pattern.setDead(dead);
		pattern.randomValDead(3);
		pattern.randomValAlive(3);
		graph = pattern.colorAutomata(graph);
		//pattern.randomAColor();
		System.out.println("Dead : " + pattern.getDead());
		System.out.println("Alive " + pattern.getAlive());
	}
	
	public Color getColor(int i) {
		return pattern.getColorMap().get(i);
	}
	
	public void printGraph(double[][] graph) {
		for (int x = 0; x < 63; x++) {
			for (int y = 0; y < 48; y ++) {
			
				System.out.print(graph[x][y]+ " ");
			}
			System.out.print("\n");
		}
	}
	
	public void live(int x, int y) {
		graph[x][y] = 1;
	}
	
	public void liveColor(double x, double y) {
		int max = pattern.getColorSize();
		int newx = (int) x;
		int newy = (int) y;
		graph[newx][newy] = max;
	}
	
	public void live(double x, double y) {
		int newx = (int) x;
		int newy = (int) y;
		graph[newx][newy] = 1;
	}

	public void dead(int x, int y) {
		graph[x][y] = 0;
	}
}

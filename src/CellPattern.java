import java.util.HashMap;
import java.util.Map;

import javafx.scene.paint.Color;

public class CellPattern {
	
	private Map<Integer, Boolean> deadMap;
	private Map<Integer, Boolean> aliveMap;
	private HashMap<Integer, Color> colorMap;
	
	//We initialize them to be empty, but can set them later on
	public CellPattern() {
		Map<Integer, Boolean> alive = new HashMap<Integer, Boolean>();
		Map<Integer, Boolean> dead = new HashMap<Integer, Boolean>();
		for (int i = 0; i < 9; i++) {
			alive.put(i, false);
			dead.put(i, false);
		}
		this.aliveMap = alive;
		this.deadMap = dead;
		 HashMap<Integer, Color> rainbow = new HashMap<Integer, Color>();
		rainbow.put(1, Color.BLACK);
		rainbow.put(0, Color.WHITE);
		this.colorMap = rainbow;
		
		/*rainbow = new HashMap<Integer, Color>();
		Color four = Color.rgb(216, 4, 1);
		Color three = Color.rgb(254, 194, 59);
		Color two = Color.rgb(78, 160, 114);
		Color one = Color.rgb(68, 145, 203);
		rainbow.put(4, four);
		rainbow.put(3, three);
		rainbow.put(2, two);
		rainbow.put(1, one);*/
	}

	
	public void setDead(Map<Integer, Boolean> dead) {
		this.deadMap = dead;
	}
	
	public void setAlive(Map<Integer, Boolean> alive) {
		this.aliveMap = alive;
	}
	
	public Map<Integer, Boolean> getDead() {
		return deadMap;
	}
	
	public Map<Integer, Boolean> getAlive() {
		return aliveMap;
	}
	
	public int getColorSize() {
		return colorMap.size()-1;
	}
	
	public HashMap<Integer, Color> getColorMap() {
		return colorMap;
	}
	
	public double[][] colorAutomata(double[][] graph) {
		int max = colorMap.size()-1;
		//New graph for next iteration
		double[][] population = makeNewPop();
		
			//Iterate through all x,y on map
			for (int x = 1; x < 63; x++) {
				for (int y = 1; y < 48; y ++) {
					//Create int neighbors to keep track of x, y's neighbors
					int neighbors = 0;
					neighbors = centerColorPoint(x, y, neighbors, max, graph);
					//IF HIGHEST COLOR	
					if (graph[x][y] == max) {
						for (int a = 0; a < aliveMap.size(); a++) {
							//If the key says true, and neighbors == key, then set the cell to max
							if (aliveMap.get(a) == true) {
								if (neighbors == a) {
									population[x][y] = max;
									break;
								}
							}else if (aliveMap.get(a) == false) {
								if (neighbors == a) {
									population[x][y] = graph[x][y] - 1;
									break;
								}
							}
						}
					}else if (graph[x][y] < max && graph[x][y] != 0) {
						for (int d = 0; d < deadMap.size(); d++) {
							if (deadMap.get(d) == true) {
								if (neighbors == d) {
									population[x][y] = max;
									break;
								}
							}else if (deadMap.get(d) == false) {
								if (neighbors == d) {
								population[x][y] = graph[x][y] - 1;
								break;
								}
							}
						}
					}else if (graph[x][y] == 0){
						for (int d = 0; d < deadMap.size(); d++) {
							if (deadMap.get(d) == true) {
								if (neighbors == d) {
									population[x][y] = max;
									break;
								}
							}else if (deadMap.get(d) == false) {
								if (neighbors == d) {
								population[x][y] = 0;
								break;
								}
							}
						}
					}
				}
			}
		return population;
	}
	
	public double[][] makeNewPop() {
		double[][] population = new double[64][49];
		//initialize it to empty
		for (int x = 0; x < 63; x++) {
			for (int y = 0; y < 48; y ++) {
				population[x][y] = 0;
			}
		}
		return population;
	}
	
	public int centerColorPoint(int x, int y, int neighbors, int max, double[][] graph) {
		if (graph[x-1][y-1] == max) {
			neighbors++;
		}if (graph[x][y-1] == max) {
			neighbors++;
		}if (graph[x+1][y-1] == max) {
			neighbors++;
		}if (graph[x-1][y] == max) {
			neighbors++;
		}if (graph[x+1][y] == max) {
			neighbors++;
		}if (graph[x-1][y+1] == max) {
			neighbors++;
		}if(graph[x][y+1] == max ) {
			neighbors++;
		}if(graph[x+1][y+1] == max) {
			neighbors++;
		}
			return neighbors;
	}

}

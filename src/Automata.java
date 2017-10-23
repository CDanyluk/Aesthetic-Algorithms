import java.util.HashMap;
import java.util.Map;

import javafx.scene.paint.Color;

public class Automata {

	//We initialize it to be nothing, nothing is needed
	public Automata() {

	}
	
	public double[][] auto(double[][] graph, Map<Integer, Boolean> deadMap, Map<Integer, Boolean> aliveMap, int max) {
		//New graph for next iteration
		double[][] population = makeNewPop();
		
			//Iterate through all x,y on map
			//With a buffer of 1 so no edge cases
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
		int up = y-1;
		int down = y+1;
		int left = x-1;
		int right = x+1;
		if (up < 0) {
			up = 48;
		}
		if (down > 48) {
			down = 0;
		}
		if (left < 0) {
			left = 63;
		}
		if (right > 63) {
			right = 0;
		}
		
		if (graph[left][up] == max) {
			neighbors++;
		}if (graph[x][up] == max) {
			neighbors++;
		}if (graph[right][up] == max) {
			neighbors++;
		}if (graph[left][y] == max) {
			neighbors++;
		}if (graph[right][y] == max) {
			neighbors++;
		}if (graph[left][down] == max) {
			neighbors++;
		}if(graph[x][down] == max ) {
			neighbors++;
		}if(graph[right][down] == max) {
			neighbors++;
		}
			return neighbors;
	}

}

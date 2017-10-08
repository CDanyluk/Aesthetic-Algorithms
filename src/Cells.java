import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Cells {
	
	//Since our grid is 610x460 and we have a pixels of 10x10
	double[][] graph = new double[64][49];
	
	public Cells() {
		for (int x = 0; x < 63; x++) {
			for (int y = 0; y < 48; y ++) {
				graph[x][y] = 0;
			}
		}
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
			generalLifeandDeath(alive, dead);
		}
	}
	
	public void generalLifeandDeath(Map<Integer, Boolean> alive, Map<Integer, Boolean> dead) {
		
			//New graph for next iteration
			double[][] population = makeNewPop();
			
				//Iterate through all x,y on map
				for (int x = 1; x < 63; x++) {
					for (int y = 1; y < 48; y ++) {
						//Create int neighbors to keep track of x, y's neighbors
						int neighbors = 0;
						neighbors = centerPoint(x, y, neighbors);
						//IF x,y is BLACK
						if (graph[x][y] == 1) {
							population = checkAliveCells(neighbors, x, y, population, alive);
						//IF x,y is WHITE
						}else if (graph[x][y] == 0) {
							population = checkDeadCells(neighbors, x, y, population, dead);
						//ELSE error
						}else {
							graph[x][y] = 2;
						}
					}
				}
			graph = population;
	}
	
	
	//Does not adjust values, values always 0??
	public void colorAutomata(Map<Integer, Boolean> alive, Map<Integer, Boolean> dead, int max) {
		//New graph for next iteration
		double[][] population = makeNewPop();
		
			//Iterate through all x,y on map
			for (int x = 1; x < 63; x++) {
				for (int y = 1; y < 48; y ++) {
					//Create int neighbors to keep track of x, y's neighbors
					int neighbors = 0;
					neighbors = centerPoint(x, y, neighbors);
					//IF HIGHEST COLOR	
					if (graph[x][y] == max) {
						for (int a = 0; a < alive.size(); a++) {
							//If the key says true, and neighbors == key, then set the cell to max
							if (alive.get(a) == true) {
								if (neighbors == a) {
									population[x][y] = max;
									break;
								}
							}else if (alive.get(a) == false) {
								if (neighbors == a) {
									population[x][y] = graph[x][y] - 1;
									break;
								}
							}
						}
					}else if (graph[x][y] < max && graph[x][y] != 0) {
						for (int d = 0; d < dead.size(); d++) {
							if (dead.get(d) == true) {
								if (neighbors == d) {
									population[x][y] = max;
									break;
								}
							}else if (dead.get(d) == false) {
								if (neighbors == d) {
								population[x][y] = graph[x][y] - 1;
								break;
								}
							}
						}
					}else if (graph[x][y] == 0){
						for (int d = 0; d < dead.size(); d++) {
							if (dead.get(d) == true) {
								if (neighbors == d) {
									population[x][y] = max;
									break;
								}
							}else if (dead.get(d) == false) {
								if (neighbors == d) {
								population[x][y] = 0;
								break;
								}
							}
						}
					}
				}
			}
			
		graph = population;
		/*System.out.println("graph in cells colorAutomata: -----------------------------");
		printGraph(graph);
		System.out.println("popuation printed in cells colorAutomata: -------------------------");
		printGraph(population);*/
	}
	
	public void printGraph(double[][] graph) {
		for (int x = 0; x < 63; x++) {
			for (int y = 0; y < 48; y ++) {
			
				System.out.print(graph[x][y]+ " ");
			}
			System.out.print("\n");
		}
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
	
	public double[][] checkAliveCells(int neighbors, int x, int y, double[][] population, Map<Integer, Boolean> alive) {
		//Go through the alive list
		for (int a = 0; a < 9; a++) {
			//If the key says true, and neighbors == key, then set the cell to black
			if (alive.get(a) == true) {
				if (neighbors == a) {
					//Set the cell to black
					population[x][y] = 1;
					break;
				}
			}else if (alive.get(a) == false) {
				if (neighbors == a) {
					population[x][y] = 0;
					break;
				}
			}
		}
		return population;
	}
	
	public double[][] checkDeadCells(int neighbors, int x, int y, double[][] population, Map<Integer, Boolean> dead) {
		//Go through the dead list
		for (int d = 0; d < 9; d++) {
			//if the map says true for that value
			//and the value of neighbors is that value
			if (dead.get(d) == true) {
				if (neighbors == d) {
					//Set the cell to black
					population[x][y] = 1;
					break;
				}
			}else if (dead.get(d) == false) {
				if (neighbors == d) {
					population[x][y] = 0;
					break;
				}
			}
		}
		return population;
	}
	
	public int centerPoint(int x, int y, int neighbors) {
		if (graph[x-1][y-1] == 1) {
			neighbors++;
		}if (graph[x][y-1] == 1) {
			neighbors++;
		}if (graph[x+1][y-1] == 1) {
			neighbors++;
		}if (graph[x-1][y] == 1) {
			neighbors++;
		}if (graph[x+1][y] == 1) {
			neighbors++;
		}if (graph[x-1][y+1] == 1) {
			neighbors++;
		}if(graph[x][y+1] == 1 ) {
			neighbors++;
		}if(graph[x+1][y+1] == 1) {
			neighbors++;
		}
			return neighbors;
	}
	
	public void live(int x, int y) {
		graph[x][y] = 1;
	}
	
	public void liveColor(double x, double y, int max) {
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

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
	
	public double[][] get() {
		return graph;
	}
	
	public void clear() {
		for (int x = 0; x < 63; x++) {
			for (int y = 0; y < 48; y ++) {
				graph[x][y] = 0;
			}
		}
	}
	
	public void iterate(int num) {
		for (int i = 0; i < num; i++) {
			conway();
		}
	}
	
	public void generalLifeandDeath(Map<Integer, Boolean> alive, Map<Integer, Boolean> dead) {
		//New graph for next iteration
				double[][] population = new double[64][49];
				//initialize it to empty
				for (int x = 0; x < 63; x++) {
					for (int y = 0; y < 48; y ++) {
						population[x][y] = 0;
					}
				}
				//Iterate through all x,y on map
				for (int x = 1; x < 63; x++) {
					for (int y = 1; y < 48; y ++) {
						//Create int neighbors to keep track of x, y's neighbors
						int neighbors = 0;
						neighbors = centerPoint(x, y, neighbors);
						//IF x,y is BLACK
						if (graph[x][y] == 1) {
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
						//IF x,y is WHITE
						}else if (graph[x][y] == 0) {
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
						//ELSE error
						}else {
							graph[x][y] = 2;
						}
					}
				}
			graph = population;
	}
	
	
	
	//Conways static game of life, this is generalized above ------------------------------------
	public void conway() {
		//New graph for next iteration
		double[][] population = new double[64][49];
		//initialize it to empty
		for (int x = 0; x < 61; x++) {
			for (int y = 0; y < 46; y ++) {
				population[x][y] = 0;
			}
		}
		//Iterate through all x,y on map
		for (int x = 0; x < 63; x++) {
			for (int y = 0; y < 48; y ++) {
				//Create int neighbors to keep track of x, y's neighbors
				int neighbors = 0;
				//IF NOT ON AN EDGE
				if (x > 0 && y > 0 && x < 63 && y < 48) {
					neighbors = centerPoint(x, y, neighbors);
				}
				//IF ON TOP EDGE
				else if (x > 0 && y == 0) {
					neighbors = topPoint(x, y, neighbors);
				//IF ON LEFT SIDE
				}else if (y > 0 && x == 0) {
					neighbors = leftPoint(x, y, neighbors);
				//IF FIRST CORNER
				}else if(y == 0 && x == 0) {
					neighbors = cornerPoint(x, y, neighbors);
				}
				//if x,y is black and neighbors > 3 turn white
				if (graph[x][y] == 1) {
					if (neighbors <= 3 && neighbors > 0) {
						//live(x,y);
						population[x][y] = 1;
					}else {
						//dead(x,y);
						population[x][y] = 0;
					}
				//if x,y is white and neighbors are 4 or 5
				}else if (graph[x][y] == 0) {
					if (neighbors == 4) {
						//live(x,y);
						population[x][y] = 1;
					}else if (neighbors == 5) {
						//live(x,y);
						population[x][y] = 1;
					}else {
						//dead(x,y);
						population[x][y] = 0;
					}
				}
			}
		}
		graph = population;
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
	
	public int topPoint(int x, int y, int neighbors) {
		if (graph[x-1][y] == 1) {
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
	
	public int leftPoint(int x, int y, int neighbors) {
		if (graph[x][y-1] == 1) {
			neighbors++;
		}if (graph[x+1][y-1] == 1) {
			neighbors++;
		}if (graph[x+1][y] == 1) {
			neighbors++;
		}if(graph[x][y+1] == 1 ) {
			neighbors++;
		}if(graph[x+1][y+1] == 1) {
			neighbors++;
		}
			return neighbors;
	}
	
	public int cornerPoint(int x, int y, int neighbors) {
		if (graph[x+1][y] == 1) {
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
	
	public void live(double x, double y) {
		int newx = (int) x;
		int newy = (int) y;
		graph[newx][newy] = 1;
	}

	public void dead(int x, int y) {
		graph[x][y] = 0;
	}
}

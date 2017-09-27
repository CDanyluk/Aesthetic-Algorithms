import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Cells {
	
	//Since our grid is 610x460 and we have a pixels of 10x10
	double[][] graph = new double[61][46];
	
	public Cells() {
		for (int x = 0; x < 61; x++) {
			for (int y = 0; y < 46; y ++) {
				graph[x][y] = 0;
			}
		}
	}
	
	public double[][] get() {
		return graph;
	}
	
	public void clear() {
		for (int x = 0; x < 61; x++) {
			for (int y = 0; y < 46; y ++) {
				graph[x][y] = 0;
			}
		}
	}
	
	public void iterate(int num) {
		//This will call a function that has rules num times
		//make num iterations and num different ones
	}
	
	public void conway() {
		//Create new graph for next iteration
		//double[][] population = new double[61][46];
		//This will affect the final chart but should make changes without it
		for (int x = 0; x < 61; x++) {
			for (int y = 0; y < 46; y ++) {
				//Create int neighbors to keep track of x, y's neighbors
				int neighbors = 0;
				//edge case check
				if (x > 0 && y > 0 && x < 45 && y < 45) {
					neighbors = centerPoint(x, y, neighbors);
					System.out.println(neighbors);
				}
				/*else {
					//now you actually need to check the goddamn edges
				}*/
				//if x,y is black and neighbors > 3 turn white
				if (graph[x][y] == 1 && neighbors > 3) {
					dead(x,y);
				//if x,y is white and neighbors are 4 or 5
				}else if ((graph[x][y] == 0 && neighbors == 4) || (graph[x][y] == 0 && neighbors == 5)) {
					live(x,y);
				}else {
					dead(x,y);
				}
			}
		}
	}
	
	public void change() {
		for (int x = 0; x < 61; x++) {
			live(x, 36);
		}
	}
	
	public int centerPoint(int x, int y, int neighbors) {
		if (graph[x-1][y-1] == 1) {
			neighbors++;
		}else if (graph[x][y-1] == 1) {
			neighbors++;
		}else if (graph[x+1][y-1] == 1) {
			neighbors++;
		}else if (graph[x-1][y] == 1) {
			neighbors++;
		}else if (graph[x+1][y] == 1) {
			neighbors++;
		}else if (graph[x-1][y+1] == 1) {
			neighbors++;
		}else if(graph[x-1][y+1] == 1) {
			neighbors++;
		}else if(graph[x-1][y+1] == 1 ) {
			neighbors++;
		}else if(graph[x+1][y+1] == 1) {
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

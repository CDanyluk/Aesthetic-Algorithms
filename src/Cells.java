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

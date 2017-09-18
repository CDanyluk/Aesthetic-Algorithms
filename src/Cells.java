import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Cells {
	
	//Since our grid is 610x460 and we have a pixels of 10x10
	double[][] graph = new double[46][61];
	
	public Cells() {
		for (int x = 0; x < 61; x++) {
			for (int y = 0; y < 46; y ++) {
				graph[x][y] = 0;
			}
		}
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

	public void dead(int x, int y) {
		graph[x][y] = 0;
	}
}

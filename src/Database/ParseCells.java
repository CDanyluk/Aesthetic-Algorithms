package Database;

public class ParseCells {
	
	//Seeds will exist in a double[][] g graph
	//Where for every x and y value stored
	//g[x][y] will be 1
	public void parseSeeds(String seedData) {
		int width = 700;
		int height = 700;
		int w = (width/10)+1;
		int h = (height/10)+1;
		double[][] graph = new double[w][h];
		String xy[] = seedData.split("%");
		for (String s : xy) {
			String x = s.substring(0,3);
			String y = s.substring(3, 6);
			int X = Integer.parseInt(x);
			int Y = Integer.parseInt(y);
			graph[X][Y] = 1;
		}
		printGraph(graph);
	}
	
	public void printGraph(double[][] graph) {
		for (int x = 0; x < graph.length; x++) {
			for (int y = 0; y < graph.length; y ++) {
			
				System.out.print(graph[x][y]+ " ");
			}
			System.out.print("\n");
		}
	}
	
}

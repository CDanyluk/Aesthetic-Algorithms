package Database;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.paint.Color;

public class ParseCells {
	
	//Seeds will exist in a double[][] g graph
	//Where for every x and y value stored
	//g[x][y] will be 1
	public boolean[][] parseSeeds(String seedData) {
		int width = 700;
		int height = 700;
		int w = (width/10)+1;
		int h = (height/10)+1;
		boolean[][] graph = new boolean[w][h];
		String xy[] = seedData.split("%");
		for (String s : xy) {
			String x = s.substring(0,3);
			String y = s.substring(3, 6);
			int X = Integer.parseInt(x);
			int Y = Integer.parseInt(y);
			//This shouldn't equal 1 but the max color
			//int max = pattern.getColorSize();
			//graph[newx][newy] = max;
			graph[X][Y] = true;
		}
		return graph;
	}
	
	public Map<Integer, Boolean> parseDead(String deadData) {
		Map<Integer, Boolean> dead = new HashMap<Integer, Boolean>();
		char[] charList = deadData.toCharArray();
		for (int i = 0; i < 9; i++) {
			if (charList[i] == 'T') {
				dead.put(i, true);
			}else {
			dead.put(i, false);
			}
		}
		return dead;
	}
	
	public Map<Integer, Boolean> parseAlive(String aliveData) {
		Map<Integer, Boolean> alive = new HashMap<Integer, Boolean>();
		char[] charList = aliveData.toCharArray();
		for (int i = 0; i < 9; i++) {
			if (charList[i] == 'T') {
				alive.put(i, true);
			}else {
			alive.put(i, false);
			}
		}
		return alive;
	}
	
	//might be waaaaay off
	public HashMap<Integer, Color> parseColor(String colorData) {
		HashMap<Integer, Color> colors = new HashMap<Integer, Color>();
		String[] colorList = colorData.split("#");
		for (int i = 1; i < colorList.length; i++) {
			Color color = hex2Rgb("#" + colorList[i]);
			colors.put(i-1, color);
		}
		return colors;
	}
	
	public Color hex2Rgb(String colorStr) {
		int red = Integer.valueOf( colorStr.substring( 1, 3 ), 16 );
		int green = Integer.valueOf( colorStr.substring( 3, 5 ), 16 );
		int blue = Integer.valueOf( colorStr.substring( 5, 7 ), 16 );
		Color color = Color.rgb(red, green, blue);
		return color;
	}
	
	public Color randomColor() {
		int red = (int)Math.floor( Math.random() * 255 );
		int green = (int)Math.floor( Math.random() * 255 );
		int blue = (int)Math.floor( Math.random() * 255 );
		Color color = Color.rgb(red, green, blue);
		return color;
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

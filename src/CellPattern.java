import java.util.HashMap;
import java.util.Map;

import javafx.scene.paint.Color;

public class CellPattern {
	
	private Map<Integer, Boolean> deadMap;
	private Map<Integer, Boolean> aliveMap;
	private HashMap<Integer, Color> colorMap = new HashMap<Integer, Color>();
	private Automata automata;
	
	//We initialize them to be empty, but can set them later on
	public CellPattern() {
		automata = new Automata();
		Map<Integer, Boolean> alive = new HashMap<Integer, Boolean>();
		Map<Integer, Boolean> dead = new HashMap<Integer, Boolean>();
		for (int i = 0; i < 9; i++) {
			alive.put(i, false);
			dead.put(i, false);
		}
		this.aliveMap = alive;
		this.deadMap = dead;
		randomizeColors(25);
		/* HashMap<Integer, Color> rainbow = new HashMap<Integer, Color>();
		rainbow.put(1, Color.BLACK);
		rainbow.put(0, Color.WHITE);
		this.colorMap = rainbow;*/
	}
	
	//All the getter and setter methods
	public void setDead(Map<Integer, Boolean> dead) {this.deadMap = dead;}
	public void setAlive(Map<Integer, Boolean> alive) {this.aliveMap = alive;}
	public Map<Integer, Boolean> getDead() {return deadMap;}
	public Map<Integer, Boolean> getAlive() {return aliveMap;}
	public int getColorSize() {return colorMap.size()-1;}
	public HashMap<Integer, Color> getColorMap() {return colorMap;}

	//Randomizes a new color map, the whole thing
	public void randomizeColors(int max) {
		int min = 2;
		int rand = (int)Math.floor(Math.random()*(max-min+1)+min);
		HashMap<Integer, Color> rainbow = new HashMap<Integer, Color>();
		for (int i = 0; i < rand; i++) {
			Color color = randomColor();
			rainbow.put(i, color);
		}
		this.colorMap = rainbow;
	}
	
	//Takes a number and randomly changes that many colors in the colorMap
	public void randomAColor(int num) {
		int max = colorMap.size()-1;
		int min = 0;
		if (num >= max) {
			HashMap<Integer, Color> rainbow = new HashMap<Integer, Color>();
			for (int i = 0; i < num; i++) {
				Color color = randomColor();
				rainbow.put(i, color);
			}
			this.colorMap = rainbow;
			return;
		}else {
			for (int i = 0; i < num; i ++ ) {
				int randColorKey = (int)Math.floor(Math.random()*(max-min+1)+min);
				Color color = randomColor();
				this.colorMap.put(randColorKey, color);
			}
		}

		
	}
	
	//Returns a random color, a helper function
	public Color randomColor() {
		int red = (int)Math.floor( Math.random() * 255 );
		int green = (int)Math.floor( Math.random() * 255 );
		int blue = (int)Math.floor( Math.random() * 255 );
		Color color = Color.rgb(red, green, blue);
		return color;
	}
	
	//randomizes the entire dead list
	public void randomDead() {
		Map<Integer, Boolean> dead = new HashMap<Integer, Boolean>();
		for (int i = 0; i < 9; i++) {
			boolean on = (Math.random() < 0.5);
			dead.put(i, on);
		}
		this.deadMap = dead;
	}
	
	//randomly changes num values in the dead list
	public void randomValDead(int num) {
		if (num >= 8) {
			randomDead();
			return;
		}else {
			int min = 0;
			int max = 8;
			for (int i = 0; i < num; i ++ ) {
				int rand = (int)Math.floor(Math.random()*(max-min+1)+min);
				//reverses whatever value was in a random spot in deadMap
				deadMap.put(rand, !deadMap.get(rand));
			}
		}
	}
	
	//randomly changes num values in the alive list
		public void randomValAlive(int num) {
			if (num >= 8) {
				randomAlive();
				return;
			}else {
				int min = 0;
				int max = 8;
				for (int i = 0; i < num; i ++ ) {
					int rand = (int)Math.floor(Math.random()*(max-min+1)+min);
					//reverses whatever value was in a random spot in deadMap
					aliveMap.put(rand, !aliveMap.get(rand));
				}
			}
		}
	
	//randomizes the entire alive list
	public void randomAlive() {
		Map<Integer, Boolean> alive = new HashMap<Integer, Boolean>();
		for (int i = 0; i < 9; i++) {
			boolean on = (Math.random() < 0.5);
			alive.put(i, on);
		}
		this.aliveMap = alive;
	}
	
	public double[][] colorAutomata(double[][] graph) {
		return automata.auto(graph, deadMap, aliveMap, colorMap.size()-1);
	}
	
}

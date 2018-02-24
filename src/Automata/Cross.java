package Automata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Database.ParseCells;
import javafx.scene.paint.Color;

public class Cross {

	public Cells cross(Cells cell1, Cells cell2) {
		Cells crossedCell = cell1;
		//Switch up all the values
		Map<Integer, Boolean> aliveMap = mergeAlive(cell1.getAlive(), cell2.getAlive(), 4);
		System.out.println("aliveMap : " + aliveMap);
		Map<Integer, Boolean> deadMap = mergeAlive(cell1.getDead(), cell2.getDead(), 4);
		System.out.println("deadMap : " + deadMap);
		boolean[][] newSeeds = mergeSeeds(cell1.getSeedData(), cell2.getSeedData());
		System.out.println("newSeeds : " + newSeeds);
		HashMap<Integer, Color> newColors = mergeColor(cell1.getColorMap(), cell2.getColorMap());
		System.out.println("newColors : " + newColors);
		//Put all the values in a new cell
		crossedCell.setAlive(aliveMap);
		crossedCell.setDead(deadMap);
		crossedCell.setSeeds(newSeeds);
		crossedCell.setColor(newColors);
		//return crossedCell;
		return crossedCell;
	}
	
	public Map<Integer, Boolean> mergeAlive(Map<Integer, Boolean> alive1, Map<Integer, Boolean> alive2, int howmany) {
		List<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < howmany; i++) {
			int rand = randomIndex();
			if (!list.contains(rand)) {
				list.add(rand);
				Boolean temp = alive1.get(rand);
				alive1.put(rand, alive2.get(rand));
				alive2.put(rand,  temp);
			}else {
				i--;
			}
		}
		return alive1;
	}
	
	public Map<Integer, Boolean> mergeDead(Map<Integer, Boolean> dead1, Map<Integer, Boolean> dead2, int howmany) {
		List<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < howmany; i++) {
			int rand = randomIndex();
			if (!list.contains(rand)) {
				list.add(rand);
				Boolean temp = dead1.get(rand);
				dead1.put(rand, dead2.get(rand));
				dead2.put(rand,  temp);
			}else {
				i--;
			}
		}
		return dead1;
	}
	
	public boolean[][] mergeSeeds(String seedData1, String seedData2) {
		int width = 700;
		int height = 700;
		int w = (width/10)+1;
		int h = (height/10)+1;
		String xy1[] = seedData1.split("%");
		String xy2[] = seedData2.split("%");
		//Take percent length of smallest xy
		double max = 0;
		if (xy1.length < xy2.length) {
			double length = xy1.length;
			max = (0.35) * length;
		}else {
			double length = xy2.length;
			max = (0.35) * length;
		}
		//Swap up to that percent randomly
		List<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < max; i++) {
			int rand = randomIndex(0, (int) max);
			if (!list.contains(rand)) {
				list.add(rand);
				String temp = xy1[rand];
				if (xy2[rand] != null) {
					xy1[rand] = xy2[rand];
				}
				//xy2[rand] = temp;
			}else {
				i--;
			}
		}
		//put the "%" back in
		String xyData = "";
		for (int p = 0; p < xy1.length; p++) {
			xyData += xy1[p] + "%";
		}
		//Call parseSeeds on it and return the graph
		ParseCells parse = new ParseCells();
		return parse.parseSeeds(xyData);
	}
	
	public HashMap<Integer, Color> mergeColor(HashMap<Integer, Color> colors1, HashMap<Integer, Color> colors2) {
		//Get the percentage to swap of the colors
		double max = 0;
		if (colors1.size() < colors2.size()) {
			double length = colors1.size();
			max = (0.5) * length;
		}else {
			double length = colors2.size();
			max = (0.5) * length;
		}
		List<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < max; i++) {
			int rand = randomIndex();
			if (!list.contains(rand)) {
				list.add(rand);
				Color temp = colors1.get(rand);
				if (colors2.get(rand) != null) {
					colors1.put(rand, colors2.get(rand));
				}
				//colors2.put(rand,  temp);
			}else {
				i--;
			}
		}
		return colors1;
	}
	
	public int randomIndex() {
		int r = (int) ((Math.random() * (9 - 0)) + 0);
		return r;
	}
	
	public int randomIndex(int min, int max) {
		int r = (int) ((Math.random() * ((max+1) - min)) + min);
		return r;
	}
	
	public void printGraph(boolean[][] graph) {
		for (int x = 0; x < graph.length; x++) {
			for (int y = 0; y < graph.length; y ++) {
				if (graph[x][y]) {
					System.out.print("@ ");
				}else {
					System.out.print(". ");
				}
			}
			System.out.print("\n");
		}
	}
}

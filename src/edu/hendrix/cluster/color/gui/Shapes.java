package edu.hendrix.cluster.color.gui;

public enum Shapes {
	
	LINE ("line"){
		public Boolean[][] getGrid() { return getLine(); }
		
	},
	SQUARE("square"){
		public Boolean[][] getGrid() { return getSquare(); }
		
	},
	TRIANGLE("triangle"){
		public Boolean[][] getGrid() { return getTriangle(); }
		
	},CIRClE("circle"){
		public Boolean[][] getGrid() { return getCircle(); }
		
	};
	
	public abstract Boolean[][] getGrid();
	
	private final String name;
	
	Shapes(String name) {
		this.name = name;
	}
	
	String getName() {
		return name;
	}

	//grid for line
	public Boolean[][] getLine() {
		int n = 6;
		Boolean[][] grid = new Boolean[n+1][n+1];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				grid[j][i] = false;
			}
		}
		for (int i = 0; i < n; i++) {
			grid[i][i] = true;
		}
		return grid;
	}
	
	//Grid for square
	public Boolean[][] getSquare() {
		int n = 6;
		Boolean[][] grid = new Boolean[n+1][n+1];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				grid[j][i] = true;
			}
		}
		return grid;
	}
	
	//Grid for triangle
	public Boolean[][] getTriangle() {
		int n = 6;
		Boolean[][] grid = new Boolean[n+1][n+1];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				grid[j][i] = true;
			}
		}
		grid[0][0] = false; grid[0][1] = false; grid[0][4] = false; grid[0][5] = false;
		grid[1][0] = false; grid[1][1] = false; grid[1][4] = false; grid[1][5] = false;
		grid[2][0] = false; grid[2][1] = false; grid[2][4] = false; grid[2][5] = false;
		grid[3][0] = false; grid[3][5] = false;
		grid[4][0] = false; grid[4][5] = false;
		
		return grid;
		
	}
	
	//Grid for circle 
	public Boolean[][] getCircle() {
		int n = 6;
		Boolean[][] grid = new Boolean[n+1][n+1];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				grid[j][i] = true;
			}
		}
		grid[0][0] = false; grid[0][5] = false;
		grid[5][0] = false; grid[5][5] = false;
		
		return grid;
		
	}
	
}

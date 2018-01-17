package edu.hendrix.cluster.color.gui;

public enum Shapes {
	
	LINE ("line"){
		public Boolean[][] getGrid(int num) { return getLine(); }
	},
	SQUARE("square"){
		public Boolean[][] getGrid(int num) { 
			if (num == 4) {
				return getSquare4();
			}else if (num == 3) {
				return getSquare3();
			}else if (num == 2) {
				return getSquare2();
			}else {
				return getSquare1();
			}
			
		}
		
	},
	TRIANGLE("triangle"){
		public Boolean[][] getGrid(int num) { 
			if (num == 1) {
				return getTriangle1();
			}else if (num == 2) {
				return getTriangle2();
			}else if (num == 3) {
				return getTriangle3();
			}else if (num == 4) {
				return getTriangle4();
			}else if (num == 5) {
				return getTriangle5();
			}else if (num == 6) {
				return getTriangle6();
			}else if (num == 7) {
				return getTriangle7();
			}else {
				return getTriangle8();
			}
		}
		
	},CIRClE("circle"){
		public Boolean[][] getGrid(int num) { return getCircle(); }
		
	};
	
	public abstract Boolean[][] getGrid(int num);
	
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
	
	//Grid for square ---------------------------------------------
	public Boolean[][] getSquare1() {
		int n = 6;
		Boolean[][] grid = fullGrid();
		return grid;
	}
	
	public Boolean[][] getSquare2() {
		int n = 6;
		Boolean[][] grid = fullGrid();
		grid[0][0] = false; grid[0][1] = false; grid[0][4] = false; grid[0][5] = false;
		grid[1][0] = false; grid[1][5] = false;
		grid[4][0] = false; grid[4][5] = false;
		grid[5][0] = false; grid[5][1] = false; grid[5][4] = false; grid[5][5] = false;
		
		return grid;
	}
	
	public Boolean[][] getSquare3() {
		int n = 6;
		Boolean[][] grid = fullGrid();
		grid[0][0] = false; grid[0][4] = false; grid[0][5] = false;
		grid[1][0] = false;
		grid[4][5] = false;
		grid[5][0] = false; grid[5][1] = false; grid[5][5] = false;
		
		return grid;
	}
	
	public Boolean[][] getSquare4() {
		int n = 6;
		Boolean[][] grid = fullGrid();
		grid[0][0] = false; grid[0][1] = false; grid[0][5] = false;
		grid[1][5] = false;
		grid[4][0] = false;
		grid[5][0] = false; grid[5][4] = false; grid[5][5] = false;
		return grid;
	}
	
	//Grid for triangle -------------------------------------------
	public Boolean[][] getTriangle1() {
		int n = 6;
		Boolean[][] grid = fullGrid();
		grid[0][0] = false; grid[0][1] = false; grid[0][4] = false; grid[0][5] = false;
		grid[1][0] = false; grid[1][1] = false; grid[1][4] = false; grid[1][5] = false;
		grid[2][0] = false; grid[2][1] = false; grid[2][4] = false; grid[2][5] = false;
		grid[3][0] = false; grid[3][5] = false;
		grid[4][0] = false; grid[4][5] = false;
		return grid;
	}
	
	public Boolean[][] getTriangle2() {
		int n = 6;
		Boolean[][] grid = fullGrid();
		grid[0][0] = false; grid[3][0] = false; grid[4][0] = false; grid[5][0] = false;
		grid[0][1] = false; grid[4][1] = false; grid[5][1] = false;
		grid[0][2] = false; grid[5][2] = false;
		grid[3][5] = false; grid[4][5] = false; grid[5][5] = false;
		return grid;
	}
	
	public Boolean[][] getTriangle3() {
		int n = 6;
		Boolean[][] grid = fullGrid();
		grid[0][1] = false; grid[0][2] = false; grid[0][3] = false; grid[0][4] = false; grid[0][5] = false;
		grid[1][3] = false; grid[1][4] = false; grid[1][5] = false;
		grid[4][3] = false; grid[4][4] = false; grid[4][5] = false;
		grid[5][1] = false; grid[5][2] = false; grid[5][3] = false; grid[5][4] = false; grid[5][5] = false;
		return grid;
	}
	
	public Boolean[][] getTriangle4() {
		int n = 6;
		Boolean[][] grid = fullGrid();
		grid[0][0] = false; grid[0][1] = false; grid[0][2] = false; grid[0][5] = false;
		grid[1][0] = false; grid[1][1] = false; grid[1][5] = false;
		grid[2][0] = false; grid[2][5] = false;
		grid[5][0] = false; grid[5][1] = false; grid[5][2] = false;
		
		return grid;
	}
	
	public Boolean[][] getTriangle5() {
		int n = 6;
		Boolean[][] grid = fullGrid();
		grid[1][0] = false; grid[2][0] = false; grid[3][0] = false; grid[4][0] = false; grid[5][0] = false;
		grid[3][1] = false; grid[4][1] = false; grid[5][1] = false;
		grid[3][4] = false; grid[4][4] = false; grid[5][4] = false;
		grid[1][5] = false; grid[2][5] = false; grid[3][5] = false; grid[4][5] = false; grid[5][5] = false;
		return grid;
	}
	
	public Boolean[][] getTriangle6() {
		int n = 6;
		Boolean[][] grid = fullGrid();
		grid[0][0] = false; grid[0][3] = false; grid[0][4] = false; grid[0][5] = false;
		grid[1][0] = false; grid[1][4] = false; grid[1][5] = false;
		grid[2][0] = false; grid[2][5] = false;
		grid[5][3] = false; grid[5][4] = false; grid[5][5] = false;
		return grid;
	}
	
	public Boolean[][] getTriangle7() {
		int n = 6;
		Boolean[][] grid = fullGrid();
		grid[0][0] = false; grid[0][1] = false; grid[0][2] = false; grid[0][3] = false; grid[0][4] = false; grid[0][5] = false;
		grid[1][0] = false; grid[1][1] = false; grid[1][2] = false;
		grid[4][0] = false; grid[4][1] = false; grid[4][2] = false;
		grid[5][0] = false; grid[5][1] = false; grid[5][2] = false; grid[5][3] = false; grid[5][4] = false; grid[5][5] = false;
		return grid;
	}
	
	public Boolean[][] getTriangle8() {
		int n = 6;
		Boolean[][] grid = fullGrid();
		grid[0][3] = false; grid[0][4] = false; grid[0][5] = false;
		grid[3][0] = false; grid[3][5] = false;
		grid[4][0] = false; grid[4][4] = false; grid[4][5] = false;
		grid[5][0] = false; grid[5][3] = false; grid[5][4] = false; grid[5][5] = false;
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
	
	//Make full grid
	public Boolean[][] fullGrid() {
		int n = 6;
		Boolean[][] grid = new Boolean[n+1][n+1];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				grid[j][i] = true;
			}
		}
		return grid;
	}
	
}

package edu.hendrix.blob;

public class Point {
	private int x, y;
	
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getX() {return x;}
	public int getY() {return y;}
	
	public double angle(Point target) {
		return Math.atan2(this.y - target.y, this.x - target.x);
	}
	
	public double distance(Point target) {
		return Math.sqrt(Math.pow(target.x - this.x, 2) + Math.pow(target.y - this.y, 2));
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof Point) {
			Point that = (Point)other;
			return this.x == that.x && this.y == that.y;
		} else {
			return false;
		}
	}
	
	@Override
	public String toString() {
		return "(" + x + "," + y + ")";
	}
	
	@Override
	public int hashCode() {
		return x + y;
	}
}

package edu.hendrix.blob;

import java.util.LinkedHashSet;

public class Blob {
	private LinkedHashSet<Point> ps;
	private int x1 = Integer.MAX_VALUE;
	private int x2 = 0;
	private int y1 = Integer.MAX_VALUE;
	private int y2 = 0;
	
	public Blob() {
		ps = new LinkedHashSet<Point>();
	}
	
	public void addPoint(int x, int y) {
		ps.add(new Point(x, y));
	}
	
	public int getSize() {
		return ps.size();
	}
	
	public LinkedHashSet<Point> getPoints() {
		return new LinkedHashSet<Point>(ps);
	}
	
	public int getMinX() {
		return x1;
	}
	
	public int getMaxX() {
		return x2;
	}
	
	public int getMinY() {
		return y1;
	}
	
	public int getMaxY() {
		return y2;
	}
	
	public int getHeight() {
		return y2 - y1;
	}
	
	public int getWidth() {
		return x2 - x1;
	}

	
	public void setHW() {
		for (Point p : ps) {
			x1 = Math.min(x1, p.getX());
			x2 = Math.max(x2, p.getX());
			y1 = Math.min(y1, p.getY());
			y2 = Math.max(y2, p.getY());
		}
		
	}
	
}

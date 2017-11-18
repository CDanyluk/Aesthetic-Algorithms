package edu.hendrix.blob;

import java.util.LinkedHashSet;

public class Blob {
	private LinkedHashSet<Point> ps;
	
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
	
}

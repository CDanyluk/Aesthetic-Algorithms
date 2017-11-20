package edu.hendrix.blob;

import java.util.LinkedHashSet;

public class Blob {
	private LinkedHashSet<Point> ps;
	private int width;
	private int height;
	
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
	
	//First try
	public void setWidth() {
		Point[] array = (Point[]) ps.toArray();
		Point p = array[1];
		Point p2 = array[array.length];
		int x1 = p.getX();
		int y1 = p.getY();
		System.out.println("Start point = " + x1 + " , " + y1);
	}
	
	//Second try
	public void setHeight() {
		int x1 = 0;
		int x2 = 0;
		int y1 = 0;
		int y2 = 0;
		for (Point p : ps) {
			x1 = Math.min(x1, p.getX());
			x2 = Math.max(x2, p.getX());
			y1 = Math.min(y1, p.getY());
			y2 = Math.max(y2, p.getY());
		}
		
		System.out.println("Start point = " + x1 + " , " + y1);
		System.out.println("End point = " + x2 + " , " + y2);
		
	}
	
}

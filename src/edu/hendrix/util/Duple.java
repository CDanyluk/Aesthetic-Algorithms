package edu.hendrix.util;

public class Duple<X,Y> {
	private X x;
	private Y y;
	
	public Duple(X x, Y y) {
		this.x = x;
		this.y = y;
	}
	
	public X getFirst() {return x;}
	public Y getSecond() {return y;}
	
	@Override
	public String toString() {
		return "{" + x + "}{" + y + "}";
	}
	
	@Override
	public int hashCode() {
		return x.hashCode() + y.hashCode();
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof Duple<?,?>) {
			@SuppressWarnings("unchecked")
			Duple<X,Y> that = (Duple<X,Y>)other;
			return this.x.equals(that.x) && this.y.equals(that.y);
		} else {
			return false;
		}
	}
}

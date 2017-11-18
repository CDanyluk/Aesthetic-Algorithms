package edu.hendrix.cluster.color;

import edu.hendrix.cluster.Clusterable;
import edu.hendrix.util.Util;
import javafx.scene.paint.Color;

public class ColorCluster implements Clusterable<ColorCluster> {

	private double red, green, blue;
	
	public ColorCluster(double red, double green, double blue) {
		this.red = red;
		this.green = green;
		this.blue = blue;
	}
	
	public ColorCluster(int rgb) {
		this(Util.getRed(rgb), Util.getGreen(rgb), Util.getBlue(rgb));
	}
	
	public Color toColor() {
		return Color.color(red / 255.0, green / 255.0, blue / 255.0);
	}
	
	@Override
	public String toString() {
		return "ColorCluster(" + red + "," + green + "," + blue + ")";
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof ColorCluster) {
			ColorCluster that = (ColorCluster)other;
			return this.red == that.red && this.green == that.green && this.blue == that.blue;
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return toRGB();
	}
	
	public int toRGB() {
		return (int)red << 16 + (int)green << 8 + (int)blue;
	}

	@Override
	public ColorCluster weightedCentroidWith(ColorCluster other, long thisCount, long otherCount) {
		return new ColorCluster(Clusterable.weightedMean(this.red, other.red, thisCount, otherCount),
				Clusterable.weightedMean(this.green, other.green, thisCount, otherCount),
				Clusterable.weightedMean(this.blue, other.blue, thisCount, otherCount));
	}

	@Override
	public double distance(ColorCluster other) {
		Util.assertArgument(other != null, "other null");
		return Math.pow(this.red - other.red, 2) + Math.pow(this.green - other.green, 2) + Math.pow(this.blue - other.blue, 2);
	}
}

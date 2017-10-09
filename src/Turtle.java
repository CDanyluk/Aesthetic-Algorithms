public class Turtle {
    public double x;
    public double y;
    public double angle;

    // this is where the turtle will start. 
    // start at point x,y and facing at a degree of "angle" away from the x axis
    public Turtle(double x, double y, double angle) {
    	this.x = x;
    	this.y = y;
    	this.angle = angle;
    }
    
    // http://introcs.cs.princeton.edu/java/32class/Turtle.java.html -- source 
    // This is where I think maybe things are getting a little off??
    public void goForward(double distance) {
        x = x + (distance * Math.cos(Math.toRadians(angle)));
        y = x + (distance * Math.sin(Math.toRadians(angle)));
    }

    // turns go counterclockwise
    public void turnLeft(double turn) {
        angle = angle + turn;
    }



}

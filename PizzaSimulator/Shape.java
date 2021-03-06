import java.awt.*;

/* Class Shape
 *
 * By Rob Nash (with minor edits by David Nixon)
 * 
 * This is the superclass in a hierarchy of shapes that you have to construct
 */

// the superclass in our inheritance hierarchy
// all "common" features, functions and data should go here
// for example, all shapes in Java2D have a x,y that declares their position
// and many of the shapes exposed have a width and a height (but not all, so we didn't put width and height here)
// note that this class is mostly empty, as there is no algorithm generic enough to guess an arbitrary shape's area 
// (future subclasses must override getArea() to provide something reasonable)
// Also, the draw method is empty too, as we don't know what shape to draw here! 
// (again, our subclasses will need to replace this method with one that actually draws things)


class Shape extends Object {
	private int x = 0;
	private int y = 0;
	
	public Shape( int a, int b ) {
		this.x = a;
		this.y = b;
	}
	
	public Shape clone(){
	    return new Shape(x,y);
	}
	
	public double getArea(){ return -1; }
	
	public void draw( Graphics g ){}
	
	public int getX() { return this.x; }
	public int getY() { return this.y; }
	protected void setX(int newX) { this.x = newX; }
	protected void setY(int newY) { this.y = newY; }
}
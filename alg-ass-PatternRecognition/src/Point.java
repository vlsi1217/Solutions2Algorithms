/**
 *  File: Point.java
 *  Package: 
 *  Project: alg-PatternRecognition
 *  Created on: Mar 2, 2013
 * 
 *  Description: Programming Assignment 3
 *
 *  @author: Huang Zhu
 *  Department of Computing and Information Sciences
 *  Kansas State University
 *
 */

import java.util.Comparator;;

public class Point implements Comparable<Point> {
	public final Comparator<Point> SLOPE_ORDER = new BySlopeOrder();;
	private final int x;
	private final int y;
	
	private class BySlopeOrder implements Comparator<Point> {
		//using slope to compare
		//be careful of float point value comparison
		public int compare(Point v, Point w) {
			//TODO - special case: comparison between infinity
			
			//Point.this.slopeTo(v);
			double vSlope = slopeTo(v);
			double wSlope = slopeTo(w);
			
			if (vSlope < wSlope) return -1;
			if (vSlope > wSlope) return +1;
			else return 0;
		}
		
		//compare slope using coordinates (don't calculate slopes)
		/*public int compare(Point v, Point w) {
		    //TODO
		}*/
	}
	
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	//draw a point
	public void draw() {
		StdDraw.point(x, y);
	}
	
	//draw a line
	public void drawTo(Point that) {
		StdDraw.line(this.x, this.y, that.x, that.y);
	}
	
	//a string output
	public String toString() {
		return "(" + x + ", " + y +")";
	}
	
	//method for Comparable<Point>
	@Override
	public int compareTo(Point that) {
		if (this.y < that.y) return -1;
	    if (this.y > that.y) return +1;
	    
	    //this.y == that.y
	    if (this.x < that.x) return -1;
	    if (this.x > that.x) return +1;

	    //this.y == that.y and this.x == that.x
		return 0;
	}
	
	//calculate the slope between two points
	public double slopeTo(Point that) {
		if ((this.x == that.x) && (this.y == that.y)) //self
			return Double.NEGATIVE_INFINITY;
		else if (this.x == that.x) //vertical line
			return Double.POSITIVE_INFINITY;
		else if (this.y == that.y) //horizontal line
			return 0.0;
		else
			return ((double)(that.y - this.y)) / (that.x - this.x);
	}
}

/**
 *  File: PointSET.java
 *  Package: 
 *  Project: alg-ass-KdTree
 *  Created on: Mar 19, 2013
 * 
 *  Description: Programming Assignment 5
 *
 *  @author: Huang Zhu
 *  Department of Computing and Information Sciences
 *  Kansas State University
 */

import java.util.Iterator;

public class PointSET {
	
	private SET<Point2D> set;
	
	//construct an empty set of points
	public PointSET() {
		set = new SET<Point2D>();
	}
	
	//is the set empty
	public boolean isEmpty() {
		return set.isEmpty();
	}
	
	//number of points in the set
	public int size() {
		return set.size();
	}
	
	//add the point p to the set (if it is not already in the set)
	public void insert(Point2D p) {
		//TODO - need to check whether p exists in set first?
		set.add(p);
	}
	
	//does the set contain the point p
	public boolean contains(Point2D p) {
		return set.contains(p);
	}
	
	//draw all the of the points to standard draw
	public void draw() {
		Iterator<Point2D> iter = set.iterator();
		StdDraw.setXscale(0.0, 1.0);
		StdDraw.setYscale(0.0, 1.0);
		StdDraw.setPenColor(StdDraw.BLACK);
		StdDraw.setPenRadius(0.01);
		while (iter.hasNext()) {
			Point2D point = iter.next();
			point.draw();
		}
	}
	
	//all points in the set that are inside the rectangle
	//return an <Iterable<Point2D> object with zero points when no points in range
	public Iterable<Point2D> range(RectHV rect) {
		Queue<Point2D> queue = new Queue<Point2D>();
		Iterator<Point2D> iter = set.iterator();
		while (iter.hasNext()) {
			Point2D point = iter.next();
			if (rect.contains(point))
				queue.enqueue(point);
		}
		return queue;
	}
	
	//a nearest neighbor in the set to p
	//null is set is empty
	public Point2D nearest(Point2D p) {
		if (set.isEmpty()) return null;
		
		Point2D nearestP = null;
		
		//possible maximum distance: sqrt of 2
		//x: 0-1, y: 0-1
		double mindist = 2.0;
		
		Iterator<Point2D> iter = set.iterator();
		while (iter.hasNext()) {
			Point2D point = iter.next();
			double dist = point.distanceSquaredTo(p);
			if (dist <= mindist) {
				nearestP = point;
				mindist = dist;
			}
		}
		return nearestP;
	}
	
	//unit test
	public static void main(String[] args) {
        /*String filename = args[0];
        In in = new In(filename);

        StdDraw.show(0);

        // initialize the two data structures with point from standard input
        PointSET brute = new PointSET();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            brute.insert(p);
        }
        brute.draw();*/
        
      //test constructor
      PointSET brute = new PointSET();
      		
      		//test size()
      		StdOut.println("KdTree size: " + brute.size());
      		
      		//points from circle10.txt
      		//test insert()
      		brute.insert(new Point2D(0.206107, 0.095492));//A
      		brute.insert(new Point2D(0.975528, 0.654508));//B
      		brute.insert(new Point2D(0.024472, 0.345492));//C
      		brute.insert(new Point2D(0.793893, 0.095492));//D
      		brute.insert(new Point2D(0.793893, 0.904508));//E
      		brute.insert(new Point2D(0.975528, 0.345492));//F
      		brute.insert(new Point2D(0.206107, 0.904508));//G
      		brute.insert(new Point2D(0.500000, 0.000000));//H
      		brute.insert(new Point2D(0.024472, 0.654508));//I
      		brute.insert(new Point2D(0.500000, 1.000000));//J
      		
      		//test draw()
      		brute.draw();
      		
      		//test size()
      		StdOut.println("KdTree size: " + brute.size());
      		
      		//test contains()
      		StdOut.println("KdTree contains (0.206107, 0.095492): " + brute.contains(new Point2D(0.206107, 0.095492)));
      		StdOut.println("KdTree contains (0.975528, 0.654508): " + brute.contains(new Point2D(0.975528, 0.654508)));
      		StdOut.println("KdTree contains (0.024472, 0.345492): " + brute.contains(new Point2D(0.024472, 0.345492)));
      		StdOut.println("KdTree contains (0.793893, 0.095492): " + brute.contains(new Point2D(0.793893, 0.095492)));
      		StdOut.println("KdTree contains (0.793893, 0.904508): " + brute.contains(new Point2D(0.793893, 0.904508)));
      		StdOut.println("KdTree contains (0.975528, 0.345492): " + brute.contains(new Point2D(0.975528, 0.345492)));
      		StdOut.println("KdTree contains (0.206107, 0.904508): " + brute.contains(new Point2D(0.206107, 0.904508)));
      		StdOut.println("KdTree contains (0.500000, 0.000000): " + brute.contains(new Point2D(0.500000, 0.000000)));
      		StdOut.println("KdTree contains (0.024472, 0.654508): " + brute.contains(new Point2D(0.024472, 0.654508)));
      		StdOut.println("KdTree contains (0.500000, 1.000000): " + brute.contains(new Point2D(0.500000, 1.000000)));
      		StdOut.println("KdTree contains (0.0, 0.0): " + brute.contains(new Point2D(0.0, 0.0)));
      		StdOut.println("KdTree contains (0.0, 1.0): " + brute.contains(new Point2D(0.0, 1.0)));
      		StdOut.println("KdTree contains (1.0, 0.0): " + brute.contains(new Point2D(1.0, 0.0)));
      		StdOut.println("KdTree contains (1.0, 1.0): " + brute.contains(new Point2D(1.0, 1.0)));
      		
      		//test range()
      		RectHV rect = new RectHV(0.206100, 0.095490, 0.975530, 0.904510);
      		StdOut.println("Rectangle: " + rect.toString());
      		StdOut.println("Rectangle contains: ");
      		for (Point2D point : brute.range(rect))
      			StdOut.println(point);
      		
      		//test nearest()
      		Point2D po = new Point2D(0.493893, 0.404508);
      		StdDraw.setPenColor(StdDraw.BLACK);
      		StdDraw.setPenRadius(0.01);
      		po.draw();
      		StdOut.println("Nearest point to (0.493893, 0.404508) is: " + brute.nearest(po));
      		
      		//test nearest()
      		po = new Point2D(0.500000, 1.000000);
      		StdDraw.setPenColor(StdDraw.BLACK);
      		StdDraw.setPenRadius(0.01);
      		po.draw();
      		StdOut.println("Nearest point to (0.500000, 1.000000) is: " + brute.nearest(po));
	}
}

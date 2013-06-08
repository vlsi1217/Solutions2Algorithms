/**
 *  File: KdTree.java
 *  Package: 
 *  Project: alg-ass-KdTree
 *  Created on: Mar 19, 2013
 * 
 *  Description: Programming Assignment 5
 *  
 *  Pay Attention to iterative or recursive implementations of function: insert, contains, ceiling, floor, draw, range, nearest.
 *
 *  @author: Huang Zhu
 *  Department of Computing and Information Sciences
 *  Kansas State University
 */

import java.util.Iterator;

public class KdTree {
	private Node root; //root of kdtree
	private int size; //number of nodes in the tree
	
	//static class
	private static class Node {
		private Point2D p;
		private Node lb;
		private Node rt;
		private boolean xcoord; //use x-coordinate to split tree
		
		public Node(Point2D p, Node lb, Node rt, boolean xcoord) 
		{
			this.p = p;
			this.lb = lb;
			this.rt = rt;
			this.xcoord = xcoord;
		}
	}
	
	
	//construct an empty kd-tree of points
	public KdTree() {
		root  = null;
		size = 0;
	}
	
	
	//is the kd-tree empty
	public boolean isEmpty() {
		return size == 0;
	}
	
	
	//number of points in the kd-tree
	public int size() {
		return size;
	}
	
	
	//add the point p to the kd-tree (if it is not already in the kd-tree)
	//xcoord: true. use x-coordinate to compare and split.
	//xcoord: false. use y-coordinate to compare and split.
	private Node insert(Node node, Point2D p, boolean xcoord) {
		if (node == null) {
			size++;
			return new Node(p, null, null, xcoord);
		}
		
		if (xcoord == true) { //vertical-split point
			//use x-coordinate to compare
			if (p.x() < node.p.x()) {
				node.lb = insert(node.lb, p, false); //left
			} else if (p.x() == node.p.x()) {
				if (p.y() != node.p.y())
					node.rt = insert(node.rt, p, false); //right
			}
			else
				node.rt = insert(node.rt, p, false); //right
		} else {              //horizontal-split point
			//use y-coordinate to compare
			if (p.y() < node.p.y()) {
				node.lb = insert(node.lb, p, true); //bottom
			} else if (p.y() == node.p.y()) {
				if (p.x() != node.p.x())
					node.rt = insert(node.rt, p, true); //right
		    } else
				node.rt = insert(node.rt, p, true); //top
		}
		
		return node;
	}
	
	//add the point p to the kd-tree (if it is not already in the kd-tree)
	public void insert(Point2D p) {
		if (p == null) return;
		//root level use x-coordinate to split: vertical-split
		root = insert(root, p, true);
	}
	
	
	//does the kd-tree contain the point p
	public boolean contains(Point2D p) {
		if (root == null) return false;
		if (p == null)  return false;
		Node temp = root;
		while (temp != null) {
			if (temp.p.equals(p))
				return true;
			if (temp.xcoord == true) {
				//move to next level
				if (p.x() < temp.p.x())
					temp = temp.lb;
				else
					temp = temp.rt;
			} else {
				//move to next level
				if (p.y() < temp.p.y())
					temp = temp.lb;
				else
					temp = temp.rt;
			}
		}
		return false;
	}
	
	//find intersections with existing split lines in order to draw line for node
	//find ceiling based on either x-coordinate or y-coordinate
	//IMPORTANT: visit only nodes upper than node	
	//xcoord: true, visit vertical-split points. compare x.
	//xcoord: false, visit horizontal-split points. compare y.
	//default is 1.0
	private double ceiling(Node node, boolean xcoord) {
		double ceil = 1.0;
		if (node == null) return ceil;
		
		//node.xcoord is true: node is vertical-split point
		//find nearest horizontal boundary so far
		//IMPORTANT: visit only nodes upper than node
		//visit only horizontal split points, i.e., nodes with xcoord false
		if (xcoord == false) {
			Node temp = root;
			while (temp != null) {
				if (temp == node)
					break;
				if (temp.xcoord == false) {
					//TODO - equal?
					if (temp.p.y() > node.p.y() && temp.p.y() <= ceil) {
						ceil = temp.p.y();
					}
					
					//move to next level
					if (node.p.y() < temp.p.y())
						temp = temp.lb;
					else
						temp = temp.rt;
					
				} else {
					//move to next level
					if (node.p.x() < temp.p.x())
						temp = temp.lb;
					else
						temp = temp.rt;
				}
			}
		}
		
		//node.xcoord is false: node is horizontal-split point
		//find nearest vertical boundary so far
		//IMPORTANT: visit only nodes upper than node
		//visit only vertical split points, i.e., nodes with xcoord true
	    if (xcoord == true) {
			Node temp = root;
			while (temp != null) {
				if (temp == node)
					break;
				if (temp.xcoord == true) {
					//TODO - equal?
					if (temp.p.x() > node.p.x() && temp.p.x() <= ceil) {
						ceil = temp.p.x();
					}
					
					//move to next level
					if (node.p.x() < temp.p.x())
						temp = temp.lb;
					else
						temp = temp.rt;
					
				} else {
					//move to next level
					if (node.p.y() < temp.p.y())
						temp = temp.lb;
					else
						temp = temp.rt;
				}
			}
	    }   
	    return ceil;
	}
	
	
	//default is 1.0
	private double ceiling(Node node) {
		if (node == null) return 1.0;
		if (node.xcoord == true) //node splits vertically
			//find y-coordinate floor among horizontal-split points
			//with levels smaller than node
			return ceiling(node, false);
		else                     //node splits horizontally
			//find x-coordinate floor among vertical-split points
			//with levels smaller than node
			return ceiling(node, true);
	}
	
	//find intersections with existing split lines in order to draw line for node
	//find floor based on either x-coordinate or y-coordinate
	//IMPORTANT: visit only nodes upper than node
	//xcoord: true, visit vertical-split points. compare x.
	//xcoord: false, visit horizontal-split points. compare y.
	//default is 0.0
	private double floor(Node node, boolean xcoord) {
		double floor = 0.0;	
		if (node == null) return floor;
		
		//node.xcoord is true: node is vertical-split point
		//find nearest horizontal boundary so far
		//IMPORTANT: visit only nodes upper than node
		//visit only horizontal split points, i.e., nodes with xcoord false
		if (xcoord == false) {
			Node temp = root;
			while (temp != null) {
				if (temp == node)
					break;
				if (temp.xcoord == false) {
					//TODO - equal?
					if (temp.p.y() <= node.p.y() && temp.p.y() >= floor) {
						floor = temp.p.y();
					}
					
					//move to next level
					if (node.p.y() < temp.p.y())
						temp = temp.lb;
					else
						temp = temp.rt;
					
				} else {
					//move to next level
					if (node.p.x() < temp.p.x())
						temp = temp.lb;
					else
						temp = temp.rt;
				}
			}
		}
		
		//node.xcoord is false: node is horizontal-split point
		//find nearest vertical boundary so far
		//IMPORTANT: visit only nodes upper than node
		//visit only vertical split points, i.e., nodes with xcoord true
	    if (xcoord == true) {
			Node temp = root;
			while (temp != null) {
				if (temp == node)
					break;
				if (temp.xcoord == true) {
					//TODO - equal?
					if (temp.p.x() <= node.p.x() && temp.p.x() >= floor) {
						floor = temp.p.x();
					}
					
					//move to next level
					if (node.p.x() < temp.p.x())
						temp = temp.lb;
					else
						temp = temp.rt;
					
				} else {
					//move to next level
					if (node.p.y() < temp.p.y())
						temp = temp.lb;
					else
						temp = temp.rt;
				}
			}
	    }
	    return floor;
	}
	
	
	//default is 0.0
	private double floor(Node node) {
		if (node == null) return 0.0;
		if (node.xcoord == true) //node splits vertically
			//find y-coordinate floor among horizontal-split points
			//with levels smaller than node
			return floor(node, false);
		else                     //node splits horizontally
			//find x-coordinate floor among vertical-split points
			//with levels smaller than node
			return floor(node, true);
	}
	
	
	//draw points, vertical and horizontal split lines
	private void draw(Node node) {
		if (node == null) return;
		
		StdDraw.setPenColor(StdDraw.BLACK);
		StdDraw.setPenRadius(0.01);
		node.p.draw();
		
		if (node.xcoord == true) {
			//red for vertical splits
			StdDraw.setPenColor(StdDraw.RED);
			StdDraw.setPenRadius();
			
			//find ceiling and floor of node.p.y()
			//floor default: 0
			//ceiling default: 1
			double ceil = ceiling(node);
			double floor = floor(node);
			
			//draw the line
			//StdOut.println("(" +node.p.x()+", "+floor+") --> (" +node.p.x()+", "+ceil+")");
			StdDraw.line(node.p.x(), floor, node.p.x(), ceil);
			
			//draw subtree
			draw(node.lb);
			draw(node.rt);
		} else {
			//blue for vertical splits
			StdDraw.setPenColor(StdDraw.BLUE);
			StdDraw.setPenRadius();
			
			//find ceiling and floor of node.p.x()
			//floor default: 0
			//ceiling default: 1
			double ceil = ceiling(node);
			double floor = floor(node);
			
			//draw the line
			//StdOut.println("(" +floor+", "+node.p.y()+") --> (" +ceil+", "+node.p.y()+")");
			StdDraw.line(floor, node.p.y(), ceil, node.p.y());
			
			//draw subtree
			draw(node.lb);
			draw(node.rt);
		}
	}
	
	//draw points, vertical and horizontal split lines
	public void draw() {
		StdDraw.setXscale(0.0, 1.0);
		StdDraw.setYscale(0.0, 1.0);
		StdDraw.square(0.5, 0.5, 0.5);
		draw(root);
	}
	
	
	private void range(Node node, Queue<Point2D> queue, RectHV rect) {
		if (node == null) return;
		
		//StdOut.println("Visiting Point " + node.p.toString());

		Point2D point = node.p;
		if (rect.contains(point)) {
			queue.enqueue(point);
			range(node.lb, queue, rect);
			range(node.rt, queue, rect);
		} else {
			if (node.xcoord == true) { //vertical-split point
				if (point.x() < rect.xmin()) {
					//points in left subtree can't be in range, search right
					//points in left subtree has an x smaller than point.x
					range(node.rt, queue, rect);
				} else if (point.x() > rect.xmax()) {
					//points in right subtree can't be in range, search left
					//points in right subtree has an x no smaller than point.x
					range(node.lb, queue, rect);
				} else {
					range(node.lb, queue, rect);
					range(node.rt, queue, rect);
				}
			} else { //horizontal-split point
				if (point.y() < rect.ymin()) {
					//points in left subtree can't be in range, search right
					//points in left subtree has a y smaller than point.y
					range(node.rt, queue, rect);
				} else if (point.y() > rect.ymax()) {
					//points in right subtree can't be in range, search left
					//points in right subtree has a y no smaller than point.y
					range(node.lb, queue, rect);
				} else {
					range(node.lb, queue, rect);
					range(node.rt, queue, rect);
				}
			}
		}
	}
	
	//all points in the kd-tree that are inside the rectangle
	//an <Iterable<Point2D> object with zero points when no points in range
	public Iterable<Point2D> range(RectHV rect) {
		Queue<Point2D> queue = new Queue<Point2D>();
		range(root, queue, rect);
		return queue;
	}
	
	//a nearest neighbor in the kd-tree to p
	//return null when no point found in node and its subtree
	//that has a distance smaller than min
	//PROBLEM: no pruning rules applied
	private Point2D nearest(Node node, double min, Point2D p) {
		//StdOut.println("Visiting " + node.p.toString());
		if (node == null) return null;
		
		Point2D left = null;
		Point2D right = null;
		Point2D target = null;
		double dist = node.p.distanceSquaredTo(p);

		//node is a candidate
		if (dist <= min) {
			target = node.p; 
			min = dist;
		}
		
		if (node.xcoord == true) { //vertical-split point	
			if (p.x() < node.p.x()) { //start from left subtree
				left = nearest(node.lb, min, p);
				if (left != null) {
					min = left.distanceSquaredTo(p);
					target = left;
				}	
				
				//compute distance from p to rectangle represented by node
				//if min is smaller than that distance, no need to search node and its subtree
				if (min < Math.pow(node.p.x() - p.x(), 2))
					return target;
				
				right = nearest(node.rt, min, p);
				if (right != null) {
					target = right;
				}
				return target;
			} else {                  //start from right subtree
				right = nearest(node.rt, min, p);
				if (right != null) {
					min = right.distanceSquaredTo(p);
					target = right;
				}	
				
				//compute distance from p to rectangle represented by node
				//if min is smaller than that distance, no need to search node and its subtree
				if (min < Math.pow(node.p.x() - p.x(), 2))
					return target;
				
				left = nearest(node.lb, min, p);
				if (left != null) {
					target = left;
				}
				return target;
			}
		} else { //horizontal-split point
			if (p.y() < node.p.y()) { //start from left subtree
				left = nearest(node.lb, min, p);
				if (left != null) {
					min = left.distanceSquaredTo(p);
					target = left;
				}
				
				//compute distance from p to rectangle represented by node
				//if min is smaller than that distance, no need to search node and its subtree
				if (min < Math.pow(node.p.y() - p.y(), 2))
					return target;
				
				right = nearest(node.rt, min, p);
				if (right != null) {
					target = right;
				}
				return target;
			} else {                  //start from right subtree
				right = nearest(node.rt, min, p);
				if (right != null) {
					min = right.distanceSquaredTo(p);
					target = right;
				}	
				
				//compute distance from p to rectangle represented by node
				//if min is smaller than that distance, no need to search node and its subtree
				if (min < Math.pow(node.p.y() - p.y(), 2))
					return target;
				
				left = nearest(node.lb, min, p);
				if (left != null) {
					target = left;
				}
				return target;
			}
		}
	}
	
	//a nearest neighbor in the kd-tree to p
	//null is kd-tree is empty
	public Point2D nearest(Point2D p) {
		if (p == null) return null;
		if (root == null) return null;
		//Point2D nearestP = null;
		//nearest(root, nearestP, root.p.distanceSquaredTo(p), p);
		return nearest(root, root.p.distanceSquaredTo(p), p);
	}
	
	//unit test
	public static void main(String[] args) {		
        /*String filename = args[0];
        In in = new In(filename);

        StdDraw.show(0);

        // initialize the two data structures with point from standard input
        KdTree kdtree = new KdTree();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdtree.insert(p);
        }
        kdtree.draw();*/
        
        
        //test constructor
		KdTree tree = new KdTree();
		
		//test size()
		StdOut.println("KdTree size: " + tree.size());
		
		//points from circle10.txt
		//test insert()
		tree.insert(new Point2D(0.206107, 0.095492));//A
		tree.insert(new Point2D(0.975528, 0.654508));//B
		tree.insert(new Point2D(0.024472, 0.345492));//C
		tree.insert(new Point2D(0.793893, 0.095492));//D
		tree.insert(new Point2D(0.793893, 0.904508));//E
		tree.insert(new Point2D(0.975528, 0.345492));//F
		tree.insert(new Point2D(0.206107, 0.904508));//G
		tree.insert(new Point2D(0.500000, 0.000000));//H
		tree.insert(new Point2D(0.024472, 0.654508));//I
		tree.insert(new Point2D(0.500000, 1.000000));//J
		
		//test draw()
		tree.draw();
		
		//test size()
		StdOut.println("KdTree size: " + tree.size());
		
		//test contains()
		StdOut.println("KdTree contains (0.206107, 0.095492): " + tree.contains(new Point2D(0.206107, 0.095492)));
		StdOut.println("KdTree contains (0.975528, 0.654508): " + tree.contains(new Point2D(0.975528, 0.654508)));
		StdOut.println("KdTree contains (0.024472, 0.345492): " + tree.contains(new Point2D(0.024472, 0.345492)));
		StdOut.println("KdTree contains (0.793893, 0.095492): " + tree.contains(new Point2D(0.793893, 0.095492)));
		StdOut.println("KdTree contains (0.793893, 0.904508): " + tree.contains(new Point2D(0.793893, 0.904508)));
		StdOut.println("KdTree contains (0.975528, 0.345492): " + tree.contains(new Point2D(0.975528, 0.345492)));
		StdOut.println("KdTree contains (0.206107, 0.904508): " + tree.contains(new Point2D(0.206107, 0.904508)));
		StdOut.println("KdTree contains (0.500000, 0.000000): " + tree.contains(new Point2D(0.500000, 0.000000)));
		StdOut.println("KdTree contains (0.024472, 0.654508): " + tree.contains(new Point2D(0.024472, 0.654508)));
		StdOut.println("KdTree contains (0.500000, 1.000000): " + tree.contains(new Point2D(0.500000, 1.000000)));
		StdOut.println("KdTree contains (0.0, 0.0): " + tree.contains(new Point2D(0.0, 0.0)));
		StdOut.println("KdTree contains (0.0, 1.0): " + tree.contains(new Point2D(0.0, 1.0)));
		StdOut.println("KdTree contains (1.0, 0.0): " + tree.contains(new Point2D(1.0, 0.0)));
		StdOut.println("KdTree contains (1.0, 1.0): " + tree.contains(new Point2D(1.0, 1.0)));
		
		//test range()
		RectHV rect = new RectHV(0.206100, 0.095490, 0.975530, 0.904510);
		StdOut.println("Rectangle: " + rect.toString());
		StdOut.println("Rectangle contains: ");
		for (Point2D point : tree.range(rect))
			StdOut.println(point);
		
		//test nearest()
		Point2D po = new Point2D(0.493893, 0.404508);
		StdDraw.setPenColor(StdDraw.BLACK);
		StdDraw.setPenRadius(0.01);
		po.draw();
		StdOut.println("Nearest point to (0.493893, 0.404508) is: " + tree.nearest(po));
		
		//test nearest()
		po = new Point2D(0.500000, 1.000000);
		StdDraw.setPenColor(StdDraw.BLACK);
		StdDraw.setPenRadius(0.01);
		po.draw();
		StdOut.println("Nearest point to (0.500000, 1.000000) is: " + tree.nearest(po));
	}
}

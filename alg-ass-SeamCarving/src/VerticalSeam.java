	/**
 *  File: SeamCarver.java
 *  Package: 
 *  Project: alg-ass-SeamCarving
 *  Created on: Apr 9, 2013
 * 
 *  Description: Seam Carving, Content-Aware Resizing. Programming Assignment 2, Algorithms II
 *
 *  @author: Huang Zhu
 *  Department of Computing and Information Sciences
 *  Kansas State University
 */
public class VerticalSeam {
	//distTo[x][y] = distance of shortest s->v (x, y) path
	private double[][] distTo; 
	
	//edgeTo[x][y] = last edge on shortest s->v path
	//three cases: (x-1, y-1), (x-1, y), (x-1, y+1)
	//store only width
	private int[][] edgeTo;
	
	//view the picture as a 2D-array
	//row - height, col - width
	private int row, col;
	
	public VerticalSeam(double[][] energy, int height, int width) {
		row = height;
		col = width;
		distTo = new double[row][col];
		edgeTo = new int[row][col];

		for (int i = 0; i < col; i++) {
			//distance from virtual source to pixels in first row
			//i.e., weight of edge v->w is the weight of w
			distTo[0][i] = energy[0][i];
			edgeTo[0][i] = -1; //virtual source
		}
		
		for (int i = 1; i < row; i++)
			for (int j = 0; j < col; j++)
				distTo[i][j] = Double.POSITIVE_INFINITY;
		
		//Topological Order: pixels in first row, pixels in second row, ...
		//calculate distance row by row
		for (int i = 1; i < row; i++)
			for (int j = 0; j < col; j++)
				relax(energy[i][j], i, j);
	}
	
	// calculate distance from virtual source to (x, y) or pixel (y, x)
	private void relax(double weight, int x, int y) {
		if (y == 0) { 
			//left vertical border. two incoming edges.
			double a1 = distTo[x-1][y];
			double a2 = distTo[x-1][y+1];
			if (a1 <= a2) {
				distTo[x][y] = a1 + weight;
				edgeTo[x][y] = y;
			} else {
				distTo[x][y] = a2 + weight;
				edgeTo[x][y] = y+1;
			}
		} else if (y == col-1) { 
			//right vertical border. two incoming edges.
			double a1 = distTo[x-1][y-1];
			double a2 = distTo[x-1][y];
			if (a1 <= a2) {
				distTo[x][y] = a1 + weight;
				edgeTo[x][y] = y-1;
			} else {
				distTo[x][y] = a2 + weight;
				edgeTo[x][y] = y;
			}
		} else {
			//three incoming edges from upper layer
			//(x-1, y-1), (x-1, y), (x-1, y+1)
			double a1 = distTo[x-1][y-1];
			double a2 = distTo[x-1][y];
			double a3 = distTo[x-1][y+1];
		
			if (a1 <= a2 && a1 <= a3) {
				distTo[x][y] = a1 + weight;
				edgeTo[x][y] = y-1;
			} else if (a2 <= a1 && a2 <= a3) {
				distTo[x][y] = a2 + weight;
				edgeTo[x][y] = y;
			} else {
				distTo[x][y] = a3 + weight;
				edgeTo[x][y] = y+1;
			}
		}
	}
	
	// distance to (x, y) or pixel (y, x)
	public double distTo(int x, int y) {
		return distTo[x][y];
	}
	
	// a path to (x, y) or pixel (y, x)?
	public boolean hasPathTo(int x, int y) {
		return distTo[x][y] < Double.POSITIVE_INFINITY;
	}
	
	// path to (x, y) or pixel (y, x)
	// start from first row (height), track the column (width) index
	public Iterable<Integer> pathTo(int x, int y) {
		if (!hasPathTo(x, y)) return null;
		Stack<Integer> path = new Stack<Integer>();
		
		path.push(y); //push the column index or width index
		int i = x;
		for (int s = edgeTo[x][y]; s != -1; s = edgeTo[--i][s]) {
			path.push(s);
		}
		
		return path;
	}
}
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
import java.awt.Color;

public class SeamCarver {

	private Picture pic;
	
	private int col; //picture width
	private int row; //picture height
	
	//use 2D-Array to store and calculate, rather than the one used by a picture
	//row - height, col - width
	private double[][] energies; // Energy of pixels
	
	// constructor
	public SeamCarver(Picture picture) {
		this.pic = picture;
		this.col = picture.width();
		this.row = picture.height();
		energies = new double[row][col];
		
		for (int i = 0; i < row; i++)
			for (int j = 0; j < col; j++) 
				//energy function takes width and height as parameters
				//i.e., col and row
				energies[i][j] = energy(j, i);
	}
	
	// current picture
	public Picture picture() {
		return pic;
	}
	
	// width of current picture (number of columns)
	public int width() {
		return col;
	}
	
	// height of current picture (number of rows)
	public int height() {
		return row;
	}
	
	// calculate x-gradient of (x, y) in 2D array
	private double xGradient(int x, int y) {
		Color leftColor = pic.get(y-1, x);
		Color rightColor = pic.get(y+1, x);
		int xR = leftColor.getRed() - rightColor.getRed();
		int xG = leftColor.getGreen() - rightColor.getGreen();
		int xB = leftColor.getBlue() - rightColor.getBlue();
		return (xR * xR + xG * xG + xB * xB);
	}
	
	// calculate y-gradient of (x, y) in 2D array
	private double yGradient(int x, int y) {
		Color upColor = pic.get(y, x-1);
		Color downColor = pic.get(y, x+1);
		int yR = upColor.getRed() - downColor.getRed();
		int yG = upColor.getGreen() - downColor.getGreen();
		int yB = upColor.getBlue() - downColor.getBlue();
		return (yR * yR + yG * yG + yB * yB);
	}
	
	// energy of pixel at column y and row x
	// will be called by width and height
	public double energy(int y, int x) {
		if (x < 0 || x >= row)
			throw new java.lang.IndexOutOfBoundsException();
		if (y < 0 || y >= col)
			throw new java.lang.IndexOutOfBoundsException();
		// border cases
		if ((x == 0) || (x == row - 1))
			return 195075;
		if ((y == 0) || (y == col - 1))
			return 195075;
		
		return xGradient(x, y) + yGradient(x, y);
	}
	
	// transpose a 2D array
	private double[][] transpose(double[][] array, int row, int col) {
		double[][] tran = new double[col][row];
		for (int i = 0; i < row; i++)
			for (int j = 0; j < col; j++) {
				tran[j][i] = array[i][j];
			}
		
		return tran;
	}
	
	// sequence of indices for horizontal seam
	public int[] findHorizontalSeam() {
		// transpose image
		// call findVerticalSeam
		// tranpose image back to orignal

		// transpose energy array: solve it in vertical way
		double[][] tran = transpose(energies, row, col);
		VerticalSeam vs = new VerticalSeam(tran, col, row);
		
		// find minimum distance
		// now number of rows: col
		// and number of columns: row
		int[] paths = new int[col];
		double minDist = Double.POSITIVE_INFINITY;
		int colIndex = row;
		for (int i = 0; i < row; i++) {
			// distance from virtual source to element in last row
			double distance = vs.distTo(col-1, i);
			if (distance <= minDist) {
				minDist = distance;
				colIndex = i;
			}	
		}
		
		// retrieve the shortest path
		int j = 0;
		for (int v : vs.pathTo(col-1, colIndex))
			paths[j++] = v;
		
		return paths;
	}
	
	// sequence of indices for vertical seam
	public int[] findVerticalSeam() {
		// use energy array
		VerticalSeam vs = new VerticalSeam(energies, row, col);
		
		// find minimum distance
		int[] paths = new int[row]; //height or rows
		double minDist = Double.POSITIVE_INFINITY;
		int colIndex = col;
		for (int i = 0; i < col; i++) {
			// distance from virtual source to element in last row
			double distance = vs.distTo(row-1, i);
			if (distance <= minDist) {
				minDist = distance;
				colIndex = i;
			}	
		}
		
		// retrieve the shortest path
		int j = 0;
		for (int v : vs.pathTo(row-1, colIndex))
			paths[j++] = v;
		
		return paths;
	}
	
	private void reBuildEnergies() {
		this.col = pic.width();
		this.row = pic.height();
		this.energies = new double[row][col];
		
		for (int i = 0; i < row; i++)
			for (int j = 0; j < col; j++) 
				//energy function takes width and height as parameters
				//i.e., col and row
				energies[i][j] = energy(j, i);
	}
	
	// remove horizontal seam from picture
	public void removeHorizontalSeam(int[] a) {
		//check width and height
		if ((row == 0) || (col == 0))
			throw new java.lang.IllegalArgumentException();
		
		//seam length
		if (col != a.length)
			throw new java.lang.IllegalArgumentException();
		
		//check whether a valid seam
		for (int i = 0; i < a.length - 1; i++)
			if (Math.abs(a[i]-a[i+1]) > 1)
				throw new java.lang.IllegalArgumentException();
		
		//new picture will one row (height) less
		Picture newpic = new Picture(col, row-1);
		for (int i = 0; i < col; i++) {
			//remove pixel (i, a[i])
			
			//pixels above pixel (i, a[i]) is unchanged
			for (int j = 0; j < a[i]; j++)
				newpic.set(i, j, pic.get(i, j));
			
			//move pixels below pixel (i, a[i]) one pixel up
			for (int j = a[i]; j < row-1; j++)
				newpic.set(i, j, pic.get(i, j+1));
		}
		
		this.pic = newpic;
		reBuildEnergies(); //update colors and energies
	}
	
	// remove vertical seam from picture
	public void removeVerticalSeam(int[] a) {
		//check width and height
		if ((row == 0) || (col == 0))
			throw new java.lang.IllegalArgumentException();
		
		//seam length
		if (row != a.length)
			throw new java.lang.IllegalArgumentException();
		
		//check whether a valid seam
		for (int i = 0; i < a.length - 1; i++)
			if (Math.abs(a[i]-a[i+1]) > 1)
				throw new java.lang.IllegalArgumentException();
		
		//new picture will one column (width) less
		Picture newpic = new Picture(col-1, row);
		for (int i = 0; i < row; i++) {
			//remove pixel (a[i], i)
			
			//pixels on pixel (a[i], i)'s left is unchanged
			for (int j = 0; j < a[i]; j++)
				newpic.set(j, i, pic.get(j, i));
			
			//move pixels on pixel (a[i], i)'s right one pixel left
			for (int j = a[i]; j < col-1; j++)
				newpic.set(j, i, pic.get(j+1, i));
		}
		
		this.pic = newpic;
		reBuildEnergies(); //update colors and energies
	}
}

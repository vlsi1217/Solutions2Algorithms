/**
 *  File: Percolation.java
 *  Package: 
 *  Project: alg-Percolation
 *  Created on: Feb 12, 2013
 *
 *  @author: Huang Zhu
 *  Department of Computing and Information Sciences
 *  Kansas State University
 *  
 *  Compilation:  javac Percolation.java
 *  Execution:    java Percolation N (N is a positive integer)
 *  Dependencies: WeightedQuickUnionUF.java StdOut.java
 */

/**
 * Percolation. This class provides methods to handling percolation problems of a square grid system.
 */

//Feedback
//Lose points for: Bug, Memory Usage, Code Style.
//Bugs (one-site-grid and backwash bugs) are introduced because of usage of virtual top site and virtual bottom site. 
//Memory waste because of two extra rows and two extra columns.

//"backwash" bug: connectivity through virtualBottom to full sites and finally to virtualTop.
//one-site-grid bug: percolates when a grid of only one unopened site.


public class OldPercolation {
	
	//Maintains connectivity relationship between different sites (Use 1D array)
	private WeightedQuickUnionUF uf;
	
	//Maintains open status of sites in the grid.
	private boolean[][] open;
	
	//Row or column size of a square grid (2D)
	//Not including two extra rows and two extra columns added for programming convenience.
	private int gridsize;
	
	//Row or column size of grid representation in the program.
	//Including two extra rows and two extra columns added for programming convenience.
	private int virtualsize;
	
	//index of virtual top site in uf. open in default.
	private int virtualTop;
	
	//index of virtual bottom site in uf. open in default.
	private int virtualBottom;

	/**
	 * Class Constructor. It does:
	 *     - initialize all sites in the grid to be blocked. 
	 *     - use WeightedQuickUnionUF to support dynamic connectivity actions.
	 *     - use two extra rows and two extra columns to simplify programming codes.
	 *     - introduce two virtual sites: top and bottom.
	 *     - connect sites in top row to virtual top, and sites in bottom row to virtual bottom.
	 * @param N - N by N grid of sites (Use 2D array)
	 */
	public OldPercolation(int N){
		if(N<=0) throw new java.lang.IllegalArgumentException("illegal argument");
		
		gridsize = N;
		virtualsize = N+2;
		virtualTop = (N+2)*(N+2);
		virtualBottom = (N+2)*(N+2)+1;
		
		//row 0 and column 0 are useless.
		open = new boolean[N+2][N+2];
		for(int i=0; i<N+2; i++)
			for(int j=0; j<N+1; j++)
				open[i][j] = false;
		
		//one-to-one matching from 2D grid to 1D array, plus a virtual top and a virtual bottom site.
		uf = new WeightedQuickUnionUF((N+2)*(N+2)+2);
		for(int i=1; i<=N; i++)
		{
			int topsite = xyTo1D(1, i); //sites in top row
			int botsite = xyTo1D(N, i); //sites in bottom row
			uf.union(topsite, virtualTop); //connect sites in top row to virtual top site
			uf.union(botsite, virtualBottom); //connect sites in bottom row to virtual bottom site
		}
	}
	
	/**
	 * Given indices of an element in a 2D array, convert them into a identification in 1D array.
	 * @param i - row index in a 2D array. has value between 1 and N.
	 * @param j - column index in a 2D array. has value between 1 and N.
	 * @return identification in 1D array
	 */
	private int xyTo1D(int i, int j){
		return i * virtualsize + j;
	}
	
	/**
	 * Check whether indices of site (i,j) are out of bound. Throw java.lang.IndexOutOfBoundsException when out of bound.
	 * @param i - row index in a 2D array. has value between 1 and N.
	 * @param j - column index in a 2D array. has value between 1 and N.
	 */
	private void checkIndex(int i, int j){
		if(i < 1 || i > gridsize) throw new java.lang.IndexOutOfBoundsException("row index out of bound");
		if(j < 1 || j > gridsize) throw new java.lang.IndexOutOfBoundsException("column index out of bound");
	}
	
	/**
	 * Open site (i, j).
	 * @param i - row index in a 2D array. has value between 1 and N.
	 * @param j - column index in a 2D array. has value between 1 and N.
	 */
	public void open(int i, int j){
		checkIndex(i,j);
		if(!open[i][j]){
			open[i][j] = true;

			int id = xyTo1D(i,j);
			int idleft = xyTo1D(i,j-1);
			int idright = xyTo1D(i,j+1);
			int idup = xyTo1D(i-1,j);
			int iddown = xyTo1D(i+1,j);
			
			//connect site (i,j) to its neighbors if they are open
			if(open[i][j-1])
				uf.union(id, idleft);
			if(open[i][j+1])
				uf.union(id, idright);
			if(open[i-1][j])
				uf.union(id, idup);
			if(open[i+1][j])
				uf.union(id, iddown);
		}
	}
	
	/**
	 * Check whether site (i, j) is open.
	 * @param i - row index in a 2D array. has value between 1 and N.
	 * @param j - column index in a 2D array. has value between 1 and N.
	 * @return true if site (i, j) is open; otherwise, false.
	 */
	public boolean isOpen(int i, int j){
		checkIndex(i,j);
		return open[i][j];
	}
	
	/**
	 * Check whether site (i,j) is a full site.
	 * A full site is an open site that can be connected to an open site in the top row via a chain of neighboring open sites.
	 * @param i - row index in a 2D array. has value between 1 and N.
	 * @param j - column index in a 2D array. has value between 1 and N.
	 * @return true if site (i, j) is a full site; otherwise, false.
	 */
	public boolean isFull(int i, int j){
		checkIndex(i,j);
		
		//check whether site (i,j) is connected to virtualTop
		if(open[i][j]){
			int id = xyTo1D(i,j);
			return uf.connected(id, virtualTop);
		}
		return false;
	}
	
	/**
	 * Check whether the grid system percolates. A grid system percolates if virtualTop and virtualBottom are connected.
	 * @return true if the grid system percolates; otherwise, false.
	 */
	public boolean percolates(){
		return uf.connected(virtualTop, virtualBottom);
	}
	
	/**
	 * Unit Test
	 * @param args - Take a positive integer N as the grid size.
	 */
	public static void main(String[] args) {
		int Size = Integer.parseInt(args[0]);
		Percolation per1 = new Percolation(Size);
		Percolation per2 = new Percolation(Size);
		for(int i=1; i<=Size; i++)
			per1.open(i,i);
		StdOut.println("Does Percolation System One Percolates: " + per1.percolates());
		
		for(int i=1; i<=Size; i++)
			per2.open(i,1);
		StdOut.println("Does Percolation System Two Percolates: " + per2.percolates());
	}
}

/**
 * File: Percolation.java Package: Project: alg-Percolation Created on: Feb 12,
 * 2013
 * 
 * Description: Programming Assignment 1
 * 
 * @author: Huang Zhu Department of Computing and Information Sciences Kansas
 *          State University
 */

public class Percolation {
    // maintain connectivity relationship between different sites
    // to determine percolates()
    private WeightedQuickUnionUF ufPercolate;

    // to determine isFull()
    private WeightedQuickUnionUF ufFull;

    // maintains open status of sites in the grid
    private boolean[] open;

    // row or column size of a square grid (2D)
    private int gridSize;

    // row or column size of grid representation
    // include extra rows or columns
    // private int virtualsize;

    // index of virtual top site in uf
    private int virtualTop;

    // index of virtual bottom site in uf
    private int virtualBottom;

    public Percolation(int N) {
        if (N <= 0)
            throw new java.lang.IllegalArgumentException();

        gridSize = N;
        virtualTop = N * N;
        virtualBottom = N * N + 1;

        open = new boolean[N * N + 2];
        for (int i = 0; i < N * N + 2; i++)
            open[i] = false;

        // one-to-one matching from 2D grid to 1D array
        // include a virtual top and a virtual bottom site
        ufPercolate = new WeightedQuickUnionUF(N * N + 2);
        ufFull = new WeightedQuickUnionUF(N * N + 2);

        // To handle one-site-grid bug
        // Connect only when sites in top row or bottom row are opened
        /*
         * for (int i = 1; i <= N; i++) { //sites in top row int topsite =
         * xyTo1D(1, i); //connect sites in top row to virtual top site
         * uf.union(topsite, virtualTop);
         * 
         * //sites in bottom row int botsite = xyTo1D(N, i);
         * 
         * //connect sites in bottom row to virtual bottom site
         * uf.union(botsite, virtualBottom); }
         */

    }

    /**
     * 2D array to 1D array convertion 1 <= i, j <= N
     */
    private int xyTo1D(int i, int j) {
        int x = i - 1; // array index: 0 <= x <= N-1
        int y = j - 1; // array index: 0 <= y <= N-1
        if (x < 0 || x > gridSize - 1)
            return -1;
        if (y < 0 || y > gridSize - 1)
            return -1;
        return x * gridSize + y;
    }

    /**
     * Check whether site (i,j) is out of bound
     */
    private void checkIndex(int i, int j) {
        if (i < 1 || i > gridSize)
            throw new java.lang.IndexOutOfBoundsException();
        if (j < 1 || j > gridSize)
            throw new java.lang.IndexOutOfBoundsException();
    }

    /**
     * Open site (i, j)
     */
    public void open(int i, int j) {
        checkIndex(i, j);

        int id = xyTo1D(i, j);

        // neighbours of (i, j)
        int idleft = xyTo1D(i, j - 1);
        int idright = xyTo1D(i, j + 1);
        int idup = xyTo1D(i - 1, j);
        int iddown = xyTo1D(i + 1, j);

        // is it reopen an open site possible?
        open[id] = true;

        // sites in top or bottom row connect to virtual top or bottom
        if (i == 1) {
            ufPercolate.union(id, virtualTop);
            ufFull.union(id, virtualTop);
        }
        if (i == gridSize)
            ufPercolate.union(id, virtualBottom);

        // connect site (i,j) to its neighbors if they are open
        if (idleft != -1)
            if (open[idleft]) {
                ufPercolate.union(id, idleft);
                ufFull.union(id, idleft);
            }
        if (idright != -1)
            if (open[idright]) {
                ufPercolate.union(id, idright);
                ufFull.union(id, idright);
            }
        if (idup != -1)
            if (open[idup]) {
                ufPercolate.union(id, idup);
                ufFull.union(id, idup);
            }
        if (iddown != -1)
            if (open[iddown]) {
                ufPercolate.union(id, iddown);
                ufFull.union(id, iddown);
            }
    }

    /**
     * Check whether site (i, j) is open
     */
    public boolean isOpen(int i, int j) {
        checkIndex(i, j);
        return open[xyTo1D(i, j)];
    }

    /**
     * Check whether site (i,j) is a full site
     */
    public boolean isFull(int i, int j) {
        checkIndex(i, j);
        int id = xyTo1D(i, j);

        // check whether site (i,j) is connected to virtualTop
        // "backwash" bug exist
        if (open[id]) {
            return ufFull.connected(id, virtualTop);
        }
        return false;
    }

    /**
     * Check whether the grid system percolates
     */
    public boolean percolates() {
        return ufPercolate.connected(virtualTop, virtualBottom);
    }

    /**
     * Unit Test
     */
    public static void main(String[] args) {
        int size = Integer.parseInt(args[0]);
        Percolation per1 = new Percolation(size);
        Percolation per2 = new Percolation(size);
        for (int i = 1; i <= size; i++)
            per1.open(i, i);
        StdOut.println("per1 Percolates: " + per1.percolates());

        for (int i = 1; i <= size; i++)
            per2.open(i, 1);
        StdOut.println("per2 Percolates: " + per2.percolates());
    }
}

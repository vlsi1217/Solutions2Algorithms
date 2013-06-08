/**
 *  File: PercolationStats.java
 *  Package: 
 *  Project: alg-Percolation
 *  Created on: Feb 12, 2013
 *
 *  Description: Programming Assignment 1
 *  
 *  @author: Huang Zhu
 *  Department of Computing and Information Sciences
 *  Kansas State University
 */

public class PercolationStats {
	//threshold values of percolation
	private double[] threshold;
	
	//running time of PercolationStats
	//private double runtime;
	
	//average of threshold values
	private double mean;
	
	//standard deviation of threshold values
	private double stddev;
	
	//lower bound of 95% confidence interval
	private double conflower;
	
	//higher bound of 95% confidence interval
	private double confhigher;
	
	public PercolationStats(int N, int T) {
		if (N <= 0 || T <= 0) 
			throw new java.lang.IllegalArgumentException();
		
		//measure running time
		//Stopwatch stopwatch = new Stopwatch();
	
		threshold = new double[T];
		  
		//percolation to be evaluated
	    Percolation percolation;
	    int opencount;
		
		//T independent experiments
		for (int i = 0; i < T; i++) {
			//initialize all sites. 
			percolation = new Percolation(N);
			opencount = 0;
		
			while (!percolation.percolates()) {
				//open a random site
				percolation.open(StdRandom.uniform(1, N+1), 
						StdRandom.uniform(1, N+1));
			}
			
			//count the number of open sites
			for (int m = 1; m <= N; m++)
				for (int n = 1; n <= N; n++)
					if (percolation.isOpen(m, n))
						opencount++;
			
			threshold[i] = (double) opencount / (N * N);
		}
		
		mean = StdStats.mean(threshold);
		stddev = StdStats.stddev(threshold);
		conflower = mean - 1.96 * stddev / Math.sqrt(T);
		confhigher = mean + 1.96 * stddev / Math.sqrt(T);
		//runtime = stopwatch.elapsedTime();
	}
	
	/**
	 * mean of percolation threshold.
	 */
	public double mean() { return mean; }
	
	/**
	 * standard deviation of percolation threshold.
	 */
	public double stddev() { return stddev; }
	
	/**
	 * lower bound of of the 95% confidence interval
	 */
	public double confidenceLo() { return conflower; }
	
	/**
	 * higher bound of of the 95% confidence interval
	 */
	public double confidenceHi() { return confhigher; }
	
	
	/**
	 * Unit Test
	 */
	public static void main(String[] args) {
		int size = Integer.parseInt(args[0]);
		int times = Integer.parseInt(args[1]);
		
		PercolationStats perstat = new PercolationStats(size, times);
		StdOut.println("mean                    = " + perstat.mean());
		StdOut.println("stddev                  = " + perstat.stddev());
		StdOut.println("95% confidence interval = " 
		+ perstat.confidenceLo() + ", " + perstat.confidenceHi());
		//StdOut.println("Total Running Time of PercolationStats: " 
		//+ perstat.runtime + " seconds");
	}
}

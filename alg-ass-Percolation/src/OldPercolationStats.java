/**
 *  File: PercolationStats.java
 *  Package: 
 *  Project: alg-Percolation
 *  Created on: Feb 12, 2013
 *
 *  @author: Huang Zhu
 *  Department of Computing and Information Sciences
 *  Kansas State University
 *  
 *  Compilation:  javac PercolationStats.java
 *  Execution:    java PercolationStats N T (N and T are positive integers)
 *  Dependencies: Percolation.java StdRandom.java StdStats.java Stopwatch.java StdOut.java
 */

/**
 * PercolationStats. This class provides methods to get statistics information of percolation threshold.
 */
public class OldPercolationStats {
	//Percolation to be evaluated
	private Percolation percolation;
	
	//Threshold values from experiments that make a grid system percolates
	private double[] threshold;
	
	//The running time of PercolationStats
	private double runtime;
	
	//Average of threshold values.
	private double mean;
	
	//Standard deviation of threshold values.
	private double stddev;
	
	//Lower bound of 95% confidence interval of threshold values.
	private double conflower;
	
	//Higher bound of 95% confidence interval of threshold values.
	private double confhigher;
	
	/**
	 * Perform T independent computational experiments on an N-by-N grid.
	 * @param N - row or column size of the grid system
	 * @param T - number of experiments
	 */
	public OldPercolationStats(int N, int T){
		if(N<=0 || T<=0) throw new java.lang.IllegalArgumentException("illegal argument");
		
		//Measure running time
		Stopwatch stopwatch = new Stopwatch();
	
		threshold = new double[T];
		
		//T independent experiments
		for(int i=0; i<T; i++){
			//initialize all sites. 
			//better if we have an initialize method in Percolation.
			//but we can't add public methods on our own because the requirement doesn't allow us
			percolation = new Percolation(N);

			int opencount = 0;
		
			while(!percolation.percolates()){
				//open a random site
				int randomx = StdRandom.uniform(1, N+1);
				int randomy = StdRandom.uniform(1, N+1);
				percolation.open(randomx, randomy);
			}
			
			//count the number of open sites
			//again, better if we have a support method directly from Percolation
			for(int m=1; m<=N; m++)
				for(int n=1; n<=N; n++)
					if(percolation.isOpen(m,n))
						opencount++;
			
			threshold[i] = (double)opencount/(N*N);
		}
		
		mean = StdStats.mean(threshold);
		stddev = StdStats.stddev(threshold);
		conflower = mean - 1.96 * stddev/Math.sqrt(T);
		confhigher = mean + 1.96 * stddev/Math.sqrt(T);
		runtime = stopwatch.elapsedTime();
	}
	
	/**
	 * Sample mean of percolation threshold.
	 * @return mean of percolation threshold
	 */
	public double mean(){return mean;}
	
	/**
	 * Sample standard deviation of percolation threshold.
	 * @return standard deviation of percolation threshold
	 */
	public double stddev(){return stddev;}
	
	/**
	 * Lower bound of of the 95% confidence interval of percolation threshold.
	 * @return lower bound of the 95% confidence interval
	 */
	public double confidenceLo(){return conflower;}
	
	/**
	 * Higher bound of of the 95% confidence interval of percolation threshold.
	 * @return higher bound of the 95% confidence interval
	 */
	public double confidenceHi(){return confhigher;}
	
	
	/**
	 * Unit Test
	 * @param args - Take two integer arguments N and T
	 * N - row or column size of the grid system
	 * T - number of experiments
	 */
	public static void main(String[] args) {
		int Size = Integer.parseInt(args[0]);
		int Times = Integer.parseInt(args[1]);
		
		OldPercolationStats perstat = new OldPercolationStats(Size, Times);
		StdOut.println("mean                    = " + perstat.mean());
		StdOut.println("stddev                  = " + perstat.stddev());
		StdOut.println("95% confidence interval = " + perstat.confidenceLo() + ", " + perstat.confidenceHi());
		StdOut.println("Total Running Time of PercolationStats: " + perstat.runtime + " seconds");
	}
}

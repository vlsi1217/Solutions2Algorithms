/**
 *  File: Subset.java
 *  Package: 
 *  Project: alg-Subset
 *  Created on: Feb 24, 2013
 * 
 *  Programming Assignment 2
 *  Description: Take a command-line integer k, reads in a sequence 
 *               of N strings from standard input, print out exactly 
 *               k of them, uniformly at random.
 *               
 *  How to Run: java Subset k
 *              Here, k is an integer greater or equal to 0.
 *
 *  @author: Huang Zhu
 *  Department of Computing and Information Sciences
 *  Kansas State University
 *
 */

public class Subset {
	public static void main(String[] args) {
		
		//k>=0 and k<=string.length
		if (args.length <= 0) {
			StdOut.println("Error: expect a non-negtive " 
		                    + "command-line integer!");
			throw new java.lang.IllegalArgumentException();
		}
		
		int k = 0;
		
		try {
			k = Integer.parseInt(args[0]);
			if (k < 0) {
				StdOut.println("Error: expect a non-negtive " 
			                    + "command-line integer!");
				throw new java.lang.IllegalArgumentException();
			}
		} catch (NumberFormatException e) {
			StdOut.println("Error: expect a non-negtive "
					        + "command-line integer!");
			throw new java.lang.IllegalArgumentException();
		}
		
		RandomizedQueue<String> rqueue = new RandomizedQueue<String>();

		while (!StdIn.isEmpty()) {
			rqueue.enqueue(StdIn.readString());
		}
		
		int count = 0;
		int length = rqueue.size();
		for (String s: rqueue) {
			if (count < k && count < length) {
				StdOut.println(s);
				count++;
			} else 
				break;
		}
	}
}

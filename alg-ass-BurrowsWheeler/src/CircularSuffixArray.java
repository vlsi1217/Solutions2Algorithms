import java.util.Arrays;

/**
 *  File: CircularSuffixArray.java
 *  Package: 
 *  Project: alg-ass-BurrowsWheeler
 *  Created on: May 6, 2013
 * 
 *  Description: Programming Assignment 4, Algorithms, Part II
 *
 *  @author: Huang Zhu
 *  Department of Computing and Information Sciences
 *  Kansas State University
 */

public class CircularSuffixArray {
	//index of the ith sorted suffix string in the original suffixes array
	private int[] index;
	private int size;
	
	private class SuffixNode implements Comparable<SuffixNode> {
		private String s; //the original string (doubled)
		private int start; //start index of this suffix in s
		
		public SuffixNode(String s, int start) {
			this.s = s;
			this.start = start;
		}
		
		public int getStart() {
			return start;
		}

		public int compareTo(SuffixNode o) {
			int i = this.getStart();
			int j = o.getStart();
			return s.substring(i, i + size).compareTo(s.substring(j, j + size));
		}
	}
	
	// circular suffix array of s
	public CircularSuffixArray(String s) {
		size = s.length();
		index = new int[size];
		
		//concatenate the same string
		//no need to create a suffix array
		//substring(i, i+size) is the ith suffix in the original suffixes array
		String doubleStr = s.concat(s);

		SuffixNode[] suffix = new SuffixNode[size];
		for (int i = 0; i < size; i++) {
			suffix[i] = new SuffixNode(doubleStr, i);
		}
		
		Arrays.sort(suffix);
		
		for (int i = 0; i < size; i++) {
			index[i] = suffix[i].getStart();
		}
		
		//selection sort: ~ N*N
		/*for (int i = 0; i < size; i++) {
			int min = i;
			for (int j = i+1; j < size; j++) {
				if (doubleStr.substring(index[j], index[j]+size).compareTo(
						doubleStr.substring(index[min], index[min]+size)) < 0) {
					min = j;
				}	
			}
			int temp = index[i];
			index[i] = index[min];
			index[min] = temp;
		}*/
		
		//for debugging
		/*for (int i = 0; i < size; i++) {
			StdOut.printf("%2d  %s  %s  %d\n", i, doubleStr.substring(i, i+size), 
					doubleStr.substring(index[i], index[i]+size), index[i]);
		}*/
	}
	
	// length of s
	public int length() {
		return size;
	}
	
	// returns index of ith sorted suffix
	public int index(int i) {
		return index[i];
	}
	
	// unit test
	public static void main(String args[]) {
		String s = BinaryStdIn.readString();
		new CircularSuffixArray(s);
	}
}

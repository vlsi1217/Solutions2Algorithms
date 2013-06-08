import java.util.Arrays;

/**
 *  File: BurrowsWheeler.java
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

public class BurrowsWheeler {
	//if we can use additional data members, the problem will be easier
	//easy to construct next using two loops over index[i]
	//private static int size;
	//private static char[] t;
	//private static int[] next;
	//private CircularSuffixArray csa;
	
	// alphabet size
	private static final int R = 256; 

	// apply Burrows-Wheeler encoding, reading from standard input and writing to standard output
	public static void encode() {
		String input = BinaryStdIn.readString();
		
		//don't create the original suffixes array, a string is enough
		String doubleStr = input.concat(input);
		
		CircularSuffixArray csa = new CircularSuffixArray(input);
		int size = csa.length();
		char[] t = new char[size];
		int first = 0;
		
		for (int i = 0; i < size; i++) {
			//the ind'th suffix in original suffixes array
			//doubleStr.substring(ind, ind+size)
			int ind = csa.index(i);
			if (ind == 0) 
				first = i;
			
			//the end character of the ind'th suffix
			t[i] = doubleStr.charAt(ind + size - 1);
		}
		
		//write first and t[] to stdout
		BinaryStdOut.write(first);
		for (int i = 0; i < size; i++)
			BinaryStdOut.write(t[i]);
		BinaryStdOut.close();
	}
	
	// apply Burrows-Wheeler decoding, reading from standard input and writing to standard output
	public static void decode() {
		int first = BinaryStdIn.readInt();
		String t = BinaryStdIn.readString();
		char[] sorted = t.toCharArray();	
		int size = t.length();
		int[] next = new int[size];
		
		//the most recent index of each character
		//start point to search for c: index[c] + 1
		int[] index = new int[R]; 
		for (int i = 0; i < R; i++) {
			index[i] = -1;
		}
		
		//use t[] and first to construct next
		Arrays.sort(sorted);
		String st = new String(sorted);
		
		for (int i = 0; i < size; i++) {
			char c = st.charAt(i);
			//search for c from the most recent found address
			int firstIndex = t.indexOf(c, index[c]+1);
			next[i] = firstIndex;
			index[c] = firstIndex;
		}
		
		//for debugging
		/*for (int i = 0; i < size; i++) {
			StdOut.println(next[i]);
		}*/
		
		//use t[], first, next[] to decode
		char[] original = new char[size];
		int nextChar = first;
		for (int i = 0; i < size; i++) {
			original[i] = sorted[nextChar];
			nextChar = next[nextChar];
		}
		
		//write the originial string to stdout
		BinaryStdOut.write(new String(original));
		BinaryStdOut.close();
	}
	
	// if args[0] is '-', apply Burrows-Wheeler encoding
	// if args[0] is '+', apply Burrows-Wheeler decoding
	public static void main(String[] args) {
        if      (args[0].equals("-")) encode();
        else if (args[0].equals("+")) decode();
        else throw new IllegalArgumentException("Illegal command line argument");
	}
}

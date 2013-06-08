/**
 *  File: MoveToFront.java
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

public class MoveToFront {
	// alphabet size
	private static final int R = 256; 
	
	// apply move-to-front encoding, reading from standard input and writing to standard output
	public static void encode() {
		char[] charSequence = new char[R];
		for (int i = 0; i < R; i++)
			charSequence[i] = (char) i;
		
		char[] inputSequence = BinaryStdIn.readString().toCharArray();
		int size = inputSequence.length;
		for (int i = 0; i < size; i++) {
			//location of character in charSequence
			//write the location
			char c = inputSequence[i];
			int location = 0;
			for (int j = 0; j < R; j++) {
				if (c == charSequence[j]) {
					location = j;
					break;
				}
			}
			
			//locations printed correctly, but HexDump gives wrong result
			//StdOut.print((byte) location); 
			BinaryStdOut.write((byte) location);
			//results in more running time. use close() in the end.
			//BinaryStdOut.flush(); 
			
			/*if (location != 0) {
				String temp = new String(charSequence);
				String front = temp.substring(0, location);
				String end = temp.substring(location + 1, R);
				
				charSequence = (Character.toString(c)).concat(front).concat(end).toCharArray();
			}*/
			
			//move c to the front of charSequence
			for (int j = location; j > 0; j--) {
				charSequence[j] =  charSequence[j-1];
			}
			charSequence[0] = c;
		}
		BinaryStdOut.close();
	}
	
	// apply move-to-front decoding, reading from stdin and writing to stdout
	public static void decode() {
		char[] charSequence = new char[R];
		for (int i = 0; i < R; i++)
			charSequence[i] = (char) i;
		
		char[] inputSequence = BinaryStdIn.readString().toCharArray();
		int size = inputSequence.length;
		for (int i = 0; i < size; i++) {
			//location of character in charSequence
			int location = (int) inputSequence[i];
			char c = charSequence[location];
			
			//write the character
			BinaryStdOut.write(c);
			//results in more running time. use close() in the end.
			//BinaryStdOut.flush(); 
			
			//move c to the front of charSequence
			for (int j = location; j > 0; j--) {
				charSequence[j] =  charSequence[j-1];
			}
			charSequence[0] = c;
		}
		BinaryStdOut.close();
	}
	
	// if args[0] is '-', apply move-to-front encoding
	// if args[0] is '+', apply move-to-front decoding
	public static void main(String[] args) {
        if      (args[0].equals("-")) encode();
        else if (args[0].equals("+")) decode();
        else throw new IllegalArgumentException("Illegal command line argument");
	}
}

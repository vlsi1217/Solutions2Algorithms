/**
 *  File: Brute.java
 *  Package: 
 *  Project: alg-PatternRecognition
 *  Created on: Mar 2, 2013
 * 
 *  Description: Programming Assignment 3
 *
 *  @author: Huang Zhu
 *  Department of Computing and Information Sciences
 *  Kansas State University
 *
 */

import java.util.Arrays;

public class Brute {
	
	public static void main(String[] args) {
        //In inputFile = new In(args[0]); //input file
		In inputFile = new In("./collinear/input6.txt");
        
        int N = inputFile.readInt();
        Point[] points = new Point[N];
        
        StdDraw.clear();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
		StdDraw.setPenColor(StdDraw.BLACK);
        
        for (int i = 0; !inputFile.isEmpty() && i < points.length; i++) {
            points[i] = new Point(inputFile.readInt(), inputFile.readInt());
            points[i].draw();
        }
        
		StdDraw.setPenColor(StdDraw.BLUE);
           
        //Sort the points first
        Arrays.sort(points);
        
        //brute-force
        //prevent permutations by avoid setting different range to i, j, m, n
        for (int i = 0; i < points.length; i++)
            for (int j = i + 1; j < points.length; j++)
                for (int m = j + 1; m < points.length; m++)
                    for (int n = m + 1; n < points.length; n++) {
                    	//calculate slopes
                    	double ijSlope = points[i].slopeTo(points[j]);
                    	double imSlope = points[i].slopeTo(points[m]);
                    	double inSlope = points[i].slopeTo(points[n]);
                    	
                    	//four points form a line segment
                    	//four points are already in natural order
                    	if(ijSlope == imSlope && ijSlope == inSlope) {
                    		
                    		StdOut.println(points[i].toString() + " -> " + points[j].toString() +
            				" -> " + points[m].toString() + " -> " + points[n].toString());
            		
                    		points[i].drawTo(points[n]);   
                    	}
                    }
	}
}

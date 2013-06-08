/**
 *  File: Fast.java
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

public class Fast {
	
	public static void main(String[] args) {
        //In inputFile = new In(args[0]); //input file
		In inputFile = new In("./collinear/input8.txt");
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
        
        //Complexity: NlgN 
        //sort points in natural order
        Arrays.sort(points);
        
        //Complexity: N*N*lgN
    	//use points[i] as the basis
        for (int i = 0; i < points.length; i++) {

        	Point[] temp = new Point[points.length];
            for (int j = 0; j < points.length; j++) {
            	temp[j] = points[j];
            }
            
            //Complexity: NlgN            
        	//Sort other points according their slope to points[i]
            //IMPORTANT
            //if elements in temp has the same slope,
            //then their original natural order is kept
        	Arrays.sort(temp, points[i].SLOPE_ORDER);

        	//Debugging
        	/*StdOut.println("Base Point: " + points[i].toString());
        	int k =0;
        	while (k < temp.length) {
        		StdOut.print(temp[k].toString() + " ");
        		k++;
        	}
        	StdOut.println();*/
        	
        	//search temp to locate line segments with at least 4 points
        	int m = 0;
        	int n = m + 2;
        	while ((m < temp.length) && (n < temp.length)) {
        		//Debugging
        		//StdOut.println("Visiting element " + m +" and "+ n);
        		
        		//compare temp[m] and temp[n]
        		//their distance is at least two (n-m)
        		if (points[i].slopeTo(temp[m]) == 
        				points[i].slopeTo(temp[n])) {
        			n++;
        		} else {
        			int count = n-m;

        			//check whether m to n-1 cover no less than 3 points
        			if (count >= 3) {
            			//process a line segment only when visiting the smallest point in the segment
        				if (points[i].compareTo(temp[m]) < 0) {
            			    int j = 0;
            				StdOut.print(points[i].toString()); //first point
            				while (j < count) {
            					StdOut.print(" -> " + temp[m+j].toString());
            					j++;
            				}
            				StdOut.println();
            				
            				//draw a line
            				points[i].drawTo(temp[n-1]);
        				}

        				m = n;
        				n = m+2;
        			} else {
            			m++;
            			n = m+2;
        			}
        		}
        	}
        	
        	//marginal case
            //when the last three elements in temp have the same slope
			if ((n == temp.length) && (n-m >= 3))
			{
				//possible line segment in the end
				if (points[i].slopeTo(temp[m]) == points[i].slopeTo(temp[n-1]))
					//process a line segment only when visiting the smallest point in the segment
					if (points[i].compareTo(temp[m]) < 0) {
						int count = n - m;
					    int j = 0;
						StdOut.print(points[i].toString()); //first point
						while (j < count) {
							StdOut.print(" -> " + temp[m+j].toString());
							j++;
						}
						StdOut.println();
						points[i].drawTo(temp[n-1]);
					}
			}
        }
	}
}

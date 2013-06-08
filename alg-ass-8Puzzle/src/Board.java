/**
 *  File: Board.java
 *  Package: 
 *  Project: alg-8Puzzle
 *  Created on: Mar 11, 2013
 * 
 *  Description: Programming Assignment 4
 *
 *  @author: Huang Zhu
 *  Department of Computing and Information Sciences
 *  Kansas State University
 */

import java.util.Iterator;

public class Board {
	private int N;
	
	//pay attention to int and char conversion during calculation
	private char[] board; //store numbers as chars
	
	private Board(Board input) {
		N = input.N;
		board = new char[N * N];
		for (int i = 0; i < N * N; i++)
			board[i] = input.board[i];
	}
	
	//construct a board from an N-by-N array of blocks
	//blocks[i][j] = block in row i, column j
	public Board(int[][] blocks){
		int rowSize = blocks.length;
		int colSize = blocks[0].length;
		if (rowSize != colSize) 
			throw new java.lang.IllegalArgumentException();
		
		N = rowSize; //number of row of blocks
		board = new char[N * N];
		for (int i = 0; i < N; i++) 
			for (int j = 0; j < N; j++)
				board[i * N + j] = (char) blocks[i][j];
	}
	
	//board dimension N
	public int dimension(){
		return N;
	}
	
	//number of blocks out of place
	public int hamming() {
		int count = 0;
		for (int i = 0; i < N * N; i++) {
			if (board[i] != 0)
				if (i != (board[i] - 1))
					count++;
		}
		return count;
	}
	
	//sum of Manhattan distances between blocks and goal
	public int manhattan() {
		int count = 0;
		for (int i = 0; i < N * N; i++) {
			if (board[i] != 0) {
				//current position: i
				//goal position: board[i] - 1
				int icurr = i / N;
				int jcurr = i % N;
				int igoal = (board[i] - 1) / N;
				int jgoal = (board[i] - 1) % N;
				int distance = Math.abs(icurr - igoal) + Math.abs(jcurr - jgoal);
				count = count + distance;
			}
		}
		return count;
	}
	
	//is this board the goal board
	public boolean isGoal() {
		for (int i = 0; i < N * N - 1; i++) {
			if ((board[i] - 1) != i)
				return false;
		}
		return true;
	}
	
	//make a twin by swapping two adjacent blocks in the same row
	//blank doesn't count
	private void makeTwin() {
		while (true) {
			//select a random position to start with
			int pick = StdRandom.uniform(N * N);
			if (board[pick] == 0)
				continue;

			if (pick % N == 0) { //pick is in left border
				if (board[pick + 1] == 0)
					continue;
				exch(pick, pick + 1);
				break;
			} else if (pick % N == (N-1)) { //pick is in right border
				if (board[pick - 1] == 0)
					continue;
				exch(pick - 1, pick);
				break;
			} else { //pick is in the middle
				int left = StdRandom.uniform(2);
				if (left == 0) {
					if (board[pick + 1] == 0)
						continue;
					exch(pick, pick + 1);
					break;
				} else {
					if (board[pick - 1] == 0)
						continue;
					exch(pick - 1, pick);
					break;
				}
			}
		}
	}
	
	//exchange board[i] and board[j]
	private void exch(int i, int j) {
		char temp = board[i];
		board[i] = board[j];
		board[j] = temp;
	}
	
	//a board obtained by exchanging two adjacent blocks in the same row
	public Board twin() {
		//int[][] temp = new int[N][N];
		//for (int i = 0; i < N * N; i++)
			//temp[i/N][i%N] = (int)board[i];
		//Board twin = new Board(temp);
		Board twin = new Board(this);
		twin.makeTwin();
		return twin;
	}
	
	//does this board equal y
	public boolean equals(Object y) {
		if (y == this) return true;
		if (y == null) return false;
		if (y.getClass() != this.getClass()) return false;
		
		Board temp = (Board) y;
		for (int i = 0; i < N * N; i++) {
			if (this.board[i] != temp.board[i])
				return false;
		}
		return true;
	}
	
	//all neighboring boards
	public Iterable<Board> neighbors() {
		Queue<Board> q = new Queue<Board>();
		int zero = 0;
		for (int i = 0; i < N * N; i++) {
			if (board[i] == 0) {
				zero = i;
				break;
			}
		}
		
		Board b1 = new Board(this); //left
		Board b2 = new Board(this); //right
		Board b3 = new Board(this); //below
		Board b4 = new Board(this); //above
		
		if (zero / N == 0) { //first row
			//neighbor: below
			//if ((zero + N) >= 0 && (zero + N) < N * N) {
				b3.exch(zero, zero + N);
				q.enqueue(b3);
			//}
			
			if (zero % N == 0) { //first column
				//two neighbors: right, below
				//if ((zero + 1) >= 0 && (zero + 1) < N * N) {
					b2.exch(zero, zero + 1);
					q.enqueue(b2);
				//}
			} else if (zero % N == N - 1) { //last column
				//two neighbors: left, below
				//if ((zero - 1) >= 0 && (zero - 1) < N * N) {
					b1.exch(zero, zero - 1);
					q.enqueue(b1);
				//}
			} else {
				//three neighbors: left, right, below
				//if ((zero - 1) >= 0 && (zero - 1) < N * N) {
					b1.exch(zero, zero - 1);
					q.enqueue(b1);
				//}
				
				//if ((zero + 1) >= 0 && (zero + 1) < N * N) {
					b2.exch(zero, zero + 1);
					q.enqueue(b2);
				//}
			}
		} else if (zero / N == N - 1) { //last row
			//neighbor: above
			//if ((zero - N) >= 0 && (zero - N) < N * N) {
				b4.exch(zero, zero - N);
				q.enqueue(b4);
			//}
			
			if (zero % N == 0) { //first column
				//two neighbors: right, above
				//if ((zero + 1) >= 0 && (zero + 1) < N * N) {
					b2.exch(zero, zero + 1);
					q.enqueue(b2);
				//}
			} else if (zero % N == N - 1) { //last column
				//two neighbors: left, above
				//if ((zero - 1) >= 0 && (zero - 1) < N * N) {
					b1.exch(zero, zero - 1);
					q.enqueue(b1);
				//}
			} else {
				//three neighbors: left, right, above
				//if ((zero - 1) >= 0 && (zero - 1) < N * N) {
					b1.exch(zero, zero - 1);
					q.enqueue(b1);
				//}
				
				if ((zero + 1) >= 0 && (zero + 1) < N * N) {
					b2.exch(zero, zero + 1);
					q.enqueue(b2);
				}
			}
		} else {
			//neighbor: above
			//if ((zero - N) >= 0 && (zero - N) < N * N) {
				b4.exch(zero, zero - N);
				q.enqueue(b4);
			//}
			
			//neighbor: below
			//if ((zero + N) >= 0 && (zero + N) < N * N) {
				b3.exch(zero, zero + N);
				q.enqueue(b3);
			//}
			
			if (zero % N == 0) { //first column
				//three neighbors: right, above, below
				//if ((zero + 1) >= 0 && (zero + 1) < N * N) {
					b2.exch(zero, zero + 1);
					q.enqueue(b2);
				//}
			} else if (zero % N == N - 1) { //last column
				//three neighbors: left, above, below
				//if ((zero - 1) >= 0 && (zero - 1) < N * N) {
					b1.exch(zero, zero - 1);
					q.enqueue(b1);
				//}
			} else {
				//four neighbors: left, right, above, below
				//if ((zero - 1) >= 0 && (zero - 1) < N * N) {
					b1.exch(zero, zero - 1);
					q.enqueue(b1);
				//}
				
				//if ((zero + 1) >= 0 && (zero + 1) < N * N) {
					b2.exch(zero, zero + 1);
					q.enqueue(b2);
				//}
			}
		}
		
		return q;
	}
	
	//string representation of the board
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append(N + "\n");
		for (int i = 0; i < N * N; i++) {
			s.append(String.format("%2d ", (int)board[i]));
			if ((i % N) == (N-1))
				s.append("\n");
		}
		return s.toString();
	}
	

	public static void main(String[] args) {
		int[][] block = {{1,2,3},{4,0,5},{6,7,8}};
		Board board = new Board(block);
		System.out.println(board.toString());
		System.out.println("Board Dimension: " + board.dimension());
		System.out.println("Board Hamming: " + board.hamming());
		System.out.println("Board Manhattan: " + board.manhattan());
		System.out.println("Board is Goal: " + board.isGoal());
		
		System.out.println("\n......Board's Twin Start!");
		System.out.printf(board.twin().toString());
		System.out.printf(board.twin().toString());
		System.out.printf(board.twin().toString());
		System.out.printf(board.twin().toString());
		System.out.println("......Board's Twin End!");
		
		//System.out.println("......Board's Neighbor Start!");
		//for(Board temp : board.neighbors())
		    //System.out.printf(temp.toString());
		//System.out.println("......Board's Neighbor End!");
		
		System.out.println("......Board's Neighbor Start!");
		Iterator<Board> iter = board.neighbors().iterator();
		while(iter.hasNext()) {
			Board b = iter.next();
			System.out.printf(b.toString());
		}
		System.out.println("......Board's Neighbor End!");
	}
}

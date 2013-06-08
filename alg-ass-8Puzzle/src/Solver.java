/**
 *  File: Solver.java
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

public class Solver {
	//solution tree
	private Node root, twinroot;
	
	//use to trace back to root
	private Node goal, twingoal;
	
	//the goal board
	private Board goalBoard;
	
	//MinPQ
	private MinPQ<Node> minpq;
	
	//MinPQ
	private MinPQ<Node> twinminpq;
	
	//search node
	private class Node implements Comparable<Node> {
		private Board board;    //current board
		private Node prev;		//previous search node
		private int moves;		//number of moves so far
		private int pri;		//priority of this Node
		//private Node[] neighbors;    //at most four neighbors
		
		public Node(Board board, Node prev, int moves) {
			this.board = board;
			this.prev = prev;
			this.moves = moves;
			
			//compute priority
			pri = board.manhattan() + moves;
			
			//children or neighbor nodes
			//neighbors = new Node[4];
		}
		
		@Override
		public int compareTo(Node that) {
			if (this.pri < that.pri) return -1;
			if (this.pri > that.pri) return +1;
			return 0;
		}
		
		public void updatePriority() {
			pri = board.manhattan() + moves;
		}
	}
	
	//find a solution to the initial board
	public Solver(Board initial) {
		//Run A* algorithm simultaneously on two puzzle instances
		root = new Node(initial, null, 0);
		twinroot = new Node(initial.twin(), null, 0);
		goal = null;
		twingoal = null;
		minpq = new MinPQ<Node>();
		twinminpq = new MinPQ<Node>();
		
		//setup goal board
		int size = initial.dimension();
		int[][] block = new int[size][size];
		for (int i = 0; i < size; i++) 
			for (int j = 0; j < size; j++)
				block[i][j] = i * size + j + 1;
		block[size - 1][size - 1] = 0;
		goalBoard = new Board(block);
		
		//Debugging
		//StdOut.println("Goal Board...");
		//StdOut.println(goalBoard);
		
		//Debugging
		//StdOut.println("Start solving puzzle...");
		
		minpq.insert(root);
		twinminpq.insert(twinroot);
		while (!minpq.isEmpty()) {
			Node node = minpq.delMin();
			Node twinnode = twinminpq.delMin();
			//Debugging
			//StdOut.println(node.board);
			
			//StdOut.println(node.board);
			//reach the goal board, solution found
			if (node.board.equals(goalBoard)) {
				goal = node;
				break;
			}
			
			if (twinnode.board.equals(goalBoard)) {
				twingoal = twinnode;
				break;
			}
			
			//enqueue neighbor nodes of current search node
			Iterator<Board> iter = node.board.neighbors().iterator();
			Iterator<Board> twiniter = twinnode.board.neighbors().iterator();
			int i = 0, j = 0;
			
			while(iter.hasNext()) {
				Board nb = iter.next();
				//optimization: don't enqueue a neighbor if its board is 
				//the same as the board of the previous search node
				if (node.prev != null) {
					if (!nb.equals(node.prev.board)) {
						Node nn = new Node(nb, node, node.moves + 1);
						//node.neighbors[i] = nn;
						minpq.insert(nn);
						i++;
					}
				} else {
					Node nn = new Node(nb, node, node.moves + 1);
					//node.neighbors[i] = nn;
					minpq.insert(nn);
					i++;
				}
			}
			
			while(twiniter.hasNext()) {
				Board twinnb = twiniter.next();
				//optimization: don't enqueue a neighbor if its board is 
				//the same as the board of the previous search node
				if (twinnode.prev != null) {
					if (!twinnb.equals(twinnode.prev.board)) {
						Node twinnn = new Node(twinnb, twinnode, twinnode.moves + 1);
						//twinnode.neighbors[j] = twinnn;
						twinminpq.insert(twinnn);
						j++;
					}
				} else {
					Node twinnn = new Node(twinnb, twinnode, twinnode.moves + 1);
					//twinnode.neighbors[j] = twinnn;
					twinminpq.insert(twinnn);
					j++;
				}
			}
		}
		//Debugging
		//StdOut.println("End solving puzzle...");
	}
	
	//is the initial board solvable
	public boolean isSolvable() {
		if (twingoal != null) return false;
		return goal != null;
	}
	
	//min number of moves to solve initial board
	//-1 if no sulution
	public int moves() {
		if (goal != null)
			return goal.moves;
		else
			return -1;
	}
	
	//sequence of board is a shortest solution
	//null if no solution
	public Iterable<Board> solution() {
		if (isSolvable()) {
			Stack<Board> solution = new Stack<Board>();
			Node prev = goal;
			while (prev != null) {
				solution.push(prev.board);
				prev = prev.prev;
			}
			return solution;
		}
		return null;
	}
	
	public static void main(String[] args) {
		//create initial board from file
		In in = new In(args[0]);
		//In in = new In("./8puzzle/puzzle4x4-hard1.txt");
		int N = in.readInt();
		int[][] blocks = new int[N][N];
		for (int i = 0; i < N; i++)
			for (int j = 0; j < N; j++)
				blocks[i][j] = in.readInt();
		Board initial = new Board(blocks);
		
		//Debugging
		//StdOut.println("Given puzzle");
		//StdOut.println(initial);
		
		//solve the puzzle
		Solver solver = new Solver(initial);
		
		//print solution to standard output
		if (!solver.isSolvable()) 
			StdOut.println("No solution possible");
		else {
			StdOut.println("Minimum number of moves = " + solver.moves());
			for (Board board : solver.solution())
				StdOut.println(board);
		}
	}
}

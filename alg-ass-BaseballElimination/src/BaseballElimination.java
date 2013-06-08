/**
 *  File: BaseballElimination.java
 *  Package: 
 *  Project: alg-ass-BaseballElimination
 *  Created on: Apr 13, 2013
 * 
 *  Description: Programming Assignment 3, Algorithms II
 *
 *  @author: Huang Zhu
 *  Department of Computing and Information Sciences
 *  Kansas State University
 */

public class BaseballElimination {
	private ST<String, SET<String>> cache; // cache: teams and elimination certificates
	private ST<String, Integer> teamIDs; // team to id
	private String[] idTeams; //id to team
	private int N; //number of teams
	private int[] w; //wins
	private int[] l; //losses
	private int[] r; //remaining plays
	private int[][] g; //against
	private boolean[] marked; //evaluated
	private boolean[] e; //eliminated

	// create a baseball division from given filename in format specified
	public BaseballElimination(String filename) {
		In inFile = new In(filename);
		N = inFile.readInt();
		if (N <= 0)
			throw new java.lang.IllegalArgumentException();
		
		w = new int[N];
		l = new int[N];
		r = new int[N];
		g = new int[N][N];
		e = new boolean[N];
		marked = new boolean[N];
		teamIDs = new ST<String, Integer>();
		idTeams = new String[N];
		cache = new ST<String, SET<String>>();
		
		for (int i = 0; i < N; i++) {
			String name = inFile.readString();
			teamIDs.put(name, i);
			idTeams[i] = name;
			w[i] = inFile.readInt();
			l[i] = inFile.readInt();
			r[i] = inFile.readInt();
			
			for (int j = 0; j < N; j++) {
				g[i][j] = inFile.readInt();
			}
			
			// for debugging
			/*StdOut.print(name + " " + w[i] + " " + l[i] + " " + r[i] + "   ");
			for (int j = 0; j < N; j++) {
				StdOut.print(g[i][j] + " ");
			}
			StdOut.println();*/
		}
	}
	
	// number of teams
	public int numberOfTeams() {
		return N;
	}
	
	// all teams
	public Iterable<String> teams() {
		return teamIDs.keys();
	}
	
	// number of wins for given team
	public int wins(String team) {
		if (!teamIDs.contains(team))
			throw new java.lang.IllegalArgumentException();
		return w[teamIDs.get(team)];
	}
	
	// number of losses for given team
	public int losses(String team) {
		if (!teamIDs.contains(team))
			throw new java.lang.IllegalArgumentException();
		return l[teamIDs.get(team)];
	}
	
	// number of remaining games for given team
	public int remaining(String team) {
		if (!teamIDs.contains(team))
			throw new java.lang.IllegalArgumentException();
		return r[teamIDs.get(team)];
	}
	
	// number of remaining games between team1 and team2
	public int against(String team1, String team2) {
		if (!teamIDs.contains(team1))
			throw new java.lang.IllegalArgumentException();
		if (!teamIDs.contains(team2))
			throw new java.lang.IllegalArgumentException();
		
		return g[teamIDs.get(team1)][teamIDs.get(team2)];
	}
	
	// build a flow network
	private FlowNetwork buildFlowNetwork(int id) {
		// number of game vertices: (N-2)*(N-1)
		// number of team vertices: N-1
		// s and t: 2
		int gameV = (N-1)*(N-2)/2; // team pair. id is excluded
		int teamV = N-1; // team id. id is excluded
		int s = gameV + teamV;
		int t = gameV + teamV + 1;
		FlowNetwork G = new FlowNetwork(gameV + teamV + 2);
		
		//number of edges between gameV and teamV: gameV * 2 or teamV * (N-2)
		//game vertex range in network: 0 ~ (N-1)(N-2)/2 - 1
		//team vertex range in network: (N-1)(N-2)/2 ~ (N-1)(N-2)/2 + (N-1) - 1
		//s: (N-1)(N-2)/2 + (N-1)
		//t: (N-1)(N-2)/2 + (N-1) + 1
		int counter = 0; // index of game vertices
		for (int i = 0; i < N; i++)
			for (int j = i+1; j < N; j++) {
				if (i != id && j != id) {
					// game vertices in array: i-j. 
					// team vertices in array: i, j.
					// convert to vertices in FlowNetwork
					int v1 = counter; //game vertex in network
					
					int v2; // team vertex in network
					if (i < id)
						v2 = gameV + i; 
					else
						v2 = gameV + i - 1;
					
					int v3; // team vertex in network
					if (j < id)
						v3 = gameV + j; 
					else
						v3 = gameV + j - 1;
					
					G.addEdge(new FlowEdge(v1, v2, Double.POSITIVE_INFINITY));
					G.addEdge(new FlowEdge(v1, v3, Double.POSITIVE_INFINITY));
					G.addEdge(new FlowEdge(s, v1, g[i][j]));
					counter++;
				}
			}
		
		for (int i = 0; i < N; i++) {
			if (i != id) {
				int v;
				if (i < id)
					v = gameV + i;
				else
					v = gameV + i - 1;
				//below will create duplicates
				G.addEdge(new FlowEdge(v, t, w[id] + r[id] - w[i]));
			}
		}
		
		return G;
	}
	
	
	// trivial elimination
	private boolean trivial(String team, int id) {
		marked[id] = true;
		e[id] = false;
		for (int i = 0; i < N; i++) {
			// no need to compare with self
			if (i != id) {
				// find a certificate
				if (w[id] + r[id] < w[i]) {
					if (!cache.contains(team))
						cache.put(team, new SET<String>());
					SET<String> set = cache.get(team);
					set.add(idTeams[i]);
					// find one certificate is enough for trivial elimination
					e[id] = true; //can be eliminated
				}
			}
		}

		return e[id]; //can not eliminated
	}
	
	// nontrivial elimination
	private boolean nontrivial(String team, int id) {
		marked[id] = true;
		
		// build a flow network
		FlowNetwork G =  buildFlowNetwork(id);
		
		int s = G.V() - 2;
		int t = G.V() - 1;
		
		//StdOut.print(G.toString());
		
		// run ford-fulkerson algorithm to find a maximum flow
		FordFulkerson maxflow = new FordFulkerson(G, s, t);
		
		//for debugging
		//StdOut.print(G.toString());
		
		e[id] = false;
		for (FlowEdge edge : G.adj(s)) {
			// a full flow on this edge ?
			//StdOut.println(edge.flow() + " v.s. " + edge.capacity());
			if (edge.flow() != edge.capacity()) {
				e[id] = true; // eliminated
				break;
			}
		}
		
		int gameV = (N-1)*(N-2)/2;
		if (e[id] == true) {
			if (!cache.contains(team))
				cache.put(team, new SET<String>());
			SET<String> set = cache.get(team);
			for (int i = 0; i < N; i++) {
				if (i != id) {
					int v;
					if (i < id)
						v = gameV + i;
					else
						v = gameV + i - 1;
					//check whether team i is in mincut of maxflow
					if (maxflow.inCut(v))
						set.add(idTeams[i]);
				}
			}
		}
		
		return e[id];
	}
	
	// is given team eliminated?
	public boolean isEliminated(String team) {
		// in cache
		if (!teamIDs.contains(team))
			throw new java.lang.IllegalArgumentException();
		int id = teamIDs.get(team);
		if (marked[id]) {
			return e[id];
		}
		
		// trivial elimination
		if (trivial(team, id))
			return true;
		
		// nontrivial elimination
		if (nontrivial(team, id))
			return true;
		
		return false;
	}
	
	// subset R of teams that eliminates given team;
	// null if not eliminated
	public Iterable<String> certificateOfElimination(String team) {
		// in cache
		if (!teamIDs.contains(team))
			throw new java.lang.IllegalArgumentException();
		int id = teamIDs.get(team);
		if (marked[id]) {
			return cache.get(team);
		}
		
		// trivial elimination
		if (trivial(team, id))
			return cache.get(team);
		
		// nontrivial elimination
		if (nontrivial(team, id))
			return cache.get(team);
		
		return null;
	}
	
	// unit test
	public static void main(String[] args) {
		//TODO - Change when submitting
		//BaseballElimination division = new BaseballElimination(args[0]);
		BaseballElimination division = new BaseballElimination("./baseball/teams4.txt");
		for (String team : division.teams()) {
			if (division.isEliminated(team)) {
				StdOut.print(team + " is eliminated by the subset R = { ");
				for (String t : division.certificateOfElimination(team))
					StdOut.print(t + " ");
				StdOut.println("}");
			} else {
				StdOut.println(team + " is not eliminated");
			}
		}
	}
}

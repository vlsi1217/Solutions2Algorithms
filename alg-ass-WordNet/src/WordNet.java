/**
 *  File: WordNet.java
 *  Package: 
 *  Project: alg-ass-WordNet
 *  Created on: Apr 3, 2013
 * 
 *  Description: Programming Assignment 1, Algorithms II
 *
 *  @author: Huang Zhu
 *  Department of Computing and Information Sciences
 *  Kansas State University
 */

import java.util.Iterator;

// immutable
public class WordNet {
	//ST implemented using TreeMap
	//SET implemented using TreeSet
	//noun to ids
	private ST<String, SET<Integer>> nounToID;
	
	//id to nouns
	private ST<Integer, String> idToNoun;
	
	//Digraph implemented using adjacent list
	private Digraph graph;
	
	private SAP sap;
	
	// constructor takes the name of the two input files
	public WordNet(String synsets, String hypernyms) {
		if (synsets.length() == 0 || hypernyms.length() == 0)
			return;
		
		// build synsets
		In synsetFile = new In(synsets);
		nounToID = new ST<String, SET<Integer>>();
		idToNoun = new ST<Integer, String>();
		int counter = 0;
		while (!synsetFile.isEmpty()) {
			counter++; //number of synsets
			String line = synsetFile.readLine();
			
			String[] tokens = line.split(",");
			int id = Integer.parseInt(tokens[0]);
			idToNoun.put(id, tokens[1]); //id to synset
			String[] nouns = tokens[1].split(" "); // a noun can be in multiple synsets
			//String gloss = tokens[2]; //gloss is not used in this program
			for (int i = 0; i < nouns.length; i++) {
				if (!nounToID.contains(nouns[i]))
					nounToID.put(nouns[i], new SET<Integer>());
				SET<Integer> set = nounToID.get(nouns[i]);
				set.add(id);
			}
		}
		
		//StdOut.println("maximum synset id: " + counter);
		
		//build digraph
		In hypernymFile = new In(hypernyms);
		graph = new Digraph(counter);
		SET<Integer> graphvertex = new SET<Integer>(); //vertices in the digraph
		while (!hypernymFile.isEmpty()) {
			String line = hypernymFile.readLine();
			String[] tokens = line.split(",");
			int id = Integer.parseInt(tokens[0]);
			graphvertex.add(id);
			for (int i = 1; i < tokens.length; i++) {
				int hypernym = Integer.parseInt(tokens[i]);
				graph.addEdge(id, hypernym);
				graphvertex.add(hypernym);
			}
		}
		
		// go through vertices in the digraph and count number of roots
		int numRoot = 0;
		for (int v : graphvertex) {
			// a root has no outgoing edges
			if (((Bag<Integer>)graph.adj(v)).isEmpty())
				numRoot++;
		}
		
		// single root?
		if (numRoot != 1)
			throw new java.lang.IllegalArgumentException("Not Single Rooted Graph");

		// DAG?
		if (hasCycle())
			throw new java.lang.IllegalArgumentException("Not DAG. Cycles Exist.");
		
		 sap = new SAP(graph);
	}
	
	//TODO - set it private
	// get id of synsets that contain word
	private SET<Integer> getIDs(String word) {
		return nounToID.get(word);
	}
	
	private boolean hasCycle() {
		return (new DirectedCycle(graph)).hasCycle();
	}
	
	// return all WordNet nouns
	public Iterable<String> nouns() {
		return nounToID.keys();
	}
	
	// is the word a WordNet noun
	public boolean isNoun(String word) {
		return nounToID.contains(word);
	}
	
	// distance between nounA and nounB
	public int distance(String nounA, String nounB) {
		if (!nounToID.contains(nounA) || !nounToID.contains(nounB))
			throw new java.lang.IllegalArgumentException("Noun not in Synset");
		
		//TODO - SAP, out of memory
		//SAP sap = new SAP(graph);
		SET<Integer> aids = getIDs(nounA);
		SET<Integer> bids = getIDs(nounB);
		
		//minimum path between one id from aids and another from bids
		return sap.length(aids, bids);
	}
	
	// a synset (second field of synsets.txt) that is the common ancestor 
	// of nounA and nounB in a shortest ancestral path
	public String sap(String nounA, String nounB) {
		if (!nounToID.contains(nounA) || !nounToID.contains(nounB))
			throw new java.lang.IllegalArgumentException("Noun not in Synset");

		//TODO - SAP, out of memory
		//SAP sap = new SAP(graph);
		
		SET<Integer> aids = getIDs(nounA);
		SET<Integer> bids = getIDs(nounB);
		
		//a common ancestor that participates in shortest ancestral path
		//between one id from aids and another from bids
		int ancestor = sap.ancestor(aids, bids);
		
		//get id of common ancestor
		return idToNoun.get(ancestor);
	}
	
	// for unit testing of this class
	public static void main(String[] args) {
		String syn = "./wordnet/synsets.txt";
		String hyper = "./wordnet/hypernyms.txt";
		WordNet wordnet = new WordNet(syn,hyper);
		
		StdOut.println("WordNet contains zymosis: " + wordnet.isNoun("zymosis"));
		StdOut.print("Corresponding synset ids: ");
		for (int id : wordnet.getIDs("zymosis"))
			StdOut.print(id + "  ");
		StdOut.println();
		
		StdOut.println("WordNet contains \"right\": " + wordnet.isNoun("right"));
		StdOut.print("Corresponding synset ids: ");
		for (int id : wordnet.getIDs("right"))
			StdOut.print(id + "  ");
		StdOut.println();
		
		StdOut.println("WordNet contains \"occupation\": " + wordnet.isNoun("occupation"));
		StdOut.print("Corresponding synset ids: ");
		for (int id : wordnet.getIDs("occupation"))
			StdOut.print(id + "  ");
		StdOut.println();
		
		StdOut.println("WordNet contains \"landmark\": " + wordnet.isNoun("landmark"));
		StdOut.print("Corresponding synset ids: ");
		for (int id : wordnet.getIDs("landmark"))
			StdOut.print(id + "  ");
		StdOut.println();
		
		StdOut.println("WordNet contains \"expedition\": " + wordnet.isNoun("expedition"));
		StdOut.print("Corresponding synset ids: ");
		for (int id : wordnet.getIDs("expedition"))
			StdOut.print(id + "  ");
		StdOut.println();
		
		StdOut.println("WordNet contains \"ASCII_character\": " + wordnet.isNoun("ASCII_character"));
		StdOut.print("Corresponding synset ids: ");
		for (int id : wordnet.getIDs("ASCII_character"))
			StdOut.print(id + "  ");
		StdOut.println();
		
		StdOut.println("WordNet contains \"International\": " + wordnet.isNoun("International"));
		StdOut.print("Corresponding synset ids: ");
		for (int id : wordnet.getIDs("International"))
			StdOut.print(id + "  ");
		StdOut.println();
		
		StdOut.println("WordNet contains \"Zhu\": " + wordnet.isNoun("Zhu"));
		StdOut.println("WordNet contains \"Huang\": " + wordnet.isNoun("Huang"));
		
		
		StdOut.println("Distance between \"International\" and \"expedition\": " + 
		               wordnet.distance("International", "expedition"));
		StdOut.println("Ancestor between \"International\" and \"expedition\": " + 
	               wordnet.sap("International", "expedition"));
		
		StdOut.println("Distance between \"occupation\" and \"landmark\": " + 
	               wordnet.distance("occupation", "landmark"));
	    StdOut.println("Ancestor between \"occupation\" and \"landmark\": " + 
            wordnet.sap("occupation", "landmark"));
	}

}

/**
 *  File: Outcast.java
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

public class Outcast {
	private final WordNet wordnet;
	
	// constructor takes a WordNet object
	public Outcast(WordNet wordnet) {
		this.wordnet = wordnet;
	}
	
	// given an array of WordNet nouns, return an outcast
	public String outcast(String[] nouns) {
		//noun to outcast
		ST<String, Integer> nounOutcast = new ST<String, Integer>();
		for (int i = 0; i < nouns.length; i++)
			for (int j = i + 1; j < nouns.length; j++) {
				int dist = wordnet.distance(nouns[i], nouns[j]);
				if (!nounOutcast.contains(nouns[i]))
					nounOutcast.put(nouns[i], dist);
				else
					nounOutcast.put(nouns[i], dist + nounOutcast.get(nouns[i]));
				
				if (!nounOutcast.contains(nouns[j]))
					nounOutcast.put(nouns[j], dist);
				else
					nounOutcast.put(nouns[j], dist + nounOutcast.get(nouns[j]));
			}
		int maxDist = 0;
		String maxNoun = "";
		for (String noun : nounOutcast) {
			int dist = nounOutcast.get(noun);
			if (dist > maxDist) {
				maxDist = dist;
				maxNoun = noun;
			}
		}
		return maxNoun;
	}
	
	// unit test
	public static void main(String[] args) {
		WordNet wordnet = new WordNet(args[0], args[1]);
		Outcast outcast = new Outcast(wordnet);
		for (int t = 2; t < args.length; t++) {
			String[] nouns = In.readStrings(args[t]);
			StdOut.println(args[t] + ": " + outcast.outcast(nouns));
		}
	}
}

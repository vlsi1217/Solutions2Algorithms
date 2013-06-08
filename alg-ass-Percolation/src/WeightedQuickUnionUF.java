/*
 *  File: WeightedQuickUnionUF.java
 *  Package: 
 *  Project: UnionFind
 *  Created on: Feb 12, 2013
 *
 *  Author: Huang Zhu
 *  Department of Computing and Information Sciences
 *  Kansas State University
 */

public class WeightedQuickUnionUF {
	
	private int id[]; //id[i] contains parent of i
	private int sz[];
	
	//initialize: O(N)
	public WeightedQuickUnionUF(int N)
	{
		id = new int[N];
		sz = new int[N];
		for(int i=0; i<N; i++){
			id[i] = i;
			sz[i] = 1;
		}
	}
	
	//root: O(lgN)
	public int root(int i)
	{
		while(i!=id[i]) i = id[i];
		return i;
	}
	
	//find: O(lgN)
	//same component when they have the same root
	public boolean connected(int p, int q)
	{ return root(p) == root(q); }
	
	
	//union: O(lgN)
	//link root of smaller tree to root of larger tree
	public void union(int p, int q)
	{
		int proot = root(p);
		int qroot = root(q);
		if(sz[proot] < sz[qroot]){
			id[proot] = qroot;
			sz[qroot] += sz[proot];
		}else{
			id[qroot] = proot;
			sz[proot] += sz[qroot];
		}
	}
	
}

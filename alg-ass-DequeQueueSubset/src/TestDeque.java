/**
 *  File: TestDequeu.java
 *  Package: 
 *  Project: alg-Deque
 *  Created on: Feb 23, 2013
 *
 *  Compilation: TO DO
 *  Execution: TO DO
 *  Dependency: TO DO
 * 
 *  Description: 
 *
 *  @author: Huang Zhu
 *  Department of Computing and Information Sciences
 *  Kansas State University
 *
 */

import java.util.Iterator;

public class TestDeque {

	/**
	 * Unit Test
	 */
	public static void main(String args[]) {
		Deque<String> queue = new Deque<String>();
		int count;
		queue.addFirst("first");
		queue.addFirst("second");
		queue.addFirst("third");
		queue.addFirst("fourth");
		queue.addFirst("fifth");
		
		StdOut.println("Current Size of Queue: " + queue.size());
		for(String s : queue)
			StdOut.println(s);
		count = queue.size();
		while (count > 0) {
			queue.removeLast();
			count--;
		}
		StdOut.println("Is Queue Empty Now: " + queue.isEmpty());
		
		queue.addLast("first");
		queue.addLast("second");
		queue.addLast("third");
		queue.addLast("fourth");
		queue.addLast("fifth");
		
		StdOut.println("Current Size of Queue: " + queue.size());
		for (String s : queue)
			StdOut.println(s);
		count = queue.size();
		while (count > 0) {
			queue.removeFirst();
			count--;
		}
		StdOut.println("Is Queue Empty Now: " + queue.isEmpty());
		
		queue.addFirst("first");
		queue.addFirst("second");
		queue.addFirst("third");
		queue.addFirst("fourth");
		queue.addFirst("fifth");
		queue.addLast("tenth");
		queue.addLast("nineth");
		queue.addLast("eighth");
		queue.addLast("seventh");
		queue.addLast("sixth");
		
		Iterator<String> iterator = queue.iterator();
		while(iterator.hasNext()) {
			String s = iterator.next();
			StdOut.println(s);
			queue.removeFirst();
		}
		StdOut.println("Is Queue Empty Now: " + queue.isEmpty());
		//queue.removeFirst();
		//queue.removeLast();
		//queue.addFirst(null);
		//queue.addLast(null);
		//String temp = iterator.next();
		//iterator.remove();
	}
}

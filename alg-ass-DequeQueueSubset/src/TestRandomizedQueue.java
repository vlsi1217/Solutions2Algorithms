import java.util.Iterator;

/**
 *  File: TestRandomizedQueue.java
 *  Package: 
 *  Project: alg-RandomizedQueue
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

public class TestRandomizedQueue {

	/**
	 * Unit Test
	 */
	public static void main(String[] args) {
		
		RandomizedQueue<String> queue = new RandomizedQueue<String>();
		int count;
		
		//expect an exception here
		StdOut.println(queue.dequeue()); 
		
		//expect an exception here
		//queue.enqueue(null);
		
		queue.enqueue("first");
		queue.enqueue("second");
		queue.enqueue("third");
		queue.enqueue("fourth");
		queue.enqueue("fifth");
		
		StdOut.println("Current Size of Queue: " + queue.size());
		StdOut.println();
		
		//should see a random order
		for(String s1 : queue)
			StdOut.println(s1); 
		StdOut.println();
		
		//should see a random order and different from previous one
		for(String s2 : queue)
			StdOut.println(s2); 
		StdOut.println();
		
		count = queue.size();
		while (count > 0) {
			StdOut.println(queue.dequeue());
			count--;
		}
		
		StdOut.println("Is Queue Empty Now: " + queue.isEmpty());
		StdOut.println();
		
		//expect an exception here
		//StdOut.println(queue.dequeue()); 
		
		queue.enqueue("first");
		queue.enqueue("second");
		queue.enqueue("third");
		queue.enqueue("fourth");
		queue.enqueue("fifth");
		
		StdOut.println("Current Size of Queue: " + queue.size());
		StdOut.println();
		
		for (String s : queue)
			StdOut.println(s);
		StdOut.println();
		
		count = queue.size();
		while (count > 0) {
			StdOut.println(queue.dequeue());
			count--;
		}
		
		StdOut.println("Is Queue Empty Now: " + queue.isEmpty());
		StdOut.println();
		
		queue.enqueue("first");
		queue.enqueue("second");
		queue.enqueue("third");
		queue.enqueue("fourth");
		queue.enqueue("fifth");
		queue.enqueue("tenth");
		queue.enqueue("nineth");
		queue.enqueue("eighth");
		queue.enqueue("seventh");
		queue.enqueue("sixth");
		
		StdOut.println("Current Size of Queue: " + queue.size());
		StdOut.println();
		
		for (String s : queue)
			StdOut.println("foreach: " + s);
		StdOut.println();
		
		count = queue.size();
		while(count > 0) {
			StdOut.println("sample: " + queue.sample());
			count--;
		}
		
		StdOut.println("Current Size of Queue: " + queue.size());
		StdOut.println();
		
		Iterator<String> iterator = queue.iterator();
		while(iterator.hasNext()) {
			String s = iterator.next();
			StdOut.println("iterator " + s);
		}
		
		//expect an exception
		//String temp = iterator.next();
		
		//expect an exception
		//iterator.remove();
		
		count = queue.size();
		while (count > 0) {
			StdOut.println(queue.dequeue());
			count--;
		}
		
		StdOut.println("Is Queue Empty Now: " + queue.isEmpty());
		StdOut.println();
		
		//expect an exception
		//queue.dequeue();
		
		//expect an exception
		//queue.enqueue(null);

	}

}

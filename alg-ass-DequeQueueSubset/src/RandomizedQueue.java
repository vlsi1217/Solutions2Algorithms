/**
 *  File: RandomizedQueue.java
 *  Package: 
 *  Project: alg-RandomizedQueue
 *  Created on: Feb 23, 2013
 *
 *  Compilation: TO DO
 *  Execution: TO DO
 *  Dependency: TO DO
 * 
 *  Description: Programming Assignment 2
 *
 *  @author: Huang Zhu
 *  Department of Computing and Information Sciences
 *  Kansas State University
 *
 */

import java.util.Iterator;

//A randomized queue
public class RandomizedQueue<Item> implements Iterable<Item> {
	private Item[] queue;
	private int count = 0;
	
	//construct an empty randomized queue
	public RandomizedQueue() {
		//stack = new Item[1]; //error
		queue = (Item[]) new Object[1];
	}
	
	//resize the queue
	private void resize(int newsize) {
		if (newsize <= 0) 
			throw new java.lang.IllegalArgumentException();
		Item[] newqueue = (Item[]) new Object[newsize];
		for (int i = 0; i < count; i++)
			newqueue[i] = queue[i];	
		queue = newqueue;
	}
	
	//is the queue full
	private boolean isFull() { 
		return count == queue.length; 
	}
	
	//is the queue empty
	public boolean isEmpty() {
		return count == 0;
	}
	
	//return number of items in the queue
	public int size() {
		return count;
	}
	
	//add the item: at the end
	//no matter where you add the item
	//items are sampled or dequeued in random
	//add item at the end is better for array implementation
	public void enqueue(Item item) {
		if (item == null) 
			throw new java.lang.NullPointerException();
		
		//expand the queue capacity
		if (isFull()) resize(queue.length * 2);
		
		queue[count++] = item;
	}
	
	private void exch(int i, int j) {
		Item temp = queue[i];
		queue[i] = queue[j];
		queue[j] = temp;
	}
	//delete and return a random item
	public Item dequeue() {
		if (isEmpty()) 
			throw new java.util.NoSuchElementException();
		
		//shuffle elements of the queue.
		//the shuffle should be confined to a range under count.
		//Problem: too expensive for dequeue
		//StdRandom.shuffle(queue, 0, count-1);
		
		int index = StdRandom.uniform(count);
		exch(index, count-1);
		
		//select the last element in the queue
		Item item = queue[--count];
		queue[count] = null;

		//shrink the queue capacity
		if (count > 0 && count == queue.length / 4) 
			resize(queue.length / 4);
		
		return item; 
	}
	
	//return (but do not delete) a random item
	public Item sample() {
		if (isEmpty()) 
			throw new java.util.NoSuchElementException();
		
		int index = StdRandom.uniform(count);
		return queue[index];
	}
	
	//return an independent iterator over items in random order
	public Iterator<Item> iterator() { 
		return new RandomizedQueueIterator(); 
	}
	
	//an independent iterator over items in random order
	private class RandomizedQueueIterator implements Iterator<Item> {
		private Item[] randomqueue;
		private int current = 0;
		
		public RandomizedQueueIterator() {
			randomqueue = (Item[]) new Object[count];
			for (int i = 0; i < count; i++)
				randomqueue[i] = queue[i];
			StdRandom.shuffle(randomqueue);
		}
		
		public boolean hasNext() { 
			return current < randomqueue.length; 
		}
		
		public Item next() {
			if (current >= randomqueue.length)
				throw new java.util.NoSuchElementException();
			Item item = randomqueue[current++];
			return item;
		}
		
		public void remove() {
			throw new java.lang.UnsupportedOperationException();
		}
	}
}

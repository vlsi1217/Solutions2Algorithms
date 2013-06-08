/**
 *  File: Deque.java
 *  Package: 
 *  Project: alg-Deque
 *  Created on: Feb 22, 2013
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

//Deque: a double-ended queue
public class Deque<Item> implements Iterable<Item> {
	private Node first, last;
	private int count;
	
	//Node: element type in the deque
	private class Node {
		private Item item; //data
		private Node prev; //reference to previous node
		private Node next; //reference to next node
	}
	
	//construct an empty queue
	public Deque() {
		first = null;
		last = null;
		count = 0;
	}
	
	//is the deque empty
	public boolean isEmpty() { 
		return count == 0; 
	}
	
	//number of items in the deque
	public int size() { 
		return count; 
	}
	
	//insert the item at the front
	public void addFirst(Item item) { 
		if (item == null) 
			throw new java.lang.NullPointerException();	
		Node oldfirst = first;
		first = new Node();
		first.item = item;
		first.prev = null;
		first.next = oldfirst;
		if (isEmpty()) last = first;
		else           oldfirst.prev = first;
		count++;
	}
	
	//insert the item at the end
	public void addLast(Item item) { 
		if (item == null) 
			throw new java.lang.NullPointerException();	
		Node oldlast = last;
		last = new Node();
		last.item = item;
		last.prev = oldlast;
		last.next = null;
		if (isEmpty()) first = last;
		else           oldlast.next = last;
		count++;
	}
	
	//remove the item at the front
	public Item removeFirst() { 
		if (isEmpty())
			throw new java.util.NoSuchElementException();
		Item item = first.item;
		if (first == last) {
			first = null;
			last = null;
		}
		else {
			Node temp = first.next;
			temp.prev = null;
			first.next = null;
			first = temp;
		}
		count--;		
		return item;
	}
	
	//remove the item at the end
	public Item removeLast() { 
		if (isEmpty())
			throw new java.util.NoSuchElementException();
		Item item = last.item;
		if (last == first) {
			last = null;
			first = null;
		}
		else {
			Node temp = last.prev;
			temp.next = null;
			last.prev = null;
			last = temp;
		}
		count--;
		return item;
	}
	
	//return an iterator over items in the deque from front to end
	public Iterator<Item> iterator() { return new DequeIterator(); }
	
	//DequeIterator
	private class DequeIterator implements Iterator<Item> {
		private Node current = first;
		
		public boolean hasNext() { return current != null; }
		
		public Item next() {
			if (current == null)
				throw new java.util.NoSuchElementException();
			Item item = current.item;
			current = current.next;
			return item;
		}
		
		public void remove() {
			throw new java.lang.UnsupportedOperationException();
		}
	}
}

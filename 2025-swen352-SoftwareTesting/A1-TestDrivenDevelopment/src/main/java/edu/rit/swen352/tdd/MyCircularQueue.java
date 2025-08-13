package edu.rit.swen352.tdd;

import java.util.Arrays;

/**
 * CircularQueue is a fixed-sized, generic, FIFO Queue that uses modulo indexes to "circle around" the end of the array.
 *
 * <p>
 * Features:
 * <ul>
 *   <li>constructor: with or without an integer capacity (defaults to 16)</li>
 *   <li>add: adds an item to the end of the queue; throws {@link java.lang.IllegalStateException} if full</li>
 *   <li>isEmpty: queries if the queue is empty</li>
 *   <li>remove: returns and removes the head of the queue; throws {@link java.lang.IllegalStateException} if empty</li>
 *   <li>peek: returns the head of the queue but does not remove it; returns {@code null} if the queue is empty</li>
 * </ul>
 *
 * @param <T> the type of elements in the queue.
 */
public class MyCircularQueue<T> {
	private T[] queue;
	static final int DEFAULT_CAPACITY = 16;
	private int currentIndex;

	public MyCircularQueue() {
		this(DEFAULT_CAPACITY);
	}

	public MyCircularQueue(int capacity) {
		queue = (T[]) new Object[capacity];
		currentIndex = 0;
	}

	/**
	 * Adds an item to the end of the queue.
	 *
	 * @param item the item to add
	 * @throws IllegalStateException if the queue is full
	 */
	public void add(T item) {
		if (currentIndex >= queue.length) {
			throw new IllegalStateException();
		}
		queue[currentIndex] = item;
		currentIndex++;
	}

	/**
	 * Query if the queue is empty.
	 * @return true if the queue is empty, false otherwise
	 */
	public Boolean isEmpty() {
		return queue[0] == null;
	}

	/**
	 * Gets the current size of the queue for testing purposes.
	 * @return the number of items in the queue
	 */
	public int getCapacity() {
		return queue.length;
	}

	/**
	 * Creates a string representation of the queue for testing purposes.
	 * @return a string representation of the queue
	 */
	@Override
	public String toString() {
		return Arrays.toString(queue);
	}
}

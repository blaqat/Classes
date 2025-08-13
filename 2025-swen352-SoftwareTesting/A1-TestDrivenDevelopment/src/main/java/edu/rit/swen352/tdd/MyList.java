package edu.rit.swen352.tdd;

import java.util.NoSuchElementException;
import java.util.function.Consumer;

/**
 * MyList is a flexible-sized sequence of elements with no gaps.
 *
 * <p>
 * You must implement these features:
 * <ul>
 *   <li>constructor: with varargs of initial elements</li>
 *   <li>add: add an element to the list; no-op if the element is already in the list (by reference)</li>
 *   <li>remove: remove an element by reference</li>
 *   <li>get: returns the element at a specific index;
 *     throw {@link java.util.NoSuchElementException} if the index is outside the size of the list</li>
 *   <li>isEmpty: queries if the list is empty</li>
 *   <li>size: queries how many elements in the list</li>
 *   <li>forEach: iterates over each element and executes the {@link java.util.function.Consumer} parameter</li>
 * </ul>
 *
 * @param <T> the type of elements in the list.
 */
public class MyList<T> {
	private T[] elements;
	private int numElements;

	public MyList(T... initialElements) {
		elements = initialElements;
		setInitNumElements();
	}

	/**
	 * Sets the Initial Number of Elements in the List
	 */
	private void setInitNumElements() {
		// If list has no items -> 0
		if (elements.length == 0 || elements[0] == null) {
			numElements = 0;
			System.out.println(0 + toString());
		}
		// If list has no null trailing -> elements.length
		else if (elements[elements.length - 1] != null) {
			numElements = elements.length;
		}
		// If list has a null trailing -> last non-null index
		else {
			numElements = indexOf(null);
		}
	}

	/**
	 * Finds the index of the specified element in the list.
	 * 
	 * @param element The element to search for in the list
	 * @return The index of the first occurrence of the element in the list, 
	 *         or -1 if the element is not found
	 */
	private int indexOf(T element) {
		for (int i = 0; i < elements.length; i++) {
			if (element == elements[i]) {
				return i;
			}
		}

		return -1;
	}

	/**
	 * Adds a new element to the list.
	 * 
	 * @param newElement the element to be added to the list
	 */
	public void add(T newElement) {
		// If list is empty create new one with newElement
		if (isEmpty()) {
			elements = (T[]) new Object[] { newElement };
			numElements = 1;
			return;
		}

		// If newElement already exists in list, ignore it
		if (indexOf(newElement) != -1)
			return;

		// Find the index of first empty slot
		int foundEmptySpace = indexOf(null);

		// If there is no room to add the newElement
		if (foundEmptySpace == -1) {
			// Expand the array
			int oldLength = elements.length;
			T[] newElements = (T[]) new Object[oldLength * 2];
			System.arraycopy(elements, 0, newElements, 0, oldLength);

			// Insert newElement in newly created emtpy space
			newElements[oldLength] = newElement;
			elements = newElements;
			numElements = oldLength + 1;
			return;
		}

		// Insert newElement in empty space
		elements[foundEmptySpace] = newElement;
		numElements += 1;
	}

	/**
	 * Removes an element from the list.
	 * If the list does not contain the element, nothing happens.
	 *
	 * @param remElement the element to be removed from this list, if present
	 */
	public void remove(T remElement) {
		int remIndex = indexOf(remElement);

		// If Element Doesnt Exist, Ignore
		if (remIndex == -1)
			return;

		// Remove Element
		elements[remIndex] = null;
		numElements -= 1;

		// Move all remaining elements back 1
		for (int j = remIndex + 1; j < elements.length; j++) {
			elements[j - 1] = elements[j];
			elements[j] = null;
		}
	}

	/**
	 * Returns the element at a specific index;.
	 *
	 * @param index the index of the element to return
	 * @return the element at the position in the list
	 * @throws NoSuchElementException if the index is out of range
	 *         (index < 0 || index >= size())
	 */
	public T get(int index) {
		try {
			return elements[index];
		} catch (Exception e) {
			throw new NoSuchElementException();
		}
	}

	/**
	 * Queries if the list is empty
	 * @return true if list is empty false if list is not empty
	 */
	public Boolean isEmpty() {
		return numElements == 0;
	}

	/**
	 * Queries how many elements in the list.
	 * @return integer of how many elements are in the list.
	 */
	public int size() {
		return numElements;
	}

	/**
	 * Iterates over each element and executes the {@link java.util.function.Consumer} } parameter</li>
	 * @param consumer to execute
	 */
	public void forEach(Consumer<T> consumer) {
		for (int i = 0; i < size(); i++) {
			consumer.accept(elements[i]);
		}
	}

	/**
	 * Returns a string representation of the items in the list
	 * If the list is empty, returns "[∅]" to indicate an empty list.
	 * 
	 * @return a string representation of this list
	 */
	@Override
	public String toString() {
		// Case where list is empty
		// Return Null Array String
		if (isEmpty()) {
			return "[∅]";
		}

		StringBuilder myListStr = new StringBuilder("[");

		// Append Items from List to String Builder
		forEach((element) -> {
			myListStr.append(element);
			myListStr.append(", ");
		});

		// https://stackoverflow.com/a/3395329 -> remove excess ', '
		myListStr.setLength(Math.max(myListStr.length() - 2, 0));

		myListStr.append("]");

		return myListStr.toString();
	}
}

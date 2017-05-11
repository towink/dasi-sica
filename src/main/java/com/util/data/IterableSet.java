package com.util.data;

public abstract class IterableSet<T> implements Iterable<T> {

	/**
	 * Get the size of this iterator
	 * @return
	 */
	public abstract int size();
	
	/**
	 * @return true if this IterableSet has nothing
	 */
	public boolean isEmpty() {
		return this.size() == 0;
	}
}

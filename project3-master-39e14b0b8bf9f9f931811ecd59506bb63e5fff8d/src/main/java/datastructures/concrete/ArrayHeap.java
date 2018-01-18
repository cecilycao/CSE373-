//  Yiran Fu, Ye Cao
//	CSE 373

package datastructures.concrete;

import datastructures.interfaces.IPriorityQueue;
import misc.exceptions.EmptyContainerException;

/**
 * See IPriorityQueue for details on what each method must do.
 */
public class ArrayHeap<T extends Comparable<T>> implements IPriorityQueue<T> {
    // See spec: you must implement a implement a 4-heap.
    private static final int NUM_CHILDREN = 4;
    protected static final int INITIAL_CAPACITY  = 1000;

    // You MUST use this field to store the contents of your heap.
    // You may NOT rename this field: we will be inspecting it within
    // our private tests.
    private T[] heap;
    private int length;
    private int capacity;

    // Feel free to add more fields and constants.

    public ArrayHeap() {
    		this.capacity = INITIAL_CAPACITY;
    		this.heap = makeArrayOfT(INITIAL_CAPACITY);
    		this.length = 0;
    }

    /**
     * This method will return a new, empty array of the given size
     * that can contain elements of type T.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private T[] makeArrayOfT(int size) {
        // This helper method is basically the same one we gave you
        // in ArrayDictionary and ChainedHashDictionary.
        //
        // As before, you do not need to understand how this method
        // works, and should not modify it in any way.
        return (T[]) (new Comparable[size]);
    }

    @Override
    public T removeMin() {
    		if (heap[1] == null) {
    			throw new EmptyContainerException();
    		}
    		T result = heap[1];
    		heap[1] = heap[length];
    		heap[length] = null;
    		if (length > 1) {
    			percolateDown(1);
    		}
    		length--;
    		return result;
    }

    @Override
    public T peekMin() {
    		if (heap[1] == null) {
			throw new EmptyContainerException();
		}
    		return heap[1];
    }

    @Override
    
    public void insert(T item) {
		if (item == null) {
			throw new IllegalArgumentException();
		}
		checkResize();
		if (length == 0) {
			heap[1] = item;
		} else {
			heap[length + 1] = item;
		}
		percolateUp(length + 1);
		length++;
    }
    
    public void percolateUp(int index) {
		if (heap[index].compareTo(heap[(index - 2) / NUM_CHILDREN + 1]) < 0) {
			T temp = heap[index];
			heap[index] = heap[(index - 2) / NUM_CHILDREN + 1];
			heap[(index - 2) / NUM_CHILDREN + 1] = temp;
			percolateUp((index - 2) / NUM_CHILDREN + 1);
		} 
    }
    
    public void percolateDown(int index) {
    		if (capacity > (NUM_CHILDREN * index - 2)) {
    			if (heap[NUM_CHILDREN * index - 2] != null) {
    				int minValueIndex = compare(index);
    				if (heap[index].compareTo(heap[minValueIndex]) > 0) {
    					T temp = heap[index];
    					heap[index] = heap[minValueIndex];
    					heap[minValueIndex] = temp;
    					percolateDown(minValueIndex);
    				} 
    			}
    		}
    }
    
    public int compare(int index) {
    		if (heap[NUM_CHILDREN * index - 1] == null) {
    			return NUM_CHILDREN * index - 2;
    		} else {
    			int minIndex = NUM_CHILDREN * index - 2;
    			T minValue = heap[NUM_CHILDREN * index - 2];
    			int i = -1;
    			while (heap[NUM_CHILDREN * index + i] != null && i < (NUM_CHILDREN - 2)) {
    				if (minValue.compareTo(heap[NUM_CHILDREN * index + i]) > 0) {
    					minIndex = NUM_CHILDREN * index + i;
    					minValue = heap[NUM_CHILDREN * index + i];
    				}
    				i++;
    			}
    			return minIndex;
    		}
    }
    
    @Override
    public int size() {
        return length;
    }
    
    private void checkResize() {
		if (length == capacity - 1) {
			capacity = 2*capacity;
			T[] temp = this.heap;
			this.heap = makeArrayOfT(capacity);  
			for (int i = 1; i <= length; i++) {
				heap[i] = temp[i];
			}
		}
    }
}

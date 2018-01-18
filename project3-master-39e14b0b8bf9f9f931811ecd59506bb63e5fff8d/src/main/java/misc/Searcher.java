//  Yiran Fu, Ye Cao
//	CSE 373
package misc;



import datastructures.concrete.ArrayHeap;
import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import datastructures.interfaces.IPriorityQueue;

public class Searcher {
    /**
     * This method takes the input list and returns the top k elements
     * in sorted order.
     *
     * So, the first element in the output list should be the "smallest"
     * element; the last element should be the "biggest".
     *
     * If the input list contains fewer then 'k' elements, return
     * a list containing all input.length elements in sorted order.
     *
     * This method must not modify the input list.
     *
     * @throws IllegalArgumentException  if k < 0
     */
    public static <T extends Comparable<T>> IList<T> topKSort(int k, IList<T> input) {
        // Implementation notes:
        //
        // - This static method is a _generic method_. A generic method is similar to
        //   the generic methods we covered in class, except that the generic parameter
        //   is used only within this method.
        //
        //   You can implement a generic method in basically the same way you implement
        //   generic classes: just use the 'T' generic type as if it were a regular type.

    		if (k < 0) {
    			throw new IllegalArgumentException();
    		}
    		
    		IList<T> topK = new DoubleLinkedList<>();
    		
    		int arrayHeapSize = 0;
    		if (k < input.size()) {
    			arrayHeapSize = k;
    		} else {
    			arrayHeapSize = input.size();
    		}
    		
    		if (arrayHeapSize == 0) {
    			return topK;
    		}
    		
    		IPriorityQueue<T> arrayHeap = new ArrayHeap<>();
    		int count = 0;
    		for (T element : input) {
    			count++;
    			if (count <= arrayHeapSize) {
    				arrayHeap.insert(element);
    			} else {
    				if (element.compareTo(arrayHeap.peekMin()) > 0) {
    					arrayHeap.removeMin();
    					arrayHeap.insert(element);
    				}
    			}
    		}
    		
    		for (int i = 0; i < arrayHeapSize; i++) {
    			topK.add(arrayHeap.removeMin());
    		}
    		
    		return topK;
    }
}

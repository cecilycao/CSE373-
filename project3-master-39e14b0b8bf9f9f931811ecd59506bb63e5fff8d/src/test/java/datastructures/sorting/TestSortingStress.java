//  Yiran Fu, Ye Cao
//	CSE 373
package datastructures.sorting;

import misc.BaseTest;
import misc.Searcher;

import org.junit.Test;

import datastructures.concrete.ArrayHeap;
import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import datastructures.interfaces.IPriorityQueue;

import static org.junit.Assert.assertTrue;

import java.util.*;

/**
 * See spec for details on what kinds of tests this class should include.
 */
public class TestSortingStress extends BaseTest {
    @Test(timeout=10*SECOND)
    public void testPlaceholder() {
        // TODO: replace this placeholder with actual code
        assertTrue(true);
    }
    
    protected <T extends Comparable<T>> IPriorityQueue<T> makeInstance() {
        return new ArrayHeap<>();
    }
    
    @Test(timeout=10*SECOND)
    public void arrayHeapInsertStressTest() {
		IPriorityQueue<Integer> heap = this.makeInstance();
		for (int i = 200000; i > 0; i--) {
			heap.insert(i);
		}
		assertEquals(200000, heap.size());
		for (int i = 1; i <= 200000; i++) {
			assertEquals(i, heap.peekMin());
			assertEquals(i, heap.removeMin());
			assertEquals(200000 - i, heap.size());
		}
    }
    
    @Test(timeout=10*SECOND)
    public void topKSearchStressTest() {
    		IList<Integer> list = new DoubleLinkedList<>();
    		List<Integer> test = new ArrayList<Integer>();
    		List<Integer> top1 = new ArrayList<Integer>();
    		List<Integer> top2 = new ArrayList<Integer>();
    		List<Integer> top3 = new ArrayList<Integer>();
    		for (int i = 300000; i > 0; i--) {
    			list.add(i);
    			test.add(i);
    		}
    		Collections.sort(test);
    		
    		IList<Integer> kEqualToSize = Searcher.topKSort(300000, list);
    		for (int element : kEqualToSize) {
    			top1.add(element);
    		}
    		assertEquals(test, top1);
    		
    		IList<Integer> kMoreThanSize = Searcher.topKSort(500000, list);
    		for (int element : kMoreThanSize) {
    			top3.add(element);
    		}
    		assertEquals(test, top3);
    		
    		IList<Integer> kLessThanSize = Searcher.topKSort(100000, list);
    		for (int element : kLessThanSize) {
    			top2.add(element);
    		}
    		assertEquals(test.subList(200000, 300000), top2);	
    }
    
    
}

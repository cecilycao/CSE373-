//  Yiran Fu, Ye Cao
//	CSE 373
package datastructures.sorting;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import misc.BaseTest;
import datastructures.concrete.ArrayHeap;
import datastructures.interfaces.IPriorityQueue;
import misc.exceptions.EmptyContainerException;
import org.junit.Test;

/**
 * See spec for details on what kinds of tests this class should include.
 */
public class TestArrayHeapFunctionality extends BaseTest {
    protected <T extends Comparable<T>> IPriorityQueue<T> makeInstance() {
        return new ArrayHeap<>();
    }
    
    protected IPriorityQueue<Integer> makeBasicHeap() {
    		IPriorityQueue<Integer> heap = this.makeInstance();
        heap.insert(1);
        heap.insert(3);
        heap.insert(2);
        heap.insert(4);
        return heap;
    }
    
    protected IPriorityQueue<Integer> makeEqualHeap() {
		IPriorityQueue<Integer> heap = this.makeInstance();
		heap.insert(1);
		heap.insert(1);
		heap.insert(4);
		heap.insert(3);
		heap.insert(3);
		return heap;
    }
    
    protected IPriorityQueue<Integer> makeNegHeap() {
 		IPriorityQueue<Integer> heap = this.makeInstance();
 		heap.insert(-1);
 		heap.insert(-1);
 		heap.insert(3);
 		heap.insert(3);
 		heap.insert(0);
 		return heap;
     }   
    
    @Test(timeout=SECOND)
    public void testInsertBaisc () {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.insert(3);
        assertEquals(1, heap.size());
		try {
			heap.insert(null);
			fail("Expected IllegalArgumentException");
		} catch (IllegalArgumentException ex) {
			// Do nothing: this is ok
		}
    }
    
    @Test(timeout=SECOND)
    public void testBasicSize() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.insert(3);
        assertEquals(1, heap.size());
    }
    
    @Test(timeout=SECOND)
    public void testPeekOneElement() {
    		IPriorityQueue<Integer> heap = this.makeInstance();
    		heap.insert(1);
    		assertEquals(1, heap.peekMin());
        assertEquals(1, heap.size());
    }
    
    @Test(timeout=SECOND)
    public void testRemoveOneElement() {
		IPriorityQueue<Integer> heap = this.makeInstance();
		heap.insert(1);
        assertEquals(1, heap.size());
		assertEquals(1, heap.removeMin());
		assertEquals(0, heap.size());
    }
    
    @Test(timeout=SECOND)
    public void testRemoveMultipleElement() {
    		IPriorityQueue<Integer> heap = this.makeBasicHeap();
    		assertEquals(1, heap.removeMin());
    		assertEquals(3, heap.size());
    		assertEquals(2, heap.removeMin());
    		assertEquals(2, heap.size());
    		assertEquals(3, heap.removeMin());
    		assertEquals(1, heap.size());
    		assertEquals(4, heap.removeMin());
    		assertEquals(0, heap.size());
    }
    
    @Test(timeout=SECOND)
    public void testRemoveWhenHeapIsNull() {
    		IPriorityQueue<Integer> heap = this.makeInstance();
    		assertEquals(0, heap.size());
    		assertTrue(heap.isEmpty());
        try {
             heap.removeMin();
                fail("Expected EmptyContainerException");
        } catch (EmptyContainerException ex) {
                // Do nothing: this is ok
        }
    }
    
    @Test(timeout=SECOND)
    public void testRemoveMultipleElementWithEqual() {
		IPriorityQueue<Integer> heap = this.makeEqualHeap();
		assertEquals(1, heap.removeMin());
		assertEquals(4, heap.size());
		assertEquals(1, heap.removeMin());
		assertEquals(3, heap.size());
		assertEquals(3, heap.removeMin());
		assertEquals(2, heap.size());
		assertEquals(3, heap.removeMin());
		assertEquals(1, heap.size());
		assertEquals(4, heap.removeMin());
		assertEquals(0, heap.size());
    }
    
    @Test(timeout=SECOND)
    public void testRemoveMultipleElementWithNeg() {
		IPriorityQueue<Integer> heap = this.makeNegHeap();
		assertEquals(-1, heap.removeMin());
		assertEquals(4, heap.size());
		assertEquals(-1, heap.removeMin());
		assertEquals(3, heap.size());
		assertEquals(0, heap.removeMin());
		assertEquals(2, heap.size());
		assertEquals(3, heap.removeMin());
		assertEquals(1, heap.size());
		assertEquals(3, heap.removeMin());
		assertEquals(0, heap.size());
    }
    
    @Test(timeout=SECOND)
    public void testPeekBasic() {
		IPriorityQueue<Integer> heap = this.makeBasicHeap();
		assertEquals(1, heap.peekMin());
		assertEquals(4, heap.size());
    }
    
    @Test(timeout=SECOND)
    public void testPeekWithEqual() {
		IPriorityQueue<Integer> heap = this.makeEqualHeap();
		assertEquals(1, heap.peekMin());
		assertEquals(5, heap.size());
    }
    
    @Test(timeout=SECOND)
    public void testPeekWithNeg() {
		IPriorityQueue<Integer> heap = this.makeNegHeap();
		assertEquals(-1, heap.peekMin());
		assertEquals(5, heap.size());
    }
    
    @Test(timeout=SECOND)
    public void testPeekWhenHeapIsNull() {
		IPriorityQueue<Integer> heap = this.makeInstance();
		assertEquals(0, heap.size());
		assertTrue(heap.isEmpty());
		try {
			heap.peekMin();
			fail("Expected EmptyContainerException");
		} catch (EmptyContainerException ex) {
			// Do nothing: this is ok
		}
    }
    
    @Test(timeout=SECOND)
    public void testIsEmpty() {
    		IPriorityQueue<Integer> heap = this.makeBasicHeap();
    		for(int i = 0; i < 4; i++) {
    			heap.removeMin();
    		}
    		assertTrue(heap.isEmpty());
    }
    
    @Test(timeout=SECOND)
    public void testInsertMany() {
    		IPriorityQueue<Integer> heap = this.makeInstance();
        for (int i = 100; i > 0; i--) {
        		heap.insert(i);
        		assertEquals(101 - i, heap.size());
        }        
        for (int i = 1; i <= 100; i++) {
        		assertEquals(i, heap.removeMin());
        }
        
    }
}

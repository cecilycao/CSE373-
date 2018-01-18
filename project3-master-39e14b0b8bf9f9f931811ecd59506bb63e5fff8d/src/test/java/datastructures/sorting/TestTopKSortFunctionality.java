//  Yiran Fu, Ye Cao
//	CSE 373
package datastructures.sorting;

import misc.BaseTest;
import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import misc.Searcher;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.*;

import org.junit.Test;

/**
 * See spec for details on what kinds of tests this class should include.
 */
public class TestTopKSortFunctionality extends BaseTest {
	
    @Test(timeout=SECOND)
    public void testSimpleUsage() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 20; i++) {
            list.add(i);
        }

        IList<Integer> top = Searcher.topKSort(5, list);
        assertEquals(5, top.size());
        for (int i = 0; i < top.size(); i++) {
            assertEquals(15 + i, top.get(i));
        }
    }
    
    @Test(timeout=SECOND)
    public void testLargeK( ) {
    		List<Integer> test = new ArrayList<Integer>();
    		List<Integer> top = new ArrayList<Integer>();
    		IList<Integer> list = new DoubleLinkedList<>();
    		for (int i = 19; i >= 0; i--) {
    			list.add(i);
    			test.add(i);
    		}    		
    		IList<Integer> largerK = Searcher.topKSort(30, list);
    		for (int element : largerK) {
    			top.add(element);
    		}
    		Collections.sort(test);
    		assertEquals(test, top);
    }
    
    @Test(timeout=SECOND)
    public void testSmallAndEqualK() {
		List<Integer> test = new ArrayList<Integer>();
		List<Integer> top1 = new ArrayList<Integer>();
		IList<Integer> list = new DoubleLinkedList<>();
		for (int i = 19; i >= 0; i--) {
			list.add(i);
			test.add(i);
		}  
		Collections.sort(test);
		
		IList<Integer> normal = Searcher.topKSort(20, list);

		for (int element : normal) {
			top1.add(element);
		}    		
		assertEquals(test, top1);
		
		List<Integer> top2 = new ArrayList<Integer>();

		IList<Integer> less = Searcher.topKSort(10, list);
		
		for (int element : less) {
			top2.add(element);
		}
		assertEquals(test.subList(10, 20), top2);
    }
    
    @Test(timeout=SECOND)
    public void testNegAndZeroK() {
		IList<Integer> list = new DoubleLinkedList<>();
		for (int i = 19; i >= 0; i--) {
			list.add(i);
		}    		
		IList<Integer> zeroK = Searcher.topKSort(0, list);
		assertTrue(zeroK.isEmpty());
		
		try {
			IList<Integer> negative = Searcher.topKSort(-10, list);
			fail("Expected IllegalArgumentException");
		} catch (IllegalArgumentException ex) {
			// Do nothing: this is ok
		}
    }
    
    @Test(timeout=SECOND)
    public void testEmptyList() {
    		IList<Integer> list = new DoubleLinkedList<>();
    		IList<Integer> emptyList = Searcher.topKSort(10, list);
    		assertTrue(emptyList.isEmpty());
    }
    
    @Test(timeout=SECOND)
    public void testSortBasic() {
    		IList<Integer> list = new DoubleLinkedList<>();
    		list.add(-3);
    		list.add(5);
    		list.add(1);
    		list.add(-100);
    		list.add(50);
    		list.add(0);
    		IList<Integer> basicList = Searcher.topKSort(3, list);
    		assertEquals(1, basicList.get(0));
    		assertEquals(5, basicList.get(1));
    		assertEquals(50, basicList.get(2));
    		assertEquals(3, basicList.size());
    }
}

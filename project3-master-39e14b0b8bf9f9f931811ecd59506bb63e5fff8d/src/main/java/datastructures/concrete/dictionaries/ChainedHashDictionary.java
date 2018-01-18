/*  Yiran Fu, Ye Cao
	CSE 373
*/
package datastructures.concrete.dictionaries;

import datastructures.concrete.DoubleLinkedList;
import datastructures.concrete.KVPair;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IList;
import misc.exceptions.NoSuchKeyException;
import misc.exceptions.NotYetImplementedException;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * See the spec and IDictionary for more details on what each method should do
 */
public class ChainedHashDictionary<K, V> implements IDictionary<K, V> {
    // You may not change or rename this field: we will be inspecting
    // it using our private tests.
	protected static final int INITIAL_CAPACITY  = 31;
	
    private IDictionary<K, V>[] chains;
    private int length;
    private int capacity;
    // You're encouraged to add extra fields (and helper methods) though!

	public ChainedHashDictionary() {
		this.capacity = INITIAL_CAPACITY;
	    this.chains = makeArrayOfChains(INITIAL_CAPACITY);
	    this.length = 0;
	}
	
	/**
	 * This method will return a new, empty array of the given size
	 * that can contain IDictionary<K, V> objects.
	 *
	 * Note that each element in the array will initially be null.
	 */
	@SuppressWarnings("unchecked")
	private IDictionary<K, V>[] makeArrayOfChains(int size) {
	    // Note: You do not need to modify this method.
	    // See ArrayDictionary's makeArrayOfPairs(...) method for
	    // more background on why we need this method.
	    return (IDictionary<K, V>[]) new IDictionary[size];
	}
	
	@Override
	public V get(K key) {
		int index = getIndex(key);
		V result;
		if (chains[index] == null) {
			throw new NoSuchKeyException();
		} else {
			result = chains[index].get(key);
		}
		return result;
	}
	
	@Override
	public void put(K key, V value) {
		checkResize();
		int index = getIndex(key);
			 if (chains[index] == null) {
				 IDictionary<K, V> element = new ArrayDictionary<K, V>();
				 chains[index] = element;
				 element.put(key, value);
				 length++;
			 } else {
				if (!chains[index].containsKey(key)) {
					length++;
				}
				chains[index].put(key, value);
			 }    		        
	}
	
	@Override
	public V remove(K key) {
		int index = getIndex(key);
		V result;
		if (chains[index] == null) {
			throw new NoSuchKeyException();
		} else {
			result = chains[index].remove(key);
			length--;
		}
		return result;
	}
	
	@Override
	public boolean containsKey(K key) {
		int index = getIndex(key);
		if (chains[index] == null) {
			return false;
		} else {
			return chains[index].containsKey(key);
		}
	}
	
	@Override
	public int size() {
	    return length;
	}

	private int getIndex(K key) {
		int keyCode = 0;
		if (key != null) { 
			keyCode = key.hashCode();
		}
		int index = Math.abs(keyCode % capacity);
		return index;
	}
	
    @Override
    public Iterator<KVPair<K, V>> iterator() {
        // Note: you do not need to change this method
        return new ChainedIterator<>(this.chains);
    }
    
    
    private void checkResize() {
		if (length >= capacity*0.75) {
			capacity = 2*capacity;
			IDictionary<K, V>[] temp = this.chains;
			this.chains = makeArrayOfChains(capacity); 
			length = 0;
			for (int i = 0; i < temp.length; i++) {
				if (temp[i] != null) {
			        for (KVPair<K, V> pair : temp[i]) {
			            put(pair.getKey(), pair.getValue());
			        }
				}
			}
			
		}
}

    /**
     * Hints:
     *
     * 1. You should add extra fields to keep track of your iteration
     *    state. You can add as many fields as you want. If it helps,
     *    our reference implementation uses three (including the one we
     *    gave you).
     *
     * 2. Before you try and write code, try designing an algorithm
     *    using pencil and paper and run through a few examples by hand.
     *
     * 3. Think about what exactly your *invariants* are. An *invariant*
     *    is something that must *always* be true once the constructor is
     *    done setting up the class AND must *always* be true both before and
     *    after you call any method in your class.
     *
     *    Once you've decided, write them down in a comment somewhere to
     *    help you remember.
     *
     *    You may also find it useful to write a helper method that checks
     *    your invariants and throws an exception if they're violated.
     *    You can then call this helper method at the start and end of each
     *    method if you're running into issues while debugging.
     *
     *    (Be sure to delete this method once your iterator is fully working.)
     *
     * Implementation restrictions:
     *
     * 1. You **MAY NOT** create any new data structures. Iterators
     *    are meant to be lightweight and so should not be copying
     *    the data contained in your dictionary to some other data
     *    structure.
     *
     * 2. You **MAY** call the `.iterator()` method on each IDictionary
     *    instance inside your 'chains' array, however.
     */
    private static class ChainedIterator<K, V> implements Iterator<KVPair<K, V>> {
        private IDictionary<K, V>[] chains;
        private KVPair<K, V> current;
        private IList<Integer> entries;
        private IDictionary<K, V> currentDict;
        private Iterator<Integer> iterEntries;
        private Iterator<KVPair<K, V>> iterDict;
        
        
        public ChainedIterator(IDictionary<K, V>[] chains) {
        		this.chains = chains;
        		this.entries = new DoubleLinkedList<Integer>();
        		KVPair<K, V> newPair = new KVPair<K, V>(null, null);
        		this.current = newPair;
        		this.currentDict = new ArrayDictionary<K, V>();
        				
        		for (int i = 0; i < chains.length; i++) {
        			if (chains[i] != null) {
        				entries.add(i);
        			}
        		}
        		if(!entries.isEmpty()) {
            		this.iterEntries = entries.iterator();
            		currentDict = chains[iterEntries.next()];
            		this.iterDict = currentDict.iterator();
        		}
        }

        @Override
        public boolean hasNext() {
        		if (entries.isEmpty()) {
        			return false;
        		}
            return (iterEntries.hasNext() || iterDict.hasNext());
        }

        @Override
        public KVPair<K, V> next() {
        		if (!hasNext()) {
        			throw new NoSuchElementException();
        		} 
        		
        		if (!iterDict.hasNext()) {
        			currentDict = chains[iterEntries.next()];
        			iterDict = currentDict.iterator();
        		} 
        		current = iterDict.next();
    			return current;
        }
    }
}

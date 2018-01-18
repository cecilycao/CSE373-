/*  Yiran Fu, Ye Cao
	CSE 373
*/
package datastructures.concrete.dictionaries;

import java.util.Iterator;
import java.util.NoSuchElementException;

import datastructures.concrete.KVPair;
import datastructures.interfaces.IDictionary;
import misc.exceptions.NoSuchKeyException;
import misc.exceptions.NotYetImplementedException;

/**
 * See IDictionary for more details on what this class should do
 */
public class ArrayDictionary<K, V> implements IDictionary<K, V> {
    // You may not change or rename this field: we will be inspecting
    // it using our private tests.
    private Pair<K, V>[] pairs;

    // You're encouraged to add extra fields (and helper methods) though!
    protected static final int INITIAL_CAPACITY  = 10;
    
    private int size;
    private int capacity;
    
    public ArrayDictionary() {
    		this.capacity = INITIAL_CAPACITY;
        this.pairs =  makeArrayOfPairs(INITIAL_CAPACITY);
        this.size = 0;
    }

    /**
     * This method will return a new, empty array of the given size
     * that can contain Pair<K, V> objects.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private Pair<K, V>[] makeArrayOfPairs(int arraySize) {
        // It turns out that creating arrays of generic objects in Java
        // is complicated due to something known as 'type erasure'.
        //
        // We've given you this helper method to help simplify this part of
        // your assignment. Use this helper method as appropriate when
        // implementing the rest of this class.
        //
        // You are not required to understand how this method works, what
        // type erasure is, or how arrays and generics interact. Do not
        // modify this method in any way.
        return (Pair<K, V>[]) (new Pair[arraySize]);
    }

    @Override
    public V get(K key) {
    		if (index(key) != -1) {
    			return pairs[index(key)].value;
    		} 
    		throw new NoSuchKeyException();
    }
    
    private void checkResize() {
    		if (size == capacity) {
    			capacity = 2*capacity;
    			Pair<K, V>[] temp = this.pairs;
    			this.pairs = makeArrayOfPairs(capacity);  
    			for (int i = 0; i < size; i++) {
    				pairs[i] = temp[i];
    			}
    		}
    }
    
    @Override
    public void put(K key, V value) {
    		checkResize();
    		if (index(key) != -1) {
    			pairs[index(key)].value = value;
    		} else {
    			pairs[size] = new Pair<K, V>(key, value);
    			size++;
    		}
    }

    @Override
    public V remove(K key) {
    		int index = index(key);
    		if (index(key) == -1) {
    			throw new NoSuchKeyException();
		}
    		V result = pairs[index].value;    		 
    		if (index != size - 1) {
    			for (int i = index; i < size - 1; i++) {    				
    				pairs[i] = pairs[i + 1];
    			}    			
    		}
		pairs[size  - 1] = null;		
		size--;
		return result;
    }

    @Override
    public boolean containsKey(K key) {
    		if (index(key) != -1) {
			return true;
		}
    		return false;
    }

    @Override
    public int size() {
        return size;
    }
    
    private int index(K key) {
		for (int i = 0; i < size; i++) {
			if (pairs[i].key == null) {
				if (key == null) {
					return i;
				}
			} else if (pairs[i].key == key || pairs[i].key.equals(key)) {	
				return i;
			}
		}
		return -1;
    }

    private static class Pair<K, V> {
        public K key;
        public V value;

        // You may add constructors and methods to this class as necessary.
        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String toString() {
            return this.key + "=" + this.value;
        }
    }
    
    public Iterator<KVPair<K, V>> iterator() {
    		return new DictionaryIterator<K, V>();
    	}
    	 
    	private class DictionaryIterator<K, V> implements Iterator<KVPair<K, V>> {
    		private KVPair<K, V> current;
		private int index;
    		
    		public DictionaryIterator() {
    			this.index = 0;
    			KVPair<K, V> newPair = new KVPair(null, null);
    			this.current = newPair;
    		}
    		
    		@Override
    	    public boolean hasNext() {			
    			return index < size;    			
    	    }

    	    @Override
    	    public KVPair<K, V> next() {
    	    		if (!hasNext()) {
    	    			throw new NoSuchElementException();
    	    		}    	    		
    	        current = new KVPair(pairs[index].key, pairs[index].value);
    	        index = index + 1;
    	        return current;
    	    	}
    	}
}

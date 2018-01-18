/*  Yiran Fu, Ye Cao
CSE 373
*/
package datastructures.concrete;

import datastructures.interfaces.IList;
import misc.exceptions.EmptyContainerException;
import misc.exceptions.NotYetImplementedException;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
* Note: For more info on the expected behavior of your methods, see
* the source code for IList.
*/
public class DoubleLinkedList<T> implements IList<T> {
    // You may not rename these fields or change their types.
    // We will be inspecting these in our private tests.
    // You also may not add any additional fields.
    private Node<T> front;
    private Node<T> back;
    private int size;
    
    public DoubleLinkedList() {
        this.front = null;
        this.back = null;
        this.size = 0;
    }
    
    @Override
    public void add(T item) {
        Node<T> newNode = new Node(back, item, null);
        if (back != null) {
            back.next = newNode;
        }
        if (front == null) {
            front = newNode;
        }
        back = newNode;
        size++;
    }
    
    @Override
    public T remove() {
        if (size == 0) {
            throw new EmptyContainerException();
        }
        Node<T> temp = back;
        if (size == 1) {
            front = null;
            back = null;
        } else {
            back = back.prev;
            back.next = null;
            temp.prev = null;
        }
        size--;
        return temp.data;
    }
    
    @Override
    public T get(int index) {
        IndexOutOfBoundException(index);
        Node<T> temp = find(index);
        return temp.data;
    }
    
    
    @Override
    public void set(int index, T item) {
        IndexOutOfBoundException(index);
        insert(index, item);
        delete(index + 1);
        
    }
    
    @Override
    public void insert(int index, T item) {
        if (index > size || index < 0) {
            throw new IndexOutOfBoundsException();
        }
        
        if (index == 0 && size != 0) {
            Node<T> nextNode = front;
            Node<T> newNode = new Node(null, item, nextNode);
            nextNode.prev = newNode;
            front = newNode;
        }  else if (index == size) {
            add(item);
            size--;
        } else {
            Node<T> nextNode = find(index);
            Node<T> prevNode = find(index).prev;
            Node<T> newNode = new Node(prevNode, item, nextNode);
            prevNode.next = newNode;
            nextNode.prev = newNode;
        }
        size++;
        
    }
    
    @Override
    public T delete(int index) {
        IndexOutOfBoundException(index);
        Node<T> deleteNode = find(index);
        Node<T> prevNode = deleteNode.prev;
        Node<T> nextNode = deleteNode.next;
        if (index == 0) {
            front = front.next;
            deleteNode.next = null;
            if (nextNode != null) {
                nextNode.prev = null;
            }
            size--;
        } else if (index == size - 1) {
            remove();
        } else {
            prevNode.next = nextNode;
            nextNode.prev = prevNode;
            deleteNode.next = null;
            deleteNode.prev = null;
            size--;
        }
        return deleteNode.data;
    }
    
    @Override
    public int indexOf(T item) {
        Node<T> temp = front;
        for (int i = 0; i < size; i++) {
            if (temp.data == item || temp.data.equals(item)) {
                return i;
            }
            temp = temp.next;
        }
        return -1;
    }
    
    @Override
    public int size() {
        return size;
    }
    
    @Override
    public boolean contains(T other) {
        if (size == 0) {
            return false;
        }
        if (indexOf(other) >= 0) {
            return true;
        }
        return false;
    }
    
    @Override
    public Iterator<T> iterator() {
        // Note: we have provided a part of the implementation of
        // an iterator for you. You should complete the methods stubs
        // in the DoubleLinkedListIterator inner class at the bottom
        // of this file. You do not need to change this method.
        return new DoubleLinkedListIterator<>(this.front);
    }
    
    //A helper method test whether the input index is out of bound
    //throw IndexOutOfBoundsException if index is out of bound
    private void IndexOutOfBoundException(int index) {
        if (index >= size || index < 0) {
            throw new IndexOutOfBoundsException();
        }
    }
    
    
    //A helper method return the node of given index
    public Node<T> find(int index) {
        Node<T> temp = front;
        if (index < (size/2)) {
            for (int i = 0; i < index; i++) {
                temp = temp.next;
            }
        } else {
            temp = back;
            for (int i = 0; i < (size - index - 1); i++) {
                temp = temp.prev;
            }
        }
        return temp;
    }
    
    private static class Node<E> {
        // You may not change the fields in this node or add any new fields.
        public final E data;
        public Node<E> prev;
        public Node<E> next;
        
        public Node(Node<E> prev, E data, Node<E> next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }
        
        public Node(E data) {
            this(null, data, null);
        }
        
        // Feel free to add additional constructors or methods to this class.
        
    }
    
    private static class DoubleLinkedListIterator<T> implements Iterator<T> {
        // You should not need to change this field, or add any new fields.
        private Node<T> current;
        
        public DoubleLinkedListIterator(Node<T> current) {
            // You do not need to make any changes to this constructor.
            this.current = current;
        }
        
        /**
        * Returns 'true' if the iterator still has elements to look at;
        * returns 'false' otherwise.
        */
        public boolean hasNext() {
            return current != null;
        }
        
        /**
        * Returns the next item in the iteration and internally updates the
        * iterator to advance one element forward.
        *
        * @throws NoSuchElementException if we have reached the end of the iteration and
        *         there are no more elements to look at.
        */
        public T next() {
            //throw new NotYetImplementedException();
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            T element = current.data; //data???
            current = current.next;
            return element;
            
        }
    }
}
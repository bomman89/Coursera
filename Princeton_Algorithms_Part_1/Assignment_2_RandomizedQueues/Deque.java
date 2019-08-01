import java.util.Iterator;
import edu.princeton.cs.algs4.StdOut;
import java.lang.NullPointerException;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private class ItemListNode {
        Item item;
        ItemListNode next;
        ItemListNode prev;
    };
    
    private ItemListNode headOfList;
    private ItemListNode tailOfList;
    private int          numOfItems;
    
    // construct an empty deque
    public Deque() {
        numOfItems = 0;
        headOfList = null;
        tailOfList = null;
    }
    
    // is the deque empty?
    public boolean isEmpty() {
        return (numOfItems == 0);
    }
    
    // return the number of items on the deque
    public int size() {
        return numOfItems;
    }
    
    // add the item to the front
    public void addFirst(Item item) {
        if (item == null)
            throw new NullPointerException("addFirst : Trying to queue null item\n");
        ItemListNode newNode = new ItemListNode();
        if (headOfList == null) {
            headOfList = newNode;
            headOfList.item = item;
            headOfList.next = null;
            headOfList.prev = null;
            tailOfList = headOfList;
        } else {
            newNode.item = item;
            newNode.next = headOfList;
            newNode.prev = null;
            headOfList.prev = newNode;
            headOfList = newNode;
        }
        numOfItems++;
    }
    
    // add the item to the end
    public void addLast(Item item) {
        if (item == null)
            throw new NullPointerException("addLast : Trying to queue null item\n");
         
        ItemListNode newNode = new ItemListNode();
        
        if (tailOfList == null) {
            tailOfList = newNode;
            tailOfList.item = item;
            tailOfList.next = null;
            tailOfList.prev = null;
            headOfList = tailOfList;
        } else {
            newNode.item = item;
            newNode.next = null;
            newNode.prev = tailOfList;
            tailOfList.next = newNode;
            tailOfList   = newNode;
        }
        numOfItems++;
    }
    
    // remove and return the item from the front
    public Item removeFirst() {
        if (headOfList == null)
            throw new NoSuchElementException("removeFirst : trying to remove when there are no items in the queue\n");
        Item item = headOfList.item;
        headOfList = headOfList.next;
        if (headOfList == null)
            tailOfList = null;
        else
            headOfList.prev = null;
        numOfItems--;
        return item;
    }
    
    // remove and return the item from the end
    public Item removeLast() {
        if (tailOfList == null)
            throw new NoSuchElementException("removeLast : trying to remove when there are no items in the queue\n");
        Item item = tailOfList.item;
        tailOfList = tailOfList.prev;
        if (tailOfList == null)
            headOfList = null;
        else 
            tailOfList.next = null;
        numOfItems--;
        return item;
    }
    
        // return an iterator over items in order from front to end
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }
    
    private class DequeIterator implements Iterator<Item> {
        private ItemListNode nextNodeForIteration;
        
        public DequeIterator() {
            nextNodeForIteration = headOfList;
        }
        
        public boolean hasNext() {
            return (nextNodeForIteration != null);
        }
        
        public Item next() {
            if (nextNodeForIteration == null)
                throw new NoSuchElementException("Iterator next : Trying to iterate when there are no more items left\n");
            Item item = nextNodeForIteration.item;
            nextNodeForIteration = nextNodeForIteration.next;
            return item;
        }
        
        public void remove() {
            throw new UnsupportedOperationException("Iterator remove not supported\n");
        }
    }
    
    // unit testing
    public static void main(String[] args) {
        Deque<String> dequeObj = new Deque<String>();
        
        dequeObj.addLast("Karthik");
        dequeObj.addLast("Thaijasa");

        for (String first : dequeObj) 
            StdOut.println(first);
    }
}
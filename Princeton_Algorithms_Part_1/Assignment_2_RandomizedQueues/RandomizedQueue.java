import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdOut;
import java.util.Iterator;
import java.lang.NullPointerException;
import java.lang.UnsupportedOperationException;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private int  arraySize;
    private int  numItemsInQueue;
    private Item[] itemArray;
    
    // construct an empty randomized queue
    public RandomizedQueue() {
        arraySize       = 1;
        numItemsInQueue = 0;
        
        itemArray = (Item[]) new Object[arraySize];
    }
    
    // is the queue empty?
    public boolean isEmpty() {
        return (numItemsInQueue == 0);
    }
    
    // return the number of items on the queue
    public int size() {
        return numItemsInQueue;
    }
    
    private void resizeArray(int newSize) {
        Item[] newItemArray = (Item[]) new Object[newSize];
        
        int i;
        for (i=0; i<numItemsInQueue; i++) {
            newItemArray[i] = itemArray[i];
        }
        itemArray = newItemArray;
        arraySize = newSize;
    }
    // add the item
    public void enqueue(Item item) {
        if (item == null)
            throw new NullPointerException("enqueue : Trying to queue null item\n");
        
        if (numItemsInQueue >= arraySize)
            resizeArray(arraySize*2);
         
        itemArray[numItemsInQueue] = item;
        numItemsInQueue++;
    }
    
    // remove and return a random item
    public Item dequeue() {
        if (numItemsInQueue == 0)
            throw new NoSuchElementException("dequeue : Trying to dequeue when there are no items in queue\n");
        int itemIdxToRemove = (numItemsInQueue > 1) 
                              ? StdRandom.uniform(0,numItemsInQueue) : 0;
        Item item = itemArray[itemIdxToRemove];
        
        itemArray[itemIdxToRemove] = itemArray[numItemsInQueue-1];
        itemArray[numItemsInQueue-1] = null;
        numItemsInQueue--;
        if ((numItemsInQueue <= arraySize/4)
            && (arraySize/2 != 0))
            resizeArray(arraySize/2);
        return item;
    }
    
    // return (but do not remove) a random item
    public Item sample() {
        if (numItemsInQueue == 0)
            throw new NoSuchElementException("sample : Trying to sample when there are no items in queue\n");
        
        int itemIdxToRemove = StdRandom.uniform(0,numItemsInQueue);
        return itemArray[itemIdxToRemove];
    }
    
    // return an iterator over items in order from front to end
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }
    
    private class RandomizedQueueIterator implements Iterator<Item> {
        private int numItemsToIterate;
        private Item[] itemArrayForIteration;
        
        public RandomizedQueueIterator() {
            numItemsToIterate = numItemsInQueue;
            itemArrayForIteration = (Item[]) new Object[numItemsToIterate];
            int i;
            for(i=0; i<numItemsToIterate; i++) {
                itemArrayForIteration[i] = itemArray[i];
            }
        }
        
        public boolean hasNext() {
            return (numItemsToIterate != 0);
        }
        
        public Item next() {
            if (numItemsToIterate == 0)
                throw new NoSuchElementException("Iterator next : Trying to iterate when there are no more items left\n");
            
            int itemIdxToRemove = StdRandom.uniform(0,numItemsToIterate);
            Item item = itemArrayForIteration[itemIdxToRemove];
        
            itemArrayForIteration[itemIdxToRemove] = itemArrayForIteration[numItemsToIterate-1];
            itemArrayForIteration[numItemsToIterate-1] = null;
            numItemsToIterate--;
            return item;
        }
        
        public void remove() {
            throw new UnsupportedOperationException("Iterator remove not supported\n");
        }
    }
    
    // unit testing
    public static void main(String[] args) {
        RandomizedQueue<String> rqObj = new RandomizedQueue<String>();
        
        rqObj.enqueue("Karthik");
        rqObj.enqueue("Thaijasa");
        rqObj.enqueue("Mysore");
        rqObj.enqueue("Lalimli");
        
        for (String stringInIteration : rqObj)
            StdOut.println(stringInIteration);
        
        for (String stringInIteration : rqObj)
            StdOut.println(stringInIteration);
        
        StdOut.println(rqObj.dequeue());
        StdOut.println(rqObj.size());
    }
}
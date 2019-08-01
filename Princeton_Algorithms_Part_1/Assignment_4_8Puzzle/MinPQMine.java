//import java.util.Comparator;
import java.util.Iterator;
import edu.princeton.cs.algs4.StdOut;

public class MinPQMine<Key> implements Iterable<Key> {    
    Key[] minPQMineKeyArrObj;
    int   minPQMineCurrArrSize;
    int   minPQMineNumElements;
    
    public MinPQMine() {
        minPQMineKeyArrObj = (Key[])new Object[1];
        minPQMineCurrArrSize = 1;
        minPQMineNumElements = 0;
    }
    
    private void MinPQMineKeyArrResize(boolean notInsertOrDelete) {
        Key[] newKeyArrObj = null;
        if (!notInsertOrDelete) {
            newKeyArrObj = (Key[]) new Object[2*minPQMineCurrArrSize];
            minPQMineCurrArrSize = 2*minPQMineCurrArrSize;
        }
        else {
            newKeyArrObj = (Key[]) new Object[minPQMineCurrArrSize/2];
            minPQMineCurrArrSize = minPQMineCurrArrSize/2; 
        }
        
        int i;
        for (i=0; i<minPQMineNumElements; i++)
            newKeyArrObj[i] = minPQMineKeyArrObj[i];
        minPQMineKeyArrObj = newKeyArrObj;
    }
    
    private void exch(Key[] arr, int idx1, int idx2) {
        Key tempKeyObj = arr[idx1];
        arr[idx1] = arr[idx2];
        arr[idx2]   = tempKeyObj;
    }
    
    private void swim(int startIdx) {
        int currIdx = startIdx;
        
        while (currIdx > 1) {
            if (((Comparable<Key>)minPQMineKeyArrObj[currIdx/2]).compareTo(minPQMineKeyArrObj[currIdx]) > 0) {
                exch(minPQMineKeyArrObj, currIdx, currIdx/2);
                currIdx = currIdx/2;
            } else 
                break;
        }
    }
    public void insert(Key keyObj) {
        if (++minPQMineNumElements >= minPQMineCurrArrSize)
            MinPQMineKeyArrResize(false);
        
        minPQMineKeyArrObj[minPQMineNumElements] = keyObj;
        swim(minPQMineNumElements);
    }
    
    
    private void sink(Key[] heapArr, int startIdx, int heapArrSize) {
        int currIdx = startIdx;
        
        while (currIdx*2 < heapArrSize) {
            int idx1 = currIdx*2;
            
            if ((idx1+1 < heapArrSize)
                    && (((Comparable<Key>)heapArr[idx1+1]).compareTo(heapArr[idx1]) < 0)) 
                idx1++;
            
            if ((((Comparable<Key>)heapArr[idx1]).compareTo(heapArr[currIdx]) < 0)) {
                exch(heapArr, idx1, currIdx);
                currIdx = idx1;
                continue;
            }
            
            currIdx = currIdx*2;
        }
    }
    public Key delMin() {
        Key keyObj = minPQMineKeyArrObj[1];
        minPQMineKeyArrObj[1] = minPQMineKeyArrObj[minPQMineNumElements];
        minPQMineKeyArrObj[minPQMineNumElements] = null;
        minPQMineNumElements--;
        sink(minPQMineKeyArrObj, 1, minPQMineNumElements);
        return keyObj;
    }
    
    public boolean isEmpty() {
        return (minPQMineNumElements <= 0);
    }
    public Key min() {
        return minPQMineKeyArrObj[1];
    }
    
    private class MinPQMineIterator implements Iterator<Key> {
        Key[] minPQMineIteratorKeyArrObj;
        int   minPQMineIteratorNumElements;
        
        public MinPQMineIterator() {
            minPQMineIteratorNumElements = minPQMineNumElements;
            minPQMineIteratorKeyArrObj = (Key[])new Object[minPQMineIteratorNumElements+1];
            
            int i;
            for (i=0; i<=minPQMineIteratorNumElements; i++) {
                minPQMineIteratorKeyArrObj[i] = minPQMineKeyArrObj[i];
            }
        }
        
        public Key next() {
            Key keyObj = minPQMineIteratorKeyArrObj[1];
            minPQMineIteratorKeyArrObj[1] = minPQMineIteratorKeyArrObj[minPQMineIteratorNumElements];
            minPQMineIteratorKeyArrObj[minPQMineIteratorNumElements] = null;
            minPQMineIteratorNumElements--;
            sink(minPQMineIteratorKeyArrObj, 1, minPQMineIteratorNumElements);
            return keyObj;
        }
        
        public boolean hasNext() {
            return (minPQMineIteratorNumElements > 0);
        }
        
        public void remove() {
        }
        
    }
    
    public Iterator<Key> iterator() {
        return new MinPQMineIterator();
    }
    
    public static void main(String[] args) {
        MinPQMine<Integer> minPQMineObj = new MinPQMine<Integer>();
        
        minPQMineObj.insert(-8);
        minPQMineObj.insert(0);
        minPQMineObj.insert(-1);
        minPQMineObj.insert(2);
        minPQMineObj.insert(0);
        minPQMineObj.insert(-8);
        minPQMineObj.insert(2);
        minPQMineObj.insert(-1);
        
        for (Integer intObj : minPQMineObj) {
            StdOut.println(intObj);
        }
        
    }
    
}
import java.util.Comparator;
import edu.princeton.cs.algs4.StdOut;
import java.util.Iterator;
import edu.princeton.cs.algs4.StdRandom;
import java.lang.Math;

public class BSTMine<Key> implements Iterable<Key>
{
    private static final boolean black = false;
    private static final boolean red   = true;
    
    private class BSTMineKeyNode {
        public Key     keyObj;
        public boolean color;
        public  BSTMineKeyNode left;
        public BSTMineKeyNode right;
        
        public BSTMineKeyNode(Key inputKeyObj) {
            keyObj = inputKeyObj;
            color  = red;
            left   = null;
            right  = null;
        }
    }
    
    public BSTMineKeyNode  bstMineRoot;
    private Comparator<Key> bstMineKeyComparator;
    private int             bstMineNumKeys;
    private int             bstMineMaxHeight;
    public BSTMine() {
        bstMineRoot          = null;
        bstMineKeyComparator = null;
        bstMineNumKeys       = 0;
    }
    
    public BSTMine(Comparator<Key> comparator) {
        bstMineRoot          = null;
        bstMineKeyComparator = comparator;
        bstMineNumKeys       = 0;
    }
    
    private BSTMineKeyNode treeRotateRight(BSTMineKeyNode parent) {
        BSTMineKeyNode leftChild = parent.left;
        BSTMineKeyNode leftChildRight = leftChild.right;
        
        leftChild.right = parent;
        parent.left     = leftChildRight;
        leftChild.color = parent.color;
        parent.color    = red;
        
        return leftChild;
    }
   
    private BSTMineKeyNode treeRotateLeft(BSTMineKeyNode parent) {
        BSTMineKeyNode rightChild = parent.right;
        BSTMineKeyNode rightChildLeft = rightChild.left;
        
        rightChild.left = parent;
        parent.right     = rightChildLeft;
        rightChild.color = parent.color;
        parent.color    = red;
        
        return rightChild;
    }
    
    private BSTMineKeyNode moveRedLeft(BSTMineKeyNode parent) {
        flipColors(parent);
        if (isRed(parent.right.left)) {
            parent.right = treeRotateRight(parent.right);
            parent = treeRotateLeft(parent);
            flipColors(parent);
        }
        return parent;
    }
    
    private BSTMineKeyNode moveRedRight(BSTMineKeyNode parent) {
        flipColors(parent);
        if (isRed(parent.left.left)) {
            parent = treeRotateRight(parent);
            flipColors(parent);
        }
        return parent;
    }
    
    private void flipColors(BSTMineKeyNode parent) {
        parent.color = !parent.color;
        parent.right.color = !parent.right.color;
        parent.left.color  = !parent.left.color;
          
    }
    
    private boolean isRed(BSTMineKeyNode keyNodeObj) {
        if (keyNodeObj == null)
            return false;
        
        if (keyNodeObj.color == red)
            return true;
        
        return false;
    }
    
    private BSTMineKeyNode bstInsert(BSTMineKeyNode keyNodeObj, Key keyObj) {
        if (keyNodeObj == null)
            return new BSTMineKeyNode(keyObj);
        
        if (bstMineKeyComparator == null) {
            if (((Comparable<Key>)keyObj).compareTo(keyNodeObj.keyObj) >= 0) 
                keyNodeObj.right = bstInsert(keyNodeObj.right, keyObj);
            else 
                keyNodeObj.left = bstInsert(keyNodeObj.left, keyObj);
        }
        
        keyNodeObj = balanceTree(keyNodeObj);
        return keyNodeObj;
    }
    
    public void add(Key keyObj) {
        bstMineNumKeys++;
        if (bstMineRoot == null) {
            bstMineRoot = new BSTMineKeyNode(keyObj);
            bstMineRoot.color = black;
            return;
        } else 
            bstMineRoot = bstInsert(bstMineRoot, keyObj);
    }
    
    public boolean contains(Key keyObj) {
        BSTMineKeyNode keyNodeObj = bstMineRoot;
        
        while (keyNodeObj != null) {
            if (((Comparable<Key>)keyObj).compareTo(keyNodeObj.keyObj) > 0)
                keyNodeObj = keyNodeObj.right;
            else if (((Comparable<Key>)keyObj).compareTo(keyNodeObj.keyObj) < 0)
                keyNodeObj = keyNodeObj.left;
            else 
                return true;
        }
        return false;
    }
    
    BSTMineKeyNode minRightKeyNodeObj;
    private BSTMineKeyNode balanceTree(BSTMineKeyNode keyNodeObj) {
        
        if (isRed(keyNodeObj.left) && isRed(keyNodeObj.left.left)) keyNodeObj = treeRotateRight(keyNodeObj);
        if (isRed(keyNodeObj.right) && !isRed(keyNodeObj.left)) keyNodeObj = treeRotateLeft(keyNodeObj);
        if (isRed(keyNodeObj.right) && isRed(keyNodeObj.left)) flipColors(keyNodeObj);
        
        return keyNodeObj;
    }
    
    public Key min() {
        BSTMineKeyNode currKeyNodeObj = bstMineRoot;
        BSTMineKeyNode prevKeyNodeObj = null;
        
        while (currKeyNodeObj != null) {
            prevKeyNodeObj = currKeyNodeObj;
            currKeyNodeObj = currKeyNodeObj.left;
        }
        return ((prevKeyNodeObj != null) ? prevKeyNodeObj.keyObj : null);
    }
    
    public Key max() {
        BSTMineKeyNode currKeyNodeObj = bstMineRoot;
        BSTMineKeyNode prevKeyNodeObj = null;
        
        while (currKeyNodeObj != null) {
            prevKeyNodeObj = currKeyNodeObj;
            currKeyNodeObj = currKeyNodeObj.right;
        }
        return ((prevKeyNodeObj != null) ? prevKeyNodeObj.keyObj : null);
    }
    
    public Key floor(Key keyObj) {
        BSTMineKeyNode currKeyNodeObj = bstMineRoot;
        BSTMineKeyNode floorKeyNodeObj = null;
        
        while (currKeyNodeObj != null) {
            if (((Comparable<Key>)keyObj).compareTo(currKeyNodeObj.keyObj) > 0)  {
                floorKeyNodeObj = currKeyNodeObj;
                currKeyNodeObj = currKeyNodeObj.right;
            } else if (((Comparable<Key>)keyObj).compareTo(currKeyNodeObj.keyObj) == 0) {
                floorKeyNodeObj = currKeyNodeObj;
                currKeyNodeObj = null;
            } else {
                currKeyNodeObj = (floorKeyNodeObj != null) ? null : currKeyNodeObj.left;
            }
        }
        return ((floorKeyNodeObj != null) ? floorKeyNodeObj.keyObj : null);
    }
    
    public Key ceiling(Key keyObj) {
        BSTMineKeyNode currKeyNodeObj = bstMineRoot;
        BSTMineKeyNode ceilingKeyNodeObj = null;
        
        while (currKeyNodeObj != null) {
            if (((Comparable<Key>)keyObj).compareTo(currKeyNodeObj.keyObj) > 0) {
                currKeyNodeObj = (ceilingKeyNodeObj != null) ? null : currKeyNodeObj.right;
            } else if (((Comparable<Key>)keyObj).compareTo(currKeyNodeObj.keyObj) == 0) {
                ceilingKeyNodeObj = currKeyNodeObj;
                currKeyNodeObj = null;
            } else {
                ceilingKeyNodeObj = currKeyNodeObj;
                currKeyNodeObj = currKeyNodeObj.left;
            }
        }
        return ((ceilingKeyNodeObj != null) ? ceilingKeyNodeObj.keyObj : null);
    }
    
    private int rangeBstWalk(BSTMineKeyNode keyNodeObj, Key minKeyObj, Key maxKeyObj, int numItemsInRange) {
            if (keyNodeObj == null)
                return numItemsInRange;
            
            if (((Comparable<Key>)minKeyObj).compareTo(keyNodeObj.keyObj) > 0) {
                numItemsInRange = rangeBstWalk(keyNodeObj.right, minKeyObj, maxKeyObj, numItemsInRange);
                return numItemsInRange;
            }
            
            if (((Comparable<Key>)maxKeyObj).compareTo(keyNodeObj.keyObj) < 0) {
                numItemsInRange = rangeBstWalk(keyNodeObj.left, minKeyObj, maxKeyObj, numItemsInRange);
                return numItemsInRange;
            }

            numItemsInRange++;
            numItemsInRange = rangeBstWalk(keyNodeObj.left, minKeyObj, maxKeyObj, numItemsInRange);
            numItemsInRange = rangeBstWalk(keyNodeObj.right, minKeyObj, maxKeyObj, numItemsInRange);
            return numItemsInRange;
    }

    public int range(Key minKeyObj, Key maxKeyObj) {
        BSTMineKeyNode currKeyNodeObj = bstMineRoot;
        BSTMineKeyNode startKeyNodeObj = null;
        
        if ((((Comparable<Key>)minKeyObj).compareTo(max()) > 0) 
            || (((Comparable<Key>)maxKeyObj).compareTo(min()) < 0))
            return 0;
        
        while ((startKeyNodeObj == null)
               && (currKeyNodeObj != null)){
            if (((Comparable<Key>)minKeyObj).compareTo(currKeyNodeObj.keyObj) > 0) {
                currKeyNodeObj = currKeyNodeObj.right;
            } else if (((Comparable<Key>)maxKeyObj).compareTo(currKeyNodeObj.keyObj) < 0) {
                currKeyNodeObj = currKeyNodeObj.left;
            } else {
                startKeyNodeObj = currKeyNodeObj;
                currKeyNodeObj = null;
            }
        }
        
        int numItemsInRange = 0;
        numItemsInRange = rangeBstWalk(startKeyNodeObj, minKeyObj, maxKeyObj, numItemsInRange);
        return numItemsInRange;
    }
    
    
    private BSTMineKeyNode deleteMin(BSTMineKeyNode keyNodeObj) {
        if (keyNodeObj.left == null) {
            minRightKeyNodeObj = keyNodeObj;
            minRightKeyNodeObj.right = null;
            return keyNodeObj.right;
        }
        
        if (!isRed(keyNodeObj.left) && !isRed(keyNodeObj.left.left))
            keyNodeObj = moveRedLeft(keyNodeObj);
        
        keyNodeObj.left = deleteMin(keyNodeObj.left);
        keyNodeObj = balanceTree(keyNodeObj);
        return keyNodeObj;
    }
    
    private BSTMineKeyNode bstDelete(BSTMineKeyNode keyNodeObj, Key keyObj) {
        
        if (bstMineKeyComparator == null) {
            if (((Comparable<Key>)keyObj).compareTo(keyNodeObj.keyObj) >= 0) {
                if (isRed(keyNodeObj.left))
                    keyNodeObj = treeRotateRight(keyNodeObj);
                
                if ((((Comparable<Key>)keyObj).compareTo(keyNodeObj.keyObj) == 0)
                        && keyNodeObj.right == null) 
                    return null;
                
                if (!isRed(keyNodeObj.right) && !isRed(keyNodeObj.right.left))
                    keyNodeObj = moveRedRight(keyNodeObj);   
                
                if (((Comparable<Key>)keyObj).compareTo(keyNodeObj.keyObj) == 0) {
                    minRightKeyNodeObj = null;
                    keyNodeObj.right = deleteMin(keyNodeObj.right);
                    if (keyNodeObj.right != minRightKeyNodeObj)
                        minRightKeyNodeObj.right = keyNodeObj.right;
                    
                    if (keyNodeObj.left != minRightKeyNodeObj)
                        minRightKeyNodeObj.left = keyNodeObj.left;
                    
                    keyNodeObj = minRightKeyNodeObj;
                } else 
                    keyNodeObj.right = bstDelete(keyNodeObj.right, keyObj);
            } else {
                if (!isRed(keyNodeObj.left) && !isRed(keyNodeObj.left.left))
                    keyNodeObj = moveRedLeft(keyNodeObj);
                keyNodeObj.left = bstDelete(keyNodeObj.left, keyObj);
            }
        }
        
        keyNodeObj = balanceTree(keyNodeObj);
        return keyNodeObj;
        
    }
    
    public void delete(Key keyObj)
    {
        if (!contains(keyObj))
            return;
        
        bstMineRoot = bstDelete(bstMineRoot, keyObj);
        bstMineNumKeys--;
        if (bstMineNumKeys != 0) {
            bstMineRoot.color = black;
        }
    }
    static int bstHeightTrack = 0;
    private void bstWalk(BSTMineKeyNode keyNodeObj) {
        if (keyNodeObj == null) {
            if (bstHeightTrack > bstMineMaxHeight)
                bstMineMaxHeight = bstHeightTrack;
            return;
        }
        int bstLevelOfThisNode = bstHeightTrack;
        bstHeightTrack++;
        bstWalk(keyNodeObj.left);
        bstHeightTrack = bstLevelOfThisNode;
        bstWalk(keyNodeObj.right);
    }
    
    public void bstTraverse() {
        bstMineMaxHeight = 0;
        bstHeightTrack = 0;
        if (bstMineRoot != null) {
            bstWalk(bstMineRoot);
        }
    }
    
    public int bstMaxHeight() {
        bstMineMaxHeight = 0;
        bstTraverse();
        return bstMineMaxHeight;
    }
    
    
    private class BSTMineIterator implements Iterator<Key> {
        private int bstMineIteratorNumKeys;
        private Key[] bstMineIteratorKeys;
        
        private void BSTMineIteratorWalk(BSTMineKeyNode keyNodeObj) {
            if (keyNodeObj == null)
                return;
            bstMineIteratorKeys[bstMineIteratorNumKeys++] = keyNodeObj.keyObj;
            
            BSTMineIteratorWalk(keyNodeObj.left);
            BSTMineIteratorWalk(keyNodeObj.right);
        }
        
        public BSTMineIterator() {
            bstMineIteratorNumKeys = 0;
            bstMineIteratorKeys    = (Key[])new Object[bstMineNumKeys];
            BSTMineIteratorWalk(bstMineRoot);
        }
        
        public Key next() {
            return bstMineIteratorKeys[--bstMineIteratorNumKeys];
        }
        
        public boolean hasNext() {
            return (bstMineIteratorNumKeys > 0);
        }
        
        public void remove() {
        }
    }
    
    public Iterator<Key> iterator() {
        return new BSTMineIterator();
    }
    
    private BSTMineKeyNode bstMineListHead;
    private BSTMineKeyNode bstMineListTail;
    public void bstListTraverse() {
        BSTMineKeyNode keyNodeObj = bstMineListHead;
        int numKeys = 0;
        while (keyNodeObj != bstMineListTail) {
            numKeys++;
            StdOut.println(keyNodeObj.keyObj);
            keyNodeObj = keyNodeObj.right;
        }
        numKeys++;
        StdOut.println(bstMineListTail.keyObj);
        StdOut.println("Number of keys");
        StdOut.println(numKeys);
    }
    private void bstToListSetup(BSTMineKeyNode keyNodeObj) {
        keyNodeObj.left = keyNodeObj;
        keyNodeObj.right = keyNodeObj;
    }
    
    private void bstToListAdd(BSTMineKeyNode keyNodeObj) {
        if (bstMineListHead == null) {
            bstMineListHead = keyNodeObj;
            bstToListSetup(keyNodeObj);
            bstMineListTail = keyNodeObj;
        } else {
            keyNodeObj.right = bstMineListTail.right;
            bstMineListTail.right = keyNodeObj;
            keyNodeObj.left = bstMineListTail;
            bstMineListTail = keyNodeObj;
        }
    }
    
    private void bstToList(BSTMineKeyNode keyNodeObj) {
        if (keyNodeObj == null)
            return;
        
        bstToList(keyNodeObj.left);
        BSTMineKeyNode right = keyNodeObj.right;
        bstToListAdd(keyNodeObj);
        bstToList(right);
    }
    public void bstMineToList() {
        bstMineListHead = null;
        if (bstMineRoot != null) {
            bstToList(bstMineRoot);
        }
    }
    
    public static void main(String[] args) {
        int numElementsToInsert = 0;
        int numIterationsOfInsert = 1;
        Integer intToInsert = 0;
        
        while (numIterationsOfInsert > 0) {
            numElementsToInsert = StdRandom.uniform(2048,65536);;
            
            int maxElementsToInsert = numElementsToInsert;
            BSTMine<Integer> bstMineObj = new BSTMine<Integer>();
            intToInsert = 0;
            while (numElementsToInsert > 0) {
                intToInsert++;
                bstMineObj.add(intToInsert);
                numElementsToInsert--;
            }
            // bstMineObj.bstTraverse();
            StdOut.println(maxElementsToInsert);
            
            // StdOut.println(bstMineObj.bstMineRoot.keyObj);
            
            numElementsToInsert = maxElementsToInsert;
            intToInsert = maxElementsToInsert;
            while (numElementsToInsert > 0) {
                bstMineObj.delete(intToInsert--);
                //StdOut.println(bstMineObj.bstMineRoot.keyObj);
                numElementsToInsert--;
            }
            if (bstMineObj.bstMineRoot != null) {
                StdOut.println("Root not null");
                StdOut.println(bstMineObj.bstMineRoot.keyObj);
            }
            
            //bstMineObj.bstMineToList();
            //bstMineObj.bstListTraverse();
            bstMineObj = null;
            numIterationsOfInsert--;
        }
        


        //StdOut.println(bstMineObj.bstMineRoot.keyObj);
    }
}
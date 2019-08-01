import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdOut;
import java.util.Comparator;

public class ArraySortMine<Key> {
    private Comparator<Key> comparator;
    
    private void exch(Key[] arr, int idx1, int idx2) {
        Key keyObj = arr[idx1];
        arr[idx1] = arr[idx2];
        arr[idx2] = keyObj;
    }
    
    private void insertionSort(Key[] arr, int lo, int hi) {
        int i,j;
        
        for (i=lo; i<=hi; i++) {
            for (j=i; j>lo; j--) {
                if (comparator == null) {
                    if (((Comparable<Key>)arr[j]).compareTo(arr[j-1]) < 0)
                        exch(arr, j, j-1);
                    else
                        break;
                } else {
                    if (comparator.compare(arr[j], arr[j-1]) < 0)
                        exch(arr, j, j-1);
                    else
                        break;
                }
            }
        }
    }
    
    private void quickSort(Key[] arr, int lo, int hi) {
        if (hi <= lo)
            return;
        
        int lengthOfArray = hi - lo + 1;
        if (lengthOfArray < 10) {
            insertionSort(arr, lo, hi);
            return;
        }
        
        int pivot = lo;
        int i = lo+1;
        int j = hi;
        while (j >= i) {
            if (comparator == null) {
                while ((i <= j)
                       && (((Comparable<Key>)arr[i]).compareTo(arr[pivot]) < 0)) i++;
                if (i > j) break;
                
                while ((j >= i)
                       && ((Comparable<Key>)arr[j]).compareTo(arr[pivot]) > 0) j--;
                if (j < i) break;
            } else {
                while ((i <= j)
                       && (comparator.compare(arr[i], arr[pivot]) < 0)) i++;
                if (i > j) break;
                
                while ((j >= i)
                       && (comparator.compare(arr[j], arr[pivot]) > 0)) j--;
                if (j < i) break;
            }
            exch(arr, i, j);
            i++; 
            j--;
        }
        
        exch(arr, --i, pivot);
        quickSort(arr, lo, i-1);
        quickSort(arr, i+1, hi);
    }
    
    private void validateSorting(Key[] inputArr, int inputArrSize) {
        int i;
        
        for (i=0; i<inputArrSize-1; i++) {
            if (comparator == null) {
                if (((Comparable<Key>)inputArr[i]).compareTo(inputArr[i+1]) > 0) {
                    throw new NullPointerException("Array Sorting : Output array not sorted");
                }
            } else {
                if (comparator.compare(inputArr[i], inputArr[i+1]) > 0) {
                    throw new NullPointerException("Array Sorting : Output array not sorted");
                }
            }
        }
        
    }
    private void ArraySortMineQuickSort(Key[] inputArr, int inputArrSize) {
        // Randomize the input array (O(N))
        int i;
        for (i=0; i<inputArrSize; i++) {
            int idxToExchange = 0;
            if (i != 0)
                idxToExchange = StdRandom.uniform(0, i);
            exch(inputArr, i, idxToExchange);
        }
        
        // Do the sort
        int hi = inputArrSize-1;
        int lo = 0;
        quickSort(inputArr, lo, hi);
    }
    
    private void ArrarySortMineInsertionSort(Key[] inputArr, int inputArrSize) {
        insertionSort(inputArr, 0, inputArrSize-1);
    }
    
    private void mergeSort(Key[] arr, int lo, int splitIdx, int hi) 
    {
        Key[] auxArray = (Key[]) new Object[hi-lo+1];
        
        int i,j,k;
        
        for (i=0; i<hi-lo+1; i++)
            auxArray[i] = arr[lo+i];

        for (i=0, j=splitIdx, k=lo; ; ) {
            if (i >= splitIdx) {
                while (j < hi-lo+1)
                    arr[k++] = auxArray[j++];
                break;
            } else if (j >= hi-lo+1) {
                while (i < splitIdx)
                    arr[k++] = auxArray[i++];
                break;
            }
            if (comparator == null) {
                if (((Comparable<Key>)auxArray[i]).compareTo(auxArray[j]) <= 0) 
                    arr[k++] = auxArray[i++];
                else 
                    arr[k++] = auxArray[j++];
            } else {
                if (comparator.compare(auxArray[i],auxArray[j]) <= 0) 
                    arr[k++] = auxArray[i++];
                else 
                    arr[k++] = auxArray[j++];
            }
            
        }
        auxArray = null;
    }
    
    private void ArrarySortMineMergeSort(Key[] inputArr, int inputArrSize)
    {
        int splitArrSize;
        
        for (splitArrSize=2; ; ) {
            for (int i=0; i<inputArrSize; i+=splitArrSize) {
                if (i+splitArrSize <= inputArrSize)
                    mergeSort(inputArr, i, splitArrSize/2, i+splitArrSize-1);
                else if ((i+splitArrSize/2) <= inputArrSize)
                    mergeSort(inputArr, i, splitArrSize/2, inputArrSize-1);
            }
            
            if (splitArrSize > inputArrSize)
                break;
            else 
                splitArrSize=splitArrSize*2;
        }
    }
    
    private void sink(Key[] arr, int arrIdx, int arrSize)
    {
        int start = arrIdx;
        
        while (start < arrSize) {
            int idx1 = 2*start;
            if (idx1 > arrSize) break;
            
            if (comparator == null) {
                if ((idx1+1 <= arrSize)
                    && ((Comparable<Key>)arr[idx1]).compareTo(arr[idx1-1]) > 0) idx1++;
                if (((Comparable<Key>)arr[idx1-1]).compareTo(arr[start-1]) > 0) exch(arr, idx1-1, start-1);
            } else {
                if ((idx1+1 <= arrSize)
                    && (comparator.compare(arr[idx1], arr[idx1-1]) > 0)) idx1++;
                if (comparator.compare(arr[idx1-1], arr[start-1]) > 0) exch(arr, idx1-1, start-1);
            }
            
            start = idx1;
        }
    }
    
    
    private void heapSortCreateHeap(Key[] inputArr, int inputArrSize) {
        int i;
        int startIdx = inputArrSize/2;
        
        for (i=startIdx; i>0; i--) {
            sink(inputArr, i, inputArrSize);
        }
    }
    
    private void heapSort(Key[] inputArr, int inputArrSize)
    {
        int numUnSorted = inputArrSize;
        
        while (numUnSorted > 0) {
            exch(inputArr, numUnSorted-1, 0);
            sink(inputArr, 1, --numUnSorted);
        }
    }
    private void ArraySortMineHeapSort(Key[] inputArr, int inputArrSize) 
    {
        heapSortCreateHeap(inputArr, inputArrSize);
        heapSort(inputArr, inputArrSize);
    }
    
    public ArraySortMine(Key[] inputArr, int inputArrSize) {
        comparator = null;
        String sortType = null;
        if (inputArrSize < 10)
            sortType = "INSERTION_SORT";
        else 
            sortType = "QUICK_SORT";
        
        if (sortType.equals("INSERTION_SORT"))
            ArrarySortMineInsertionSort(inputArr, inputArrSize);
        else if (sortType.equals("MERGE_SORT"))
            ArrarySortMineMergeSort(inputArr, inputArrSize);
        else if (sortType.equals("QUICK_SORT"))
            ArraySortMineQuickSort(inputArr, inputArrSize);
        else if (sortType.equals("HEAP_SORT"))
            ArraySortMineHeapSort(inputArr, inputArrSize);
        
        validateSorting(inputArr, inputArrSize);
    }
    
    public ArraySortMine(Key[] inputArr, int inputArrSize, Comparator<Key> comparatorFunc) {
        comparator = null;
        String sortType = null;
        if (comparatorFunc != null)
            comparator = comparatorFunc;
        
        if (inputArrSize < 10)
            sortType = "INSERTION_SORT";
        else 
            sortType = "QUICK_SORT";
        
        if (sortType.equals("INSERTION_SORT"))
            ArrarySortMineInsertionSort(inputArr, inputArrSize);
        else if (sortType.equals("MERGE_SORT"))
            ArrarySortMineMergeSort(inputArr, inputArrSize);
        else if (sortType.equals("QUICK_SORT"))
            ArraySortMineQuickSort(inputArr, inputArrSize);
        else if (sortType.equals("HEAP_SORT"))
            ArraySortMineHeapSort(inputArr, inputArrSize);
        
        validateSorting(inputArr, inputArrSize);
    }
    
    public static void main(String[] args) {
        int lengthOfArray = 12;
        Integer[] intArr = new Integer[lengthOfArray];
        int i, j;
        for (i=0,j=lengthOfArray; i<lengthOfArray; i++,j--)
            intArr[i] = j;
        
        ArraySortMine<Integer> arrSortObj = new ArraySortMine<Integer>(intArr, lengthOfArray);
        
        for (i=0; i<lengthOfArray; i++)
            StdOut.println(intArr[i]);
    }
}
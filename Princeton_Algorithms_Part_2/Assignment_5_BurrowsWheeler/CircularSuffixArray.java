import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Merge;

public class CircularSuffixArray 
{
    public static String origStringObj;  
    private class SuffixStartCharNode implements Comparable<SuffixStartCharNode>
    {
        public int    charIdx;
        
        public SuffixStartCharNode(int idx)
        {
            charIdx = idx;
        }
        
        
        public int compareTo(SuffixStartCharNode that)
        {
            int node1Idx = this.charIdx;
            int node2Idx = that.charIdx;
            int strLength = origStringObj.length();
            
            int numCharsCompared = 0;
            while (origStringObj.charAt(node1Idx) == origStringObj.charAt(node2Idx))
            {
                node1Idx = (node1Idx+1)%strLength;
                node2Idx = (node2Idx+1)%strLength;
                
                numCharsCompared++;
                if (numCharsCompared == strLength)
                  return 0;
            }
            
            if (origStringObj.charAt(node1Idx) > origStringObj.charAt(node2Idx))
              return 1;
            else
              return -1;
        }
    }
    
    private SuffixStartCharNode[] suffixStartCharNodeArr;
    private int stringLength;
    
    // circular suffix array of s
    public CircularSuffixArray(String s)
    {
        stringLength = s.length();      
        suffixStartCharNodeArr = new SuffixStartCharNode[stringLength];
        origStringObj = s;
        
        int idx = 0;
        while (idx < stringLength)
        {
            suffixStartCharNodeArr[idx] = new SuffixStartCharNode(idx);
            idx++;
        }
        Merge.sort(suffixStartCharNodeArr);
        
    }
   
    // length of s
    public int length()
    {
        return stringLength;
    }
   
    // returns index of ith sorted suffix
    public int index(int i)
    {
        return suffixStartCharNodeArr[i].charIdx;  
    }
   
    // unit testing (required)
    public static void main(String[] args)
    {
        In inObj = new In(args[0]);
        String str = inObj.readAll();
      
        CircularSuffixArray circularSuffixArrayObj = new CircularSuffixArray(str);
        
        int idx = 0;
        while (idx < circularSuffixArrayObj.length())
        {
            StdOut.println(circularSuffixArrayObj.index(idx));
            idx++;
        }      
    }
}
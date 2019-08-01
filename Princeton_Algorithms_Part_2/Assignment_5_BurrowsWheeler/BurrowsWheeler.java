import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.Merge;

public class BurrowsWheeler 
{   
    public BurrowsWheeler()
    { 
    }
    
    // apply Burrows-Wheeler transform, reading from standard input and writing to standard output
    public static void transform()
    {
        String origString = BinaryStdIn.readString();
        CircularSuffixArray circularSuffixArrayObj = new CircularSuffixArray(origString);  
        
        int sortedArIdx = 0;
        int first = 0;
        
        while (sortedArIdx < circularSuffixArrayObj.length())
        {
            if (circularSuffixArrayObj.index(sortedArIdx) == 0)
            {
                first = sortedArIdx;
                break;
            }
            sortedArIdx++;
        }

        BinaryStdOut.write((((byte)((first&0xff000000) >> 24))));
        BinaryStdOut.write(((byte)((first&0xff0000) >> 16)));
        BinaryStdOut.write(((byte)((first&0xff00) >> 8)));
        BinaryStdOut.write(((byte)(first&0xff)));

        sortedArIdx = 0;
        while (sortedArIdx < circularSuffixArrayObj.length())
        {
            int strIndex = (circularSuffixArrayObj.index(sortedArIdx) + (circularSuffixArrayObj.length()-1))%circularSuffixArrayObj.length();
            BinaryStdOut.write(origString.charAt(strIndex));
            sortedArIdx++;
        }
        BinaryStdOut.flush();
        BinaryStdOut.close();
    }
    
    // apply Burrows-Wheeler inverse transform, reading from standard input and writing to standard output
    public static void inverseTransform()
    {
        String origString = BinaryStdIn.readString();
        int strLength = origString.length();        
        int first = (origString.charAt(3)) + (origString.charAt(2)<<8) + (origString.charAt(1)<<16) + (origString.charAt(0)<<24);
        
        int[] sortedIdxToStrIdx = new int[strLength-4];
        int[] countAr = new int[257];
        int idx=0;
        
        for (idx=0; idx<=256; idx++)
            countAr[idx] = 0;
        
        for (idx=0; idx<(strLength-4); idx++)
            countAr[(int)(origString.charAt(idx+4))+1]++;
        
        for (idx=1; idx<256; idx++)
            countAr[idx] += countAr[idx-1];

        for (idx=0; idx<(strLength-4); idx++)
        {
            sortedIdxToStrIdx[countAr[(int)(origString.charAt(idx+4))]] = idx+4;
            countAr[(int)(origString.charAt(idx+4))]++;            
        }
        
        // StringBuilder inverseTransformString = new StringBuilder();
        idx = 0;
        first = first+4;
        while (idx < strLength-4)
        {
            BinaryStdOut.write(origString.charAt(sortedIdxToStrIdx[first-4]));
            // inverseTransformString.append(origString.charAt(first));
            first = sortedIdxToStrIdx[first-4];
            idx++;
        }
        BinaryStdOut.flush();
        BinaryStdOut.close();
    }

    // if args[0] is '-', apply Burrows-Wheeler transform
    // if args[0] is '+', apply Burrows-Wheeler inverse transform
    public static void main(String[] args)
    {
        BurrowsWheeler burrowsWheelerObj = new BurrowsWheeler();
        if (args[0].compareTo("-") == 0) 
        {
            BurrowsWheeler.transform();
        }
        else if (args[0].compareTo("+") == 0)
            BurrowsWheeler.inverseTransform();        
    }
}
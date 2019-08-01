import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdRandom;

public class Subset {
    public static void main(String[] args) {
        int numStringsToPrint  = Integer.parseInt(args[0]);
        RandomizedQueue<String> rqObj = new RandomizedQueue<String>();
        
        int numStringsRead = 0;
        String stringRead;
        while (!StdIn.isEmpty()) {
            stringRead = StdIn.readString();
            numStringsRead++;
            if (numStringsRead <= numStringsToPrint) {
                rqObj.enqueue(stringRead);
            } else if (numStringsToPrint > 0){
                int randomOutput = StdRandom.uniform(0,numStringsRead);
                if (randomOutput < numStringsToPrint) {
                    rqObj.dequeue();
                    rqObj.enqueue(stringRead);
                }
            }
        }
        
        for (String stringToPrint : rqObj) 
            StdOut.println(stringToPrint);
        
    }
}
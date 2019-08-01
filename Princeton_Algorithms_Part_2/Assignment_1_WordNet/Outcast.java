import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;

public class Outcast {
    /*
    private class NounPairKey implements Comparable<NounPairKey> {
        public String nounA;
        public String nounB;
        
        public NounPairKey(String nounA, String nounB) {
            this.nounA = nounA;
            this.nounB = nounB;
        }
        
        public int compareTo(NounPairKey that) {
            if ((this.nounA.compareTo(that.nounB) == 0) && (this.nounB.compareTo(that.nounA) == 0))
                return 0;
            
            if (this.nounA.compareTo(that.nounA) > 0)
                return 1;
            else if (this.nounA.compareTo(that.nounA) < 0)
                return -1;
            else {
                if (this.nounB.compareTo(that.nounB) > 0)
                    return 1;
                else if (this.nounB.compareTo(that.nounB) < 0)
                    return -1;
            }
            return 0;
        }
    }
    
    private ST<NounPairKey, Integer>  nounPairDistanceST;
    private String[] nounArr;
    private int nounArrMaxSize;
    private int nounArrCurrSize;
    
    private void NounArrResize() {
        String[] newArr = new String[nounArrMaxSize*2];
        
        int i = 0;
        while (i < nounArrCurrSize) {
            newArr[i] = nounArr[i];
            i++;
        }
        nounArr = newArr;
        nounArrMaxSize = nounArrMaxSize*2;
    }
    
    private void NounArrAdd(String noun) {
        if (nounArrCurrSize >= nounArrMaxSize)
            NounArrResize();
        
        nounArr[nounArrCurrSize++] = noun;
    }
    */
    private WordNet wordNetObj;
    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        /*
        nounPairDistanceST = new ST<NounPairKey, Integer>();
        nounArrCurrSize = 0;
        nounArrMaxSize = 1;
        nounArr = new String[nounArrMaxSize];
        
        for (String wordNetNoun : wordnet.nouns()) 
            NounArrAdd(wordNetNoun);
        
        int i,j;
        
        StdOut.println(nounArrCurrSize);
        for (i=0; i<nounArrCurrSize; i++) {
            for (j=i+1; j<nounArrCurrSize; j++) {
                NounPairKey keyObj = new NounPairKey(nounArr[i], nounArr[j]);
                int distance = wordnet.distance(nounArr[i], nounArr[j]);
                nounPairDistanceST.put(keyObj, distance);
            }
        }
        */
        wordNetObj = wordnet;
    }
    
    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        int maxDistance = -1;
        String outcastString = null;
        int i,j;
        
        // NounPairKey keyObj = new NounPairKey(nouns[0], nouns[1]);
        for (i=0; i<nouns.length; i++) {
            int distance = 0;
            for (j=0; j<nouns.length; j++) {
                if (nouns[j].compareTo(nouns[i]) == 0) continue;
                // keyObj.nounA = nouns[i];
                // keyObj.nounB = nouns[j];
                //distance += nounPairDistanceST.get(keyObj);
                distance += wordNetObj.distance(nouns[i], nouns[j]);
            }
            
            if ((maxDistance == -1) || (maxDistance < distance)) {
                maxDistance = distance;
                outcastString = nouns[i];
            }
        }
        
        return outcastString;
    }
    
    // see test client below
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
        
    }
}
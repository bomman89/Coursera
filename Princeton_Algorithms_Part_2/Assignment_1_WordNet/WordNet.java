import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.ResizingArrayQueue;

public class WordNet {

    private class WordNetVertex {
        int id;
        String synset;
        String description;
        
        public WordNetVertex(int id, String synset, String description) {
            this.id = id;
            this.synset = synset;
            this.description = description;
        }
    }
    
    private class WordNetNounTreeValue {
        public ResizingArrayQueue<Integer> synsetIdArr;
        
        public WordNetNounTreeValue(String noun, int id) {
            
            synsetIdArr = new ResizingArrayQueue<Integer>();
            synsetIdArr.enqueue(id);
        }
       
        public void addId(int id) {
            synsetIdArr.enqueue(id);
        }
    }
    private ST<String, WordNetNounTreeValue> wordNetNounST;
    private Digraph wordNetDiGraphObj;
    private WordNetVertex[] wordNetVerticesArr;
    private int wordNetVerticesArrSize;
    private int wordNetVerticesCurrSize;
    
    private void WordNetResizeVerticesArray() {
        WordNetVertex[] newWordNetVerticesArr = new WordNetVertex[wordNetVerticesArrSize*2];
        
        int j = 0;
        while (j < wordNetVerticesArrSize) {
            newWordNetVerticesArr[j] =  wordNetVerticesArr[j];
            j++;
        }
        wordNetVerticesArrSize *= 2;
        wordNetVerticesArr = newWordNetVerticesArr;
    }
    
    private void AddSynset(int id, String synset, String description) {
        if (wordNetVerticesCurrSize >= wordNetVerticesArrSize)
            WordNetResizeVerticesArray();
        
        wordNetVerticesArr[wordNetVerticesCurrSize++] = new WordNetVertex(id, synset, description);
    }
    
    private SAP sapObj;
    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        wordNetVerticesCurrSize = 0;
        wordNetVerticesArrSize  = 1;
        wordNetVerticesArr = new WordNetVertex[1];
        
        In synsetFile    = new In(synsets);
        In hypernymsFile = new In(hypernyms);
        
        wordNetNounST = new ST<String, WordNetNounTreeValue>();
        String line;
        while ((line = synsetFile.readLine()) != null) {
            String[] lineParse;
            String[] nounParse;
            lineParse = line.split(",");
            int id = Integer.parseInt(lineParse[0]);
            AddSynset(id, lineParse[1], lineParse[2]);
            nounParse = lineParse[1].split(" ");
            int j = 0;
            while (j < nounParse.length) {
                if (!wordNetNounST.contains(nounParse[j])) {
                    WordNetNounTreeValue treeValueObj = new WordNetNounTreeValue(nounParse[j], id);
                    wordNetNounST.put(nounParse[j], treeValueObj);
                } else {
                    WordNetNounTreeValue treeValueObj = wordNetNounST.get(nounParse[j]);
                    treeValueObj.addId(id);
                }
                j++;
            }   
        }
        
        wordNetDiGraphObj = new Digraph(wordNetVerticesCurrSize);
        while ((line = hypernymsFile.readLine()) != null) {
            
            String[] lineParse;
            lineParse = line.split(",");
            int synsetId = Integer.parseInt(lineParse[0]);

            int j = 1;
            while (j < lineParse.length) {
                int hypernymId = Integer.parseInt(lineParse[j]);
                wordNetDiGraphObj.addEdge(synsetId, hypernymId);
                j++;
            }   
        }
        sapObj = new SAP(wordNetDiGraphObj);
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return wordNetNounST.keys();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        return wordNetNounST.contains(word);
    }
    
    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        WordNetNounTreeValue treeValueAObj = wordNetNounST.get(nounA);
        WordNetNounTreeValue treeValueBObj = wordNetNounST.get(nounB);

        Queue<Integer> queueObjA = new Queue<Integer>();
        Queue<Integer> queueObjB = new Queue<Integer>();
        for (int synsetAId : treeValueAObj.synsetIdArr) 
            queueObjA.enqueue(synsetAId);
        
        for (int synsetBId : treeValueBObj.synsetIdArr) {
            queueObjB.enqueue(synsetBId);
        }
        
        return (sapObj.length(queueObjA, queueObjB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        WordNetNounTreeValue treeValueAObj = wordNetNounST.get(nounA);
        WordNetNounTreeValue treeValueBObj = wordNetNounST.get(nounB);
        
        Queue<Integer> queueObjA = new Queue<Integer>();
        Queue<Integer> queueObjB = new Queue<Integer>();
        for (int synsetAId : treeValueAObj.synsetIdArr) 
            queueObjA.enqueue(synsetAId);
        
        for (int synsetBId : treeValueBObj.synsetIdArr) 
            queueObjB.enqueue(synsetBId);

        int sapId = sapObj.ancestor(queueObjA, queueObjB);
        if (sapId != -1)
            return wordNetVerticesArr[sapId].synset;
        
        return null;
    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet wordNetObj = new WordNet("wordnet-testing\\synsets.txt", 
                                         "wordnet-testing\\hypernyms.txt");
        StdOut.println(wordNetObj.distance("Plough", "Jean-Claude_Duvalier"));
    }
}
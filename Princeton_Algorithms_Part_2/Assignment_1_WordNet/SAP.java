import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.In;

public class SAP {

    private Digraph digraphObj;
    // constructor takes a digraph (not necessarily a DAG)
    private int[] distanceToV;
    private int[] distanceToW;
    
    public SAP(Digraph G) {
        digraphObj = new Digraph(G);
        distanceToV = new int[digraphObj.V()];
        distanceToW = new int[digraphObj.V()];
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        Queue<Integer> queueObj = new Queue<Integer>();
        boolean[] markedV        = new boolean[digraphObj.V()];
        boolean[] markedW        = new boolean[digraphObj.V()];
        
        markedV[v] = true;
        distanceToV[v] = 0;
        queueObj.enqueue(v);
        markedW[w] = true;
        distanceToW[w] = 0;
        queueObj.enqueue(w);
        
        int minDistance = -1;
        while (!queueObj.isEmpty()) {
            int id = queueObj.dequeue();
            
            if ((markedV[id]) && (markedW[id])) {
                int totalDistance = distanceToV[id] + distanceToW[id];
                if ((minDistance == -1) || (minDistance > totalDistance))
                    minDistance = totalDistance;
            }

            for (int adjId : digraphObj.adj(id)) {
                if (markedV[id] && !markedV[adjId]) {
                    markedV[adjId] = true;
                    distanceToV[adjId] = distanceToV[id] + 1; 
                    if ((minDistance != -1) && (distanceToV[id] > minDistance))
                        continue;
                    queueObj.enqueue(adjId);
                } else if  (markedW[id] && !markedW[adjId]) {
                    markedW[adjId] = true;
                    distanceToW[adjId] = distanceToW[id] + 1; 
                    if ((minDistance != -1) && (distanceToW[id] > minDistance))
                        continue;
                    queueObj.enqueue(adjId);
                }
            }
        }
        return minDistance;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        Queue<Integer> queueObj = new Queue<Integer>();
        boolean[] markedV        = new boolean[digraphObj.V()];
        boolean[] markedW        = new boolean[digraphObj.V()];
        
        markedV[v] = true;
        queueObj.enqueue(v);
        distanceToV[v] = 0;
        markedW[w] = true;
        distanceToW[w] = 0;
        queueObj.enqueue(w);
        
        int minDistance = -1;
        int sapId = -1;
        while (!queueObj.isEmpty()) {
            int id = queueObj.dequeue();
            
            if ((markedV[id]) && (markedW[id])) {
                int totalDistance = distanceToV[id] + distanceToW[id];
                if ((minDistance == -1) || (minDistance > totalDistance)) {
                    minDistance = totalDistance;
                    sapId = id;
                }
            }
            
            for (int adjId : digraphObj.adj(id)) {
                if (markedV[id] && !markedV[adjId]) {
                    markedV[adjId] = true;
                    distanceToV[adjId] = distanceToV[id] + 1; 
                    if ((minDistance != -1) && (distanceToV[id] > minDistance))
                        continue;
                    queueObj.enqueue(adjId);
                } else if  (markedW[id] && !markedW[adjId]) {
                    markedW[adjId] = true;
                    distanceToW[adjId] = distanceToW[id] + 1; 
                    if ((minDistance != -1) && (distanceToW[id] > minDistance))
                        continue;
                    queueObj.enqueue(adjId);
                }
            }
        }
        return sapId;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        Queue<Integer> queueObj  = new Queue<Integer>();
        boolean[] markedV        = new boolean[digraphObj.V()];
        boolean[] markedW        = new boolean[digraphObj.V()];
        
        for (int vId : v) {
            markedV[vId] = true;
            distanceToV[vId] = 0;
            queueObj.enqueue(vId);
        }
        for (int wId : w) {
            markedW[wId] = true;
            distanceToW[wId] = 0;
            queueObj.enqueue(wId);
        }
        
        int minDistance = -1;
        while (!queueObj.isEmpty()) {
            int id = queueObj.dequeue();
            
            
            if ((markedV[id]) && (markedW[id])) {
                int totalDistance = distanceToV[id] + distanceToW[id];
                if ((minDistance == -1) || (minDistance > totalDistance)) 
                    minDistance = totalDistance;
            }
            
            for (int adjId : digraphObj.adj(id)) {
                if (markedV[id] && !markedV[adjId]) {
                    markedV[adjId] = true;
                    distanceToV[adjId] = distanceToV[id] + 1; 
                    if ((minDistance != -1) && (distanceToV[id] > minDistance))
                        continue;
                    queueObj.enqueue(adjId);
                } else if  (markedW[id] && !markedW[adjId]) {
                    markedW[adjId] = true;
                    distanceToW[adjId] = distanceToW[id] + 1; 
                    if ((minDistance != -1) && (distanceToW[id] > minDistance))
                        continue;
                    queueObj.enqueue(adjId);
                }
            }
        }
        return minDistance;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        Queue<Integer> queueObj  = new Queue<Integer>();
        boolean[] markedV        = new boolean[digraphObj.V()];
        boolean[] markedW        = new boolean[digraphObj.V()];
        
        for (int vId : v) {
            markedV[vId] = true;
            distanceToV[vId] = 0;
            queueObj.enqueue(vId);
        }
        for (int wId : w) {
            markedW[wId] = true;
            distanceToW[wId] = 0;
            queueObj.enqueue(wId);
        }
        
        int minDistance = -1;
        int sapId = -1;
        while (!queueObj.isEmpty()) {
            int id = queueObj.dequeue();
            
            if ((markedV[id]) && (markedW[id])) {
                int totalDistance = distanceToV[id] + distanceToW[id];
                if ((minDistance == -1) || (minDistance > totalDistance)) {
                    minDistance = totalDistance;
                    sapId = id;
                }
            }
            for (int adjId : digraphObj.adj(id)) {
                if (markedV[id] && !markedV[adjId]) {
                    markedV[adjId] = true;
                    distanceToV[adjId] = distanceToV[id] + 1; 
                    if ((minDistance != -1) && (distanceToV[id] > minDistance))
                        continue;
                    queueObj.enqueue(adjId);
                } else if  (markedW[id] && !markedW[adjId]) {
                    markedW[adjId] = true;
                    distanceToW[adjId] = distanceToW[id] + 1; 
                    if ((minDistance != -1) && (distanceToW[id] > minDistance))
                        continue;
                    queueObj.enqueue(adjId);
                }
            }
        }
        return sapId;
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
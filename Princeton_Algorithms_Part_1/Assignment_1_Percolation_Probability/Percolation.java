import edu.princeton.cs.algs4.WeightedQuickUnionUF;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class Percolation {
    private int                   gridSize;
    private boolean[]             openState;
    private boolean[]             bottomConnectionState;
    private boolean[]             topConnectionState;
    private WeightedQuickUnionUF  quickFindUFObj;
    private boolean               percolates;
    
    // create n-by-n grid, with all sites blocked
    public Percolation(int N)
    {
        openState             = new boolean[N*N];
        bottomConnectionState = new boolean[N*N];
        topConnectionState    = new boolean[N*N];
        quickFindUFObj        = new WeightedQuickUnionUF(N*N);
        
        int i;
        gridSize              = N;
        for (i=0; i<N*N; i++) 
        {   
            openState[i]                 = false; 
            bottomConnectionState[i]     = false;
            topConnectionState[i]        = false;
        }
        percolates                       = false;
    }
   
    // open site (row i, column j) if it is not open already
    public void open(int i, int j)
    {
        if (isOpen(i,j))
            return;
        
        int gridArIdx              = (i-1)*gridSize + (j-1);
        int[] gridArIdxToConnect   = new int[4];
        
        // Need to start from here : 
        gridArIdxToConnect[0]      = (i != 1)        && isOpen(i-1,j) ? ((i-2)*gridSize + (j-1)) : gridSize*gridSize+1;
        gridArIdxToConnect[1]      = (j != gridSize) && isOpen(i,j+1) ? ((i-1)*gridSize + j)     : gridSize*gridSize+1;
        gridArIdxToConnect[2]      = (i != gridSize) && isOpen(i+1,j) ? (i*gridSize + (j-1))     : gridSize*gridSize+1;
        gridArIdxToConnect[3]      = (j != 1)        && isOpen(i,j-1) ? ((i-1)*gridSize + (j-2)) : gridSize*gridSize+1;
        
        int x;
        boolean bottomConnected,topConnected;
        bottomConnected = false;
        topConnected    = false;
        
        if ((gridArIdx >= ((gridSize-1)*gridSize))
            && (gridArIdx < (gridSize*gridSize)))
            bottomConnected = true;
        
        if ((gridArIdx >= 0) && (gridArIdx < gridSize))
            topConnected = true;
        
        for (x=0; x<4; x++)
        {
            if (gridArIdxToConnect[x] == (gridSize*gridSize+1))
                continue;
            if (!bottomConnected || !topConnected)
            {
                int rootOfCurrentComponent  = quickFindUFObj.find(gridArIdxToConnect[x]);
                bottomConnected            |= bottomConnectionState[rootOfCurrentComponent];
                topConnected               |= topConnectionState[rootOfCurrentComponent];
            }
            quickFindUFObj.union(gridArIdx, gridArIdxToConnect[x]);
        }
        int rootAfterUnion = quickFindUFObj.find(gridArIdx);
        openState[gridArIdx] = true;
        bottomConnectionState[rootAfterUnion] |= bottomConnected;
        topConnectionState[rootAfterUnion]    |= topConnected;
        
        if (bottomConnectionState[rootAfterUnion] && topConnectionState[rootAfterUnion])
            percolates = true;
    }
    
    // is site (row i, column j) open?
    public boolean isOpen(int i, int j)
    {
        int arIdx = (i-1)*gridSize + (j-1);
        return openState[arIdx];        
    }
    
    // is site (row i, column j) full?
    public boolean isFull(int i, int j)
    {
        int arIdx = (i-1)*gridSize + (j-1);
        int root  = quickFindUFObj.find(arIdx);
        return topConnectionState[root];
    }
    
    // does the system percolate?
    public boolean percolates()
    {
        return percolates;
    }

    // test client (optional)
    public static void main(String[] args)  
    {
        int gridSize               = 1000;
        Percolation percolationObj = new Percolation(gridSize);
        int row, column;
        int numOpen;
        
        numOpen = 0;
        while (!percolationObj.percolates()) 
        {
            row    = StdRandom.uniform(1,gridSize+1);
            column = StdRandom.uniform(1,gridSize+1);
            
            if (!percolationObj.isOpen(row,column)) 
            {
                //StdOut.println(row);
                //StdOut.println(column);
                numOpen++;
                percolationObj.open(row,column);
            }
        }
        StdOut.println(numOpen/((double)gridSize*gridSize));
    }
}
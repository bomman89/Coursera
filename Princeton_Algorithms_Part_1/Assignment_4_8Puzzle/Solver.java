import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
import java.util.Iterator;

public class Solver {
    private int numMoves;
    private boolean solvable;
    private class BoardComparator implements Comparable<BoardComparator> {
        public Board boardObj;
        public BoardComparator prevBoardCompObj;
        public int numMovesToThisBoard; 
        public int distanceToGoal;
        public int hammingDistanceToGoal;
        public int priority;
        public Board[] boardNeighbors;
        public int numBoardNeighbors;
        public boolean isTwin;
        
        
        public BoardComparator(BoardComparator prevCompObj, Board initial, int numMoves, boolean fromTwinBoard) {
            if (initial == null)
                throw new NullPointerException("NULL pointer board passed\n");
            boardObj = initial;
            prevBoardCompObj = prevCompObj;
            numMovesToThisBoard = numMoves;
            distanceToGoal = boardObj.manhattan();
            hammingDistanceToGoal = boardObj.hamming();
            
            priority = distanceToGoal + numMovesToThisBoard;
            numBoardNeighbors = 0;
            isTwin = fromTwinBoard;
        }
        
        public void calculateNeighbors() {
            if (numBoardNeighbors == 0) {
                boardNeighbors = new Board[4];
                
                for (Board neighbor : boardObj.neighbors()) {
                    boardNeighbors[numBoardNeighbors++] = neighbor;
                }
            }
        }
        
        public int compareTo(BoardComparator that) {
            if (this.priority > that.priority) 
                return 1;
            else if (this.priority < that.priority)
                return -1;
            else {
                if (this.distanceToGoal > that.distanceToGoal)
                    return 1;
                else if (this.distanceToGoal < that.distanceToGoal)
                    return -1;
                else {
                    if (this.hammingDistanceToGoal > that.hammingDistanceToGoal)
                        return 1;
                    else if (this.hammingDistanceToGoal < that.hammingDistanceToGoal)
                        return -1;
                    return 0;
                }
            }
        }
    }
    private MinPQMine<BoardComparator> minPQObj;
    
    private class SolutionBoardList implements Iterable<Board> {
        private BoardComparator[] boardRouteToSolution;
        private int currentArraySize; 
        private int numElements;
        private boolean directionOfIteration;
        
        public SolutionBoardList(boolean direction) {
            boardRouteToSolution = new BoardComparator[1];
            currentArraySize     = 1;
            numElements          = 0;
            directionOfIteration = direction;
        }
        
        private void ResizeArray() {
            BoardComparator[] newBoardArray = new BoardComparator[currentArraySize*2];
            
            int i;
            for (i=0; i<numElements; i++) {
                newBoardArray[i] = boardRouteToSolution[i];
            }
            boardRouteToSolution = newBoardArray;
            currentArraySize = currentArraySize*2;
        }
        
        public void Insert(BoardComparator boardComparatorObjToInsert) {
            if (numElements >= currentArraySize)
                ResizeArray();
            
            boardRouteToSolution[numElements++] = boardComparatorObjToInsert;
        }
        
        private class SolutionBoardListIterator implements Iterator<Board> {
            private int numElementsIterated;
            
            public SolutionBoardListIterator() {
                if (!directionOfIteration)
                    numElementsIterated = 0;
                else 
                    numElementsIterated = numElements;
            }
            
            public Board next() {
                if (!directionOfIteration)
                    return boardRouteToSolution[numElementsIterated++].boardObj;
                else 
                    return boardRouteToSolution[--numElementsIterated].boardObj;
            }
            
            public boolean hasNext() {
                if (!directionOfIteration)
                    return (numElementsIterated < numElements);
                else 
                    return (numElementsIterated > 0);
            }
            
            public void remove() {
            }
        }
        
        public Iterator<Board> iterator() {
            return new SolutionBoardListIterator();
        }
    }
    private BoardComparator solutionBoardCompObj;
    
    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        solvable = true;
        minPQObj = new MinPQMine<BoardComparator>();
        BoardComparator boardComparatorObj = new BoardComparator(null, initial, 0, false);
        BoardComparator twinBoardComparatorObj = new BoardComparator(null, initial.twin(), 0, true);
        
        //StdOut.println(initial.twin().toString());
        minPQObj.insert(twinBoardComparatorObj);
        minPQObj.insert(boardComparatorObj);
        
        BoardComparator currBoardCompObj = null;
        Board currBoardObj = null;
        Board prevBoardObj = null;
        Board prevTwinBoardObj = null;
        int numBoardNeighbors;
        
        int twinEvaluate = 0;
        int initialEvaluate = 0;
        int insertEvaluate = 0;
        int insertTwinEvaluate = 0;
        BoardComparator nextBoardCompObj = null;
        int continueOnMismatch = 0;
        
        nextBoardCompObj = null;
        while(true) {
            if (nextBoardCompObj != null) {
                BoardComparator currMinBoardCompObj = minPQObj.isEmpty() ? null : minPQObj.min();

                if (currMinBoardCompObj.priority < nextBoardCompObj.priority) {
                    
                    if (currMinBoardCompObj.isTwin)
                        insertTwinEvaluate++;
                    else 
                        insertEvaluate++;
                    
                    minPQObj.insert(nextBoardCompObj);
                    nextBoardCompObj = null;
                }

            }
            
            currBoardCompObj = (nextBoardCompObj != null) ? nextBoardCompObj : minPQObj.delMin();
            currBoardObj     = currBoardCompObj.boardObj;
            boolean isTwin   = currBoardCompObj.isTwin;
            
            if (currBoardObj.isGoal()) {
                if (isTwin)
                    solvable = false;
                break;
            }
            
            if (isTwin)
                twinEvaluate++;
            else
                initialEvaluate++;
            
            currBoardCompObj.calculateNeighbors();
            numBoardNeighbors = currBoardCompObj.numBoardNeighbors; 
            int numMovesToBoard = currBoardCompObj.numMovesToThisBoard + 1;
            
            nextBoardCompObj = null;
            while (numBoardNeighbors > 0) {
                numBoardNeighbors--;
                Board boardNeighborObj = currBoardCompObj.boardNeighbors[numBoardNeighbors];
                                
                if ((!isTwin && boardNeighborObj.equals(prevBoardObj)) 
                    || (isTwin && boardNeighborObj.equals(prevTwinBoardObj))) 
                    continue;
                
                BoardComparator boardNeighborCompObj = new BoardComparator(currBoardCompObj, boardNeighborObj, numMovesToBoard, isTwin); 
                
                if (nextBoardCompObj == null) {
                    nextBoardCompObj = boardNeighborCompObj;
                    continue;
                } else if (boardNeighborCompObj.priority < nextBoardCompObj.priority) {
                    if (isTwin)
                        insertTwinEvaluate++;
                    else 
                        insertEvaluate++;
                    minPQObj.insert(nextBoardCompObj);  
                    nextBoardCompObj = boardNeighborCompObj;
                    continue;
                } else {
                    if (isTwin)
                        insertTwinEvaluate++;
                    else 
                        insertEvaluate++;
                    minPQObj.insert(boardNeighborCompObj); 
                }
                
            }
            
            if (!isTwin) 
                prevBoardObj = currBoardObj;
            else 
                prevTwinBoardObj = currBoardObj;
        }
        minPQObj = null;
        solutionBoardCompObj = currBoardCompObj;
        solution();
        //StdOut.println(twinEvaluate);
        //StdOut.println(initialEvaluate);
        //StdOut.println(insertEvaluate);
        //StdOut.println(insertTwinEvaluate);
        //StdOut.println(continueOnMismatch);
    }
    
    // is the initial board solvable?
    public boolean isSolvable() {
        return solvable;
    }
    
    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (solvable)
            return numMoves;
        else 
            return -1;
    }
    
    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!solvable)
            return null;
        
        int numMovesToSolution = 0;
        SolutionBoardList retSolutionBoardListObj = new SolutionBoardList(true);
        BoardComparator boardCompObjPrev = solutionBoardCompObj;
        
        retSolutionBoardListObj.Insert(boardCompObjPrev);
        boardCompObjPrev = boardCompObjPrev.prevBoardCompObj;
        while (boardCompObjPrev != null) {
            numMovesToSolution++;
            retSolutionBoardListObj.Insert(boardCompObjPrev);
            boardCompObjPrev = boardCompObjPrev.prevBoardCompObj;
        }
        numMoves = numMovesToSolution;
        return retSolutionBoardListObj;
    }
    // solve a slider puzzle (given below)
    public static void main(String[] args) {
        
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
            blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);
        
        // solve the puzzle
        Solver solver = new Solver(initial);
        
        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
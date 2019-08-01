import java.lang.UnsupportedOperationException;
import edu.princeton.cs.algs4.StdOut;
import java.util.Iterator;

public class Board {
    private final int   boardDimension;
    private final int[] boardValues;
    private Board[] neighbourBoardList;
    private int  boardNumNeighbours;
    /* construct a board from an n-by-n array of blocks
     * (where blocks[i][j] = block in row i, column j)
     */
    public Board(int[][] blocks) {
        boardDimension = blocks.length;
        boardValues = new int[boardDimension*boardDimension];
        neighbourBoardList = null;
        boardNumNeighbours = 0;
        
        int i,j;
        for (i=0; i<boardDimension; i++) {
            for (j=0; j<boardDimension; j++)
                boardValues[i*boardDimension + j] = blocks[i][j];
        }
    }
    
    // board dimension n
    public int dimension() {
        return boardDimension;
    }
    
    // number of blocks out of place
    public int hamming() {
        int i;
        int hammingDistance = 0;
        for (i=0; i<(boardDimension*boardDimension); i++) {
            if ((boardValues[i] != (i+1)) 
                && (boardValues[i] != 0))
                hammingDistance++;
        }
        return hammingDistance;
    }
    
    private int arIdxToRow(int arIdx) {
        return (arIdx/boardDimension);
    }
    
    private int arIdxToColumn(int arIdx) {
        return (arIdx%boardDimension);
    }
    
    // sum of Manhattan distances between blocks and goal
    public int manhattan() {
        int i, row, column;
        int rowDistance, columnDistance;
        int manhattanDistance = 0;
        
        for (i=0; i<(boardDimension*boardDimension); i++) {
            if (boardValues[i] == 0)
                continue;
            
            row    = arIdxToRow(i);
            column = arIdxToColumn(i);
            
            rowDistance = (row > arIdxToRow(boardValues[i]-1)) 
                          ? (row - arIdxToRow(boardValues[i]-1)) 
                            : (arIdxToRow(boardValues[i]-1) - row); 
            columnDistance = (column > arIdxToColumn(boardValues[i]-1)) 
                             ? (column - arIdxToColumn(boardValues[i]-1)) 
                               : (arIdxToColumn(boardValues[i]-1) - column); 
            manhattanDistance += rowDistance + columnDistance;
        }
        return manhattanDistance;
    }
    
    // is this board the goal board?
    public boolean isGoal() {
        int i;
        for (i=0; i<(boardDimension*boardDimension)-1; i++) {
            if (boardValues[i] != (i+1))
                return false;
        }
        if (boardValues[i] != 0)
            return false;
        
        return true;
    }
    
    private void swap(int[] ar, int idx1, int idx2) {
        int swap = ar[idx1];
        ar[idx1] = ar[idx2];
        ar[idx2] = swap;
    }
    // a board that is obtained by exchanging any pair of blocks
    public Board twin() {
        int i,j;
        int[][] twinBoardBlocks = new int[boardDimension][boardDimension];
        
        for (i=0; i<boardDimension; i++) 
            for (j=0; j<boardDimension; j++) {
                twinBoardBlocks[i][j] = boardValues[i*boardDimension + j];
            }
    
        
        if ((twinBoardBlocks[0][0] != 0) && (twinBoardBlocks[0][1] != 0)) 
            swap(twinBoardBlocks[0], 0, 1);
        else
            swap(twinBoardBlocks[1], 0, 1);
        
        
            
        Board twinBoard = new Board(twinBoardBlocks);
        return twinBoard;
    }
    
    // does this board equal y?
    public boolean equals(Object y) {
        if (this == y) return true;
        if (y == null) return false;
        if (this.getClass() != y.getClass()) return false;
        
        Board that = (Board)y;
        if (this.boardDimension == that.boardDimension) {
            int i;
            int numBoardValues = this.boardDimension * this.boardDimension;
            
            for (i=0; i<numBoardValues-1; i++)
                if (this.boardValues[i] != that.boardValues[i]) return false;
        } else
            return false;
        
        return true;
    }
    
    private void BoardFindNeighbours() {
        int numNeighbouringBoards;
        int origSpaceRowIdx = 0;
        int origSpaceColumnIdx = 0;
        int i,j;
        neighbourBoardList = new Board[4];
        int[][] boardValuesToIterate = new int[boardDimension][boardDimension];
        
        for (i=0; i<boardDimension; i++) {
            for (j=0; j<boardDimension; j++) {
                if (boardValues[i*boardDimension + j] == 0) {
                    origSpaceRowIdx = i;
                    origSpaceColumnIdx = j;
                }
                boardValuesToIterate[i][j] = boardValues[i*boardDimension+j];
            }
        }
        numNeighbouringBoards = 0;
        if (origSpaceColumnIdx-1 >= 0) {
            swap(boardValuesToIterate[origSpaceRowIdx], origSpaceColumnIdx-1, origSpaceColumnIdx);
            neighbourBoardList[numNeighbouringBoards++] = new Board(boardValuesToIterate);
            int prevColumnArIdx = origSpaceRowIdx*boardDimension + (origSpaceColumnIdx-1);
            boardValuesToIterate[origSpaceRowIdx][origSpaceColumnIdx-1] 
                = boardValues[prevColumnArIdx];
            boardValuesToIterate[origSpaceRowIdx][origSpaceColumnIdx] = 0;
        }
        if (origSpaceRowIdx-1 >= 0) {
            int swap = boardValuesToIterate[origSpaceRowIdx-1][origSpaceColumnIdx];
            boardValuesToIterate[origSpaceRowIdx-1][origSpaceColumnIdx] = 0;
            boardValuesToIterate[origSpaceRowIdx][origSpaceColumnIdx] = swap;
            neighbourBoardList[numNeighbouringBoards++] = new Board(boardValuesToIterate);
            int prevRowArIdx = (origSpaceRowIdx-1)*boardDimension + origSpaceColumnIdx;
            boardValuesToIterate[origSpaceRowIdx-1][origSpaceColumnIdx] 
                = boardValues[prevRowArIdx];
            boardValuesToIterate[origSpaceRowIdx][origSpaceColumnIdx] = 0;
        }
        if (origSpaceColumnIdx+1 < boardDimension) {
            swap(boardValuesToIterate[origSpaceRowIdx], origSpaceColumnIdx, origSpaceColumnIdx+1);   
            neighbourBoardList[numNeighbouringBoards++] = new Board(boardValuesToIterate);
            int nextColumnArIdx = origSpaceRowIdx*boardDimension + (origSpaceColumnIdx+1);
            boardValuesToIterate[origSpaceRowIdx][origSpaceColumnIdx+1] 
                = boardValues[nextColumnArIdx];
            boardValuesToIterate[origSpaceRowIdx][origSpaceColumnIdx] = 0;
        }
        if (origSpaceRowIdx+1 < boardDimension) {
            int swap = boardValuesToIterate[origSpaceRowIdx+1][origSpaceColumnIdx];
            boardValuesToIterate[origSpaceRowIdx+1][origSpaceColumnIdx] = 0;
            boardValuesToIterate[origSpaceRowIdx][origSpaceColumnIdx] = swap;
            neighbourBoardList[numNeighbouringBoards++] = new Board(boardValuesToIterate);
        }
        boardNumNeighbours = numNeighbouringBoards;
    }
    
    private class NeighbourBoardQueue implements Iterable<Board> {
        public NeighbourBoardQueue() {
        }
        
        private class NeighbouringQueueIterator implements Iterator<Board> {
            int numValidNeighboursRemaining;
            
            public NeighbouringQueueIterator() {
                numValidNeighboursRemaining = boardNumNeighbours;
            }
            
            public boolean hasNext() {
                return (numValidNeighboursRemaining > 0);
            }
            
            public Board next() {
                return neighbourBoardList[--numValidNeighboursRemaining];
            }
            
            public void remove() {
                throw new UnsupportedOperationException("Iterator remove not supported\n");
            }
        }
        
        public Iterator<Board> iterator() {
            return new NeighbouringQueueIterator();
        }
    }
    
    // all neighboring boards
    public Iterable<Board> neighbors() {
        BoardFindNeighbours();
        return new NeighbourBoardQueue();
    }
    
    // string representation of this board (in the output format specified below)
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(boardDimension + "\n");
        for (int i = 0; i < boardDimension*boardDimension; i++) {
            if ((i != 0) && (i%boardDimension == 0))
                s.append("\n");
            s.append(String.format("%2d ", boardValues[i]));
        }
        return s.toString();
    }

    // unit tests (not graded)
    public static void main(String[] args) {
        int[][] boardValues = new int[3][3];
        int i,j;
        for (i=0; i<3; i++) 
            for (j=0; j<3; j++)
               boardValues[i][j] = i*3 + j + 1;
        boardValues[2][2] = 5;
        boardValues[1][1] = 0;
        Board boardObj = new Board(boardValues);
        boardValues[2][2] = 0;
        boardValues[1][1] = 5;
        Board boardObj2 = new Board(boardValues);
        StdOut.println(boardObj.toString());
        StdOut.println(boardObj.equals(boardObj2));
        
        for (Board boardNeighbour : boardObj.neighbors()) {
            StdOut.println(boardNeighbour.hamming());
            StdOut.println(boardNeighbour.toString());
        }
    }
}
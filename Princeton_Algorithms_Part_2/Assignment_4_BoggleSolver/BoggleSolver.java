import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.ST;
import java.util.Iterator;

public class BoggleSolver
{
    private TST<Integer> trieDictionarySTObj;
    private final int[] scoreTable = {0,0,0,1,1,2,3,5,11};
    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary)
    {
        trieDictionarySTObj = new TST<Integer>();
        int numWords = dictionary.length;
        int wordIndex = 0;
        while (wordIndex < numWords)
        {
            trieDictionarySTObj.put(dictionary[wordIndex], wordIndex);
            wordIndex++;
        }
    }

    private void findValidWords(BoggleBoard board, int row, int column, char[] currWord, int currWordLength, ST<String,Integer> st)
    {   
        if ((row < 0) || (row >= boardRows))
          return;
        
        if ((column < 0) || (column >= boardCols))
          return;
        
        if (boardMarker[row*boardCols + column] == true)
          return;
  
        if (currWordLength > 1)
        {
            if (!trieDictionarySTObj.isPrefixValid(currWord, currWordLength))
                return;
        }
        
        int board1DIdx = row*boardCols + column;
        boardMarker[board1DIdx] = true;
        currWord[currWordLength++] = boardLetters[board1DIdx];          
        if (boardLetters[board1DIdx] == 'Q')
          currWord[currWordLength++] = 'U';
        findValidWords(board, row-1, column-1, currWord, currWordLength, st);
        findValidWords(board, row-1, column, currWord, currWordLength, st);
        findValidWords(board, row-1, column+1, currWord, currWordLength, st);
        findValidWords(board, row, column+1, currWord, currWordLength, st);
        findValidWords(board, row+1, column+1, currWord, currWordLength, st);
        findValidWords(board, row+1, column, currWord, currWordLength, st);
        findValidWords(board, row+1, column-1, currWord, currWordLength, st);
        findValidWords(board, row, column-1, currWord, currWordLength, st);
        boardMarker[board1DIdx] = false;         


        if (trieDictionarySTObj.contains(currWord, currWordLength))
        {
            if (currWordLength > 2) 
            {
                String currWordString = new String(currWord, 0, currWordLength); 
                st.put(currWordString, 0);  
            }
        }
        
        // StdOut.println(currWord.toString());
        currWordLength--;
        if (boardLetters[board1DIdx] == 'Q')
           currWordLength--;
        
        // StdOut.println(currWord.toString());
        
    }
    
    private boolean[] boardMarker;
    private char[] boardLetters;
    private int boardRows, boardCols;
    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board)
    {
        boardRows = board.rows();
        boardCols = board.cols();
        
        int i, row, column;
        ST<String,Integer> validWordsTree = new ST<String,Integer>();
        boardMarker = new boolean[boardRows*boardCols];
        boardLetters = new char[boardRows*boardCols];
        for (i=0; i<boardRows*boardCols; i++)
                boardMarker[i] = false; 
        
        for (row=0; row<boardRows; row++)
            for (column=0; column<boardCols; column++)
                boardLetters[row*boardCols+column] = board.getLetter(row, column);

        for (row=0; row<boardRows; row++)
        {
            for (column=0; column<boardCols; column++)
            {
                char[] possibleWord = new char[100];
                findValidWords(board, row, column, possibleWord, 0, validWordsTree);
            }
        }
        return validWordsTree.keys();
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word)
    {
        if (!trieDictionarySTObj.contains(word))
          return 0;
        
        int wordLength = word.length();
        if (wordLength > 8)
          wordLength = 8;
        return (scoreTable[wordLength]);
    }
    
    public static void main(String[] args) {
      In in = new In(args[0]);
      String[] dictionary = in.readAllStrings();
      BoggleSolver solver = new BoggleSolver(dictionary);
      BoggleBoard board = new BoggleBoard(args[1]);
      int score = 0;
      for (String word : solver.getAllValidWords(board)) {
        StdOut.println(word);
        score += solver.scoreOf(word);
      }
      StdOut.println("Score = " + score);
    }
    
}
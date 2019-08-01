import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

   private double[] percolationThreshold;
   private int      numSamples;
   
   // perform trials independent experiments on an n-by-n grid
   public PercolationStats(int n, int trials)   
   {
       percolationThreshold = new double[trials];
       numSamples           = trials;
       
       int trialCount;
       int gridSize = n;
       for (trialCount=0; trialCount<numSamples; trialCount++)
       {
           int row, column;
           int numOpen;
           
           numOpen = 0;
           Percolation percolationObj = new Percolation(n);
           while (!percolationObj.percolates()) 
           {
               row    = StdRandom.uniform(1,gridSize+1);
               column = StdRandom.uniform(1,gridSize+1);
               
               if (!percolationObj.isOpen(row,column)) 
               {
                   numOpen++;
                   percolationObj.open(row,column);
               }
           }
           percolationThreshold[trialCount] = (double)numOpen/(gridSize*gridSize);
       }
   }
   
   // sample mean of percolation threshold
   public double mean()
   {
       return StdStats.mean(percolationThreshold);
   }
   
   // sample standard deviation of percolation threshold
   public double stddev()  
   {
       return StdStats.stddev(percolationThreshold); 
   }
   
   // low  endpoint of 95% confidence interval
   public double confidenceLo()
   {
       return (mean() - 1.96*stddev()/Math.sqrt(numSamples));
   }
   
   // high endpoint of 95% confidence interval
   public double confidenceHi() 
   {
       return (mean() + 1.96*stddev()/Math.sqrt(numSamples));
   }

   // test client (described below)
   public static void main(String[] args)
   {
       int gridSize   = Integer.parseInt(args[0]);
       int maxTrials  = Integer.parseInt(args[1]);
       
       PercolationStats percolationStatsObj = new PercolationStats(gridSize, maxTrials);
       
       StdOut.printf("Mean %f\n",percolationStatsObj.mean());
       StdOut.printf("Std Deviation %f\n",percolationStatsObj.stddev());
       StdOut.printf("confidenceLow %f confidenceHi %f\n",
                     percolationStatsObj.confidenceLo(), percolationStatsObj.confidenceHi());
   }
}
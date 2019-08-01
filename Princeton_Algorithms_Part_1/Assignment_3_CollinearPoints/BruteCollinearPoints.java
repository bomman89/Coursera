import java.util.Arrays;

public class BruteCollinearPoints {
    private boolean bruteCollinearPointsDone = false;
    private int numLineSegments;
    private int lineSegmentArraySize;
    private LineSegment[] lineSegmentArray;
    private class LineSegmentTracker {
        public Point pa;
        public Point pb;
    }
    private LineSegmentTracker[] lineSegmentTrackerObj;    

    private void LineSegmentArrayResize() {
        LineSegmentTracker[] newTracker = new LineSegmentTracker[2*numLineSegments];
        
        int i;
        for (i=0; i<numLineSegments; i++) {
            newTracker[i] = lineSegmentTrackerObj[i];
        }
        lineSegmentTrackerObj = newTracker;
        lineSegmentArraySize = 2*numLineSegments;
    }
    
    private void LineSegmentAdd(Point a, Point b) {
       
        if (numLineSegments >= lineSegmentArraySize) {
            LineSegmentArrayResize();
        }
        
        lineSegmentTrackerObj[numLineSegments] = new LineSegmentTracker();
        lineSegmentTrackerObj[numLineSegments].pa = a;
        lineSegmentTrackerObj[numLineSegments].pb = b;
        numLineSegments++;
    }
    
    private void BruteCollinearPointsSaveDetectedSegments() {
        lineSegmentArray = new LineSegment[numLineSegments];
        int i;
        for (i=0; i<numLineSegments; i++) {
            lineSegmentArray[i] = new LineSegment(lineSegmentTrackerObj[i].pa, lineSegmentTrackerObj[i].pb);
        }
    }
    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        if (bruteCollinearPointsDone)
            return;
        if (points == null)
            throw new NullPointerException("BruteCollinearPoints : points array is null\n");
        lineSegmentArray = null;
        numLineSegments  = 0; 
        lineSegmentArraySize = 1;
        int i,j,k,l;
        int lengthOfArray = 0;
        for (Point p : points) {
            if (p == null)
                throw new NullPointerException("BruteCollinearPoints : one of the points in array is null\n");                
            lengthOfArray++;
        }
        
        Point[] pointsToSort = new Point[lengthOfArray];
        for (i=0; i<lengthOfArray; i++) 
            pointsToSort[i] = points[i];
        ArraySortMine<Point> arraySortMineObj = new ArraySortMine<Point>(pointsToSort, lengthOfArray);
        arraySortMineObj = null;
        
        //Arrays.sort(pointsToSort);
        for (i=0; i<lengthOfArray-1; i++) {
            if (pointsToSort[i].compareTo(pointsToSort[i+1]) == 0)
                throw new IllegalArgumentException("FastCollinearPoints : duplicate points in array");
        }
        
        lineSegmentTrackerObj = new LineSegmentTracker[1];
        for (i=0; i<lengthOfArray; i++) {
            for (j=i+1; j<lengthOfArray; j++) {
                for (k=j+1; k<lengthOfArray; k++) {
                    for (l=k+1; l<lengthOfArray; l++) {
                        double slope1 = pointsToSort[i].slopeTo(pointsToSort[j]);
                        double slope2 = pointsToSort[i].slopeTo(pointsToSort[k]);
                        double slope3 = pointsToSort[i].slopeTo(pointsToSort[l]);
                        if ((slope1 == slope2) && (slope1 == slope3)) {
                            LineSegmentAdd(pointsToSort[i], pointsToSort[l]);                            
                        }
                    }
                }
            }
        }
        bruteCollinearPointsDone = true;
        BruteCollinearPointsSaveDetectedSegments();
    }
    
    // the number of line segments
    public int numberOfSegments() 
    {
        return numLineSegments;
    }
    
    // the line segments
    public LineSegment[] segments()
    {
        final LineSegment[] returnArray = new LineSegment[numLineSegments];
        int i;
        for (i=0; i<numLineSegments; i++) 
            returnArray[i] = lineSegmentArray[i];
                
        return returnArray;
    }
}
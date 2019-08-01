import java.util.Arrays;
import edu.princeton.cs.algs4.StdOut;

public class FastCollinearPoints {
    private boolean fastCollinearPointsDone = false;
    private int numLineSegments;
    private int lineSegmentArraySize;
    private LineSegment[] lineSegmentArray;

    private class LineSegmentTracker implements Comparable<LineSegmentTracker> {
        public Point pa;
        public Point pb;
        
        public int compareTo(LineSegmentTracker that) {
            if (this.pb.compareTo(that.pb) > 0)
                return 1;
            else if (this.pb.compareTo(that.pb) < 0)
                return -1;
            else {
                if (this.pa.compareTo(that.pa) > 0)
                    return 1;
                else if (this.pa.compareTo(that.pa) < 0)
                    return -1;
                else 
                    return 0;
            }
        }
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
    
    private void FastCollinearPointsSaveDetectedSegments() {
        int i;
        Point prevPa = null, prevPb = null;
        int numUniqueLineSegments = 0;
        /*
         * Truncate the line segment tracker array to number of segments detected.
         * Sort them to remove the duplicate line segments.
         */
        LineSegmentTracker[] lineSegmentTrackerArray = new LineSegmentTracker[numLineSegments];
        for (i=0; i<numLineSegments; i++) {
            lineSegmentTrackerArray[i] = lineSegmentTrackerObj[i];
        }
        Arrays.sort(lineSegmentTrackerArray);
        lineSegmentArray = new LineSegment[numLineSegments];

        for (i=0; i<numLineSegments; i++) {
            if (i != 0) {
                if ((lineSegmentTrackerArray[i].pa == prevPa)
                    && (lineSegmentTrackerArray[i].pb == prevPb))
                    continue;
            }
            lineSegmentArray[numUniqueLineSegments++] = new LineSegment(lineSegmentTrackerArray[i].pa, lineSegmentTrackerArray[i].pb);
            prevPa = lineSegmentTrackerArray[i].pa;
            prevPb = lineSegmentTrackerArray[i].pb;
        }
        
        /*
         * Truncate the line segment array to the number of 
         * unique line segments detected.
         */
        LineSegment[] uniqueLineSegmentArray = new LineSegment[numUniqueLineSegments];
        for (i=0; i<numUniqueLineSegments; i++) {
            uniqueLineSegmentArray[i] = lineSegmentArray[i];
        }
        lineSegmentArray = uniqueLineSegmentArray;
        numLineSegments = numUniqueLineSegments;
    }
    // finds all line segments containing 4 points
    public FastCollinearPoints(Point[] points) {
        if (fastCollinearPointsDone)
            return;
        
        if (points == null)
            throw new NullPointerException("FastCollinearPoints : points array is null\n");
        numLineSegments  = 0;
        lineSegmentArraySize = 1;
        lineSegmentArray = null;
        int lengthOfArray = 0;
        for (Point p : points) {
            if (p == null)
                throw new NullPointerException("FastCollinearPoints : one of the points in array is null\n");                
            lengthOfArray++;
        }
        int i,j;
        
        Point[] pointsToSort = new Point[lengthOfArray];
        for (i=0; i<lengthOfArray; i++) 
            pointsToSort[i] = points[i];
        ArraySortMine<Point> arraySortMineObj = new ArraySortMine<Point>(pointsToSort, lengthOfArray);
        arraySortMineObj = null;
        // Arrays.sort(pointsToSort);
        
        for (i=0; i<lengthOfArray-1; i++) {
            if (pointsToSort[i].compareTo(pointsToSort[i+1]) == 0)
                throw new IllegalArgumentException("FastCollinearPoints : duplicate points in array");
        }
        
        
        lineSegmentTrackerObj = new LineSegmentTracker[1];
        for (i=0; i<lengthOfArray; i++) {
            arraySortMineObj = new ArraySortMine<Point>(pointsToSort, lengthOfArray, points[i].slopeOrder());
            arraySortMineObj = null;
            //Arrays.sort(pointsToSort, points[i].slopeOrder());
            /*
             * The below logic is to walk through the entire array once to 
             * figure out the contiguous segments.
             * Keep track of the segment details and when the segment breaks,
             * check if the number of collinear points is greater than 4.
             */
            double prevSlope, currSlope;
            Point maxPoint = points[i], minPoint = points[i];
            int numCollinearPoints = 0;
            prevSlope = points[i].slopeTo(pointsToSort[0]);
            
            for (j=0; j<lengthOfArray; j++) {
                currSlope = points[i].slopeTo(pointsToSort[j]);
                if (prevSlope == currSlope) {
                    numCollinearPoints++;
                    if (pointsToSort[j].compareTo(maxPoint) > 0)
                        maxPoint = pointsToSort[j];
                    else if (pointsToSort[j].compareTo(minPoint) < 0)
                        minPoint = pointsToSort[j];
                    if ((j == (lengthOfArray-1))
                        && (numCollinearPoints >= 4))
                        LineSegmentAdd(minPoint, maxPoint);
                } else {
                    if (numCollinearPoints >= 4) 
                        LineSegmentAdd(minPoint, maxPoint);
                    numCollinearPoints = 2;
                    if (points[i].compareTo(pointsToSort[j]) > 0) {
                        maxPoint = points[i];
                        minPoint = pointsToSort[j];
                    }
                    else {
                        maxPoint = pointsToSort[j];
                        minPoint = points[i];
                    }
                }
                prevSlope = currSlope;
            }
        }
        fastCollinearPointsDone = true;
        FastCollinearPointsSaveDetectedSegments();
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
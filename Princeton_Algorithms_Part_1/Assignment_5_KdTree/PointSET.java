import java.util.Iterator;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class PointSET {
    private BSTMine<Point2D> PointSETSetObj;
    private int          PointSETNumPointsInTree;            
    
    // construct an empty set of points 
    public PointSET() {
        PointSETSetObj      = new BSTMine<Point2D>();
        PointSETNumPointsInTree = 0;
    }

    // is the set empty?
    public boolean isEmpty() {
        return (PointSETNumPointsInTree == 0);
    }
    
    // number of points in the set
    public int size() {
        return PointSETNumPointsInTree;
    }
    
    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (contains(p))
            return;
        
        PointSETNumPointsInTree++;
        PointSETSetObj.add(p);
    }
    
    // does the set contain point p?
    public boolean contains(Point2D p) {
        return PointSETSetObj.contains(p);
    }
    
    // draw all points to standard draw
    public void draw() {
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        
        for (Point2D pObj : PointSETSetObj) {
            StdDraw.point(pObj.x(), pObj.y());
        }
    }
    private class PointSETRectIntersect implements Iterable<Point2D> {
        private Point2D[] rectIntersectArray;
        private int       rectIntersectNumPoints;
        private int       rectIntersectArraySize;
        
        private void RectIntersectArrayResize() { 
            Point2D[] newRectIntersectArray = new Point2D[rectIntersectArraySize*2];
            
            int i;
            for (i=0; i<rectIntersectNumPoints; i++)
                newRectIntersectArray[i] = rectIntersectArray[i];
            
            rectIntersectArray = newRectIntersectArray;
            rectIntersectArraySize = rectIntersectArraySize*2;
        }
        
        private void PointSETRectIntersectInsert(Point2D p) {
            if (rectIntersectNumPoints >= rectIntersectArraySize)
                RectIntersectArrayResize();
            
            rectIntersectArray[rectIntersectNumPoints++] = p;
        }
        
        public PointSETRectIntersect(RectHV rect) {
            rectIntersectArray = new Point2D[1];
            rectIntersectNumPoints = 0;
            rectIntersectArraySize = 1;
            
            for (Point2D pObj : PointSETSetObj) {
                if (rect.contains(pObj)) 
                    PointSETRectIntersectInsert(pObj);
            }
        }
        
        private class PointSETRectIntersectIterator implements Iterator<Point2D> {
            private int PointSETRectIntersectIteratorNumPointsToIterate;
            
            public PointSETRectIntersectIterator() {
                PointSETRectIntersectIteratorNumPointsToIterate = rectIntersectNumPoints;
            }
            
            public Point2D next() {
                return (rectIntersectArray[--PointSETRectIntersectIteratorNumPointsToIterate]);
            }
            
            public boolean hasNext() {
                return (PointSETRectIntersectIteratorNumPointsToIterate > 0);
            }
            
            public void remove() {
            }
        }
        
        public Iterator<Point2D> iterator() {
            return new PointSETRectIntersectIterator();
        }       
    }
     // all points that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect) {
        return new PointSETRectIntersect(rect);
    }
    
    // a nearest neighbor in the set to point p; null if the set is empty 
    public Point2D nearest(Point2D p) {
        Point2D minPoint = null;
        double minDistance = 0;
        
        for (Point2D pObj : PointSETSetObj){
            if (minPoint == null) {
                minPoint = pObj;
                minDistance = p.distanceSquaredTo(pObj);
                continue;
            }
            
            if (p.distanceSquaredTo(pObj) < minDistance) {
                minPoint = pObj;
                minDistance = p.distanceSquaredTo(pObj);
            }
        }
        
        return minPoint;
    }
    
    // unit testing of the methods (optional) 
    public static void main(String[] args) {
        PointSET pointSetObj = new PointSET();
        Point2D point0 = new Point2D(0.1, 0.1);
        Point2D point1 = new Point2D(0.5, 0.5);
        
        RectHV rectHvObj = new RectHV(0,0,0.2,0.3);
        pointSetObj.insert(point0);
        pointSetObj.insert(point1);
        //pointSetObj.draw();
        
        for (Point2D pObj : pointSetObj.range(rectHvObj)) {
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(0.01);
            pObj.draw();
        }
    }
}
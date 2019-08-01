import java.util.Iterator;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class KdTree {
    private static class KdTreeNode implements Comparable<Point2D> {
        public Point2D p;
        public double xValue;
        public double yValue;
        public KdTreeNode leftOrDown;
        public KdTreeNode rightOrTop;
        public RectHV     rectObj;
        public boolean xOry;
        
        public KdTreeNode(Point2D pObj, RectHV rect) {
            p = pObj;
            xValue = p.x();
            yValue = p.y();
            leftOrDown = null;
            rightOrTop = null;
            rectObj = rect;
        }
        
        public void KdTreeNodeSetSplitCriteria(boolean splitCriteria) {
            xOry = splitCriteria;
        }
        
        public boolean KdTreeNodeGetSplitCriteria() {
            return xOry;
        }
        
        public int compareTo(Point2D that) {
            if (xOry) {
                if (that.x() >= this.xValue)
                    return 1;
                else 
                    return -1;
            }
            else {
                if (that.y() >= this.yValue)
                    return 1;
                else 
                    return -1;
            }
        }
    };
    
    private KdTreeNode KdTreeRootNodeObj;
    private int        KdTreeNumPointsInTree;            
    
    // construct an empty set of points 
    public KdTree() {
        KdTreeRootNodeObj = null;
        KdTreeNumPointsInTree = 0;
    }

    // is the set empty?
    public boolean isEmpty() {
        return (KdTreeNumPointsInTree == 0);
    }
    
    // number of points in the set
    public int size() {
        return KdTreeNumPointsInTree;
    }
    
    private RectHV KdTreeNodeGetRectFormed(KdTreeNode prev, boolean leftOrDown) {
        double xmin, ymin, xmax, ymax;
        
        if (prev.xOry) {
            xmin = leftOrDown ? prev.rectObj.xmin() : prev.xValue;
            xmax = leftOrDown ? prev.xValue : prev.rectObj.xmax();
            ymin = prev.rectObj.ymin();
            ymax = prev.rectObj.ymax();
        } else {
            xmin = prev.rectObj.xmin(); 
            xmax = prev.rectObj.xmax();
            ymin = leftOrDown ? prev.rectObj.ymin() : prev.yValue;
            ymax = leftOrDown ? prev.yValue : prev.rectObj.ymax();
        }
        return (new RectHV(xmin, ymin, xmax, ymax));
    }
    
    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (contains(p))
            return;
        
        KdTreeNumPointsInTree++;
        KdTreeNode kdTreeNodeObj = KdTreeRootNodeObj;
        
        if (KdTreeRootNodeObj == null) {
            RectHV rectHvObj  = new RectHV(0,0,1,1);
            KdTreeRootNodeObj = new KdTreeNode(p, rectHvObj);
            KdTreeRootNodeObj.KdTreeNodeSetSplitCriteria(true);
            return;
        }
        
        while (true) {
            if (kdTreeNodeObj.compareTo(p) > 0) {
                if (kdTreeNodeObj.rightOrTop == null) {
                    RectHV rectHvObj = KdTreeNodeGetRectFormed(kdTreeNodeObj, false);
                    kdTreeNodeObj.rightOrTop = new KdTreeNode(p, rectHvObj);
                    kdTreeNodeObj.rightOrTop.KdTreeNodeSetSplitCriteria(!kdTreeNodeObj.KdTreeNodeGetSplitCriteria());
                    break;
                }
                kdTreeNodeObj = kdTreeNodeObj.rightOrTop;
            } else {
                if (kdTreeNodeObj.leftOrDown == null) {
                    RectHV rectHvObj = KdTreeNodeGetRectFormed(kdTreeNodeObj, true);
                    kdTreeNodeObj.leftOrDown = new KdTreeNode(p, rectHvObj);
                    kdTreeNodeObj.leftOrDown.KdTreeNodeSetSplitCriteria(!kdTreeNodeObj.KdTreeNodeGetSplitCriteria());
                    break;
                }
                kdTreeNodeObj = kdTreeNodeObj.leftOrDown;                
            }
        }
    }
    
    // does the set contain point p?
    public boolean contains(Point2D p) {
        KdTreeNode kdTreeNodeObj = KdTreeRootNodeObj;
        
        while (kdTreeNodeObj != null) {
            if (kdTreeNodeObj.p.equals(p))
                return true;
            
            if (!kdTreeNodeObj.rectObj.contains(p))
                return false;
                
            if (kdTreeNodeObj.compareTo(p) > 0)
                kdTreeNodeObj = kdTreeNodeObj.rightOrTop;
            else if (kdTreeNodeObj.compareTo(p) < 0)
                kdTreeNodeObj = kdTreeNodeObj.leftOrDown;
        }
        return false;
    }
    
    private void KdTreeDraw(KdTreeNode kdTreeNodeObj) {
        if (kdTreeNodeObj == null)
            return;
        
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        kdTreeNodeObj.p.draw();
        StdDraw.setPenRadius();
        if (!kdTreeNodeObj.KdTreeNodeGetSplitCriteria()) 
            StdDraw.setPenColor(StdDraw.RED);
        else
            StdDraw.setPenColor(StdDraw.BLUE); 
        kdTreeNodeObj.rectObj.draw();
        
        
        KdTreeDraw(kdTreeNodeObj.leftOrDown);
        KdTreeDraw(kdTreeNodeObj.rightOrTop);
        
        if (kdTreeNodeObj.KdTreeNodeGetSplitCriteria()) 
            StdDraw.setPenColor(StdDraw.RED);
        else
            StdDraw.setPenColor(StdDraw.BLUE);
        
        if (kdTreeNodeObj.leftOrDown == null) {
            RectHV leftOrDownRectObj = KdTreeNodeGetRectFormed(kdTreeNodeObj, true);
            leftOrDownRectObj.draw();
        } else if (kdTreeNodeObj.rightOrTop == null) {
            RectHV rightOrTopRectObj = KdTreeNodeGetRectFormed(kdTreeNodeObj, false);
            rightOrTopRectObj.draw();
        }
    }
    
    // draw all points to standard draw
    public void draw() {
        KdTreeDraw(KdTreeRootNodeObj);
    }
    
    private class KdTreeRectIntersect implements Iterable<Point2D> {
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
        
        private void KdTreeRectIntersectInsert(Point2D p) {
            if (rectIntersectNumPoints >= rectIntersectArraySize)
                RectIntersectArrayResize();
            
            rectIntersectArray[rectIntersectNumPoints++] = p;
        }
        
        private void KdTreeRectIntersectFind(KdTreeNode kdTreeNodeObj, RectHV rect) {
            if (kdTreeNodeObj == null)
                return;
            
            if (rect.intersects(kdTreeNodeObj.rectObj)) {
                if (rect.contains(kdTreeNodeObj.p))
                    KdTreeRectIntersectInsert(kdTreeNodeObj.p);
            } else 
                return;
            
            KdTreeRectIntersectFind(kdTreeNodeObj.leftOrDown, rect);
            KdTreeRectIntersectFind(kdTreeNodeObj.rightOrTop, rect);
        }
        
        public KdTreeRectIntersect(RectHV rect) {
            rectIntersectArray = new Point2D[1];
            rectIntersectNumPoints = 0;
            rectIntersectArraySize = 1;
            
            KdTreeRectIntersectFind(KdTreeRootNodeObj, rect);
        }
        
        private class KdTreeRectIntersectIterator implements Iterator<Point2D> {
            private int KdTreeRectIntersectIteratorNumPointsToIterate;
            
            public KdTreeRectIntersectIterator() {
                KdTreeRectIntersectIteratorNumPointsToIterate = rectIntersectNumPoints;
            }
            
            public Point2D next() {
                return (rectIntersectArray[--KdTreeRectIntersectIteratorNumPointsToIterate]);
            }
            
            public boolean hasNext() {
                return (KdTreeRectIntersectIteratorNumPointsToIterate > 0);
            }
            
            public void remove() {
            }
        }
        
        public Iterator<Point2D> iterator() {
            return new KdTreeRectIntersectIterator();
        }       
    }
     // all points that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect) {
        return new KdTreeRectIntersect(rect);
    }
    
    private class NearestPointInfo {
        Point2D pObj;
        double  distance;
    };
    // a nearest neighbor in the set to point p; null if the set is empty 
    private void KdTreeFindNearest(Point2D p, NearestPointInfo nearestPointInfoObj, KdTreeNode kdTreeNodeObj) {
        
        if (kdTreeNodeObj == null)
            return;
        
        if (!kdTreeNodeObj.rectObj.contains(p)){
            if (nearestPointInfoObj.distance > kdTreeNodeObj.rectObj.distanceSquaredTo(p)) {
                double distanceToNodePoint = p.distanceSquaredTo(kdTreeNodeObj.p);
                if (nearestPointInfoObj.distance > distanceToNodePoint) {
                    nearestPointInfoObj.pObj = kdTreeNodeObj.p;
                    nearestPointInfoObj.distance = distanceToNodePoint;
                }
                KdTreeFindNearest(p, nearestPointInfoObj, kdTreeNodeObj.leftOrDown);
                KdTreeFindNearest(p, nearestPointInfoObj, kdTreeNodeObj.rightOrTop);    
            }
            return;
        }
        
        double distanceToNodePoint = p.distanceSquaredTo(kdTreeNodeObj.p);
        if (nearestPointInfoObj.distance > distanceToNodePoint) {
            nearestPointInfoObj.pObj = kdTreeNodeObj.p;
            nearestPointInfoObj.distance = distanceToNodePoint;
        }
        
        if (kdTreeNodeObj.rectObj.contains(p)) {
            RectHV leftOrDownRectObj = (kdTreeNodeObj.leftOrDown != null) 
                ? kdTreeNodeObj.leftOrDown.rectObj : null;
            RectHV rightOrTopRectObj = (kdTreeNodeObj.rightOrTop != null)
                 ? kdTreeNodeObj.rightOrTop.rectObj : null;

            if ((leftOrDownRectObj != null) 
                    && (leftOrDownRectObj.contains(p))) {
                KdTreeFindNearest(p, nearestPointInfoObj, kdTreeNodeObj.leftOrDown);   
                KdTreeFindNearest(p, nearestPointInfoObj, kdTreeNodeObj.rightOrTop); 
            } else {
                KdTreeFindNearest(p, nearestPointInfoObj, kdTreeNodeObj.rightOrTop);   
                KdTreeFindNearest(p, nearestPointInfoObj, kdTreeNodeObj.leftOrDown); 
            }
                
        }
    }
    
    public Point2D nearest(Point2D p) {
        NearestPointInfo nearestPointInfoObj = new NearestPointInfo();
        if (KdTreeRootNodeObj == null)
            return null;
        
        nearestPointInfoObj.pObj = KdTreeRootNodeObj.p;
        nearestPointInfoObj.distance = p.distanceSquaredTo(KdTreeRootNodeObj.p);
        
        KdTreeFindNearest(p, nearestPointInfoObj, KdTreeRootNodeObj);
        
        return nearestPointInfoObj.pObj;
    }
    
    // unit testing of the methods (optional) 
    public static void main(String[] args) {
        KdTree kdTreeObj = new KdTree();
        Point2D point0 = new Point2D(0.1, 0.1);
        Point2D point1 = new Point2D(0.5, 0.5);
        Point2D point2 = new Point2D(0.3, 0.7);
        Point2D point3 = new Point2D(0.4, 0.8);
       
        kdTreeObj.insert(point0);
        kdTreeObj.insert(point1);
        kdTreeObj.insert(point2);
        kdTreeObj.insert(point3);
        kdTreeObj.draw();
        Point2D point4 = new Point2D(0.4,0.4);
        Point2D nearest = kdTreeObj.nearest(point4);
        //StdOut.println(nearest.toString());
        //StdOut.println(kdTreeObj.contains(point1));
        
        /*
        RectHV rectHvObj = new RectHV(0,0,0.5,0.5);
        //pointSetObj.draw();
        
        for (Point2D pObj : kdTreeObj.range(rectHvObj)) {
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(0.01);
            pObj.draw();
        }
        */
        
    }
}
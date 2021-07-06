/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;

import java.util.ArrayList;

public class PointSET {
    private final SET<Point2D> set;

    public PointSET() {  // construct an empty set of points
        set = new SET<>();
    }

    public boolean isEmpty() { // is the set empty?
        return set.isEmpty();
    }

    public int size() { // number of points in the set
        return set.size();
    }

    public void insert(Point2D p) { // add the point to the set (if it is not already in the set)
        checkArg(p);
        set.add(p);
    }

    public boolean contains(Point2D p) { // does the set contain point p?
        checkArg(p);
        return set.contains(p);
    }

    public void draw() { // draw all points to standard draw
        for (Point2D p : set) {
            p.draw();
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        // all points that are inside the rectangle (or on the boundary)
        checkArg(rect);

        ArrayList<Point2D> inRect = new ArrayList<>();
        for (Point2D p : set) {
            if (rect.contains(p)) inRect.add(p);
        }
        return inRect;
    }

    public Point2D nearest(Point2D p) {
        checkArg(p);
        // a nearest neighbor in the set to point p; null if the set is empty
        if (isEmpty()) return null;

        double minDist = Double.POSITIVE_INFINITY;
        Point2D nearest = null;
        for (Point2D point : set) {
            if (point.distanceSquaredTo(p) < minDist) {
                minDist = point.distanceSquaredTo(p);
                nearest = point;
            }
        }
        return nearest;
    }

    private void checkArg(Object obj) {
        if (obj == null) throw new IllegalArgumentException("Argument cannnot be null");
    }

    public static void main(String[] args) { // unit testing of the methods (optional)
        // Test completed: isEmpty, size(), insert(), contains(), draw(), range(), nearest(), checkArg()
        // PointSET test = new PointSET();
        // test.insert(new Point2D(1.0, 1.0));
        // test.insert(new Point2D(2.0, 2.0));
        // test.insert(new Point2D(3.0, 3.0));
        // StdOut.println(test.nearest(new Point2D(2.3, 2.4)));
        // RectHV rect = new RectHV(1.5, 1.5, 3.0, 3.0);
        // Iterable<Point2D> inRect = test.range(rect);
        // for (Point2D p : inRect) {
        //     StdOut.println(p);
        // }
        // StdOut.println(test.size());
        // StdOut.println(test.isEmpty());
    }
}

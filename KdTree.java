/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;

public class KdTree {
    private Node root;
    private int size;

    private static class Node {
        private final Point2D p;
        private final RectHV rect;
        private Node lb;
        private Node rt;

        private Node(Point2D p, RectHV rect) {
            this.p = p;
            this.rect = rect;
            this.lb = null;
            this.rt = null;
        }
    }

    public KdTree() { // construct an empty set of points
        root = null;
        size = 0;
    }

    public boolean isEmpty() { // is the set empty?
        return size == 0;
    }

    public int size() { // number of points in the set
        return size;
    }

    private int cmp(Point2D p1, Point2D p2, int depth) {
        if (depth % 2 == 0) {  // Vertical Partition
            if (p1.x() != p2.x()) return Double.compare(p1.x(), p2.x());
            else return Double.compare(p1.y(), p2.y());
        }
        else { // Horizontal Partition
            if (p1.y() != p2.y()) return Double.compare(p1.y(), p2.y());
            else return Double.compare(p1.x(), p2.x());
        }
    }

    private Node insert(Node x, Point2D p, int depth, double minX, double minY, double maxX,
                        double maxY) {
        if (x == null) {
            size++;
            return new Node(p, new RectHV(minX, minY, maxX, maxY));
        }

        int cmp = cmp(p, x.p, depth);
        if (depth % 2 == 0) {
            if (cmp > 0) x.rt = insert(x.rt, p, depth + 1, x.p.x(), minY, maxX, maxY);
            if (cmp < 0) x.lb = insert(x.lb, p, depth + 1, minX, minY, x.p.x(), maxY);
        }
        else {
            if (cmp > 0) x.rt = insert(x.rt, p, depth + 1, minX, x.p.y(), maxX, maxY);
            if (cmp < 0) x.lb = insert(x.lb, p, depth + 1, minX, minY, maxX, x.p.y());
        }

        return x;
    }

    public void insert(Point2D p) { // add the point to the set (if it is not already in the set)
        checkArg(p);
        root = insert(root, p, 0, 0.0, 0.0, 1.0, 1.0);
    }

    public boolean contains(Point2D p) { // does the set contain point p?
        checkArg(p);
        Node x = root;
        int depth = 0;
        while (x != null) {
            int cmp = cmp(p, x.p, depth);
            if (cmp == 0) return true;
            if (cmp < 0) x = x.lb;
            if (cmp > 0) x = x.rt;
            depth++;
        }
        return false;
    }

    public void draw() {
        // draw all points to standard draw
    }

    public Iterable<Point2D> range(
            RectHV rect) { // all points that are inside the rectangle (or on the boundary)
        checkArg(rect);
        ArrayList<Point2D> points = new ArrayList<>();
        if (root != null) range(root, rect, points);
        return points;
    }

    private void range(Node x, RectHV rect, ArrayList<Point2D> points) {
        if (rect.contains(x.p)) points.add(x.p);
        if (x.lb != null && rect.intersects(x.lb.rect)) range(x.lb, rect, points);
        if (x.rt != null && rect.intersects(x.rt.rect)) range(x.rt, rect, points);
    }


    public Point2D nearest(// a nearest neighbor in the set to point p; null if the set is empty
                           Point2D p) {
        checkArg(p);
        if (isEmpty()) return null;

        Point2D champion = root.p;

        champion = nearest(root, p, champion);

        return champion;
    }

    private Point2D nearest(Node x, Point2D p, Point2D champion) {
        if (x == null) return champion;

        if (x != root) {
            if (champion.distanceSquaredTo(p) <= x.rect.distanceSquaredTo(p)) return champion;
        }

        if (p.distanceSquaredTo(x.p) < p.distanceSquaredTo(champion)) champion = x.p;

        if (x.rt != null && x.rt.rect.contains(p)) { // Go right subtree first.
            champion = nearest(x.rt, p, champion);
            champion = nearest(x.lb, p, champion);
        }
        else {  // Go left subtree first.
            champion = nearest(x.lb, p, champion);
            champion = nearest(x.rt, p, champion);
        }

        return champion;
    }

    private void checkArg(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("Argument should not be null.");
        }
    }

    public static void main(// unit testing of the methods (optional)
                            String[] args) {
        // Tests completed: isEmpty, size, insert, contains, range
        // To do: nearest
        // read the n points from a file
        In in = new In(args[0]);
        KdTree tree = new KdTree();
        while (in.hasNextLine()) {
            String line = in.readLine();
            double x = Double.parseDouble(line.split(" ")[0]);
            double y = Double.parseDouble(line.split(" ")[1]);
            Point2D p = new Point2D(x, y);
            tree.insert(p);
        }

        double x = 0.98;
        double y = 0.35;
        Point2D p = new Point2D(x, y);
        Point2D n = tree.nearest(p);
        StdOut.print(n.x());
        StdOut.print(" ");
        StdOut.print(n.y());


        // RectHV rect = new RectHV(0.0, 0.0, 0.5, 0.5);
        // Iterable<Point2D> inrect = tree.range(rect);
        // for (Point2D p : inrect) {
        //     StdOut.print(p.x());
        //     StdOut.print(" ");
        //     StdOut.print(p.y());
        //     StdOut.println("");
        // }
    }
}

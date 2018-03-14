import java.util.Arrays;
import java.util.Comparator;

public class TwoDRangeTree implements DRT {
    public TwoDNode root;
    int numOfLeaves;
    public Comparator<Point> compX;

    /**
     * Builds a 2D range tree from an array of points.
     * @param points array of points to use.
     */
	public TwoDRangeTree(Point[] points) {
        compX = new PointComparatorX();

        Point[] sortedPointsX = new Point[points.length];
        Point[] sortedPointsY = new Point[points.length];

	    // Sort arrays by the given type.
        Arrays.sort(points, new PointComparatorX());
        System.arraycopy(points, 0, sortedPointsX, 0, points.length);
        Arrays.sort(points, new PointComparatorY());
        System.arraycopy(points, 0, sortedPointsY, 0, points.length);

        this.root = BuildTwoDRangeTree(null, sortedPointsX, 0, points.length, sortedPointsY);
    }

    /**
     * Helper function - creates nodes in the 2D range tree by the structure's definition.
     * @param parent parent of the node being build currently.
     * @param sortedPointsX original points array, sorted by their X values.
     * @param from min index range for sortedPointsX.
     * @param to max index range for sortedPointsX.
     * @param sortedPointsY points array, downsizes to be relevant to the current node, sorted by Y values.
     * @return the node created.
     */
    public TwoDNode BuildTwoDRangeTree(TwoDNode parent, Point[] sortedPointsX, int from, int to, Point[] sortedPointsY) {
        TwoDNode newNode = new TwoDNode();
        newNode.parent = parent;

        if (to - from == 1) {
            newNode.data = sortedPointsX[from];
            newNode.yTree = new OneDRangeTree(newNode.data);
            return newNode;
        }

        // Select middle variable.
        int middle = (int) (from + Math.floor((to - from) / 2));

        newNode.data = sortedPointsX[middle - 1];
        newNode.yTree = new OneDRangeTree(sortedPointsY, true);

        Point[] leftPartition = partition(sortedPointsY, newNode.data, true);
        Point[] rightPartition = partition(sortedPointsY,newNode.data, false);
        newNode.left = BuildTwoDRangeTree(newNode, sortedPointsX, from, middle, leftPartition);
        newNode.right = BuildTwoDRangeTree(newNode, sortedPointsX, middle, to, rightPartition);

        return newNode;
    }


    /**
     * Get the number of points in a given rectangular area.
     * @param point1 Range point.
     * @param point2 Range point.
     * @return Number of points between point1 and point2.
     */
    public int numOfPointsInRectangle(Point point1, Point point2) {
        return numOfPointsInRectangle(root, point1, point2);
    }

    /**
     * Get the number of points in a given rectangular area.
     * @param point1 Range point.
     * @param point2 Range point.
     * @return Number of points between point1 and point2.
     */
    public int numOfPointsInRectangle(TwoDNode node, Point point1, Point point2) {
        TwoDNode minPointer = node, maxPointer = node;
        int pointsFound = 0;

        // Get range values.
        Point min = new Point(Math.min(point1.getX(), point2.getX()), Math.min(point1.getY(), point2.getY()));
        Point max = new Point(Math.max(point1.getX(), point2.getX()), Math.max(point1.getY(), point2.getY()));

        while (minPointer == maxPointer && maxPointer != null && minPointer != null && maxPointer.left != null && minPointer.left != null) {
            // While the path to minValue and maxValue doesn't split, go further down the tree.
            if (compX.compare(minPointer.data, min) >= 0) {
                minPointer = minPointer.left;
            }

            else if (compX.compare(minPointer.data, min) < 0) {
                minPointer = minPointer.right;
            }

            if (compX.compare(maxPointer.data, max) > 0) {
                maxPointer = maxPointer.left;
            }

            else if (compX.compare(maxPointer.data, max) <= 0) {
                maxPointer = maxPointer.right;
            }
        }
        if (minPointer == maxPointer) {
            // Both pointers reached leaf, without parting.
            if ((maxPointer.data.getX() <= max.getX()) & (maxPointer.data.getX() >= min.getX())) {
                pointsFound += maxPointer.yTree.numOfPointsInRange(min.getY(), max.getY());
            }
            return pointsFound;
        }

        while (minPointer.right != null) {
            // While minPointer isn't a leaf. since maxPointer is right to minPointer, all nodes X value are smaller than max.
            if (minPointer.data.getX() - min.getX() >= 0) {
                pointsFound += minPointer.right.yTree.numOfPointsInRange(min.getY(),max.getY());
                minPointer = minPointer.left;
            }
            else {
                minPointer = minPointer.right;
            }
        }
        // Reached a leaf.
        if (minPointer.data.getX() - min.getX()  >= 0) {
            pointsFound += minPointer.yTree.numOfPointsInRange(min.getY(), max.getY());
        }

        // -- Found all points for min pointer. --

        while (maxPointer.left != null) {
            // While maxPointer isn't a leaf. since minPointer is left to maxPointer, all nodes X value are larger than min.
            if (maxPointer.data.getX() - max.getX() <= 0) {
                pointsFound = pointsFound + maxPointer.left.yTree.numOfPointsInRange(min.getY(),max.getY());
                maxPointer = maxPointer.right;
            }
            else {
                maxPointer = maxPointer.left;
            }
        }

        // Reached a leaf.
        if (maxPointer.data.getX() - max.getX() <= 0) {
            pointsFound += maxPointer.yTree.numOfPointsInRange(min.getY(), max.getY());
        }

        return pointsFound;
    }

    /**
     * Get all points in range from point1 to point2.
     * @param point1 range indicator.
     * @param point2 range indicator.
     * @return array of all the points in the range.
     */
    public Point[] getPointsInRectangle(Point point1, Point point2) {
        return getPointsInRectangle(root, point1, point2);
    }

    /**
     * Get all points in range from point1 to point2.
     * @param node node to start at.
     * @param point1 range indicator.
     * @param point2 range indicator.
     * @return array of all the points in the range.
     */
    public Point[] getPointsInRectangle(TwoDNode node, Point point1, Point point2) {
        TwoDNode minPointer = node, maxPointer = node;
        Point[] points = new Point[numOfPointsInRectangle(node, point1, point2)];
        int firstNullIndexInAns = 0;

        // Get range values.
        Point min = new Point(Math.min(point1.getX(), point2.getX()), Math.min(point1.getY(), point2.getY()));
        Point max = new Point(Math.max(point1.getX(), point2.getX()), Math.max(point1.getY(), point2.getY()));

        while (minPointer == maxPointer && maxPointer != null && minPointer != null && maxPointer.left != null && minPointer.left != null) {
            // While the path to minPointer and maxPointer doesn't split go further down the tree.
            if (compX.compare(minPointer.data, min) >= 0) {
                minPointer= minPointer.left;
            }
            else if (compX.compare(minPointer.data, min) < 0) {
                minPointer= minPointer.right;
            }

            if (compX.compare(maxPointer.data, max) > 0) {
                maxPointer= maxPointer.left;
            }
            else if (compX.compare(maxPointer.data, max) <= 0) {
                maxPointer= maxPointer.right;
            }
        }

        if (minPointer == maxPointer) {
            // Both pointers reached leaf.
            if ((compX.compare(maxPointer.data, max) <= 0) & (compX.compare(maxPointer.data, min) >= 0)) {
                points[firstNullIndexInAns] = maxPointer.data;
            }
            return points;
        }

        while (minPointer.right != null) {
            // While minPointer isn't a leaf. since maxPointer is right to minPointer, all nodes X value are smaller than max.
            if (compX.compare(minPointer.data, min) >= 0) {
                Point[] yTreePoints = minPointer.right. yTree.getPointsInNode(min.getY(),max.getY());
                append(points, yTreePoints,firstNullIndexInAns);
                firstNullIndexInAns += yTreePoints.length;
                minPointer = minPointer.left;
            }
            else {
                minPointer = minPointer.right;
            }
        }
        // Reached a leaf.
        if (compX.compare(minPointer.data, min) >= 0) {
            points[firstNullIndexInAns] = minPointer.data;
            firstNullIndexInAns += minPointer.yTree.numOfPointsInRange(min.getY(), max.getY());
        }

        // -- Found all points for min pointer.--

        while (maxPointer.left != null) {
            // While maxPointer isn't a leaf. since minPointer is left to maxPointer, all nodes X value are larger than min.
            if (compX.compare(maxPointer.data, max) <= 0) {
                Point[] yTreePoints = maxPointer.left.yTree.getPointsInNode(min.getY(),max.getY());
                append(points, yTreePoints,firstNullIndexInAns);
                firstNullIndexInAns += yTreePoints.length;
                maxPointer = maxPointer.right;
            }
            else {
                maxPointer = maxPointer.left;
            }
        }

        // Reached a leaf.
        if (compX.compare(maxPointer.data, max) <= 0){
            points[firstNullIndexInAns] = maxPointer.data;
            firstNullIndexInAns += maxPointer.yTree.numOfPointsInRange(min.getY(), max.getY());
        }

        return points;
    }

    /**
     * Add a point to the tree.
     * @param point point to add.
     */
    public void addPoint(Point point) {
        TwoDNode iterator = root;

        while (iterator.left != null && iterator.right != null) {
            // Not a leaf.

            iterator.yTree.addPoint(point);
            if (compX.compare(iterator.data, point) < 0) {
                // If the node is smaller go right.
                iterator = iterator.right;
            }
            else {
                // If the node is larger go left.
                iterator = iterator.left;
            }
        }

        // Reached a leaf.
        iterator.yTree.addPoint(point);

        if (compX.compare(iterator.data, point) >= 0) {
            // iterator is larger-equal to new node.
            TwoDNode leftNode = new TwoDNode();
            leftNode.data = point;
            leftNode.parent = iterator;
            leftNode.yTree = new OneDRangeTree(point);

            TwoDNode rightNode = new TwoDNode();
            rightNode.data = iterator.data;
            rightNode.parent = iterator;
            rightNode.yTree = new OneDRangeTree(iterator.data);

            iterator.left = leftNode;
            iterator.right = rightNode;
            iterator.data = point;
        }
        else {
            // iterator is smaller than new node.
            TwoDNode leftNode = new TwoDNode();
            leftNode.data = iterator.data;
            leftNode.parent = iterator;
            leftNode.yTree = new OneDRangeTree(iterator.data);

            TwoDNode rightNode = new TwoDNode();
            rightNode.data = point;
            rightNode.parent = iterator;
            rightNode.yTree = new OneDRangeTree(point);

            iterator.left = leftNode;
            iterator.right = rightNode;

            TwoDNode node = iterator.parent;
            while (node != null) {
                // Go upwards in the tree until reaching a place where iterator is a sub-right tree.
                if (node.left == iterator) {
                    // If iterator is a sub right tree - change data to the new point's data.
                    node.data = point;
                    break;
                }
                else {
                    node = node.parent;
                    iterator = iterator.parent;
                }
            }
        }

        this.numOfLeaves++;
    }

    /**
     * Remove a point from the tree.
     * @param point
     * @return whether the point was removed successfully (fails if not found).
     */
    public boolean removePoint(Point point) {
        if (!existsPoint(point)) {
            return false;
        }

        TwoDNode iterator = root;

        while (iterator.left != null && iterator.right != null) {
            // Not a leaf.

            iterator.yTree.removePoint(point);
            if (compX.compare(iterator.data, point) < 0) {
                // If the node is smaller go right.
                iterator = iterator.right;
            }
            else {
                // If the node is larger go left.
                iterator = iterator.left;
            }
        }

        if (iterator.parent == null) {
            // The tree has only one node.
            if (point.equals(root.data)) {
                root = null;
            }
        }
        else if (iterator.parent.parent == null) {
            // This path has only one parent.
            if (point.equals(root.data)) {
                // The removal leaf is in the left node.
                root = root.right;
            }
            else {
                // The removal leaf is in the right node.
                root = root.left;
            }
        }
        else {
            // Delete the leaf's parent, and move the other child to be instead of the parent.
            if (iterator.parent.parent.right == iterator.parent) {
                Point fixData;
                // Right subtree.
                if (iterator.parent.left == iterator) {
                    iterator.parent.parent.right = iterator.parent.right;
                    fixData = iterator.parent.right.data;
                }
                else {
                    iterator.parent.parent.right = iterator.parent.left;
                    fixData = iterator.parent.left.data;
                }

                // Run up the tree to see if this subtree is the left subtree of any node.
                iterator = iterator.parent.parent;

                boolean found = false;
                while (iterator.parent != null && !found) {
                    if (iterator.parent.left == iterator) {
                        // Left son of the subtree so far.
                        iterator.parent.data = fixData;
                        found = true;
                    }
                }
            }
            else {
                // Left subtree.
                if (iterator.parent.left == iterator) {
                    iterator.parent.parent.left = iterator.parent.right;
                    iterator.parent.parent.data = iterator.parent.right.data;
                }
                else {
                    iterator.parent.parent.left = iterator.parent.left;
                    iterator.parent.parent.data = iterator.parent.left.data;
                }
            }

        }
        return true;
    }

    /**
     * Check for a point's existence in the tree.
     * @param point point to search for.
     * @return whether the point exists in the tree.
     */
    public boolean existsPoint(Point point) {
        TwoDNode iterator = root;
        int neededX = point.getX();
        int currentX;

        while (iterator != null) {
            currentX = iterator.data.getX();
            if (currentX == neededX) {
                // Search the yTree for the correct point.
                return iterator.yTree.existsPoint(point);
            }
            else if (currentX > neededX) {
                iterator = iterator.left;
            }
            else {
                iterator = iterator.right;
            }
        }

        return false;
    }

    /**
     * Get the number of points in half Plane X.
     * @param X Split point.
     * @param greaterThan Whether to get the higher part.
     */
    public int numOfPointsInHalfPlaneX(int X, boolean greaterThan) {

        int yMax = root.yTree.findMax(root.yTree.root).getY();
        int yMin = root.yTree.findMin(root.yTree.root).getY();

        if (greaterThan) {
            int max = findMax(root).getX();
            return numOfPointsInRectangle(new Point(X, yMin), new Point(max, yMax));
        }
        else {
            int min = findMin(root).getX();
            return numOfPointsInRectangle(new Point(X, yMin), new Point(min, yMax));

        }
    }

    /**
     * Get the number of points in half Plane Y.
     * @param Y Split point.
     * @param greaterThan Whether to get the higher part.
     */
    public int numOfPointsInHalfPlaneY(int Y, boolean greaterThan) {
        return root.yTree.numOfPointsInHalfPlaneY(Y, greaterThan);
    }

    /**
     * Get all points in the tree.
     */
    public Point[] getAllPoints() {
        return this.root.yTree.getAllPoints();
    }

    /**
     * Finds the Max node in a sub-tree.
     * @param node - the node from which the search begins
     * @return the point with the maximum value.
     */
    protected Point findMax(TwoDNode node) {
        while (node.right != null) {
            node = node.right;
        }
        return node.data;
    }

    /**
     * Finds the Max node in a sub-tree.
     * @param node - the node from which the search begins
     * @return the point with the maximum value.
     */
    protected Point findMin(TwoDNode node) {
        while (node.left != null) {
            node = node.left;
        }
        return node.data;
    }    
    
    /**
     * Auxiliary-function - concatenates two arrays. node that null pointer exception IS NOT CHECKED.
     * @param mainArray The array that should combine the data from two arrays.
     * @param apendeeArray - The function copies all the data to the main array
     * @param emptyIndex - The index to start copying to.
     */
    private void append(Point[] mainArray, Point[] apendeeArray, int emptyIndex) {
        for (int i = emptyIndex; i < emptyIndex + apendeeArray.length; i++) {
            mainArray[i] = apendeeArray[i-emptyIndex];
        }
    }

    /**
     * Get left partition from an array by x value. Runs in O(n).
     * @param points array to partition.
     * @param p point to partition at.
     * @param smallerThan whether this is the left partition.
     * @return the point with the maximum value.
     */
    public Point[] partition(Point[] points, Point p, boolean smallerThan) {
        Point[] thisPartition = new Point[points.length];
        int i = 0, count = 0;

        if (smallerThan) {
            while (i < points.length) {
                if (compX.compare(p,points[i]) >= 0) {
                    thisPartition[count] = points[i];
                    count++;
                }
                i++;
            }
        }
        else {
            while (i < points.length) {
                if (compX.compare(p,points[i]) < 0) {
                    thisPartition[count] = points[i];
                    count++;
                }
                i++;
            }
        }

        if (count == points.length) {
            // Everything went to the thisPartition.
            return thisPartition;
        }

        // Shorten array.
        Point[] shortedPartition = new Point[count];
        for (i = 0; i < shortedPartition.length; i++) {
           shortedPartition[i] = thisPartition[i];
        }
        return shortedPartition;
    }
}

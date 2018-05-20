import java.util.Arrays;
import java.util.Comparator;

public class OneDRangeTree {

    public OneDNode root;
    public int numOfLeafs;
    public Comparator<Point> comparator;

    // Generic pointer.
    public int currentIndex;

    public OneDRangeTree(Point point) {
        comparator = new PointComparatorY();
        Point[] points = new Point[1];
        points[0] = point;
        BuildOneDRangeTree(points, comparator, true);
    }

    public OneDRangeTree(Point[] points) {
        comparator = new PointComparatorY();
        BuildOneDRangeTree(points, comparator, false);
    }

    public OneDRangeTree(Point[] points, boolean sorted) {
        comparator = new PointComparatorY();
        if (sorted) {
            BuildOneDRangeTree(points, comparator, sorted);
        }
        else {
            this.root = BuildOneDRangeTree(points, 0, numOfLeafs, null);
        }
    }

    /**
     * Constructor, builds a 1D tree as in assignment.
     * @param points - the array of points.
     * @param sorted - a boolean that indicates whether the array is sorted or not. default false.
     * @param pointComparator - if sorting is needed, the comperator for sorting. Default PointComperatorY.
     */
    public void BuildOneDRangeTree(Point[] points, Comparator<Point> pointComparator, boolean sorted) {
        comparator = pointComparator;
        this.numOfLeafs = points.length;
        if (!sorted) {
            Arrays.sort(points, comparator);
        }
        this.root = BuildOneDRangeTree(points, 0, numOfLeafs, null);
    }

    /**
     * Creates the tree's nodes in the correct structure.
     */
    private OneDNode BuildOneDRangeTree(Point[] sortedPoints, int from, int to, OneDNode parent) {

        OneDNode node = new OneDNode();
        node.parent = parent;
        node.leavesUnderNode = to - from;
        if (node.leavesUnderNode == 1){ //leaf
            node.data = sortedPoints[from];
            return node;
        }

        int mid = (int) (from + Math.floor((to - from) / 2));
        node.data = sortedPoints[mid-1];
        node.left = BuildOneDRangeTree(sortedPoints, from, mid, node);
        node.right = BuildOneDRangeTree(sortedPoints, mid, to, node);

        return node;

    }

    /**
     * Returns all the points in the tree, in an array.
     * @return array of points under tree.
     */
    public Point[] getAllPoints () {
        Point[] pointsFound = new Point[this.numOfLeafs];
        currentIndex = 0;
        getAllPoints(root, pointsFound);
        return pointsFound;
    }

    /**
     * Returns all the points in the tree, under a specific node.
     * @return array of points under node.
     */
    public Point[] getAllPoints (OneDNode node) {
        Point[] pointsFound = new Point[node.leavesUnderNode];
        currentIndex = 0;
        getAllPoints(node, pointsFound);
        return pointsFound;
    }

    /**
     * Helper function to retrieve all nodes under a certain node.
     * If node is leaf, adds the node to the array at index arrayIndex.
     * if node is not leaf - calls func recursively to left and right node.
     * @return number of points int range [x1, x2]
     */
    private void getAllPoints (OneDNode node, Point[] p) {
        if (node.left == null && node.right == null){
            // The node is a leaf. Add it's data to the array.
            p[currentIndex] = node.data;
            currentIndex++;
            return;
        }

        // the node is an inner node
        getAllPoints(node.right, p);
        getAllPoints(node.left, p);
    }

    /**
     * adds a point to the OneDTree.
     * RT: O(h) = O(logn) as long as it it only called log(n) times in the same tree.
     * @param point
     */
    public void addPoint(Point point) {
        this.numOfLeafs++;

        OneDNode place = findCloseLeaf(point, true);

        if (comparator.compare(place.data, point) >= 0) {
            // Place is larger-equal to new node.
            OneDNode leftNode = new OneDNode();
            leftNode.leavesUnderNode = 1;
            leftNode.data = point;
            leftNode.parent = place;

            OneDNode rightNode = new OneDNode();
            rightNode.leavesUnderNode = 1;
            rightNode.data = place.data;
            rightNode.parent = place;

            place.left = leftNode;
            place.right = rightNode;
            place.data = point;
        }
        else {
            // Place is smaller than new node.
            OneDNode leftNode = new OneDNode();
            leftNode.leavesUnderNode = 1;
            leftNode.data = place.data;
            leftNode.parent = place;

            OneDNode rightNode = new OneDNode();
            rightNode.leavesUnderNode = 1;
            rightNode.data = point;
            rightNode.parent = place;

            place.left = leftNode;
            place.right = rightNode;

            OneDNode node = place.parent;
            while (node != null) {
                // Go upwards in the tree until reaching a place where place is a sub-right tree.
                if (node.left == place) {
                    // If place is a sub right tree - change x's data to point's data.
                    node.data = point;
                    break;
                }
                else {
                    node = node.parent;
                    place = place.parent;
                }
            }
        }

    }

    /**
     * finds the leaf which is the minimum of the leafs that are larger or equal to point (acording to the comperator).
     * @param point the point to compare
     */
    protected OneDNode findCloseLeaf(Point point, boolean addLeaf) {
        OneDNode iterator = root;
        while (iterator.left != null && iterator.right != null) {
            // Not a leaf.

            if (addLeaf) {
                iterator.leavesUnderNode++;
            }
            if (comparator.compare(iterator.data, point) < 0) {
                // If the node is smaller go right.
                iterator = iterator.right;
            }
            else {
                // If the node is larger go left.
                iterator = iterator.left;
            }

        }
        if (addLeaf) iterator.leavesUnderNode++;
        return iterator;
    }

    /**
     * searches the point in the tree.
     * @param point - the point to remove.
     * @return a boolean value for whether the node was found and removed (true), or not found (false).
     */
    public boolean removePoint(Point point) {
        OneDNode removalNode = findCloseLeaf(point, false);

        if (point.equals(removalNode.data)) {
        // The node is in the tree.

            if (removalNode.parent == null) {
                // The tree has only one node.
                if (point.equals(root.data)) {
                    root = null;
                }
            }

            else if (removalNode.parent.parent == null) {
                // This path has only one parent.
                if (point.equals(root.data)) {
                    // The leaf to be removed is in the left node.
                    root = root.right;
                }
                else {
                    // The leaf to be removed is in the right node.
                    root = root.left;
                }
            }
            else {
                // Delete the leaf's parent, and move the other child to be instead of the parent.
                OneDNode removalNodeParent = removalNode.parent;
                OneDNode removalNodeGrandParent = removalNodeParent.parent;
                liftParent(removalNode, removalNodeParent, removalNodeGrandParent);

                // Move upwards in the tree until reaching root, or until finding another node with the removal point data.
                OneDNode iteratingNode = removalNodeGrandParent;
                while (iteratingNode != null && (!(iteratingNode.data.equals(point)))) {
                    iteratingNode.leavesUnderNode--; // update leavesUnderNode of all parents.

                    iteratingNode = iteratingNode.parent;
                }
                if (iteratingNode != null) {
                    iteratingNode.data = findMax(iteratingNode.left);

                    while (iteratingNode != null) {
                    // Update leavesUnderNode.
                        iteratingNode.leavesUnderNode--;
                        iteratingNode = iteratingNode.parent;
                    }

                }
            }
            this.numOfLeafs--;

            // Successfully removed.
            return true;
        }
        else {
            // The node isn't in the tree.
            return false;
        }
    }

    /**
     * Finds the Max node in a sub-tree.
     * @param node - the node from which the search begins
     * @return the point with the maximum value.
     */
    protected Point findMax(OneDNode node) {
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
    protected Point findMin(OneDNode node) {
        while (node.left != null) {
            node = node.left;
        }
        return node.data;
    }

    /**
     * moves a child up one level in the tree, while deleting the parent.
     * @param child - the child node.
     * @param parent - the parent node to be deleted.
     * @param grandParent - the grandparent node.
     */
    protected void liftParent(OneDNode child, OneDNode parent, OneDNode grandParent) {
        if (parent.left == child) {
            // Child is the left child.
            if (grandParent.left == parent) {
                // Child's parent is a left child.
                grandParent.left = parent.right;
            }
            else {
                // Child's parent is a right child.
                grandParent.right = parent.right;
            }

        }
        else {
            if (grandParent.left == parent) {
                // Child's parent is a left child.
                grandParent.left = parent.left;
            }
            else {
                // Child's parent is a right child.
                grandParent.right = parent.left;
            }
        }
    }

    /**
     * Checks if point exists in tree.
     * @param point - the point to check if exists.
     * @return true iff point exists in tree. Otherwise return false.
     */
    public boolean existsPoint(Point point) {
        OneDNode iterator = root;
        while (iterator.left != null && iterator.right != null) {
            int compare = comparator.compare(iterator.data, point);
            // Not a leaf.
            if (compare == 0) {
                return true;
            }
            else if (compare < 0) {
                // If the node is smaller got right.
                iterator = iterator.right;
            }
            else {
                // If the node is larger go left.
                iterator = iterator.left;
            }
        }

        return false;
    }

    /**
     * get the number of points that are in Y-range given in whole tree
     * @param min the minimal Y value in range
     * @param max the maximum Y value in range
     * @return number of points that are in Y-range given
     */
    public int numOfPointsInRange(int min, int max) {
        return numOfPointsInRange(root, min, max);
    }

    /**
     * get the number of points that are in Y-range given in a specific node,
     * @param node the node to search from.
     * @param min the minimal Y value in range
     * @param max the maximum Y value in range
     * @return number of points that are in Y-range given
     */
    public int numOfPointsInRange(Node node, int min, int max) {
        OneDNode minPointer = (OneDNode) node, maxPointer = (OneDNode) node;
        int pointsFound = 0;
        
        while (minPointer == maxPointer && maxPointer != null && minPointer != null && maxPointer.left != null && minPointer.left != null) {
            // While the path to minValue and maxValue doesn't split, go further down the tree.
            if (minPointer.data.getY() >= min) {
                minPointer= minPointer.left;
            }

            else if (minPointer.data.getY() < min) {
                minPointer= minPointer.right;
            }

            if (maxPointer.data.getY() > max) {
                maxPointer= maxPointer.left;
            }

            else if (maxPointer.data.getY() <= max) {
                maxPointer= maxPointer.right;
            }
        }
        if (minPointer == maxPointer) {
            // Both pointers reached leaf, without parting.
            if ((maxPointer.data.getY() <= max) && (maxPointer.data.getY() >= min)) {
                pointsFound++;
            }
            return pointsFound;
        }

        
        // While minPointer isn't a leaf. since maxPointer is right to minPointer, all nodes Y value are smaller than max.
        while (minPointer.right != null) {
            if (minPointer.data.getY() >= min) {
                pointsFound = pointsFound + minPointer.right.leavesUnderNode;
                minPointer = minPointer.left;
            }
            else {
                minPointer = minPointer.right;
            }
        }
        // Reached a leaf.
        if (minPointer.data.getY() >= min) {
            pointsFound++;
        }
        
        // -- Found all points for min pointer. --
        
        
        // While maxPointer isn't a leaf. since minPointer is left to maxPointer, all nodes Y value are larger than min.
        while (maxPointer.left != null) {
            if (maxPointer.data.getY() <= max) {
                pointsFound = pointsFound + maxPointer.left.leavesUnderNode;
                maxPointer = maxPointer.right;
            }
            else {
                maxPointer = maxPointer.left;
            }
        }
        
        // Reached a leaf.
        if (maxPointer.data.getY() <= max) {
            pointsFound++;
        }
    
        return pointsFound;
    }

    /**
     * Retrieve an array of points that are in the range given.
     * @param min the minimal Y value in range
     * @param max the maximum Y value in range
     * @return an array of points that are in Y-range given
     */
    public Point[] getPointsInNode(int min, int max) {
        return getPointsInNode(root ,min, max);
    }

    /**
     * Retrieve an array of points that are in the range given.
     * @param node the node to search from.
     * @param min the minimal Y value in range
     * @param max the maximum Y value in range
     * @return an array of points that are in Y-range given
     */
    public Point[] getPointsInNode(Node node, int min, int max) {
        OneDNode minPointer = (OneDNode) node, maxPointer = (OneDNode) node;
        Point[] pointsFound = new Point[numOfPointsInRange(node, min, max)];
        int firstNullIndexInAns = 0;

        while (minPointer == maxPointer && maxPointer != null && minPointer != null && maxPointer.left != null && minPointer.left != null) {
            // While the path to minValue and maxValue doesn't split, go further down the tree.
            if (minPointer.data.getY() >= min) {
                minPointer= minPointer.left;
            }

            else if (minPointer.data.getY() < min) {
                minPointer= minPointer.right;
            }

            if (maxPointer.data.getY() > max) {
                maxPointer= maxPointer.left;
            }

            else if (maxPointer.data.getY() <= max) {
                maxPointer= maxPointer.right;
            }
        }

        if (minPointer == maxPointer) {
            // Both pointers reached the same leaf.
            if ((maxPointer.data.getY() <= max) & (maxPointer.data.getY() >= min)) pointsFound[firstNullIndexInAns] = maxPointer.data;
            return pointsFound;
        }
        
        while (minPointer.right != null) {
            // While minPointer isn't a leaf. since maxPointer is right to minPointer, all nodes Y value are smaller than max.
            if (minPointer.data.getY() >= min) {
            append(pointsFound, getAllPoints(minPointer.right),firstNullIndexInAns);
            firstNullIndexInAns += minPointer.right.leavesUnderNode;
            minPointer = minPointer.left;
            }
            else {
                minPointer = minPointer.right;
            }
        }
        // Reached a leaf
        if (minPointer.data.getY()>= min) {
                pointsFound[firstNullIndexInAns] = minPointer.data;
                firstNullIndexInAns++;
        }

        
        // -- Found all points for min pointer. --
     
        while (maxPointer.left != null) {
            // While maxPointer isn't a leaf. since minPointer is left to maxPointer, all nodes Y value are larger than min.
            if (maxPointer.data.getY() <= max) {
                append(pointsFound, getAllPoints(maxPointer.left),firstNullIndexInAns);
                firstNullIndexInAns += maxPointer.left.leavesUnderNode;
                maxPointer = maxPointer.right;
            }
            else {
                maxPointer = maxPointer.left;
            }
        }
     
        // Reached a leaf.
        if (maxPointer.data.getY() <= max){
                pointsFound[firstNullIndexInAns] = maxPointer.data;
                firstNullIndexInAns++;
        }
    
        return pointsFound;
    }
    /**
     * Concatenates two arrays. node that null pointer exception IS NOT CHECKED.
     * @param mainArray The array that should combine the data from two arrays.
     * @param apendeeArray - The function copies all the data to the main array
     * @param emptyIndex - The index to start copying to.
     */
    public void append(Point[] mainArray, Point[] apendeeArray, int emptyIndex) {
        for (int i = emptyIndex; i < emptyIndex + apendeeArray.length ; i++) {
            mainArray[i] = apendeeArray[i-emptyIndex];
        }
    }

    /**
     * Finds number of all points that are equal and larger/smaller than Y value.
     * @param Y - Y value to split the tree in.
     * @param greaterThan - A switch. Use true for returning number of values equal or greater than Y. For equal and smaller than - use false.
     * @return number of all points that are equal and larger/smaller than Y value.
     */
    public int numOfPointsInHalfPlaneY(int Y, boolean greaterThan){
        if (greaterThan) {
            int max = findMax(root).getY();
            return numOfPointsInRange(Y, max);
        }
        else {
            int min = findMin(root).getY();
            return numOfPointsInRange(min, Y);

        }
    }
}

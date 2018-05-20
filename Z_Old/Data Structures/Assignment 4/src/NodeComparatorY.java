import java.util.Comparator;

/**
 */
public class NodeComparatorY implements Comparator<Node> {

    public int compare(Node node1, Node node2) {
        int yDiff = node1.data.getY() - node2.data.getY();

        if (yDiff == 0) {
            return node1.data.getX() - node2.data.getX();
        }
        return yDiff;
    }
}

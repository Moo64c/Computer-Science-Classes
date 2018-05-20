import java.util.Comparator;

/*
 */
public class PointComparatorX implements Comparator<Point> {

    public int compare(Point point1, Point point2) {
        int xDiff = point1.getX() - point2.getX();

        if (xDiff == 0) {
            return point1.getY() - point2.getY();
        }
        return xDiff;
    }
}
